package com.plasticene.fast.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.fast.dto.FolderDTO;
import com.plasticene.fast.param.FolderParam;
import com.plasticene.fast.query.FolderQuery;

import java.util.List;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/27 10:28
 */
public interface FolderService {

    void addFolder(FolderParam folderParam);

    void updateFolder(Long folderId, FolderParam folderParam);

    PageResult<FolderDTO> getList(FolderQuery folderQuery);

    Map<Long, String> getFolderMap(List<Long> folderIds);

    List<FolderDTO> getList(Integer type);

}
