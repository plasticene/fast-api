package com.plasticene.fast.service.impl;

import com.plasticene.boot.common.exception.BizException;
import com.plasticene.fast.constant.SqlTypeConstant;
import com.plasticene.fast.dto.Column;
import com.plasticene.fast.dto.TableInfo;
import com.plasticene.fast.manager.DataSourceManager;
import com.plasticene.fast.query.BaseQuery;
import com.plasticene.fast.service.DataQueryService;
import com.plasticene.fast.utils.DateUtils;
import com.plasticene.fast.vo.DataResultVO;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.enums.DatabaseSqlEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

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
        DatabaseSqlEnum schemaEnum = DatabaseSqlEnum.getType(ds.getType(), SqlTypeConstant.DATABASE_TABLE);
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

    @Override
    public List<Column> getTableStruct(DataSourceDTO ds, String tableName) {
        List<Column> columns = new ArrayList<>();
        DatabaseSqlEnum schemaEnum = DatabaseSqlEnum.getType(ds.getType(), SqlTypeConstant.TABLE_STRUCT);
        String sql = String.format(schemaEnum.getSql(), ds.getSelectDatabase(), tableName);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = dataSourceManager.getConn(ds);
        try {
            log.info("data query execute sql: "+sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                Column column = new Column();
                column.setColumnName(rs.getString(1));
                column.setDataType(rs.getString(2));
                column.setColumnType(rs.getString(3));
                column.setColumnComment(rs.getString(4));
                columns.add(column);
            }
            return columns;
        } catch (Exception e) {
            log.error("execute sql error:", e);
            throw new BizException("数据查询失败");
        } finally {
            dataSourceManager.close(conn, ps, rs);
        }
    }

    @Override
    public DataResultVO getTableData(DataSourceDTO ds, String tableName, BaseQuery query) {
        Integer pageSize = query.getPageSize();
        Integer pageNo = query.getPageNo();
        StringBuilder page = new StringBuilder(String.valueOf((pageNo - 1) * pageSize)).append(",").append(pageSize);
        String sql = DatabaseSqlEnum.getType(ds.getType(), SqlTypeConstant.TABLE_DATA).getSql();
        sql = String.format(sql, tableName, page.toString());
        String countSql = DatabaseSqlEnum.getType(ds.getType(), SqlTypeConstant.TABLE_COUNT).getSql();
        countSql = String.format(countSql, tableName);
        Integer total = getCount(ds, countSql);
        DataResultVO dataResultVO = queryResult(ds, sql);
        dataResultVO.setTotal(total);
        int pages = total / pageSize + ((total % pageSize) == 0 ? 0 : 1);
        dataResultVO.setPages(pages);
        dataResultVO.setCurrent(pageNo);
        dataResultVO.setSize(pageSize);
        return dataResultVO;
    }

    @Override
    public DataResultVO queryResult(DataSourceDTO ds, String sql) {
        List<Map<String, Object>> content = new ArrayList<>();
        List<Column> head = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dataSourceManager.getConn(ds);
            log.info("data provide execute sql: "+sql);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            //获取列
            ResultSetMetaData md = rs.getMetaData();
            int columnSize = md.getColumnCount();
            for (int i = 1; i <= columnSize; i++) {
                Column column = new Column();
                column.setIndex(i);
                column.setColumnName(md.getColumnLabel(i));
                head.add(column);
            }
            //封装所有记录
            while (rs.next()) {
                Map<String ,Object> value = new HashMap<>();
                for (int i = 1; i <= columnSize; i++) {
                    String columnTypeName = md.getColumnTypeName(i);
                    Object obj;
                    if("TIMESTAMP".equals(columnTypeName) || "DATETIME".equals(columnTypeName)){
                        obj = DateUtils.formatFullDate(rs.getTimestamp(i));
                    }else{
                        obj = rs.getObject(i);
                    }
                    value.put(md.getColumnLabel(i), obj);
                }
                content.add(value);
            }
            DataResultVO dataResult = new DataResultVO();
            dataResult.setHead(head);
            dataResult.setContent(content);
            return dataResult;
        } catch (Exception e) {
            log.error("execute sql error: {}", sql);
            throw new BizException("数据查询失败");
        } finally {
            //关闭进行回收资源
            dataSourceManager.close(conn, pstmt, rs);
        }
    }

    Integer getCount(DataSourceDTO ds, String sql) {
        int total = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = dataSourceManager.getConn(ds);
        try {
            log.info("data query execute sql: "+sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            log.error("execute sql error:", e);
            throw new BizException("数据查询失败");
        } finally {
            dataSourceManager.close(conn, ps, rs);
        }
        return total;
    }
}
