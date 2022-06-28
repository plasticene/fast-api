package com.shepherd.fast.manager;

import cloud.agileframework.sql.SqlUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/28 11:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AnalyseDynamicSql {
    @Test
    public void test() {
        //声明参数集，参数集也可以是pojo对象，并支持对象嵌套，多层参数时使用点分隔形式声明占位符
        Map<String, Object> param = Maps.newHashMap();
        param.put("a", "aColumn");
        param.put("b", "b");
        param.put("c", 12);
        param.put("d", new String[]{"c1", "c2"});

        //举例使用嵌套对象
        Demo g = new Demo();
        g.setC(Lists.newArrayList("in1", "in2"));
        param.put("g", g);

        String sql = SqlUtil.parserSQL("select {a},bColumn from your_table " +
                "where c = {c} and d in {d} and e = {e} and f in {f} or g in {g}",param);
        System.out.println(sql);
    }


    @Test
    public void test2(){

        //声明参数集，参数集也可以是pojo对象，并支持对象嵌套，多层参数时使用点分隔形式声明占位符
        Map<String, Object> param = Maps.newHashMap();
        param.put("orgId", 432);
        param.put("state", new int[]{0,5,6});
        param.put("isAgent", 12);
        param.put("createTime", new Date());


        System.out.println(System.currentTimeMillis());
        String sql = SqlUtil.parserSQL(" select * from (select ca.id,ca.id_card,ca.create_time,ca.update_time,ca.create_by,ca.update_by,ca.out_serial_no,\n" +
                "        ca.serial_no,ca.name,ca.oper_status,ca.overdue_date,\n" +
                "        ca.overdue_days,ca.entrust_start_time,ca.entrust_end_time,\n" +
                "        ca.amount,ca.`desc`,ca.status,ca.field_json,ca.call_status,ca.state,ca.repair_status,ca.recovery,\n" +
                "        ca.operation_state,ca.last_follow_time,ca.follow_count,ca.sync_status,ca.operation_next_time,\n" +
                "        ca.out_serial_temp,ca.pay_amount,ca.return_time,ca.own_mobile,ca.division_time,ca.color,ca.ignore_plan,\n" +
                "        ca.cooperation_status,\n" +
                "\t\t\t\tdeb.last_follow_time debt_last_follow_time,\n" +
                "        deb.follow_count debt_follow_count,deb.type as conjoint_type,\n" +
                "\t\t\t\t ca.product_id,ca.org_delt_id,ca.user_id,ca.dep_id,ca.team_id,ca.store_id,ca.org_id,\n" +
                "        ca.out_batch_id,ca.inner_batch_id,debt_id,\n" +
                "\t\t\t\tur.name as userName,ur.status as userStatus,\n" +
                "\t\t\t\tp.type product_type,ca.vsearch_key1,ca.vsearch_key2\n" +
                "        ,ca.auto_assist_result,\n" +
                "        ca.auto_assist_date, ca.tag, ca.auto_assist_record,ca.call_type,ca.case_tag_id\n" +
                "\t\t\t\tfrom `case_info` as ca  \n" +
                "\t\t\t\tinner join `product` p on p.id=ca.product_id\n" +
                "        left join `user` as ur on ur.id=ca.user_id and ur.status=0\n" +
                "        left join `case_debtor` deb on deb.id=ca.debt_id and deb.status=0\n" +
                "\t\t\t\twhere ca.recovery = 0 and ca.org_id={orgId} and ca.state not in {state}\n" +
                "\t\t\t\tand (ca.dep_id in(select id from org_dep_team dept where dept.is_agent={isAgent})\n" +
                "                or ca.team_id in(select id from org_dep_team dept where dept.is_agent=0 and dept.under_team=1))\n" +
                "\t\t\t\torder by  ca.id desc\n" +
                "\t\t\t\tlimit 60,20) as a where is_agent=0 and create_time >= {createTime} and name = {name} \n" +
                "\t\t\t\t\t\t", param);
        System.out.println(System.currentTimeMillis());
        System.out.println(sql);
    }

    private static class Demo {
        private List<Object> c;

        public List<Object> getC() {
            return c;
        }

        public void setC(List<Object> c) {
            this.c = c;
        }
    }

}
