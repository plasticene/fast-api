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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/29 11:28
 */
@Component
@Slf4j
public class DynamicSqlParser {

    /**
     * 常量表达式
     */
    public static final String CONSTANT_CONDITION = "1 = 1";

    public static final ThreadLocal<Map<String, Set<String>>> varToWhere = new ThreadLocal<>();

    public String parseSQL(String sql, Map<String, String> params) {
        String beautySQL = parseSQL(sql);
        Map<String, Set<String>> map = varToWhere.get();
        varToWhere.remove();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            String var = entry.getKey();
            Set<String> wheres = entry.getValue();
            String replaceWhere = null;
            for(String where : wheres) {
                // 没有传这个参数，那么把这个条件变成1=1
                if (!params.containsKey(var)) {
                    replaceWhere = CONSTANT_CONDITION;
                } else {
                    replaceWhere = where.replaceAll(var, params.get(var));
                }
                beautySQL = beautySQL.replace(where, replaceWhere);
            }
        }
        return beautySQL;
    }

    /**
     * 通过druid生成sql语法树，方便后续对sql解析
     * @param sql
     * @return
     */
    public String parseSQL(String sql) {
        // 新建 MySQL Parser
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcUtils.MYSQL);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLSelectStatement statement = (SQLSelectStatement) parser.parseStatement();

        // 使用visitor来访问AST
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);
        // 解析sql
        parseSelect(statement);

        // druid格式化之后的sql
        String beautySQL = SQLUtils.toSQLString(statement);

        return beautySQL;
    }

    private  void parseSelect(SQLSelectStatement statement) {
        SQLSelect sqlSelect = statement.getSelect();
        parseSQLSelect(sqlSelect);
    }

    private  void parseSQLSelect(SQLSelect sqlSelect) {
        parseQuery(sqlSelect.getQuery());
    }

    /**
     * 真正解析动态sql语句的核心方法
     * @param query
     */
    private  void parseQuery(SQLSelectQuery query) {
        if (query instanceof SQLSelectQueryBlock) {
            // 1.解析查询字段
            SQLSelectQueryBlock sqlSelectQueryBlock = ((SQLSelectQueryBlock) query);
            List<SQLSelectItem> selectList = sqlSelectQueryBlock.getSelectList();

            // 2.解析表，嵌套查询
            SQLTableSource from = sqlSelectQueryBlock.getFrom();
            parseTableSource(from);

            // 3.解析where条件
            parseWhere(sqlSelectQueryBlock.getWhere());

            // 4.解析group by分组
            SQLSelectGroupByClause groupBy = sqlSelectQueryBlock.getGroupBy();

            // 5.解析order by排序
            SQLOrderBy order = sqlSelectQueryBlock.getOrderBy();

        } else if (query instanceof SQLUnionQuery) {
            // 如果是union复合查询，拆成左右两部分查询
            parseQuery(((SQLUnionQuery) query).getLeft());
            parseQuery(((SQLUnionQuery) query).getRight());
        }
    }

    /**
     * 解析 from table表
     * @param from
     */
    private  void parseTableSource(SQLTableSource from) {
        // 1.from子句是查询语句
        if (from instanceof SQLSubqueryTableSource) {
            SQLSelect childSelect = ((SQLSubqueryTableSource) from).getSelect();
            parseSQLSelect(childSelect);
        } else if (from instanceof SQLJoinTableSource) {
            SQLTableSource left = ((SQLJoinTableSource) from).getLeft();
            parseTableSource(left);

            SQLTableSource right = ((SQLJoinTableSource) from).getRight();
            parseTableSource(right);

            SQLExpr condition = ((SQLJoinTableSource) from).getCondition();
            if (condition != null) {
                parseWhere(condition);
            }
        } else if (from instanceof SQLUnionQueryTableSource) {
            parseQuery(((SQLUnionQueryTableSource) from).getUnion());
        }
    }


    /**
     * 解析where添加语句， 这里的sqlObject一定是一个SQLExpr
     * @param sqlObject
     */
    private  void parseWhere(SQLExpr sqlObject) {
        if (sqlObject == null) {
            return;
        }
        List<SQLObject> sqlPartInfo = getMuchPart(sqlObject);
        for (SQLObject part : sqlPartInfo) {
            parsePart(part);
        }
    }

    private void parsePart(SQLObject part) {
        if (part instanceof SQLInListExpr) {
            handleSQLInListExpr((SQLInListExpr) part);
        } else if (part instanceof SQLBinaryOpExpr) {
            handleSQLBinaryOpExpr((SQLBinaryOpExpr) part);
        }  else if (part instanceof SQLSelectStatement) {
            parseSelect((SQLSelectStatement) part);
        } else if (part instanceof SQLInSubQueryExpr) {
            parseInSubQuery((SQLInSubQueryExpr) part);
        } else if (part instanceof SQLSelect) {
            parseSQLSelect((SQLSelect) part);
        } else if (part instanceof SQLSelectQuery) {
            parseQuery((SQLSelectQuery) part);
        } else if (part instanceof SQLTableSource) {
            parseTableSource((SQLTableSource) part);
        } else if (part instanceof SQLPropertyExpr) {
            parsePart(part.getParent());
        }
    }

    /**
     * 子查询，再次调用parseSQL解析
     * @param c
     */
    private void parseInSubQuery(SQLInSubQueryExpr c) {
        SQLSelect sqlSelect = c.getSubQuery();
        String sql = SQLUtils.toSQLString(sqlSelect);
        parseSQL(sql);
    }


    /**
     * sql分段，比如把where条件按照表达式拆分成段
     * 方便对每一个where条件进行分析
     * @param sqlObject
     * @return
     */
    private  List<SQLObject> getMuchPart(SQLObject sqlObject) {
        List<SQLObject> result = new LinkedList<>();

        if (sqlObject == null) {
            return result;
        }
        // 获取孩子
        List<SQLObject> children = ((SQLExpr) sqlObject).getChildren();
        // 如果孩子不为空
        if (children != null && !children.isEmpty()) {
            // 遍历孩子节点
            for (SQLObject child : children) {
                // 如果是SQLExpr
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
            // 孩子为空
            return getMuchPart(sqlObject.getParent());
        }
        return result;
    }


    private void handleSQLInListExpr(SQLInListExpr sqlInListExpr) {
        List<SQLExpr> targetList = sqlInListExpr.getTargetList();
        SQLExpr sqlExpr = targetList.get(0);
        if (sqlExpr instanceof SQLVariantRefExpr) {
            String var = SQLUtils.toSQLString(sqlExpr);
            if (var.startsWith("@")) {
                String where = SQLUtils.toSQLString(sqlInListExpr);
                setVarToWhere(var, where);
            }
        }

    }


    private void handleSQLBinaryOpExpr(SQLBinaryOpExpr sqlBinaryOpExpr) {
        SQLExpr right = sqlBinaryOpExpr.getRight();
        if (right instanceof SQLVariantRefExpr) {
            String var = SQLUtils.toSQLString(right);
            if (var.startsWith("@")) {
                String where = SQLUtils.toSQLString(sqlBinaryOpExpr);
                setVarToWhere(var, where);
            }
        }
    }

    private void setVarToWhere(String var, String where) {
        Map<String, Set<String>> map = varToWhere.get();
        if (map == null) {
            map = new HashMap<>();
        }
        Set<String> wheres = map.get(var);
        if (wheres == null) {
            wheres = new HashSet<>();
        }
        wheres.add(where);
        map.put(var, wheres);
        varToWhere.set(map);
    }


    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("@orgId", "1604");
        params.put("@aaa", "'2022-06-30 16:05:00'");
        params.put("@ddd", "'adc', 'sup', 'top'");
        params.put("@fff", "6,7,8,9,10000");
        String sql = "select a, b,c, (select id from user where dep_id=@depId) from table1 t where t.name='张三' and t.id in (select id from user where org_id=@orgId) and a>@aaa and c = @ccc and d in     @ddd and e <> @eee and (f in @fff or g in @ggg)";
        DynamicSqlParser dynamicSqlParser = new DynamicSqlParser();
        String beautySQL = dynamicSqlParser.parseSQL(sql, params);
        System.out.println(beautySQL);

//        Map<String, String> params = new HashMap<>();
//        params.put("@orgId", "432");
//        params.put("@createTime", "'2022-06-30 16:05:00'");
//        params.put("@states", "6,7,8,9,10000");
//        params.put("@idCard", "'34476657345'");
//
//        String sql = " select * from (select ca.id,ca.id_card,ca.create_time,ca.update_time,ca.create_by,ca.update_by,ca.out_serial_no,\n" +
//                "        ca.serial_no,ca.name,ca.oper_status,ca.overdue_date,\n" +
//                "        ca.overdue_days,ca.entrust_start_time,ca.entrust_end_time,\n" +
//                "        ca.amount,ca.`desc`,ca.status,ca.field_json,ca.call_status,ca.state,ca.repair_status,ca.recovery,\n" +
//                "        ca.operation_state,ca.last_follow_time,ca.follow_count,ca.sync_status,ca.operation_next_time,\n" +
//                "        ca.out_serial_temp,ca.pay_amount,ca.return_time,ca.own_mobile,ca.division_time,ca.color,ca.ignore_plan,\n" +
//                "        ca.cooperation_status,\n" +
//                "\t\t\t\tdeb.last_follow_time debt_last_follow_time,\n" +
//                "        deb.follow_count debt_follow_count,deb.type as conjoint_type,\n" +
//                "\t\t\t\t ca.product_id,ca.org_delt_id,ca.user_id,ca.dep_id,ca.team_id,ca.store_id,ca.org_id,\n" +
//                "        ca.out_batch_id,ca.inner_batch_id,debt_id,\n" +
//                "\t\t\t\tur.name as userName,ur.status as userStatus,\n" +
//                "\t\t\t\tp.type product_type,ca.vsearch_key1,ca.vsearch_key2\n" +
//                "        ,ca.auto_assist_result,\n" +
//                "        ca.auto_assist_date, ca.tag, ca.auto_assist_record,ca.call_type,ca.case_tag_id\n" +
//                "\t\t\t\tfrom `case_info` as ca  \n" +
//                "\t\t\t\tinner join `product` p on p.id=ca.product_id and p.org_id=@orgId\n" +
//                "        left join `user` as ur on ur.id=ca.user_id and ur.status=0\n" +
//                "        left join `case_debtor` deb on deb.id=ca.debt_id and deb.status=0\n" +
//                "\t\t\t\twhere ca.recovery = 0 and ca.org_id=@orgId and ca.state not in @states\n" +
//                "\t\t\t\tand (ca.dep_id in(select id from org_dep_team dept where dept.is_agent=@isAgent)\n" +
//                "                or ca.team_id in(select id from org_dep_team dept where dept.is_agent=0 and dept.under_team=1))\n" +
//                "\t\t\t\torder by  ca.id desc\n" +
//                "\t\t\t\tlimit 60,20) as a where is_agent=0 and create_time >= @createTime and name = @name and id_card=@idCard\n" +
//                "\t\t\t\t\t\t";
//        DynamicSqlParser dynamicSqlParser = new DynamicSqlParser();
//        String beautySQL = dynamicSqlParser.parseSQL(sql, params);
//        System.out.println(beautySQL);
    }






}
