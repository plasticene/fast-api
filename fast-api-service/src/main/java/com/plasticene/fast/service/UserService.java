package com.plasticene.fast.service;

import com.plasticene.fast.entity.User;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 19:09
 */
public interface UserService {

    User getUserByUsername(String username);

    User getUserByMobile(String mobile);
}
