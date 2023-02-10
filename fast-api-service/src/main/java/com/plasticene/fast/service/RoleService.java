package com.plasticene.fast.service;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.fast.dto.RoleDTO;
import com.plasticene.fast.param.RoleParam;
import com.plasticene.fast.query.RoleQuery;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/29 15:36
 */
public interface RoleService {

    void addRole(RoleParam param);

    void updateRole(Long id, RoleParam param);

    void deleteRole(Long id);

    PageResult<RoleDTO> getList(RoleQuery query);

    List<RoleDTO> getRoleList(Long userId);
}
