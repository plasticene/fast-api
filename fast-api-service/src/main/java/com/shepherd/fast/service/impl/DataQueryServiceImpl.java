package com.shepherd.fast.service.impl;

import com.shepherd.fast.dto.DataSourceDTO;
import com.shepherd.fast.dto.TableInfo;
import com.shepherd.fast.enums.DatabaseSchemaEnum;
import com.shepherd.fast.exception.BizException;
import com.shepherd.fast.manager.DataSourceManager;
import com.shepherd.fast.service.DataQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/20 11:11
 */

@Service
@Slf4j
public class DataQueryServiceImpl implements DataQueryService {
    @Resource
    private DataSourceManager dataSourceManager;

    @Override
    public List<TableInfo> getTableList(DataSourceDTO ds) {
        List<TableInfo> tableInfos = new ArrayList<>();
        DatabaseSchemaEnum schemaEnum = DatabaseSchemaEnum.getType(ds.getType());
        String sql = String.format(schemaEnum.getSql(), ds.getSelectDatabase());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = dataSourceManager.getConn(ds);
        try {
            log.info("data query execute sql: "+sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableComment(rs.getString(1));
                tableInfo.setTableName(rs.getString(2));
                tableInfos.add(tableInfo);
            }
            return tableInfos;
        } catch (Exception e) {
            log.error("execute sql error:", e);
            throw new BizException("数据查询失败");
        } finally {
            dataSourceManager.close(conn, ps, rs);
        }
    }
}
