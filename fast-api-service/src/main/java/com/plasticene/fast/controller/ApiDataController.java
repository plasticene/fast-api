package com.plasticene.fast.controller;

import cn.hutool.core.thread.ThreadUtil;
//import com.plasticene.boot.cache.core.manager.MultilevelCache;
import com.plasticene.boot.cache.core.manager.MultilevelCache;
import com.plasticene.boot.redis.core.anno.RateLimit;
import com.plasticene.boot.redis.core.enums.LimitType;
import com.plasticene.fast.entity.DataSource;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 17:29
 */
@RestController
@RequestMapping("/fapi/")
@Api(tags = "api数据")
@Slf4j
public class ApiDataController {

}
