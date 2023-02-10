package com.plasticene.fast.service.impl;

import cn.hutool.core.util.StrUtil;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.fast.constant.UserConstant;
import com.plasticene.fast.dao.UserDAO;
import com.plasticene.fast.dto.RoleDTO;
import com.plasticene.fast.dto.UserDTO;
import com.plasticene.fast.entity.User;
import com.plasticene.fast.param.UserParam;
import com.plasticene.fast.query.UserQuery;
import com.plasticene.fast.service.RoleService;
import com.plasticene.fast.service.UserRoleService;
import com.plasticene.fast.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 19:11
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDAO userDAO;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleService roleService;


    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapperX<User> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(User::getUsername, username);
        queryWrapperX.ne(User::getStatus, UserConstant.IS_DELETE);
        User user = userDAO.selectOne(queryWrapperX);
        return user;
    }

    @Override
    public User getUserByMobile(String mobile) {
        LambdaQueryWrapperX<User> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(User::getMobile, mobile);
        queryWrapperX.ne(User::getStatus, UserConstant.IS_DELETE);
        User user = userDAO.selectOne(queryWrapperX);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserParam param) {
        String username = param.getUsername();
        checkUsernameUnique(username);
        User user = PtcBeanUtils.copy(param, User.class);
        userDAO.insert(user);
        if (!CollectionUtils.isEmpty(param.getRoleIds())) {
            userRoleService.addUserRole(user.getId(), param.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long id, UserParam param) {
        User user = PtcBeanUtils.copy(param, User.class);
        user.setId(id);
        userDAO.updateById(user);
        if (!CollectionUtils.isEmpty(param.getRoleIds())) {
            userRoleService.deleteUserRole(id);
            userRoleService.addUserRole(id, param.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setStatus(UserConstant.IS_DELETE);
        userDAO.updateById(user);
        userRoleService.deleteUserRole(id);
    }

    @Override
    public PageResult<UserDTO> getList(UserQuery query) {
        LambdaQueryWrapperX<User> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.ne(User::getStatus, UserConstant.IS_DELETE);
        queryWrapperX.likeRightIfPresent(User::getUsername, query.getUsername());
        queryWrapperX.likeRightIfPresent(User::getName, query.getName());
        queryWrapperX.likeRightIfPresent(User::getMobile, query.getMobile());
        queryWrapperX.eqIfPresent(User::getStatus, query.getStatus());
        PageParam param = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<User> result = userDAO.selectPage(param, queryWrapperX);
        List<UserDTO> userDTOList = toUserDTOList(result.getList());
        return new PageResult<>(userDTOList, result.getTotal(), result.getPages());
    }

    public void checkUsernameUnique(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            throw new BizException("用户名已存在");
        }
    }

    List<UserDTO> toUserDTOList(List<User> users) {
        List<UserDTO> userDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(users)) {
            return userDTOList;
        }
        users.forEach(user -> {
            UserDTO userDTO = PtcBeanUtils.copy(user, UserDTO.class);
            List<RoleDTO> roleList = roleService.getRoleList(user.getId());
            userDTO.setRoleList(roleList);
            userDTOList.add(userDTO);
        });
        return userDTOList;
    }
}
