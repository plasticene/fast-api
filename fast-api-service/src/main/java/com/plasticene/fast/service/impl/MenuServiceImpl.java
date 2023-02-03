package com.plasticene.fast.service.impl;

import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.fast.dao.MenuDAO;
import com.plasticene.fast.dto.MenuDTO;
import com.plasticene.fast.entity.Menu;
import com.plasticene.fast.param.MenuParam;
import com.plasticene.fast.query.MenuQuery;
import com.plasticene.fast.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/31 18:57
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuDAO menuDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMenu(MenuParam param) {
        Menu menu = PtcBeanUtils.copy(param, Menu.class);
        menuDAO.insert(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Long id, MenuParam param) {
        Menu menu = PtcBeanUtils.copy(param, Menu.class);
        menu.setId(id);
        menuDAO.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        menuDAO.deleteById(id);
    }

    @Override
    public List<MenuDTO> getList(MenuQuery query) {
        LambdaQueryWrapperX<Menu> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(Menu::getType, query.getType());
        queryWrapperX.eqIfPresent(Menu::getStatus, query.getStatus());
        queryWrapperX.likeRightIfPresent(Menu::getName, query.getName());
        queryWrapperX.likeRightIfPresent(Menu::getCode, query.getCode());
        List<Menu> menuList = menuDAO.selectList(queryWrapperX);
        List<MenuDTO> menuDTOList = toMenuDTOList(menuList);
        List<MenuDTO> list = new ArrayList<>();
        listToTree(menuDTOList, list);
        return list;
    }

    List<MenuDTO> listToTree(List<MenuDTO> menuDTOList, List<MenuDTO> list) {
        for (MenuDTO menuDTO :menuDTOList) {
            if (menuDTO.getParentId() == 0) {
                list.add(menuDTO);
            }
            for (MenuDTO node: menuDTOList) {
                if (Objects.equals(node.getParentId(), menuDTO.getId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(node);
                }
            }
        }
        return list;
    }

    List<MenuDTO> toMenuDTOList(List<Menu> menuList) {
        List<MenuDTO> menuDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(menuList)) {
            return menuDTOList;
        }
        menuList.forEach(menu -> {
            MenuDTO menuDTO = PtcBeanUtils.copy(menu, MenuDTO.class);
            menuDTOList.add(menuDTO);
        });
        return menuDTOList;
    }
}
