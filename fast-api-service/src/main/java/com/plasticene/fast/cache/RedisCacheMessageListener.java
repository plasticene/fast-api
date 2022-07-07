package com.plasticene.fast.cache;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import java.util.Objects;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 15:42
 */
@Slf4j
@Data
public class RedisCacheMessageListener implements MessageListener {

    private CaffeineCache caffeineCache;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("监听的redis message: {}" + message.toString());
        CacheMessage cacheMessage = JSONObject.parseObject(message.toString(), CacheMessage.class);
        if (Objects.isNull(cacheMessage.getKey())) {
            caffeineCache.invalidate();
        } else {
            caffeineCache.evict(cacheMessage.getKey());
        }
    }
}
