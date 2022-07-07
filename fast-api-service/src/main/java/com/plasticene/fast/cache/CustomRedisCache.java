package com.plasticene.fast.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 11:20
 */
public class CustomRedisCache extends RedisCache {

    /**
     * redisCache的构造方法是protected，外部不能调用，所以通过该类来new redisCache
     * @param name
     * @param cacheWriter
     * @param cacheConfig
     */
    protected CustomRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }
}
