package com.plasticene.fast.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/3 17:20
 */
@ResponseResultBody
@RestController
@RequestMapping("/fds/auth")
@Api(tags = "权限认证管理")
public class AuthController {

    @PostMapping("/login")
    @ApiOperation("登录")
    public void login() {

    }

    @PostMapping("/code")
    @ApiOperation("验证码")
    public void code() {

    }

    @GetMapping("/info")
    @ApiOperation("获取登录人信息")
    public void info() {

    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    public void logout() {

    }

}
