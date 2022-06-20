package com.shepherd.fast.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shepherd.fast.anno.ResponseResultBody;
import com.shepherd.fast.dto.DataSourceDTO;
import com.shepherd.fast.dto.TableInfo;
import com.shepherd.fast.param.DataSourceParam;
import com.shepherd.fast.query.DataSourceQuery;
import com.shepherd.fast.service.DataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @PostMapping("/test")
    @ApiOperation("测试连接")
    public void  test(@RequestBody @Validated DataSourceParam dataSourceParam) {
        dataSourceService.test(dataSourceParam);
    }

    @PostMapping
    @ApiOperation("添加数据源")
    public void addDataSource(@RequestBody @Validated DataSourceParam dataSourceParam) {
        dataSourceService.addDataSource(dataSourceParam);
    }

    @PutMapping("/{dataSourceId}")
    @ApiOperation("更新数据源")
    public void updateDataSource(@PathVariable("dataSourceId") Long dataSourceId, @RequestBody DataSourceParam dataSourceParam) {
        dataSourceService.updateDataSource(dataSourceId, dataSourceParam);
    }

    @DeleteMapping
    @ApiOperation("删除数据源(批量)")
    public void batchDelDataSource(@RequestBody List<Long> dataSourceIds) {
        dataSourceService.batchDelDataSource(dataSourceIds);
    }

    @GetMapping
    @ApiOperation("查询数据源列表")
    public IPage<DataSourceDTO> getList(DataSourceQuery query) {
        IPage<DataSourceDTO> page = dataSourceService.getList(query);
        return page;
    }

    @GetMapping("/{dataSourceId}/{databaseName}/table")
    @ApiOperation("查询数据库表信息")
    public List<TableInfo> getTableInfo(@PathVariable("dataSourceId") Long dataSourceId, @PathVariable("databaseName") String databaseName) {
        return dataSourceService.getTableInfo(dataSourceId, databaseName);
    }

}
