package com.plasticene.fast.service;

import com.plasticene.boot.common.pojo.ResponseVO;
import com.plasticene.fast.vo.DataResultVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/3/2 13:52
 */
public interface ApiDataService {

    DataResultVO getApiData(HttpServletRequest request);
}
