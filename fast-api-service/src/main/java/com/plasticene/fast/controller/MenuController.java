package com.plasticene.fast.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.MenuDTO;
import com.plasticene.fast.param.MenuParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/31 16:39
 */
@ResponseResultBody
@RestController
@RequestMapping("/fds/menu")
@Api(tags = "菜单管理")
public class MenuController {

    @ApiOperation("添加菜单")
    @PostMapping
    public void addMenu(@RequestBody MenuParam param) {

    }

    @ApiOperation("更新菜单")
    @PutMapping("/{id}")
    public void updateMenu(@PathVariable("id") Long id, @RequestBody MenuParam param) {

    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable("id") Long id) {

    }

    @ApiOperation("菜单列表")
    @GetMapping
    public List<MenuDTO> getList() {
        return null;
    }


}
