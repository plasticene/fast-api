package com.plasticene.fast.controller;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.ApiInfoDTO;
import com.plasticene.fast.dto.ApiReleaseDTO;
import com.plasticene.fast.param.ApiInfoParam;
import com.plasticene.fast.query.ApiInfoQuery;
import com.plasticene.fast.service.ApiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 17:25
 */
@ResponseResultBody
@RestController
@RequestMapping("/api/info")
@Api(tags = "api管理")
public class ApiInfoController {
    @Resource
    private ApiInfoService apiInfoService;

    @PostMapping
    @ApiOperation("添加api")
    public Long addApiInfo(@RequestBody @Validated ApiInfoParam param) {
        return apiInfoService.addApiInfo(param);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改api")
    public void updateApiInfo(@RequestBody ApiInfoParam param, @PathVariable("id") Long id) {
        apiInfoService.updateApiInfo(id, param);
    }

    @PostMapping("/smoke/{id}")
    @ApiOperation("冒烟测试api")
    public void smokeTest(@PathVariable("id") Long id) {
        apiInfoService.smokeTest(id);
    }


    @PostMapping("/release/{id}")
    @ApiOperation("发布api")
    public void releaseApi(@PathVariable("id") Long id) {
        apiInfoService.releaseApi(id);
    }

    @GetMapping("/release/record")
    @ApiOperation("api发布历史记录")
    public List<ApiReleaseDTO> releaseRecord(@RequestParam("id") Long id) {
        return apiInfoService.releaseRecord(id);
    }


    @PostMapping("/offline/{id}")
    @ApiOperation("下线api")
    public void offlineApi(@PathVariable("id") Long id) {
        apiInfoService.offlineApi(id);
    }

    @GetMapping
    @ApiOperation("api列表")
    public PageResult<ApiInfoDTO> getList(ApiInfoQuery query) {
        PageResult<ApiInfoDTO> result = apiInfoService.getList(query);
        return result;
    }

    @GetMapping("/{id}")
    @ApiOperation("查询api详情")
    public ApiInfoDTO getApiInfo(@PathVariable("id") Long id) {
        return apiInfoService.getApiInfo(id);
    }




}
