package com.plasticene.fast.service.impl;

import com.google.common.collect.Lists;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.fast.dao.SqlQueryDAO;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.dto.SqlQueryDTO;
import com.plasticene.fast.entity.SqlQuery;
import com.plasticene.fast.param.SqlExecuteParam;
import com.plasticene.fast.param.SqlQueryParam;
import com.plasticene.fast.query.SqlQueryQuery;
import com.plasticene.fast.service.DataQueryService;
import com.plasticene.fast.service.DataSourceService;
import com.plasticene.fast.service.FolderService;
import com.plasticene.fast.service.SqlQueryService;
import com.plasticene.fast.vo.DataResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 11:14
 */

@Service
@Slf4j
public class SqlQueryServiceImpl implements SqlQueryService {
    @Resource
    private SqlQueryDAO sqlQueryDAO;
    @Resource
    private DataSourceService dataSourceService;
    @Resource
    private FolderService folderService;
    @Resource
    private DataQueryService dataQueryService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSqlQuery(SqlQueryParam param) {
        SqlQuery sqlQuery = PtcBeanUtils.copy(param, SqlQuery.class);
        sqlQueryDAO.insert(sqlQuery);
    }

    @Override
    public void updateSqlQuery(Long id, SqlQueryParam param) {
        SqlQuery sqlQuery = PtcBeanUtils.copy(param, SqlQuery.class);
        sqlQuery.setId(id);
        sqlQueryDAO.updateById(sqlQuery);
    }

    @Override
    public PageResult<SqlQueryDTO> getList(SqlQueryQuery query) {
        LambdaQueryWrapperX<SqlQuery> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(SqlQuery::getName, query.getName());
        queryWrapperX.eqIfPresent(SqlQuery::getFolderId, query.getFolderId());
        queryWrapperX.orderByDesc(SqlQuery::getId);
        PageParam pageParam = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<SqlQuery> pageResult = sqlQueryDAO.selectPage(pageParam, queryWrapperX);
        List<SqlQuery> sqlQueryList = pageResult.getList();
        PageResult<SqlQueryDTO> result = new PageResult<>();
        result.setList(toSqlQueryDTOList(sqlQueryList));
        result.setTotal(pageResult.getTotal());
        result.setPages(pageResult.getPages());
        return result;
    }

    @Override
    public DataResultVO executeSql(SqlExecuteParam param) {
        Long dataSourceId = param.getDataSourceId();
        String sqlContent = param.getSqlContent();
        Integer pageNo = param.getPageNo();
        Integer pageSize = param.getPageSize();
        DataSourceDTO ds = dataSourceService.getDataSourceDTO(dataSourceId);
        ds.setSelectDatabase(param.getDatabaseName());
        String finalSql = new StringBuilder("select * from (").append(sqlContent).append(") as t limit ")
                .append((pageNo-1)*pageSize).append(",").append(pageSize).toString();
        String countSql = new StringBuilder("select count(*) from (").append(sqlContent).append(") as t").toString();
        DataResultVO dataResultVO = new DataResultVO();
        Integer total = 0;
        StringBuilder sqlLog = new StringBuilder();
        long startTime = System.currentTimeMillis();
        try {
            dataResultVO = dataQueryService.queryResult(ds, finalSql);
            if (!CollectionUtils.isEmpty(dataResultVO.getContent())) {
                total = dataQueryService.getCount(ds, countSql);
            }
            long endTime = System.currentTimeMillis();
            BigDecimal costTime = new BigDecimal(String.valueOf(endTime - startTime)).divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);
            sqlLog.append("\n>OK\n>Time: "+costTime+"s\n\n");
            dataResultVO.setIsSuccess(true);
        } catch (Exception e) {
            dataResultVO.setIsSuccess(false);
            long endTime = System.currentTimeMillis();
            BigDecimal costTime = new BigDecimal(String.valueOf(endTime - startTime)).divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);
            sqlLog.append("\n>fail-"+e.getMessage()+"\n>time:"+costTime+"s");
        }
        dataResultVO.setSqlLog(sqlLog.toString());
        dataResultVO.setTotal(total);
        int pages = total / pageSize + ((total % pageSize) == 0 ? 0 : 1);
        dataResultVO.setPages(pages);
        dataResultVO.setCurrent(pageNo);
        dataResultVO.setSize(pageSize);
        return dataResultVO;
    }

    @Override
    public SqlQueryDTO getDetail(Long id) {
        SqlQuery sqlQuery = sqlQueryDAO.selectById(id);
        List<SqlQueryDTO> sqlQueryDTOList = toSqlQueryDTOList(Lists.newArrayList(sqlQuery));
        return sqlQueryDTOList.get(0);
    }

    List<SqlQueryDTO> toSqlQueryDTOList(List<SqlQuery> sqlQueryList) {
        List<SqlQueryDTO> sqlQueryDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(sqlQueryList)) {
            return sqlQueryDTOList;
        }
        List<Long> dataSourceIds = sqlQueryList.parallelStream().map(SqlQuery::getDataSourceId).distinct().collect(Collectors.toList());
        List<Long> folderIds = sqlQueryList.parallelStream().map(SqlQuery::getFolderId).distinct().collect(Collectors.toList());
        Map<Long, String> dataSourceMap = dataSourceService.getDataSourceMap(dataSourceIds);
        Map<Long, String> folderMap = folderService.getFolderMap(folderIds);
        sqlQueryList.forEach(sqlQuery -> {
            SqlQueryDTO sqlQueryDTO = PtcBeanUtils.copy(sqlQuery, SqlQueryDTO.class);
            sqlQueryDTO.setDataSourceName(dataSourceMap.get(sqlQuery.getDataSourceId()));
            sqlQueryDTO.setFolderName(folderMap.get(sqlQuery.getFolderId()));
            sqlQueryDTOList.add(sqlQueryDTO);
        });
        return sqlQueryDTOList;
    }
}
