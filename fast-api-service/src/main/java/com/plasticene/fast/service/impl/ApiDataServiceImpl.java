package com.plasticene.fast.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.ResponseVO;
import com.plasticene.fast.constant.ApiConstant;
import com.plasticene.fast.dto.DataSourceDTO;
import com.plasticene.fast.dto.Parameter;
import com.plasticene.fast.entity.ApiInfo;
import com.plasticene.fast.parser.DynamicSqlParser;
import com.plasticene.fast.service.ApiDataService;
import com.plasticene.fast.service.ApiInfoService;
import com.plasticene.fast.service.DataQueryService;
import com.plasticene.fast.service.DataSourceService;
import com.plasticene.fast.vo.DataResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/3/2 13:55
 */
@Service
@Slf4j
public class ApiDataServiceImpl implements ApiDataService {

    @Resource
    private ApiInfoService apiInfoService;
    @Resource
    private DynamicSqlParser sqlParser;
    @Resource
    private DataSourceService dataSourceService;
    @Resource
    private DataQueryService dataQueryService;


    private static final String URL_PREFIX = "/fds/fapi/";




    @Override
    public DataResultVO getApiData(HttpServletRequest request) {
        String url = request.getRequestURI();
        String path = url.substring(URL_PREFIX.length());
        ApiInfo apiInfo = apiInfoService.getApiInfoByPath(path);
        if (apiInfo == null) {
            throw new BizException("请求接口路径不存在");
        }

        // todo 鉴权

        // 检验参数
        String sqlContent = apiInfo.getSqlContent();
        List<Parameter> param = apiInfo.getParam();
        if (!CollectionUtils.isEmpty(param)) {
            Map<String, String> paramMap = checkParam(param, request, apiInfo.getType());
            sqlContent = sqlParser.parseSQL(sqlContent, paramMap);
        }
        DataSourceDTO dataSourceDTO = dataSourceService.getDataSourceDTO(apiInfo.getDataSourceId());
        dataSourceDTO.setSelectDatabase(apiInfo.getDatabaseName());
        DataResultVO dataResultVO = dataQueryService.queryResult(dataSourceDTO, sqlContent);
        return dataResultVO;
    }

    Map<String, String> checkParam(List<Parameter> parameterList, HttpServletRequest request, Integer apiType) {
        Map<String, String> map = new HashMap<>();
        JSONObject rst = null;
        if (Objects.equals(apiType, ApiConstant.TYPE_POST)) {
            rst = getPostParameter(request);
        }
        JSONObject finalRst = rst;
        parameterList.forEach(parameter -> {
            String name = parameter.getName();
            String value = null;
            if (Objects.equals(apiType, ApiConstant.TYPE_GET)) {
                value = request.getParameter(name);
            }
            if (Objects.equals(apiType, ApiConstant.TYPE_POST)) {
                value = finalRst.getString(name);
            }
            Boolean required = parameter.getRequired();
            if (required && StringUtils.isBlank(value)) {
                throw new BizException("参数:" + name + "，不能为空");
            }
            if (StringUtils.isBlank(value)) {
                return;
            }
            String type = parameter.getType();
            if (Objects.equals(type, "string") || Objects.equals(type, "date")) {
                value = "'" + value + "'";
            }
            if (Objects.equals(type, "array")) {
                Map<String, String> items = parameter.getItems();
                String subType = items.get("type");
                if (Objects.equals(subType, "string") || Objects.equals(subType, "date")) {
                    String str = value.replace(" ", "");
                    List<String> valList = Arrays.asList(StringUtils.split(str, ","));
                    for(int i = 0; i < valList.size(); i++) {
                        valList.set(i, "'" + valList.get(i) + "'");
                    }
                    value = StringUtils.join(valList, ",");
                }
            }
            map.put(name, value);
        });
        return map;
    }

    private JSONObject getPostParameter(HttpServletRequest request) {
        try {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            return JSONObject.parseObject(responseStrBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
