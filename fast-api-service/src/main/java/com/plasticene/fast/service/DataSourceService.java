package com.plasticene.fast.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plasticene.fast.dto.Column;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.dto.TableInfo;
import com.plasticene.fast.param.DataSourceParam;
import com.plasticene.fast.query.BaseQuery;
import com.plasticene.fast.query.DataSourceQuery;
import com.plasticene.fast.vo.DataResultVO;

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

    DataResultVO getTableData(Long dataSourceId, String databaseName, String tableName, BaseQuery query);


}
