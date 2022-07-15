package com.plasticene.fast.manager;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.fast.enums.DatabaseEnum;
import com.plasticene.fast.dto.DataSourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/17 15:44
 */
@Component
@Slf4j
public class DataSourceManager {

    /**
     * 5分钟过期，使用本地缓存数据源dataSource，有效减少数据库连接池的初始化，当然初始化大概100ms~200ms，并不是很多
     * orm框架的dataSource，在项目启动就是注入到容器中了，单例的，等于说只需要初始化一次，而我们这里需要根据动态数据源
     * 创建dataSource，如果每次都说new一个新的dataSource，在获取连接时都需要初始化数据源连接池，具体源码可看获取连接
     * 代码：{@link DruidDataSource}的getConnection()方法
     * 这里并不没有用多级缓存，也没有考虑分布式，只使用本地缓存dataSource，原因如下：
     * ① dataSource连接池初始化本身并不需要多长时间，即时分布式访问不同服务器，第一次都进行初始化也问题不大
     * ② DruidDataSource不支持序列，所以不能放在redis里面
     * ③ 使用多级缓存 如果是从redis读取性能并和直接初始化相差无几，所以没别要
     */
    private Cache<String, DataSource> datasourceMap = Caffeine.newBuilder()
            .removalListener((key, value, cause) -> {
                // 过期移除datasource时需要显式关闭pool.
                if (Objects.nonNull(value)) {
                    ((DruidDataSource)value).close();
                }
            })
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    public DataSource getDataSource(DataSourceDTO ds) throws SQLException {
        Integer type = ds.getType();
        DatabaseEnum databaseEnum = DatabaseEnum.getType(type);
        String driverClass = databaseEnum.getDriverClass();
        String jdbcUrl = String.format(databaseEnum.getUrl(), ds.getHost(), ds.getPort(), ds.getSelectDatabase());
        String key = jdbcUrl + ds.getUserName() + ds.getPassword();
        String md5key = DigestUtils.md5DigestAsHex(key.getBytes());
        if (datasourceMap.asMap().containsKey(md5key)) {
            return datasourceMap.getIfPresent(md5key);
        }
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName(ds.getName());
        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(ds.getUserName());
        druidDataSource.setPassword(ds.getPassword());
        druidDataSource.setDriverClassName(driverClass);
        druidDataSource.setConnectionErrorRetryAttempts(3);       //失败后重连次数
        druidDataSource.setBreakAfterAcquireFailure(true);
        datasourceMap.put(md5key, druidDataSource);
        return druidDataSource;
    }


    public void removeDataSourceCache(DataSourceDTO ds) {
        Integer type = ds.getType();
        DatabaseEnum databaseEnum = DatabaseEnum.getType(type);
        String jdbcUrl = String.format(databaseEnum.getUrl(), ds.getHost(), ds.getPort(), ds.getSelectDatabase());
        String key = jdbcUrl + ds.getUserName() + ds.getPassword();
        String md5key = DigestUtils.md5DigestAsHex(key.getBytes());
        if (datasourceMap.asMap().containsKey(md5key)) {
            datasourceMap.asMap().remove(md5key);
        }

    }

    /**
     * 获取连接
     * @param ds
     * @return
     */
    public Connection getConn(DataSourceDTO ds) {
        try {
            DataSource dataSource = getDataSource(ds);
            return dataSource.getConnection();
        } catch (SQLException e) {
            // 连接失败，清除对应的缓存。
            removeDataSourceCache(ds);
            log.error("data source connection error", e);
            throw new BizException("数据库连接失败");
        }
    }



    /**
     * 关闭连接
     *
     * @param conn
     * @param ps
     * @param rs
     */
    public  void close(Connection conn, Statement ps, ResultSet rs) {
        DbUtils.closeQuietly(conn, ps, rs);
    }
}
