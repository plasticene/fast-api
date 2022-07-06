package com.shepherd.fast.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shepherd.fast.dto.FolderDTO;
import com.shepherd.fast.param.FolderParam;
import com.shepherd.fast.query.FolderQuery;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/27 10:28
 */
public interface FolderService {

    void addFolder(FolderParam folderParam);

    void updateFolder(Long folderId, FolderParam folderParam);

    IPage<FolderDTO> getList(FolderQuery folderQuery);

}
