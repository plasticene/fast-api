package com.plasticene.fast.service;

import com.plasticene.fast.dto.Column;
import com.plasticene.fast.dto.TableInfo;
import com.plasticene.fast.query.BaseQuery;
import com.plasticene.fast.vo.DataResultVO;
import com.plasticene.fast.dto.DataSourceDTO;

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
