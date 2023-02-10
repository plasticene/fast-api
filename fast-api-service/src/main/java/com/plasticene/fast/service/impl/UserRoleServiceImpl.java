package com.plasticene.fast.service.impl;

import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.fast.dao.UserRoleDAO;
import com.plasticene.fast.entity.UserRole;
import com.plasticene.fast.param.UserRoleParam;
import com.plasticene.fast.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/2/3 13:55
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Resource
    private UserRoleDAO userRoleDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRole(Long userId, List<Long> roleIds) {
        List<UserRole> userRoles = new ArrayList<>();
        roleIds.forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        userRoleDAO.insertBatch(userRoles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserRole(Long userId) {
        LambdaQueryWrapperX<UserRole> queryWrapperX = new LambdaQueryWrapperX();
        queryWrapperX.eq(UserRole::getUserId, userId);
        userRoleDAO.delete(queryWrapperX);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRole(UserRoleParam param) {
        Long userId = param.getUserId();
        List<Long> roleIds = param.getRoleIds();
        deleteUserRole(userId);
        addUserRole(userId, roleIds);
    }
}
