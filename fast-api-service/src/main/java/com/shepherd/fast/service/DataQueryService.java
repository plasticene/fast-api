package com.shepherd.fast.service;

import com.shepherd.fast.dto.Column;
import com.shepherd.fast.dto.DataSourceDTO;
import com.shepherd.fast.dto.TableInfo;
import com.shepherd.fast.query.BaseQuery;
import com.shepherd.fast.vo.DataResultVO;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/20 11:08
 */
public interface DataQueryService {

    List<TableInfo> getTableList(DataSourceDTO ds);

    List<Column> getTableStruct(DataSourceDTO ds, String tableName);

    DataResultVO getTableData(DataSourceDTO ds, String tableName, BaseQuery query);

    DataResultVO queryResult(DataSourceDTO ds, String sql);


}
