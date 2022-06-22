package com.shepherd.fast.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 15:42
 */
@Slf4j
public class RedisCacheMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("监听到key:{}" + message.toString());

    }
}
