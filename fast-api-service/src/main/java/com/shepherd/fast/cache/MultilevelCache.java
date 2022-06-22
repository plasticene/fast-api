package com.shepherd.fast.cache;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 15:27
 */

@Slf4j
public class MultilevelCache extends AbstractValueAdaptingCache {

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("cache-pool-%d").build();

    private static ExecutorService cacheExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 40,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors() * 20),
            namedThreadFactory);

    private RedisCache redisCache;
    private CaffeineCache caffeineCache;

    public MultilevelCache(boolean allowNullValues,RedisCache redisCache, CaffeineCache caffeineCache) {
        super(allowNullValues);
        this.redisCache = redisCache;
        this.caffeineCache = caffeineCache;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if (Objects.isNull(value)) {
            // 异步从db打数据到本地缓存
            cacheExecutor.submit(() -> doAsyncLoad(key, valueLoader));
            return null;
        }
        return (T) value;
    }

    @SneakyThrows
    <T> void doAsyncLoad(Object key, Callable<T> valueLoader) {
        put(key, valueLoader.call());
    }


    @Override
    public void put(@NonNull Object key, Object value) {
        Assert.notNull(key, "key不可为空");
        caffeineCache.put(key, value);
        /*
        注意：redis缓存的对象object必须序列化 implements Serializable, 不然缓存对象不成功。
         */
        redisCache.put(key, value);

    }

    @Override
    public ValueWrapper putIfAbsent(@NonNull Object key, Object value) {
        Assert.notNull(key, "key不可为空");
        caffeineCache.putIfAbsent(key, value);
        return redisCache.putIfAbsent(key, value);
    }


    @Override
    public void evict(Object key) {
        log.info("enter#evict");
        redisCache.evict(key);
        caffeineCache.evict(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        return false;
    }

    @Override
    public void clear() {
        redisCache.clear();
        caffeineCache.clear();
    }

    @Override
    public boolean invalidate() {
        return false;
    }

    @Override
    protected Object lookup(Object key) {
        Assert.notNull(key, "key不可为空");
        ValueWrapper value = caffeineCache.get(key);
        if (Objects.nonNull(value)) {
            log.info("查询caffeine 一级缓存 key:{}, 返回值是:{}", key, value);
            return value.get();
        }
        value = Optional.ofNullable(redisCache.get(key)).orElse(null);
        if (Objects.nonNull(value)) {
            log.info("查询redis 二级缓存 key:{}, 返回值是:{}", key, value);
            return value.get();
        }
        return null;
    }


}
