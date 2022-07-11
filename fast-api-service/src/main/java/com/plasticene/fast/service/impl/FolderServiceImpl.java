package com.plasticene.fast.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plasticene.fast.dao.FolderDAO;
import com.plasticene.fast.service.FolderService;
import com.plasticene.fast.constant.CommonConstant;
import com.plasticene.fast.dto.FolderDTO;
import com.plasticene.fast.entity.Folder;
import com.plasticene.fast.param.FolderParam;
import com.plasticene.fast.query.FolderQuery;
import com.plasticene.fast.utils.FdsBeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public IPage<FolderDTO> getList(FolderQuery query) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getName())) {
            queryWrapper.likeRight(Folder::getName, query.getName());
        }
        IPage<Folder> pageParam = new Page(query.getPageNo(), query.getPageSize());
        IPage<Folder> folderIPage = folderDAO.selectPage(pageParam, queryWrapper);
        List<Folder> folders = folderIPage.getRecords();
        List<FolderDTO> dataSourceDTOList = toFolderDTOList(folders);
        Page result = FdsBeanUtils.copy(folderIPage, Page.class);
        result.setRecords(dataSourceDTOList);
        return result;
    }

    List<FolderDTO> toFolderDTOList(List<Folder> folders) {
        List<FolderDTO> folderDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(folders)) {
            return folderDTOList;
        }
        folders.forEach(folder -> {
            FolderDTO folderDTO = FdsBeanUtils.copy(folder, FolderDTO.class);
            folderDTOList.add(folderDTO);
        });
        return folderDTOList;
    }
}
