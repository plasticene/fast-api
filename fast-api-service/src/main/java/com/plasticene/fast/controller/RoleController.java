package com.plasticene.fast.controller;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.RoleDTO;
import com.plasticene.fast.param.RoleParam;
import com.plasticene.fast.query.RoleQuery;
import com.plasticene.fast.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @Resource
    private RoleService roleService;

    @ApiOperation("添加角色")
    @PostMapping
    public void addRole(@RequestBody @Validated RoleParam param) {
        roleService.addRole(param);
    }

    @ApiOperation("更新角色")
    @PutMapping("/{id}")
    public void updateRole(@PathVariable("id") Long id, @RequestBody RoleParam param) {
        roleService.updateRole(id, param);
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
    }

    @ApiOperation("角色列表")
    @GetMapping
    public PageResult<RoleDTO> getList(RoleQuery query) {
        return roleService.getList(query);
    }
}
