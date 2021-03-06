package com.plasticene.fast.manager;

import com.plasticene.fast.entity.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 09:54
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DataSourceManagerTest {

    @Resource
    private RedisTemplate redisTemplate;


    @Test
    public void test1() {
        DataSource ds = new DataSource();
        ds.setName("中文");
        ds.setType(1);
        ds.setCreateTime(new Date());
        ds.setHost("127.0.0.1");
        redisTemplate.opsForValue().set("data_source_4", ds);
    }

    @Test
    public void testTopic() {
        DataSource ds = new DataSource();
        ds.setName("多级缓存");
        ds.setType(1);
        ds.setCreateTime(new Date());
        ds.setHost("127.0.0.1");
        redisTemplate.convertAndSend("multilevel-redis-cache", ds);
    }


}