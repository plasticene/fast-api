package com.shepherd.fast.service;

import com.shepherd.fast.param.FolderParam;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/27 10:28
 */
public interface FolderService {

    void addFolder(FolderParam folderParam);

    void updateFolder(Long folderId, FolderParam folderParam);
}
