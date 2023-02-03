package com.plasticene.fast.controller;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.plasticene.boot.cache.core.manager.MultilevelCache;
import com.plasticene.boot.redis.core.anno.DistributedLock;
import com.plasticene.boot.redis.core.anno.RateLimit;
import com.plasticene.boot.redis.core.enums.LimitType;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.ServerInfo;
import com.plasticene.fast.entity.DataSource;
import com.plasticene.fast.param.DataSourceParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/21 17:20
 */
@RestController
@RequestMapping("/fds/test")
@Api(tags = "测试样例管理")
@Slf4j
@ResponseResultBody
public class TestController {
    @Resource
    private ExecutorService executorService;

    @Resource
    private MultilevelCache multilevelCache;

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("letter-pool-%d").build();
    private ExecutorService fixedThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2,
            Runtime.getRuntime().availableProcessors() * 40,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors() * 20),
            namedThreadFactory);



    @GetMapping()
    public void test() {
        log.info("打印日志了");
        fixedThreadPool.execute(()->{
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


    @ApiOperation("测试log")
    @PostMapping("/biz/log")
    @LogRecord(success = "添加了一个数据源名称为：{{#dataSource.name}}, host：{{#dataSource.host}}", type = "1", bizNo = "123")
    public void testLog(@RequestBody DataSourceParam dataSource) {
        System.out.println(dataSource);
        String s = dataSource.getType().toString();
        System.out.println(s);

    }



    @GetMapping("/auth")
    public String testAuth() {
        return "auth pass, success back";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/auth/admin")
    public String testAuthAdmin() {
        return "admin";
    }

    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @GetMapping("/auth/normal")
    public String testAuthNormal() {
        return "normal";
    }


    @DeleteMapping("/del")
    public void delTest(@RequestBody DataSourceParam param) {
        System.out.println(param);
    }












//    @GetMapping("/server/info")
//    public ServerInfo getServerInfo() {
//
//        ServerInfo info = new ServerInfo();
//        info.setSystemUuid(DmcUtils.getSystemUuid());
//        info.setCpuSerial(DmcUtils.getCpuId());
//        info.setMainBoardSerial(DmcUtils.getMainBordId());
//        return info;
//    }
//
//    @GetMapping("/license")
//    @License
//    public Integer testLicense() {
//        return 1;
//    }



    public static void main(String[] args) {
        String text = "hello, 我们来了哦12345";
        String key = AES.generateRandomKey();
        System.out.println(key);
        String encrypt = AES.encrypt(text, key);
        System.out.println(encrypt);
    }
}
