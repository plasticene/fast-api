package com.plasticene.fast.controller;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.SqlQueryDTO;
import com.plasticene.fast.param.SqlExecuteParam;
import com.plasticene.fast.param.SqlQueryParam;
import com.plasticene.fast.query.SqlQueryQuery;
import com.plasticene.fast.service.SqlQueryService;
import com.plasticene.fast.vo.DataResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 09:53
 */
@RequestMapping("/fds/sql/query")
@Api(tags = "SQL查询管理")
@RestController
@ResponseResultBody
public class SqlQueryController {
    @Resource
    private SqlQueryService sqlQueryService;

    @PostMapping
    @ApiOperation("添加SQL查询")
    public void addSqlQuery(@RequestBody @Validated SqlQueryParam param) {
        sqlQueryService.addSqlQuery(param);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新sql查询")
    public void updateSqlQuery(@RequestBody SqlQueryParam param, @PathVariable("id") Long id) {
        sqlQueryService.updateSqlQuery(id, param);
    }

    @GetMapping
    @ApiOperation("SQL查询列表")
    public PageResult<SqlQueryDTO> getList(SqlQueryQuery query) {
        PageResult<SqlQueryDTO> result = sqlQueryService.getList(query);
        return result;
    }

    @GetMapping("/{id}")
    @ApiOperation("SQL查询列表")
    public SqlQueryDTO getDetail(@PathVariable("id") Long id) {
        return sqlQueryService.getDetail(id);
    }

    @ApiOperation("执行查询SQL")
    @PostMapping("/execute")
    public DataResultVO executeSql(@RequestBody SqlExecuteParam param) {
        DataResultVO dataResultVO = sqlQueryService.executeSql(param);
        return dataResultVO;
    }

}
