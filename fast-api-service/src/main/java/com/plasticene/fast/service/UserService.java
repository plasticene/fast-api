package com.plasticene.fast.service;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.fast.dto.UserDTO;
import com.plasticene.fast.entity.User;
import com.plasticene.fast.param.UserParam;
import com.plasticene.fast.query.UserQuery;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 19:09
 */
public interface UserService {

    User getUserByUsername(String username);

    User getUserByMobile(String mobile);

    void addUser(UserParam param);

    void updateUser(Long id, UserParam param);

    void deleteUser(Long id);

    PageResult<UserDTO> getList(UserQuery query);




}
