package com.plasticene.fast.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.Column;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.dto.TableInfo;
import com.plasticene.fast.param.DataSourceParam;
import com.plasticene.fast.query.BaseQuery;
import com.plasticene.fast.query.DataSourceQuery;
import com.plasticene.fast.service.DataSourceService;
import com.plasticene.fast.vo.DataResultVO;
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
    public PageResult<DataSourceDTO> getList(DataSourceQuery query) {
        PageResult<DataSourceDTO> pageResult = dataSourceService.getList(query);
        return pageResult;
    }

    @GetMapping("/database/table")
    @ApiOperation("查询数据库表信息")
    public List<TableInfo> getTableInfo(@RequestParam("dataSourceId") Long dataSourceId, @RequestParam("databaseName") String databaseName) {
        return dataSourceService.getTableInfo(dataSourceId, databaseName);
    }


    @GetMapping("/table/struct")
    @ApiOperation("查询表结构信息")
    public List<Column> getTableStruct(@RequestParam("dataSourceId") Long dataSourceId, @RequestParam("databaseName")
            String databaseName, @RequestParam("tableName") String tableName) {
        return dataSourceService.getTableStruct(dataSourceId, databaseName, tableName);
    }

    @GetMapping("/table/data")
    @ApiOperation("查询表数据")
    public DataResultVO getTableData(@RequestParam("dataSourceId") Long dataSourceId, @RequestParam("databaseName")
            String databaseName, @RequestParam("tableName") String tableName, BaseQuery query) {
        return dataSourceService.getTableData(dataSourceId, databaseName, tableName, query);

    }



}
