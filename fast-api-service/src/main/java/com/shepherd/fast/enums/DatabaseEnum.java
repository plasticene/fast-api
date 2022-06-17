package com.shepherd.fast.enums;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/17 11:45
 */
@Getter
public enum DatabaseEnum {

    MYSQL(0, "MySQL", "jdbc:mysql://%s:%s?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&useSSL=false",
            "com.mysql.jdbc.Driver", "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA"),

    CLICKHOUSE(1, "CLICKHOUSE", "jdbc:clickhouse://%s:%s?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&useSSL=false",
            "ru.yandex.clickhouse.ClickHouseDriver", "select name from `system`.databases"),

    ORACLE(2, "ORACLE", "jdbc:oracle:thin:@%s:%s", "oracle.jdbc.driver.OracleDriver",
            "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA");
    /**
     * 类型代码
     */
    private Integer code;

    /**
     * 数据库名称
     */
    private String name;

    /**
     * 连接地址
     */
    private String url;
    /**
     * 驱动类
     */
    private String driverClass;

    private String sql;

    DatabaseEnum(Integer code, String name, String url, String driverClass, String sql) {
        this.code = code;
        this.name = name;
        this.url = url;
        this.driverClass = driverClass;
        this.sql = sql;
    }

    public static DatabaseEnum getType(Integer code) {
        return Stream.of(DatabaseEnum.values()).filter(bean -> bean.getCode().equals(code)).findFirst().orElse(null);
    }
}
