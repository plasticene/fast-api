package com.shepherd.fast.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shepherd.fast.dao.FolderDAO;
import com.shepherd.fast.entity.Folder;
import com.shepherd.fast.param.FolderParam;
import com.shepherd.fast.service.FolderService;
import com.shepherd.fast.utils.FdsBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/27 10:29
 */
@Service
public class FolderServiceImpl extends ServiceImpl<FolderDAO, Folder> implements FolderService {
    @Resource
    private FolderDAO folderDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFolder(FolderParam folderParam) {
        Folder folder = FdsBeanUtils.copy(folderParam, Folder.class);
        save(folder);
    }

    @Override
    public void updateFolder(Long folderId, FolderParam folderParam) {
        Folder folder = FdsBeanUtils.copy(folderParam, Folder.class);
        folder.setId(folderId);
        updateById(folder);
    }
}
