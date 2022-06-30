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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/29 11:28
 */
public class DynamicSqlParser {

    public static final String CONSTANT_CONDITION_REGEX = "((OR|AND|LIKE)[\\s]+1[\\s]*=[\\s]*1)|(1[\\s]*=[\\s]*1[\\s]+(OR|AND|LIKE))|(^1[\\s]*=[\\s]*1)";
    /**
     * 常量表达式
     */
    public static final String CONSTANT_CONDITION = "1 = 1";

    public static final ThreadLocal<Map<String, String>> paramToWhere = new ThreadLocal<>();

    public static String parseSQL(String sql) {
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

    private static void parserSelect(SQLSelectStatement statement) {
        SQLSelect sqlSelect = statement.getSelect();
        parserSQLSelect(sqlSelect);
    }

    private static void parserSQLSelect(SQLSelect sqlSelect) {
        parserQuery(sqlSelect.getQuery());
    }

    /**
     * 真正解析动态sql语句的核心方法
     * @param query
     */
    private static void parserQuery(SQLSelectQuery query) {

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
    private static void parsingTableSource(SQLTableSource from) {
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
                SQLExpr newCondition = parsingWhereConstant(condition);
                ((SQLJoinTableSource) from).setCondition(newCondition);
            }
        } else if (from instanceof SQLUnionQueryTableSource) {
            parserQuery(((SQLUnionQueryTableSource) from).getUnion());
        }
    }

    private static <T extends SQLExpr> void parsingWhere(T where) {
        if (where == null) {
            return;
        }
        parserSQLObject(where);

//        SQLObject parent = where.getParent();

//        SQLExpr newWhere;
//        if (parent instanceof SQLSelectQueryBlock) {
//            newWhere = ((SQLSelectQueryBlock) parent).getWhere();
//            SQLExpr newParseWhere = parsingWhereConstant(newWhere);
//            ((SQLSelectQueryBlock) parent).setWhere(newParseWhere);
//        } else if (parent instanceof SQLUpdateStatement) {
//            newWhere = ((SQLUpdateStatement) parent).getWhere();
//            SQLExpr newParseWhere = parsingWhereConstant(newWhere);
//            ((SQLUpdateStatement) parent).setWhere(newParseWhere);
//        } else {
//            newWhere = ((SQLDeleteStatement) parent).getWhere();
//            SQLExpr newParseWhere = parsingWhereConstant(newWhere);
//            ((SQLDeleteStatement) parent).setWhere(newParseWhere);
//        }
    }

    public static void parserSQLObject(SQLExpr sqlObject) {
        if (sqlObject == null) {
            return;
        }
        List<SQLObject> sqlPartInfo = getMuchPart(sqlObject);
        for (SQLObject part : sqlPartInfo) {
            parsingPart(part);
        }
    }

    private static void parsingPart(SQLObject part) {
        if (part instanceof SQLInListExpr) {
        } else if (part instanceof SQLBinaryOpExpr) {
        } else if (part instanceof SQLSelectQueryBlock) {
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

    private static void parsingInSubQuery(SQLInSubQueryExpr c) {
        SQLSelect sqlSelect = c.getSubQuery();
        SQLStatementParser sqlStatementParser = SQLParserUtils.createSQLStatementParser(parseSQL(sqlSelect.toString()), JdbcUtils.MYSQL);
        sqlSelect.setQuery(((SQLSelectStatement) sqlStatementParser.parseStatement()).getSelect().getQueryBlock());
    }

    /**
     * sql分段，比如把where条件按照表达式拆分成段
     * @param sqlObject
     * @return
     */
    private static List<SQLObject> getMuchPart(SQLObject sqlObject) {
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

    public static SQLExpr parsingWhereConstant(SQLExpr sqlExpr) {
        String where = SQLUtils.toMySqlString(sqlExpr);
        where = where.replaceAll(CONSTANT_CONDITION_REGEX, "").trim();
        final int minSize = 3;
        if (where.trim().length() < minSize || CONSTANT_CONDITION.equals(where)) {
            return null;
        }
        sqlExpr = SQLUtils.toMySqlExpr(where);
        if (where.contains(CONSTANT_CONDITION)) {
            return parsingWhereConstant(sqlExpr);
        } else {
            return sqlExpr;
        }
    }

    private static void handleSQLInListExpr(SQLInListExpr sqlInListExpr) {


    }


    private static void handleSQLBinaryOpExpr(SQLBinaryOpExpr sqlBinaryOpExpr) {
        SQLExpr right = sqlBinaryOpExpr.getRight();
        if (right instanceof SQLVariantRefExpr) {
            String param = SQLUtils.toSQLString(right);
            if (param.startsWith("@")) {
                String where = SQLUtils.toSQLString(sqlBinaryOpExpr);
                setParamToWhere(param, where);
            }
        }
    }

    private static void setParamToWhere(String param, String where) {
        Map<String, String> map = paramToWhere.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(param, where);
    }


    public static void main(String[] args) {

        String sql = "select a, bColumn from table1 t where t.name='张三' and id in (select id from user where org_id=100) and a>@aaa and c = @ccc and d in     @ddd and e <> @eee and f in @ffff or g in @ggg";
        DynamicSqlParser.parseSQL(sql);
    }






}
