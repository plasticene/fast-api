package com.plasticene.fast.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.param.UserLoginParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/10/31 11:33
 */
@ResponseResultBody
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
@Slf4j
public class UserController {


    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public String login(@RequestBody UserLoginParam param) {
        log.info("login param: {}", param);
        return UUID.randomUUID().toString();

    }

}
