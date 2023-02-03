package com.plasticene.fast.controller;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.UserDTO;
import com.plasticene.fast.param.UserLoginParam;
import com.plasticene.fast.param.UserParam;
import com.plasticene.fast.query.UserQuery;
import com.plasticene.fast.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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


    @ApiOperation("添加用户")
    @PostMapping
    public void addUser(@RequestBody UserParam param) {
        userService.addUser(param);
    }

    @ApiOperation("更新用户")
    @PutMapping("/{id}")
    public void updateUser(@PathVariable("id") Long id, @RequestBody UserParam param) {
        userService.updateUser(id, param);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @ApiOperation("员工列表")
    @GetMapping
    public PageResult<UserDTO> getList(UserQuery query) {
        return userService.getList(query);
    }


}
