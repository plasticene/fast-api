package com.shepherd.fast.cache;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import javax.annotation.Resource;
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

    @Resource
    private MultilevelCacheProperties multilevelCacheProperties;
    @Resource
    private RedisTemplate redisTemplate;

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
        return multilevelCacheProperties.getName();

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


    /**
     *  注意：redis缓存的对象object必须序列化 implements Serializable, 不然缓存对象不成功。
     * @param key
     * @param value
     */
    @Override
    public void put(@NonNull Object key, Object value) {
        Assert.notNull(key, "key不可为空");
        caffeineCache.put(key, value);
        asyncPublish(key, value);
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
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，
        // 避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        redisCache.evict(key);
        asyncPublish(key, null);
        caffeineCache.evict(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        return false;
    }

    @Override
    public void clear() {
        redisCache.clear();
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(multilevelCacheProperties.getName());
        asyncPublish(null, null);
        caffeineCache.clear();
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
            // 异步将二级缓存redis写到一级缓存caffeine
            ValueWrapper finalValue = value;
            cacheExecutor.execute(()->{
                caffeineCache.put(key, finalValue.get());
            });
            return value.get();
        }
        return null;
    }

    /**
     * 缓存变更时通知其他节点清理本地缓存
     * 异步通过发布订阅主题消息，其他节点监听到之后进行相关本地缓存操作，防止本地缓存脏数据
     */
    void asyncPublish(Object key, Object value) {
        cacheExecutor.execute(()->{
            CacheMessage cacheMessage = new CacheMessage();
            cacheMessage.setCacheName(multilevelCacheProperties.getName());
            cacheMessage.setKey(key);
            cacheMessage.setValue(value);
            redisTemplate.convertAndSend(multilevelCacheProperties.getTopic(), cacheMessage);
        });
    }


}
