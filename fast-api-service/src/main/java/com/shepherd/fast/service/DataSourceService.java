package com.shepherd.fast.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shepherd.fast.dto.Column;
import com.shepherd.fast.dto.DataSourceDTO;
import com.shepherd.fast.dto.TableInfo;
import com.shepherd.fast.param.DataSourceParam;
import com.shepherd.fast.query.DataSourceQuery;

import java.util.List;

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

    IPage<DataSourceDTO> getList(DataSourceQuery query);

    List<TableInfo> getTableInfo(Long dataSourceId, String databaseName);

    List<Column> getTableStruct(Long dataSourceId, String databaseName, String tableName);


}
