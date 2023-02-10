package com.plasticene.fast.dao;

import com.plasticene.boot.mybatis.core.mapper.BaseMapperX;
import com.plasticene.fast.dto.RoleDTO;
import com.plasticene.fast.entity.Role;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/29 15:25
 */
public interface RoleDAO extends BaseMapperX<Role> {
    List<RoleDTO> getRoleList(Long userId);
}
