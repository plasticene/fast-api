package com.shepherd.fast.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shepherd.fast.constant.CommonConstant;
import com.shepherd.fast.dao.DataSourceDAO;
import com.shepherd.fast.dto.DataSourceDTO;
import com.shepherd.fast.entity.DataSource;
import com.shepherd.fast.enums.DatabaseEnum;
import com.shepherd.fast.exception.BizException;
import com.shepherd.fast.param.DataSourceParam;
import com.shepherd.fast.query.DataSourceQuery;
import com.shepherd.fast.service.DataSourceService;
import com.shepherd.fast.utils.FdsBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public void test(DataSourceParam dataSourceParam) {
        List<String> databases = new ArrayList<>();
        Connection con = null;
        try {
            DatabaseEnum databaseEnum = DatabaseEnum.getType(dataSourceParam.getType());
            Class.forName(databaseEnum.getDriverClass());
            con = DriverManager.getConnection(String.format(databaseEnum.getUrl(), dataSourceParam.getHost(), dataSourceParam.getPort()), dataSourceParam.getUserName(), dataSourceParam.getPassword());
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = databaseEnum.getSql();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                databases.add(rs.getString(1));
            }
        } catch (Exception e) {
            log.error("test data source error", e);
            throw new BizException("测试数据库连接错误");
        } finally {
            DbUtils.closeQuietly(con, null, null);
        }
        // 选择了数据库，那么就要判断数据库是否存在
        if (!CollectionUtils.isEmpty(dataSourceParam.getDatabaseList())) {
            List<String> notExistDatabase = dataSourceParam.getDatabaseList().stream().filter(database -> !databases.contains(database)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(notExistDatabase)) {
                String s = StringUtils.join(notExistDatabase);
                throw new BizException(s + "数据库不存在");
            }

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDataSource(DataSourceParam dataSourceParam) {
        DataSource dataSource = FdsBeanUtils.copy(dataSourceParam, DataSource.class);
        dataSource.setDatabaseName(JSON.toJSONString(dataSourceParam.getDatabaseList()));
        dataSource.setCreateTime(new Date());
        dataSource.setUpdateTime(new Date());
        dataSourceDAO.insert(dataSource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataSource(Long dataSourceId, DataSourceParam dataSourceParam) {
        DataSource dataSource = FdsBeanUtils.copy(dataSourceParam, DataSource.class);
        if (!CollectionUtils.isEmpty(dataSourceParam.getDatabaseList())) {
            dataSource.setDatabaseName(JSON.toJSONString(dataSourceParam.getDatabaseList()));
        }
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
            dataSourceDTO.setDatabaseList(JSONObject.parseArray(dataSource.getDatabaseName(), String.class));
            dataSourceDTOList.add(dataSourceDTO);
        });
        return dataSourceDTOList;
    }
}
