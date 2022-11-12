package com.plasticene.fast.handlers;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plasticene.boot.common.utils.JsonUtils;
import com.plasticene.fast.dto.Parameter;

import java.util.List;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 15:14
 */
public class JsonListTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static final TypeReference<List<Parameter>> typeReference = new TypeReference<List<Parameter>>(){};

    @Override
    protected Object parse(String json) {
        return JsonUtils.parseObject(json, typeReference);
    }

    @Override
    protected String toJson(Object obj) {
        return JsonUtils.toJsonString(obj);
    }
}
