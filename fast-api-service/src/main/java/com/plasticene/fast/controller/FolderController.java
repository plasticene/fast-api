package com.plasticene.fast.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import com.plasticene.fast.dto.FolderDTO;
import com.plasticene.fast.param.FolderParam;
import com.plasticene.fast.query.FolderQuery;
import com.plasticene.fast.service.FolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 17:27
 */
@ResponseResultBody
@RestController
@RequestMapping("/folder")
@Api(tags = "分组管理")
public class FolderController {
    @Resource
    private FolderService folderService;

    @ApiOperation("添加分组")
    @PostMapping
    public void addFolder(@RequestBody @Validated FolderParam folderParam) {
        folderService.addFolder(folderParam);
    }

    @ApiOperation("修改分组")
    @PutMapping("/{folderId}")
    public void updateFolder(@RequestBody FolderParam folderParam, @PathVariable("folderId") Long folderId) {
        folderService.updateFolder(folderId, folderParam);
    }

    @ApiOperation("分组列表")
    @GetMapping
    public IPage<FolderDTO> getList(FolderQuery query) {
        return folderService.getList(query);
    }

    @DeleteMapping
    @ApiOperation("删除分组(批量)")
    public void delFolder(@RequestBody List<Long> folderIds) {


    }



}
