package com.plasticene.fast.controller;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.plasticene.boot.cache.core.manager.MultilevelCache;
import com.plasticene.boot.redis.core.anno.DistributedLock;
import com.plasticene.boot.redis.core.anno.RateLimit;
import com.plasticene.boot.redis.core.enums.LimitType;
import com.plasticene.fast.constant.CommonConstant;
import com.plasticene.fast.entity.DataSource;
import io.swagger.annotations.Api;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/21 17:20
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试样例管理")
@Slf4j
public class TestController {
    @Resource
    private ExecutorService executorService;

    @Resource
    private MultilevelCache multilevelCache;


    @GetMapping()
    public void test() {
        log.info("打印日志了");
        executorService.execute(()->{
            log.info("异步执行了");
        });
    }

    @RateLimit( period = 10, count = 3)
    @GetMapping("/test1")
    public int testLimiter1() {
        return 1;
    }


    @RateLimit(key = "customer_limit_test", period = 10, count = 3, limitType = LimitType.CUSTOMER)
    @GetMapping("/test2")
    public int testLimiter2() {

        return 1;
    }


    @RateLimit(key = "ip_limit_test", period = 10, count = 3, limitType = LimitType.IP)
    @GetMapping("/test3")
    public int testLimiter3() {

        return 1;
    }

    @GetMapping("/pdf")
    public void testPdf(HttpServletResponse response) throws IOException {
        String path = "/Users/shepherdmy/Desktop/111.pdf";
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        response.setContentType("application/pdf");
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/put/cache")
    public void put() {
        DataSource ds = new DataSource();
        ds.setName("多级缓存");
        ds.setType(1);
        ds.setCreateTime(new Date());
        ds.setHost("127.0.0.1");
        multilevelCache.put("test-key", ds);
    }

    @GetMapping("/get/cache")
    public DataSource get() {
        DataSource dataSource = multilevelCache.get("test-key", DataSource.class);
        return dataSource;
    }

    @RateLimit( period = 10, count = 3)
    @DistributedLock(key = "distributed-lock")
    @GetMapping("/lock")
    public void lock() {
        log.info("加锁成功，执行业务..." + Thread.currentThread().getId());
        try {
            TimeUnit.SECONDS.sleep(60);
            log.info("执行结束：" + Thread.currentThread().getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String text = "hello, 我们来了哦12345";
        String key = AES.generateRandomKey();
        System.out.println(key);
        String encrypt = AES.encrypt(text, key);
        System.out.println(encrypt);
    }
}
