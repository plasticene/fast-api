package com.plasticene.fast.service;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.fast.dto.SqlQueryDTO;
import com.plasticene.fast.param.SqlExecuteParam;
import com.plasticene.fast.param.SqlQueryParam;
import com.plasticene.fast.query.SqlQueryQuery;
import com.plasticene.fast.vo.DataResultVO;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 11:14
 */
public interface SqlQueryService {

    void addSqlQuery(SqlQueryParam param);

    void updateSqlQuery(Long id, SqlQueryParam param);

    PageResult<SqlQueryDTO> getList(SqlQueryQuery query);

    DataResultVO executeSql(SqlExecuteParam param);

    SqlQueryDTO getDetail(Long id);

}
