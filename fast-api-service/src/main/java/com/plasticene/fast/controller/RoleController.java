package com.plasticene.fast.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/3 13:20
 */
@ResponseResultBody
@RestController
@RequestMapping("/fds/role")
@Api(tags = "角色管理")
public class RoleController {
}
