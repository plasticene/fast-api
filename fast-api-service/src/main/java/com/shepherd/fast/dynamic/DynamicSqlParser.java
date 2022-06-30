package com.shepherd.fast.dynamic;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/29 11:28
 */
@Component
public class DynamicSqlParser {

    /**
     * 常量表达式
     */
    public static final String CONSTANT_CONDITION = "1 = 1";

    public static final ThreadLocal<Map<String, String>> paramToWhere = new ThreadLocal<>();

    public String parseSQL(String sql) {
        // 新建 MySQL Parser
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcUtils.MYSQL);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLSelectStatement statement = (SQLSelectStatement) parser.parseStatement();

        // 使用visitor来访问AST
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);
        parserSelect(statement);
        return null;
    }

    private  void parserSelect(SQLSelectStatement statement) {
        SQLSelect sqlSelect = statement.getSelect();
        parserSQLSelect(sqlSelect);
    }

    private  void parserSQLSelect(SQLSelect sqlSelect) {
        parserQuery(sqlSelect.getQuery());
    }

    /**
     * 真正解析动态sql语句的核心方法
     * @param query
     */
    private  void parserQuery(SQLSelectQuery query) {
        if (query instanceof SQLSelectQueryBlock) {
            // 1.解析查询字段
            SQLSelectQueryBlock sqlSelectQueryBlock = ((SQLSelectQueryBlock) query);
            List<SQLSelectItem> selectList = sqlSelectQueryBlock.getSelectList();

            // 2.解析表，嵌套查询
            SQLTableSource from = sqlSelectQueryBlock.getFrom();
            parsingTableSource(from);

            // 3.解析where条件
            parsingWhere(sqlSelectQueryBlock.getWhere());

            // 4.解析group by分组
            SQLSelectGroupByClause groupBy = sqlSelectQueryBlock.getGroupBy();

            // 5.解析order by排序
            SQLOrderBy order = sqlSelectQueryBlock.getOrderBy();

        } else if (query instanceof SQLUnionQuery) {
            // 如果是union复合查询，拆成左右两部分查询
            parserQuery(((SQLUnionQuery) query).getLeft());
            parserQuery(((SQLUnionQuery) query).getRight());
        }
    }

    /**
     * 解析 from 表
     * @param from
     */
    private  void parsingTableSource(SQLTableSource from) {
        // 1.from子句是查询语句
        if (from instanceof SQLSubqueryTableSource) {
            SQLSelect childSelect = ((SQLSubqueryTableSource) from).getSelect();
            parserSQLSelect(childSelect);
        } else if (from instanceof SQLJoinTableSource) {
            SQLTableSource left = ((SQLJoinTableSource) from).getLeft();
            parsingTableSource(left);

            SQLTableSource right = ((SQLJoinTableSource) from).getRight();
            parsingTableSource(right);

            SQLExpr condition = ((SQLJoinTableSource) from).getCondition();
            if (condition != null) {
                parserSQLObject(condition);
            }
        } else if (from instanceof SQLUnionQueryTableSource) {
            parserQuery(((SQLUnionQueryTableSource) from).getUnion());
        }
    }

    private  <T extends SQLExpr> void parsingWhere(T where) {
        if (where == null) {
            return;
        }
        parserSQLObject(where);
    }

    private  void parserSQLObject(SQLExpr sqlObject) {
        if (sqlObject == null) {
            return;
        }
        List<SQLObject> sqlPartInfo = getMuchPart(sqlObject);
        for (SQLObject part : sqlPartInfo) {
            parsingPart(part);
        }
    }

    private void parsingPart(SQLObject part) {
        if (part instanceof SQLInListExpr) {
            handleSQLInListExpr((SQLInListExpr) part);
        } else if (part instanceof SQLBinaryOpExpr) {
            handleSQLBinaryOpExpr((SQLBinaryOpExpr) part);
        }  else if (part instanceof SQLSelectStatement) {
            parserSelect((SQLSelectStatement) part);
        } else if (part instanceof SQLInSubQueryExpr) {
            parsingInSubQuery((SQLInSubQueryExpr) part);
        } else if (part instanceof SQLSelect) {
            parserSQLSelect((SQLSelect) part);
        } else if (part instanceof SQLSelectQuery) {
            parserQuery((SQLSelectQuery) part);
        } else if (part instanceof SQLTableSource) {
            parsingTableSource((SQLTableSource) part);
        }
    }

    private void parsingInSubQuery(SQLInSubQueryExpr c) {
        SQLSelect sqlSelect = c.getSubQuery();
        SQLStatementParser sqlStatementParser = SQLParserUtils.createSQLStatementParser(parseSQL(sqlSelect.toString()), JdbcUtils.MYSQL);
        sqlSelect.setQuery(((SQLSelectStatement) sqlStatementParser.parseStatement()).getSelect().getQueryBlock());
    }

    /**
     * sql分段，比如把where条件按照表达式拆分成段
     * @param sqlObject
     * @return
     */
    private  List<SQLObject> getMuchPart(SQLObject sqlObject) {
        List<SQLObject> result = new LinkedList<>();

        if (sqlObject == null) {
            return result;
        }
        List<SQLObject> children = ((SQLExpr) sqlObject).getChildren();
        if (children != null && !children.isEmpty()) {
            for (SQLObject child : children) {
                if (child instanceof SQLExpr) {
                    List<SQLObject> grandson = ((SQLExpr) child).getChildren();
                    if (grandson == null || grandson.isEmpty()) {
                        result.add(sqlObject);
                        break;
                    } else {
                        result.addAll(getMuchPart(child));
                    }
                }
            }
        } else {
            return getMuchPart(sqlObject.getParent());
        }
        return result;
    }


    private void handleSQLInListExpr(SQLInListExpr sqlInListExpr) {
        List<SQLExpr> targetList = sqlInListExpr.getTargetList();
        SQLExpr sqlExpr = targetList.get(0);
        if (sqlExpr instanceof SQLVariantRefExpr) {
            String param = SQLUtils.toSQLString(sqlExpr);
            if (param.startsWith("@")) {
                String where = SQLUtils.toSQLString(sqlInListExpr);
                setParamToWhere(param, where);
            }
        }

    }


    private void handleSQLBinaryOpExpr(SQLBinaryOpExpr sqlBinaryOpExpr) {
        SQLExpr right = sqlBinaryOpExpr.getRight();
        if (right instanceof SQLVariantRefExpr) {
            String param = SQLUtils.toSQLString(right);
            if (param.startsWith("@")) {
                String where = SQLUtils.toSQLString(sqlBinaryOpExpr);
                setParamToWhere(param, where);
            }
        }
    }

    private void setParamToWhere(String param, String where) {
        Map<String, String> map = paramToWhere.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(param, where);
    }


    public static void main(String[] args) {
        String sql = "select a, bColumn from table1 t where t.name='张三' and id in (select id from user where org_id=100) and a>@aaa and c = @ccc and d in     @ddd and e <> @eee and f in @ffff or g in @ggg";
        DynamicSqlParser dynamicSqlParser = new DynamicSqlParser();
        dynamicSqlParser.parseSQL(sql);
    }






}
