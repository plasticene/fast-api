package com.plasticene.fast.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.plasticene.boot.cache.core.listener.CaffeineCacheRemovalListener;
import com.plasticene.boot.cache.core.manager.MultilevelCache;
import com.plasticene.boot.common.pojo.ResponseVO;
import com.plasticene.boot.redis.core.anno.DistributedLock;
import com.plasticene.boot.redis.core.anno.RateLimit;
import com.plasticene.boot.redis.core.enums.LimitType;
import com.plasticene.boot.web.core.anno.ApiSecurity;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.boot.web.core.prop.ApiSecurityProperties;
import com.plasticene.boot.web.core.utils.AESUtil;
import com.plasticene.boot.web.core.utils.RSAUtil;
import com.plasticene.boot.web.core.utils.SignUtil;
import com.plasticene.fast.dto.ServerInfo;
import com.plasticene.fast.dto.WorkshopDTO;
import com.plasticene.fast.entity.DataSource;
import com.plasticene.fast.entity.User;
import com.plasticene.fast.feign.WorkshopService;
import com.plasticene.fast.param.DataSourceParam;
import com.plasticene.fast.query.BaseQuery;
import com.plasticene.fast.query.UserQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
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

    @Resource
    private ApiSecurityProperties apiSecurityProperties;
    @Resource
    private WorkshopService workshopService;

//    @Resource
//    private TestController testController;

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("letter-pool-%d").build();
    private ExecutorService fixedThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2,
            Runtime.getRuntime().availableProcessors() * 40,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors() * 20),
            namedThreadFactory);



    @GetMapping("/async")
    @Lookup
    public void testAsync() {
        log.info("打印日志了");
        fixedThreadPool.execute(()->{
            log.info("异步执行了");
            try {
                Student student = null;
                String name = student.getName();
            } catch (Exception e) {
                log.error("异步报错了：", e);
            }

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


    @GetMapping("/heap/oom")
    public void  testHeapOom() {
        List<Student> students = new ArrayList<>();
        while (true) {
            Student student = new Student();
            students.add(student);
        }
    }

    @GetMapping("/test/user")
    public ResponseVO<User> testUser(@RequestParam("id") Long id) {
        System.out.println(id);
        User user = new User();
        user.setId(123l);
        user.setName("shepherd");
        return ResponseVO.success(user);
    }

    @PostMapping("/sign")
    @ApiSecurity(encryptResponse = true, decryptRequest = true)
    public User testSign(@RequestBody User user) {
        System.out.println(user);
        return user;
    }


    @GetMapping("/trace")
    public void testTrace() {
        log.info("开始执行咯");
        BaseQuery query = new BaseQuery();
        ResponseVO<List<WorkshopDTO>> responseVO = workshopService.getList();
        log.info("接口返回结果：{}", responseVO);
    }









    public static void main(String[] args) {
        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEArd+hCsqwEjddzpNEOmMKUjLiK4uqUhx3o7qyV7iEKIVrdcnDQHLlO0juQX+xeselFqpqe9m25xH8TO3VmZCo7RkdwxsFTuk7vvObb3/STsWPry9X1jYxilw8/ui9TcBAgY2HwkfxhO3WqEzCKDs0EI0Z+16F0P7KeGSyTgL5FoPyYvFQgMQSygqsaPe3055RCr4vhOvyfhbe1tw5X/TU5l90j3eebMzN9r6m3dCG+CmA7Gr/GRytRtzXx68Rc75Z7jkWsEMk5fentMsjaJ4hP2heqK21bI9Z7dvtNG4ZTFVO05pJXecru2eSHU9KXfOCT2e/6h5vEoLibTdd99smEJRBEnfuIfMkLAtndbWq3vQkVe+d7Z+uof3CasJrYO0MYkpcxPmZzlUbrdPRKnc3FI/J8R1toWnsDiN8MKbHj+jd6ojfk/HqIsQJcj+SWGmYS/Dh3j/XngFdFu9yWprC2bJgdVt9w5hskHi9cpaWn2BXj7DQ5SvNR9RULS3qMs8HLaTQdATMfX0FiMZl4AIsIwQv1aiHOTuw/HVc0KECSwy14YH3pY33Iu2waGBrOjmbwVYikgoOK8TCeDzC0SvWu7PRpPpwIwjppEJZVf/u/sR64+n2UGtsVT4Xk5CtlVNyyz2bQ7I6QxcThtdv86+vrVcSwhJrApr1h8aHg5pncPcCAwEAAQ==";
        String privateKey = "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";
        String aesKey = AESUtil.generateAESKey();
        String key = RSAUtil.encryptByPublicKey(aesKey, publicKey);
        System.out.println(key);
        User user = new User();
        user.setId(123l);
        user.setName("shepherd");
        user.setEmail("shepherd_zfj@163.com");
        user.setCreateTime(new Date());
        String content = JSONObject.toJSONString(user);
        String data = AESUtil.encrypt(content, aesKey);
        System.out.println(data);
        String timestamp = String.valueOf(System.currentTimeMillis());
        System.out.println("==========");
        System.out.println(timestamp);
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(nonce);
        SortedMap sortedMap = SignUtil.beanToMap(user);
        String text = SignUtil.getContent(sortedMap, nonce, timestamp);
        System.out.println(text);
        String sign = RSAUtil.signByPrivateKey(text, privateKey);
        System.out.println(sign);
        boolean b = RSAUtil.verifySignByPublicKey(text, sign, publicKey);
        System.out.println(b);
        String s = JSONObject.toJSONString(null);
        System.out.println(s);


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


    @Data
    static class Student {
        private Long id = 1l;
        private String name = "张三拉";
        private Integer age = 28;
        private Byte[] address = new Byte[2*1024*1024]; // 2m
    }





}
