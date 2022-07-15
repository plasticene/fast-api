package com.plasticene.fast.controller;

import cn.hutool.core.thread.ThreadUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 17:29
 */
@RestController
@RequestMapping("/api/data")
@Api(tags = "api数据")
@Slf4j
public class ApiDataController {
    @Resource
    private ExecutorService executorService;

    @GetMapping()
    public void test() {
        log.info("打印日志了");
        executorService.execute(()->{
            log.info("异步执行了");
        });
    }
}
