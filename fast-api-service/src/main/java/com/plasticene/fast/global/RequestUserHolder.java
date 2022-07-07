package com.plasticene.fast.global;

import com.plasticene.fast.dto.LoginUser;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/5 10:06
 */
public class RequestUserHolder {
    private static final ThreadLocal<LoginUser> userHolder = new ThreadLocal<>();

    /**
     * 存储用户信息
     *
     * @param userSession
     */
    public static void add(LoginUser userSession) {
        userHolder.set(userSession);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static LoginUser getCurrentUser() {
        return userHolder.get();
    }





}