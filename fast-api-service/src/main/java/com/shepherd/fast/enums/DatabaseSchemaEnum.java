package com.shepherd.fast.enums;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/20 10:32
 */
@Getter
public enum DatabaseSchemaEnum {


    /**
     * 数据源对应的表
     */
    MYSQL_TABLE(0,"MySQL", "select case when LENGTH(table_comment)>0 then " +
            " table_comment else table_name  end tableComment ," +
            " table_name as 'tableName' from information_schema.TABLES " +
            " where TABLE_SCHEMA = '%s'"
    ),

    CLICKHOUSE_TABLE(1,"ClickHouse","SELECT name as tableComment, name AS tableName FROM `system`.tables " +
            " WHERE database = '%s'"
    );


    Integer code;
    String name;
    String sql;

    DatabaseSchemaEnum(Integer code, String name, String sql) {
        this.code = code;
        this.name = name;
        this.sql = sql;
    }

    public static DatabaseSchemaEnum getType(Integer code) {
        return Stream.of(DatabaseSchemaEnum.values()).filter(bean -> bean.getCode().equals(code)).findFirst().orElse(null);
    }


}
