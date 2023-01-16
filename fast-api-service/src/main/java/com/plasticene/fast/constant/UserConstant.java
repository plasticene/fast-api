package com.plasticene.fast.constant;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/3 22:29
 */
public interface UserConstant {

    String LOGIN_KEY_PREFIX = "token_";

    Integer IS_ACTIVE = 0;

    Integer IS_NOT_ACTIVE = -1;

    Integer LOGIN_TYPE_USER_NAME = 0;

    Integer LOGIN_TYPE_MOBILE_CODE = 1;
}
