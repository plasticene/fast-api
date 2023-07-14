package com.plasticene.fast.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
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
import com.plasticene.fast.service.FolderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private FolderService folderService;
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
        PARAM_MOCK_DATA.put("array_date", "'2022-08-08 00:00:00', '2022-09-09 00:00:00'");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addApiInfo(ApiInfoParam param) {
        String path = param.getPath();
        ApiInfo info = getApiInfoByPath(path);
        if (Objects.nonNull(info)) {
            throw new BizException("路径已存在，请修改");
        }
        ApiInfo apiInfo = PtcBeanUtils.copy(param, ApiInfo.class);
        apiInfoDAO.insert(apiInfo);
        return apiInfo.getId();
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
        redisTemplate.delete(API_SMOKE_PREFIX + id);
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
        LambdaQueryWrapperX<ApiRelease> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(ApiRelease::getApiInfoId, id);
        queryWrapperX.orderByDesc(ApiRelease::getId);
        List<ApiRelease> apiReleases = apiReleaseDAO.selectList(queryWrapperX);
        List<ApiReleaseDTO> apiReleaseDTOList = new ArrayList<>();
        apiReleases.forEach(apiRelease -> {
            ApiReleaseDTO apiReleaseDTO = PtcBeanUtils.copy(apiRelease, ApiReleaseDTO.class);
            apiReleaseDTOList.add(apiReleaseDTO);
        });
        return apiReleaseDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offlineApi(Long id) {
        LambdaUpdateWrapper<ApiInfo> updateWrapper = new LambdaUpdateWrapper<>();
        List<Integer> statusList = Lists.newArrayList(ApiConstant.API_STATUS_RELEASE, ApiConstant.API_STATUS_CHANGE);
        updateWrapper.eq(ApiInfo::getId, id).in(ApiInfo::getStatus, statusList);
        updateWrapper.set(ApiInfo::getStatus, ApiConstant.API_STATUS_OFFLINE);
        int update = apiInfoDAO.update(new ApiInfo(), updateWrapper);
        if (update < 1) {
            throw new BizException("只能下线已发布或者已更新的接口");
        }

    }

    @Override
    public PageResult<ApiInfoDTO> getList(ApiInfoQuery query) {
        LambdaQueryWrapperX<ApiInfo> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(ApiInfo::getName, query.getName());
        queryWrapperX.eqIfPresent(ApiInfo::getFolderId, query.getFolderId());
        queryWrapperX.eqIfPresent(ApiInfo::getStatus, query.getStatus());
        queryWrapperX.orderByDesc(ApiInfo::getId);
        queryWrapperX.select(ApiInfo::getId, ApiInfo::getName, ApiInfo::getPath, ApiInfo::getFolderId,
                ApiInfo::getDataSourceId, ApiInfo::getType, ApiInfo::getStatus, ApiInfo::getReleaseTime);
        PageParam pageParam = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<ApiInfo> pageResult = apiInfoDAO.selectPage(pageParam, queryWrapperX);
        List<ApiInfoDTO> apiInfoDTOList = toApiInfoDTOList(pageResult.getList());
        apiInfoDTOList.forEach(apiInfoDTO -> {
            Object isSmoke = redisTemplate.opsForValue().get(API_SMOKE_PREFIX + apiInfoDTO.getId());
            if (Objects.equals(isSmoke, 1)) {
                apiInfoDTO.setIsPass(true);
            }
        });

        PageResult<ApiInfoDTO> result = new PageResult<>();
        result.setList(apiInfoDTOList);
        result.setPages(pageResult.getPages());
        result.setTotal(pageResult.getTotal());
        return result;
    }

    @Override
    public ApiInfoDTO getApiInfo(Long id) {
        ApiInfo apiInfo = apiInfoDAO.selectById(id);
        ApiInfoDTO apiInfoDTO = PtcBeanUtils.copy(apiInfo, ApiInfoDTO.class);
        Object isSmoke = redisTemplate.opsForValue().get(API_SMOKE_PREFIX + apiInfoDTO.getId());
        if (Objects.equals(isSmoke, 1)) {
            apiInfoDTO.setIsPass(true);
        }
        return apiInfoDTO;
    }

    @Override
    public ApiInfo getApiInfoByPath(String path) {
        ApiInfo apiInfo = apiInfoDAO.selectOne("path", path);
        return apiInfo;
    }

    List<ApiInfoDTO> toApiInfoDTOList(List<ApiInfo> apiInfos) {
        List<ApiInfoDTO> apiInfoDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(apiInfos)) {
            return apiInfoDTOList;
        }
        List<Long> dataSourceIds = apiInfos.parallelStream().map(ApiInfo::getDataSourceId).distinct().collect(Collectors.toList());
        List<Long> folderIds = apiInfos.parallelStream().map(ApiInfo::getFolderId).distinct().collect(Collectors.toList());
        Map<Long, String> dataSourceMap = dataSourceService.getDataSourceMap(dataSourceIds);
        Map<Long, String> folderMap = folderService.getFolderMap(folderIds);
        apiInfos.forEach(apiInfo -> {
            ApiInfoDTO apiInfoDTO = PtcBeanUtils.copy(apiInfo, ApiInfoDTO.class);
            apiInfoDTO.setDataSourceName(dataSourceMap.get(apiInfo.getDataSourceId()));
            apiInfoDTO.setFolderName(folderMap.get(apiInfo.getFolderId()));
            apiInfoDTOList.add(apiInfoDTO);
        });
        return apiInfoDTOList;
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
                result.put(parameter.getName(), PARAM_MOCK_DATA.get(type + "_" + subType));
            } else {
                result.put(parameter.getName(), PARAM_MOCK_DATA.get(type));
            }
        });
        return result;
    }
}
