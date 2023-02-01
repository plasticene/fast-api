package com.plasticene.fast.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.param.UserLoginParam;
import com.plasticene.fast.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/10/31 11:33
 */
@ResponseResultBody
@RestController
@RequestMapping("/fds/user")
@Api(tags = "用户管理")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;


}
