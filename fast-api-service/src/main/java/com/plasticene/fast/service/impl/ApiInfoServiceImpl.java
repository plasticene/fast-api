package com.plasticene.fast.service.impl;

import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.fast.constant.ApiConstant;
import com.plasticene.fast.dao.ApiInfoDAO;
import com.plasticene.fast.dao.ApiReleaseDAO;
import com.plasticene.fast.dto.ApiInfoDTO;
import com.plasticene.fast.dto.ApiReleaseDTO;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.dto.Parameter;
import com.plasticene.fast.entity.ApiInfo;
import com.plasticene.fast.entity.ApiRelease;
import com.plasticene.fast.param.ApiInfoParam;
import com.plasticene.fast.parser.DynamicSqlParser;
import com.plasticene.fast.query.ApiInfoQuery;
import com.plasticene.fast.service.ApiInfoService;
import com.plasticene.fast.service.DataQueryService;
import com.plasticene.fast.service.DataSourceService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/27 10:29
 */
@Service
public class ApiInfoServiceImpl implements ApiInfoService {
    @Resource
    private ApiInfoDAO apiInfoDAO;
    @Resource
    private ApiReleaseDAO apiReleaseDAO;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private DataQueryService dataQueryService;
    @Resource
    private DataSourceService dataSourceService;
    @Resource
    private DynamicSqlParser sqlParser;

    private static final String API_SMOKE_PREFIX = "api_smoke_";

    private static final Map<String, String> PARAM_MOCK_DATA;

    static {
        PARAM_MOCK_DATA = new HashMap<>();
        PARAM_MOCK_DATA.put("string", "'abc'");
        PARAM_MOCK_DATA.put("number", "123");
        PARAM_MOCK_DATA.put("date", "'2022-08-08 00:00:00'");
        PARAM_MOCK_DATA.put("array_string", "'a','b','c'");
        PARAM_MOCK_DATA.put("array_number", "1,2,3");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addApiInfo(ApiInfoParam param) {
        ApiInfo apiInfo = PtcBeanUtils.copy(param, ApiInfo.class);
        apiInfoDAO.insert(apiInfo);
    }

    /**
     * 更新api涉及到状态的变更，如果是已发布状态，变为已更新
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApiInfo(Long id, ApiInfoParam param) {
        ApiInfo info = apiInfoDAO.selectById(id);
        ApiInfo apiInfo = PtcBeanUtils.copy(param, ApiInfo.class);
        apiInfo.setId(id);
        if (Objects.equals(info.getStatus(), ApiConstant.API_STATUS_RELEASE)) {
            apiInfo.setStatus(ApiConstant.API_STATUS_CHANGE);
        }
        apiInfoDAO.updateById(apiInfo);
    }

    /**
     * 这里需要验证配置的接口是否能正常通过，如果有动态参数需要根据数据类型一一mock生产最终SQL去执行验证
     * 这里只需要执行通过代表SQL语法没有问题即可，冒烟通过，在redis缓存一下该接口冒烟通过标志，发布时候
     * 判断是否冒烟通过
     * @param id
     */
    @Override
    public void smokeTest(Long id) {
        ApiInfo apiInfo = apiInfoDAO.selectById(id);
        Long dataSourceId = apiInfo.getDataSourceId();
        String databaseName = apiInfo.getDatabaseName();
        String sqlContent = apiInfo.getSqlContent();
        List<Parameter> param = apiInfo.getParam();
        if (!CollectionUtils.isEmpty(param)) {
            Map<String, String> paramMap = mockParam(param);
            sqlContent = sqlParser.parseSQL(sqlContent, paramMap);
        }
        String finalSql = new StringBuilder("select * from (").append(sqlContent).append(") as t limit 1").toString();
        DataSourceDTO ds = dataSourceService.getDataSourceDTO(dataSourceId);
        ds.setSelectDatabase(databaseName);
        dataQueryService.testSql(ds, finalSql);
        // 30分钟内有效
        redisTemplate.opsForValue().set(API_SMOKE_PREFIX + id, 1, 30, TimeUnit.MINUTES);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseApi(Long id) {
        Object isSmoke = redisTemplate.opsForValue().get(API_SMOKE_PREFIX + id);
        if (!Objects.equals(isSmoke, 1)) {
            throw new BizException("请先通过接口冒烟测试");
        }
        ApiInfo info = apiInfoDAO.selectById(id);
        if (Objects.equals(info.getStatus(), ApiConstant.API_STATUS_RELEASE)) {
            throw new BizException("当前保存的接口配置已发布，请勿重复发布");
        }
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setId(id);
        apiInfo.setStatus(ApiConstant.API_STATUS_RELEASE);
        apiInfo.setReleaseTime(new Date());
        apiInfoDAO.updateById(apiInfo);
        info.setId(null);
        info.setCreateTime(null);
        info.setUpdateTime(null);
        ApiRelease apiRelease = PtcBeanUtils.copy(info, ApiRelease.class);
        apiRelease.setApiInfoId(id);
        apiReleaseDAO.insert(apiRelease);
    }

    @Override
    public List<ApiReleaseDTO> releaseRecord(Long id) {
        return null;
    }

    @Override
    public void offlineApi(Long id) {

    }

    @Override
    public PageResult<ApiInfoDTO> getList(ApiInfoQuery query) {
        return null;
    }

    public ApiInfo getApiInfo(Long id) {
        return apiInfoDAO.selectById(id);
    }

    Map<String, String> mockParam(List<Parameter> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return new HashMap<>();
        }
        Map<String, String> result = new HashMap<>();
        parameters.forEach(parameter -> {
            String type = parameter.getType();
            if (Objects.equals(type, "array")) {
                String subType = parameter.getItems().get("type");
                result.put("@"+parameter.getName(), PARAM_MOCK_DATA.get(type + "_" + subType));
            } else {
                result.put("@"+parameter.getName(), PARAM_MOCK_DATA.get(type));
            }
        });
        return result;
    }
}
