package com.plasticene.fast.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.fast.dto.Column;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.dto.TableInfo;
import com.plasticene.fast.param.DataSourceParam;
import com.plasticene.fast.query.BaseQuery;
import com.plasticene.fast.query.DataSourceQuery;
import com.plasticene.fast.vo.DataResultVO;

import java.util.List;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 11:40
 */
public interface DataSourceService {

    void test(DataSourceParam dataSourceParam);

    void addDataSource(DataSourceParam dataSourceParam);

    void updateDataSource(Long dataSourceId, DataSourceParam dataSourceParam);

    void batchDelDataSource(List<Long> dataSourceIds);

    void changeDataSourceStatus(List<Long> dataSourceIds, Integer status);

    PageResult<DataSourceDTO> getList(DataSourceQuery query);

    List<DataSourceDTO> getList();

    List<TableInfo> getTableInfo(Long dataSourceId, String databaseName);

    List<Column> getTableStruct(Long dataSourceId, String databaseName, String tableName);

    DataResultVO getTableData(Long dataSourceId, String databaseName, String tableName, BaseQuery query);

    Map<Long, String> getDataSourceMap(List<Long> dataSourceIds);

    DataSourceDTO getDataSourceDTO(Long dataSourceId);


}
