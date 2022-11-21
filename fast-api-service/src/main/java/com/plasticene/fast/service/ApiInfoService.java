package com.plasticene.fast.service;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.fast.dto.ApiInfoDTO;
import com.plasticene.fast.dto.ApiReleaseDTO;
import com.plasticene.fast.param.ApiInfoParam;
import com.plasticene.fast.query.ApiInfoQuery;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/27 10:28
 */
public interface ApiInfoService {

    Long addApiInfo(ApiInfoParam param);

    void updateApiInfo(Long id, ApiInfoParam param);

    void smokeTest(Long id);

    void releaseApi(Long id);

    List<ApiReleaseDTO> releaseRecord(Long id);

    void offlineApi(Long id);

    PageResult<ApiInfoDTO> getList(ApiInfoQuery query);

    ApiInfoDTO getApiInfo(Long id);




}
