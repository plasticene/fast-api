package com.shepherd.fast.manager;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableMap;
import com.shepherd.fast.dto.DataSourceDTO;
import com.shepherd.fast.enums.DatabaseEnum;
import com.shepherd.fast.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/17 15:44
 */
@Component
@Slf4j
public class DataSourceManager {


    /**
     * 5分钟过期
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
        if (datasourceMap.asMap().containsKey(key)) {
            return datasourceMap.getIfPresent(key);
        }
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName(ds.getName());
        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(ds.getUserName());
        druidDataSource.setPassword(ds.getPassword());
        druidDataSource.setDriverClassName(driverClass);
        druidDataSource.setConnectionErrorRetryAttempts(3);       //失败后重连次数
        druidDataSource.setBreakAfterAcquireFailure(true);
        datasourceMap.put(key, druidDataSource);
        return druidDataSource;
    }


    public void removeDataSourceCache(DataSourceDTO ds) {
        Integer type = ds.getType();
        DatabaseEnum databaseEnum = DatabaseEnum.getType(type);
        String jdbcUrl = String.format(databaseEnum.getUrl(), ds.getHost(), ds.getPort(), ds.getSelectDatabase());
        String key = jdbcUrl + ds.getUserName() + ds.getPassword();
        if (datasourceMap.asMap().containsKey(key)) {
            datasourceMap.asMap().remove(key);
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
