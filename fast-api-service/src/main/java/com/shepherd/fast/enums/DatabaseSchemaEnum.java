package com.shepherd.fast.enums;

import com.shepherd.fast.constant.SqlTypeConstant;
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
    MYSQL_TABLE(0,"MySQL", SqlTypeConstant.DATABASE_TABLE, "select case when LENGTH(table_comment)>0 then " +
            " table_comment else table_name  end tableComment, table_name as 'tableName' from information_schema.TABLES " +
            " where TABLE_SCHEMA = '%s'"
    ),

    CLICKHOUSE_TABLE(1,"ClickHouse", SqlTypeConstant.DATABASE_TABLE, "SELECT name as tableComment, name" +
            " AS tableName FROM system.tables WHERE database = '%s'"
    ),

    MYSQL_TABLE_COLUMN(0, "MySQL", SqlTypeConstant.TABLE_STRUCT, "select column_name, data_type, column_type," +
            " column_comment from information_schema.COLUMNS where table_schema='%s' and table_name='%s'"
    ),

    CLICKHOUSE_TABLE_COLUMN(1,"ClickHouse", SqlTypeConstant.TABLE_STRUCT,"select name, replaceRegexpAll" +
            "(`type`, 'Nullable\\\\((.*)\\\\)', '\\\\1') AS dataType, replaceRegexpAll(`type`, 'Nullable\\\\((.*)\\\\)', '\\\\1')" +
            " AS columnType, comment from system.columns where database='%s' and table='%s'"
    );


    // 数据库类型
    Integer code;
    // 数据库名称
    String name;
    // sql类型 0：查数据库表   1：查表结构
    Integer sqlType;
    // 查询sql
    String sql;

    DatabaseSchemaEnum(Integer code, String name, Integer sqlType, String sql) {
        this.code = code;
        this.name = name;
        this.sqlType = sqlType;
        this.sql = sql;
    }

    public static DatabaseSchemaEnum getType(Integer code, Integer sqlType) {
        return Stream.of(DatabaseSchemaEnum.values()).filter(bean -> bean.getCode().equals(code) && bean.getSqlType().equals(sqlType)).findFirst().orElse(null);
    }


}
