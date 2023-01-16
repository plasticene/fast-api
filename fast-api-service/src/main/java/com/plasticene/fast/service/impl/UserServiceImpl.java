package com.plasticene.fast.service.impl;

import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.fast.dao.UserDAO;
import com.plasticene.fast.entity.User;
import com.plasticene.fast.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 19:11
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDAO userDAO;


    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapperX<User> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(User::getUsername, username);
        User user = userDAO.selectOne(queryWrapperX);
        return user;
    }

    @Override
    public User getUserByMobile(String mobile) {
        LambdaQueryWrapperX<User> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(User::getMobile, mobile);
        User user = userDAO.selectOne(queryWrapperX);
        return user;
    }
}
