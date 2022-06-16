package com.shepherd.fast.controller;

import com.shepherd.fast.anno.ResponseResultBody;
import com.shepherd.fast.param.DataSourceParam;
import com.shepherd.fast.service.DataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/15 15:40
 */
@ResponseResultBody
@RestController
@RequestMapping("/data/source")
@Api(tags = "数据源管理")
public class DataSourceController {


    @Resource
    private DataSourceService dataSourceService;


    @PostMapping
    @ApiOperation("添加数据源")
    public void addDataSource(@RequestBody @Validated DataSourceParam dataSourceParam) {
        dataSourceService.addDataSource(dataSourceParam);
    }

    @GetMapping("/test")
    @ApiOperation("测试")
    public String test() {
        return null;
    }
}
