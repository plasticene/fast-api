package com.plasticene.fast.service;

import com.plasticene.fast.dto.MenuDTO;
import com.plasticene.fast.param.MenuParam;
import com.plasticene.fast.query.MenuQuery;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/31 16:56
 */
public interface MenuService {

    void addMenu(MenuParam param);

    void updateMenu(Long id, MenuParam param);

    void deleteMenu(Long id);

    List<MenuDTO> getList(MenuQuery query);


}
