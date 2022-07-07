package com.plasticene.fast.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 13:47
 */
public class FdsBeanUtils {
    private static Logger log = LoggerFactory.getLogger(FdsBeanUtils.class);

    public static <A, B> B copy(A a, Class<B> clazz) {
        if (a == null || clazz == null) {
            return null;
        }

        try {
            B b = clazz.newInstance();
            BeanUtils.copyProperties(a, b);
            return b;
        } catch (Exception e) {
            log.error("FdsBeanUtils#copy error.", e);
        }
        return null;
    }

    public static <A, B> B copy(A a, B b) {
        if (a == null || b == null) {
            return null;
        }

        try {
            BeanUtils.copyProperties(a, b);
            return b;
        } catch (Exception e) {
            log.error("FdsBeanUtils#copy error.", e);
        }
        return null;
    }
}

