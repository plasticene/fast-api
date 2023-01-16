package com.plasticene.fast.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.param.LoginParam;
import com.plasticene.fast.service.AuthService;
import com.plasticene.fast.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @Resource
    private AuthService authService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public LoginVO login(@RequestBody LoginParam param) {
        return authService.login(param);
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
