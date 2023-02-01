package com.plasticene.fast.service.impl;

import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.fast.dao.MenuDAO;
import com.plasticene.fast.dto.MenuDTO;
import com.plasticene.fast.entity.Menu;
import com.plasticene.fast.param.MenuParam;
import com.plasticene.fast.query.MenuQuery;
import com.plasticene.fast.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
        return null;
    }
}
