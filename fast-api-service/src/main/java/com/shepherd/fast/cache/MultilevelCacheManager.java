package com.shepherd.fast.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 10:21
 */
@Component
//@EnableConfigurationProperties(MultilevelCacheProperties.class)
public class MultilevelCacheManager {

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("cache-pool-%d").build();

    private static ExecutorService cacheExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 40,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors() * 20),
            namedThreadFactory);

    @Resource
    private MultilevelCacheProperties multilevelCacheProperties;


    @Bean
    public RedisCache redisCache (@Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.of(multilevelCacheProperties.getAccessExpireTime(), ChronoUnit.SECONDS));
        RedisCache redisCache = new CustomRedisCache(multilevelCacheProperties.getRedisName(), redisCacheWriter, redisCacheConfiguration);
        return redisCache;
    }

    /**
     * 由于Caffeine 不会再值过期后立即执行清除，而是在写入或者读取操作之后执行少量维护工作，或者在写入读取很少的情况下，偶尔执行清除操作。
     * 如果我们项目写入或者读取频率很高，那么不用担心。如果想入写入和读取操作频率较低，那么我们可以通过Cache.cleanUp()或者加scheduler去定时执行清除操作。
     * Scheduler可以迅速删除过期的元素，***Java 9 +***后的版本，可以通过Scheduler.systemScheduler(), 调用系统线程，达到定期清除的目的
     * @return
     */
    @Bean
    public CaffeineCache caffeineCache() {
        int maxCapacity = (int) (Runtime.getRuntime().totalMemory() * multilevelCacheProperties.getMaxCapacityRate());
        int initCapacity = (int) (maxCapacity * multilevelCacheProperties.getInitRate());
        CaffeineCache caffeineCache = new CaffeineCache(multilevelCacheProperties.getCaffeineName(), Caffeine.newBuilder()
                // 设置初始缓存大小
                .initialCapacity(initCapacity)
                // 设置最大缓存
                .maximumSize(maxCapacity)
                // 设置缓存线程池
                .executor(cacheExecutor)
                // 设置定时任务执行过期清除操作
//                .scheduler(Scheduler.systemScheduler())
                // 监听器(超出最大缓存)
                .removalListener(new CaffeineCacheRemovalListener())
                // 设置缓存读时间的过期时间
                .expireAfterAccess(Duration.of(multilevelCacheProperties.getAccessExpireTime(), ChronoUnit.SECONDS))
                // 开启metrics监控
                .recordStats()
                .build());
        return caffeineCache;
    }


    @Bean
    public MultilevelCache multilevelCache(@Autowired RedisCache redisCache, @Autowired CaffeineCache caffeineCache) {
        MultilevelCache multilevelCache = new MultilevelCache(true, redisCache, caffeineCache);
        return multilevelCache;
    }

    @Bean
    public RedisCacheMessageListener redisCacheMessageListener(@Autowired CaffeineCache caffeineCache) {
        RedisCacheMessageListener redisCacheMessageListener = new RedisCacheMessageListener();
        redisCacheMessageListener.setCaffeineCache(caffeineCache);
        return redisCacheMessageListener;
    }



    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(@Autowired RedisConnectionFactory redisConnectionFactory,
                                                                       @Autowired RedisCacheMessageListener redisCacheMessageListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(redisCacheMessageListener, new ChannelTopic(multilevelCacheProperties.getTopic()));
        return redisMessageListenerContainer;
    }






}
