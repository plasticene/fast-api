package com.plasticene.fast.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/16 10:36
 */
@Configuration
public class RedisConfig {
    @Resource
    private RedisProperties redisProperties;

    private static final String REDISSON_PREFIX = "redis://";


    @Bean
    public RedissonClient redissonClient() {
        // 1、创建配置
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        config.useSingleServer().setAddress(REDISSON_PREFIX + host + ":" + port)
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword())
                .setConnectionMinimumIdleSize(10)
                .setTimeout(10000)
                .setRetryAttempts(3)
                .setRetryInterval(10000)
                .setPingConnectionInterval(10000);
        return Redisson.create(config);
    }
}
