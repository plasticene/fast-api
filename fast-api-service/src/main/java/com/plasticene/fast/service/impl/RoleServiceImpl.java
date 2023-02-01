package com.plasticene.fast.service.impl;

import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.fast.dao.RoleDAO;
import com.plasticene.fast.dto.RoleDTO;
import com.plasticene.fast.entity.Role;
import com.plasticene.fast.param.RoleParam;
import com.plasticene.fast.query.RoleQuery;
import com.plasticene.fast.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/29 16:15
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleDAO roleDAO;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleParam param) {
        Role role = PtcBeanUtils.copy(param, Role.class);
        roleDAO.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long id, RoleParam param) {
        Role role = PtcBeanUtils.copy(param, Role.class);
        role.setId(id);
        roleDAO.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        roleDAO.deleteById(id);
        //todo 删除角色关联的菜单权限
    }

    @Override
    public PageResult<RoleDTO> getList(RoleQuery query) {
        LambdaQueryWrapperX<Role> queryWrapperX = new LambdaQueryWrapperX<>();
        PageParam param = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<Role> pageResult = roleDAO.selectPage(param, queryWrapperX);
        List<RoleDTO> roleDTOList = toRoleDTOList(pageResult.getList());
        return new PageResult<>(roleDTOList, pageResult.getTotal(), pageResult.getPages());
    }

    List<RoleDTO> toRoleDTOList(List<Role> roles) {
        List<RoleDTO> roleDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        roles.forEach(role -> {
            RoleDTO roleDTO = PtcBeanUtils.copy(role, RoleDTO.class);
            roleDTOList.add(roleDTO);
        });
        return roleDTOList;
    }
}
