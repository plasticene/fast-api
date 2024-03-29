package com.plasticene.fast.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.fast.constant.CommonConstant;
import com.plasticene.fast.dao.DataSourceDAO;
import com.plasticene.fast.dto.Column;
import com.plasticene.fast.entity.DataSource;
import com.plasticene.fast.enums.DatabaseEnum;
import com.plasticene.fast.query.BaseQuery;
import com.plasticene.fast.query.DataSourceQuery;
import com.plasticene.fast.service.DataSourceService;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.dto.TableInfo;
import com.plasticene.fast.param.DataSourceParam;
import com.plasticene.fast.service.DataQueryService;
import com.plasticene.fast.utils.FdsBeanUtils;
import com.plasticene.fast.vo.DataResultVO;
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
import java.util.*;
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
    @Resource
    private DataQueryService dataQueryService;


    @Override
    public void test(DataSourceParam dataSourceParam) {
        List<String> databases = new ArrayList<>();
        Connection con = null;
        try {
            DatabaseEnum databaseEnum = DatabaseEnum.getType(dataSourceParam.getType());
            Class.forName(databaseEnum.getDriverClass());
            con = DriverManager.getConnection(String.format(databaseEnum.getTestUrl(), dataSourceParam.getHost(), dataSourceParam.getPort()), dataSourceParam.getUserName(), dataSourceParam.getPassword());
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
    public void changeDataSourceStatus(List<Long> dataSourceIds, Integer status) {
        if (CollectionUtils.isEmpty(dataSourceIds)) {
            return;
        }
        LambdaUpdateWrapper<DataSource> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(DataSource::getId, dataSourceIds);
        updateWrapper.set(DataSource::getStatus, status);
        dataSourceDAO.update(new DataSource(), updateWrapper);
    }


    @Override
    public PageResult<DataSourceDTO> getList(DataSourceQuery query) {
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getName())) {
            queryWrapper.likeRight(DataSource::getName, query.getName());
        }
        if (query.getType() != null) {
            queryWrapper.eq(DataSource::getType, query.getType());
        }
        if (query.getStatus() != null) {
            queryWrapper.eq(DataSource::getStatus, query.getStatus());
        }
        queryWrapper.orderByDesc(DataSource::getId);
        PageParam pageParam = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<DataSource> pageResult = dataSourceDAO.selectPage(pageParam, queryWrapper);
        List<DataSource> dataSources = pageResult.getList();
        List<DataSourceDTO> dataSourceDTOList = toDataSourceDTOList(dataSources);
        PageResult<DataSourceDTO> result = new PageResult<>();
        result.setList(dataSourceDTOList);
        result.setTotal(pageResult.getTotal());
        result.setPages(pageResult.getPages());
        return result;
    }

    @Override
    public List<DataSourceDTO> getList() {
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getStatus, CommonConstant.IS_OPEN);
        queryWrapper.orderByDesc(DataSource::getId);
        List<DataSource> dataSources = dataSourceDAO.selectList(queryWrapper);
        return toDataSourceDTOList(dataSources);
    }

    @Override
    public List<TableInfo> getTableInfo(Long dataSourceId, String databaseName) {
        DataSourceDTO ds = getDataSourceDTO(dataSourceId);
        if (Objects.isNull(ds)) {
            throw new BizException("数据源不存在");
        }
        ds.setSelectDatabase(databaseName);
        List<TableInfo> tableList = dataQueryService.getTableList(ds);
        return tableList;
    }

    @Override
    public List<Column> getTableStruct(Long dataSourceId, String databaseName, String tableName) {
        DataSourceDTO ds = getDataSourceDTO(dataSourceId);
        if (Objects.isNull(ds)) {
            throw new BizException("数据源不存在");
        }
        ds.setSelectDatabase(databaseName);
        List<Column> columns = dataQueryService.getTableStruct(ds, tableName);
        return columns;
    }

    @Override
    public DataResultVO getTableData(Long dataSourceId, String databaseName, String tableName, BaseQuery query) {
        DataSourceDTO ds = getDataSourceDTO(dataSourceId);
        if (Objects.isNull(ds)) {
            throw new BizException("数据源不存在");
        }
        ds.setSelectDatabase(databaseName);
        DataResultVO dataResultVO = dataQueryService.getTableData(ds, tableName, query);
        return dataResultVO;
    }

    @Override
    public Map<Long, String> getDataSourceMap(List<Long> dataSourceIds) {
        if (CollectionUtils.isEmpty(dataSourceIds)) {
            return new HashMap<>();
        }
        LambdaQueryWrapperX<DataSource> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.select(DataSource::getId, DataSource::getName);
        queryWrapperX.in(DataSource::getId, dataSourceIds);
        List<DataSource> dataSources = dataSourceDAO.selectList(queryWrapperX);
        Map<Long, String> map = dataSources.parallelStream().collect(Collectors.toMap(DataSource::getId, DataSource::getName));
        return map;
    }


    @Override
    public DataSourceDTO getDataSourceDTO(Long dataSourceId) {
        DataSource dataSource = dataSourceDAO.selectById(dataSourceId);
        return toDataSourceDTO(dataSource);
    }

    List<DataSourceDTO> toDataSourceDTOList(List<DataSource> dataSources) {
        List<DataSourceDTO> dataSourceDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dataSources)) {
            return dataSourceDTOList;
        }
        dataSources.forEach(dataSource -> {
            DataSourceDTO dataSourceDTO = FdsBeanUtils.copy(dataSource, DataSourceDTO.class);
            dataSourceDTOList.add(dataSourceDTO);
        });
        return dataSourceDTOList;
    }

    DataSourceDTO toDataSourceDTO(DataSource dataSource) {
        if (dataSource == null) {
            return null;
        }
        DataSourceDTO dataSourceDTO = FdsBeanUtils.copy(dataSource, DataSourceDTO.class);
        return dataSourceDTO;

    }
}
