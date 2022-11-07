package com.plasticene.fast.enums;

import com.plasticene.fast.constant.SqlTypeConstant;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/20 10:32
 */
@Getter
public enum DatabaseSqlEnum {


    /**
     * 数据源对应的表
     */
    MYSQL_TABLE(0,"MySQL", SqlTypeConstant.DATABASE_TABLE, "select table_comment as 'tableComment', table_name as 'tableName' from information_schema.TABLES " +
            " where TABLE_SCHEMA = '%s'"
    ),

    CLICKHOUSE_TABLE(1,"ClickHouse", SqlTypeConstant.DATABASE_TABLE, "SELECT '' as tableComment, name" +
            " AS tableName FROM system.tables WHERE database = '%s'"
    ),

    DORIS_TABLE(2,"Doris", SqlTypeConstant.DATABASE_TABLE, "select table_comment as 'tableComment', table_name as 'tableName' from information_schema.TABLES " +
            " where TABLE_SCHEMA = '%s'"
    ),

    MYSQL_TABLE_COLUMN(0, "MySQL", SqlTypeConstant.TABLE_STRUCT, "select column_name, data_type, column_type," +
            " column_comment from information_schema.COLUMNS where table_schema='%s' and table_name='%s'"
    ),

    CLICKHOUSE_TABLE_COLUMN(1,"ClickHouse", SqlTypeConstant.TABLE_STRUCT,"select name, replaceRegexpAll" +
            "(`type`, 'Nullable\\\\((.*)\\\\)', '\\\\1') AS dataType, replaceRegexpAll(`type`, 'Nullable\\\\((.*)\\\\)', '\\\\1')" +
            " AS columnType, comment from system.columns where database='%s' and table='%s'"
    ),

    DORIS_TABLE_COLUMN(2, "Doris", SqlTypeConstant.TABLE_STRUCT, "select column_name, data_type, column_type," +
            " column_comment from information_schema.COLUMNS where table_schema='%s' and table_name='%s'"
    ),

    MYSQL_TABLE_DATA(0, "MySQL", SqlTypeConstant.TABLE_DATA, "select * from %s limit %s"
    ),

    CLICKHOUSE_TABLE_DATA(1, "ClickHouse", SqlTypeConstant.TABLE_DATA, "select * from %s limit %s"
    ),

    DORIS_TABLE_DATA(2, "Doris", SqlTypeConstant.TABLE_DATA, "select * from %s limit %s"
    ),

    MYSQL_TABLE_COUNT(0, "MySQL", SqlTypeConstant.TABLE_COUNT, "select count(*) AS total from %s"
    ),

    CLICKHOUSE_TABLE_COUNT(1, "ClickHouse", SqlTypeConstant.TABLE_COUNT, "select count(*) AS total from %s"
    ),

    DORIS_TABLE_COUNT(2, "Doris", SqlTypeConstant.TABLE_COUNT, "select count(*) AS total from %s"
    );




    // 数据库类型
    Integer code;
    // 数据库名称
    String name;
    // sql类型 0：查数据库表   1：查表结构
    Integer sqlType;
    // 查询sql
    String sql;

    DatabaseSqlEnum(Integer code, String name, Integer sqlType, String sql) {
        this.code = code;
        this.name = name;
        this.sqlType = sqlType;
        this.sql = sql;
    }

    public static DatabaseSqlEnum getType(Integer code, Integer sqlType) {
        return Stream.of(DatabaseSqlEnum.values()).filter(bean -> bean.getCode().equals(code) && bean.getSqlType().equals(sqlType)).findFirst().orElse(null);
    }


}
