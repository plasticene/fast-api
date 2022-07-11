package com.plasticene.fast.global;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plasticene.fast.utils.JsonUtils;

import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/11 17:02
 */
public class JsonStringSetTypeHandler extends AbstractJsonTypeHandler<Object> {

    private static final TypeReference<Set<String>> typeReference = new TypeReference<Set<String>>(){};

    @Override
    protected Object parse(String json) {
        return JsonUtils.parseObject(json, typeReference);
    }

    @Override
    protected String toJson(Object obj) {
        return JsonUtils.toJsonString(obj);
    }

}
