package com.plasticene.fast.service;

import com.plasticene.fast.param.UserRoleParam;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/2/3 13:51
 */
public interface UserRoleService {

    void addUserRole(Long userId, List<Long> roleIds);

    void deleteUserRole(Long userId);

    void assignUserRole(UserRoleParam param);
}
