package com.plasticene.fast.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
            throw new BizException("???????????????????????????");
        } finally {
            DbUtils.closeQuietly(con, null, null);
        }
        // ????????????????????????????????????????????????????????????
        if (!CollectionUtils.isEmpty(dataSourceParam.getDatabaseList())) {
            List<String> notExistDatabase = dataSourceParam.getDatabaseList().stream().filter(database -> !databases.contains(database)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(notExistDatabase)) {
                String s = StringUtils.join(notExistDatabase);
                throw new BizException(s + "??????????????????");
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
    public PageResult<DataSourceDTO> getList(DataSourceQuery query) {
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getName())) {
            queryWrapper.likeRight(DataSource::getName, query.getName());
        }
        if (query.getType() != null) {
            queryWrapper.eq(DataSource::getType, query.getType());
        }
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
    public List<TableInfo> getTableInfo(Long dataSourceId, String databaseName) {
        DataSourceDTO ds = getDataSourceDTO(dataSourceId);
        if (Objects.isNull(ds)) {
            throw new BizException("??????????????????");
        }
        ds.setSelectDatabase(databaseName);
        List<TableInfo> tableList = dataQueryService.getTableList(ds);
        return tableList;
    }

    @Override
    public List<Column> getTableStruct(Long dataSourceId, String databaseName, String tableName) {
        DataSourceDTO ds = getDataSourceDTO(dataSourceId);
        if (Objects.isNull(ds)) {
            throw new BizException("??????????????????");
        }
        ds.setSelectDatabase(databaseName);
        List<Column> columns = dataQueryService.getTableStruct(ds, tableName);
        return columns;
    }

    @Override
    public DataResultVO getTableData(Long dataSourceId, String databaseName, String tableName, BaseQuery query) {
        DataSourceDTO ds = getDataSourceDTO(dataSourceId);
        if (Objects.isNull(ds)) {
            throw new BizException("??????????????????");
        }
        ds.setSelectDatabase(databaseName);
        DataResultVO dataResultVO = dataQueryService.getTableData(ds, tableName, query);
        return dataResultVO;
    }


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
