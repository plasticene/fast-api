package com.shepherd.fast.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shepherd.fast.constant.CommonConstant;
import com.shepherd.fast.dao.DataSourceDAO;
import com.shepherd.fast.dto.DataSourceDTO;
import com.shepherd.fast.entity.DataSource;
import com.shepherd.fast.param.DataSourceParam;
import com.shepherd.fast.query.DataSourceQuery;
import com.shepherd.fast.service.DataSourceService;
import com.shepherd.fast.utils.FdsBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 13:45
 */
@Service
@Slf4j
public class DataSourceServiceImpl implements DataSourceService {
    @Resource
    private DataSourceDAO dataSourceDAO;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDataSource(DataSourceParam dataSourceParam) {
        DataSource dataSource = FdsBeanUtils.copy(dataSourceParam, DataSource.class);
        dataSource.setCreateTime(new Date());
        dataSource.setUpdateTime(new Date());
        dataSourceDAO.insert(dataSource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataSource(Long dataSourceId, DataSourceParam dataSourceParam) {
        DataSource dataSource = FdsBeanUtils.copy(dataSourceParam, DataSource.class);
        dataSource.setId(dataSourceId);
        dataSource.setUpdateTime(new Date());
        dataSourceDAO.updateById(dataSource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelDataSource(List<Long> dataSourceIds) {
        if (CollectionUtils.isEmpty(dataSourceIds)) {
            return;
        }
        dataSourceDAO.deleteBatchIds(dataSourceIds);
    }

    @Override
    public IPage<DataSourceDTO> getList(DataSourceQuery query) {
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getIsDelete, CommonConstant.IS_NOT_DEL);
        if (StringUtils.isNotBlank(query.getName())) {
            queryWrapper.likeRight(DataSource::getName, query.getName());
        }
        if (query.getType() != null) {
            queryWrapper.eq(DataSource::getType, query.getType());
        }
        IPage<DataSource> pageParam = new Page(query.getPageNo(), query.getPageSize());
        IPage<DataSource> dataSourceIPage = dataSourceDAO.selectPage(pageParam, queryWrapper);
        List<DataSource> dataSources = dataSourceIPage.getRecords();
        List<DataSourceDTO> dataSourceDTOList = toDataSourceDTOList(dataSources);
        Page result = FdsBeanUtils.copy(dataSourceIPage, Page.class);
        result.setRecords(dataSourceDTOList);
        return result;
    }

    List<DataSourceDTO> toDataSourceDTOList(List<DataSource> dataSources) {
        List<DataSourceDTO> dataSourceDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dataSources)) {
            return dataSourceDTOList;
        }
        dataSources.forEach(dataSource -> {
            DataSourceDTO dataSourceDTO = FdsBeanUtils.copy(dataSource, DataSourceDTO.class);
            dataSourceDTO.setDatabaseList(JSONObject.parseArray(dataSource.getDatabase(), String.class));
            dataSourceDTOList.add(dataSourceDTO);
        });
        return dataSourceDTOList;
    }
}
