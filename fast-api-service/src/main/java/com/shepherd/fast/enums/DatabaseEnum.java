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

    MYSQL(0, "MySQL", "com.mysql.jdbc.Driver",
            "jdbc:mysql://%s:%s?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&useSSL=false",
             "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&useSSL=false",
             "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA"),

    CLICKHOUSE(1, "CLICKHOUSE", "ru.yandex.clickhouse.ClickHouseDriver",
            "jdbc:clickhouse://%s:%s?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&useSSL=false",
             "jdbc:clickhouse://%s:%s/%s?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&useSSL=false",
             "select name from `system`.databases"),

    ORACLE(2, "ORACLE", "oracle.jdbc.driver.OracleDriver",
            "jdbc:oracle:thin:@%s:%s",
            "jdbc:oracle:thin:@//%s:%s/%s",
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
     * 驱动类
     */
    private String driverClass;

    /**
     * 连接地址不带数据数据库，用于测试连接
     */
    private String testUrl;

    /**
     * 连接地址，带数据库，用于数据库操作。
     */
    private String url;

    /**
     * 测试接口
     */
    private String sql;



    DatabaseEnum(Integer code, String name, String driverClass, String testUrl,  String url, String sql) {
        this.code = code;
        this.name = name;
        this.driverClass = driverClass;
        this.testUrl = testUrl;
        this.url = url;
        this.sql = sql;
    }

    public static DatabaseEnum getType(Integer code) {
        return Stream.of(DatabaseEnum.values()).filter(bean -> bean.getCode().equals(code)).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        String url = DatabaseEnum.getType(0).getUrl();
        url = String.format(url, "127.0.0.1", "3306", null);
        System.out.println(url);
    }
}
