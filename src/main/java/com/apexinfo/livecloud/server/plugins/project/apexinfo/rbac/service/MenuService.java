package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.MenuMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.RoleMenuMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.MenuVO;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName: MenuService
 * @Description: 菜单业务逻辑层
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class MenuService {
    private static final Logger logger = Logger.getLogger(DemoService.class);

    private static MenuService instance;

    private IMenuMapper menuMapper;

    public static MenuService getInstance() {
        if (instance == null) {
            instance = new MenuService();
        }
        return instance;
    }

    private MenuService() {
        menuMapper = new MenuMapperImpl();
    }

    /**
     * 查询菜单树 / 根据角色id查询菜单树
     *
     * @param roleId
     * @param menuId
     * @param userId
     * @return
     */
    public List<MenuVO> queryToTree(Long roleId, Long menuId, Long userId) {
        List<MenuVO> menusTree = new ArrayList<>();
        List<Menu> menuList = null;
        if (menuId != null) {
            // 根据菜单id查询菜单
            menuList = menuMapper.queryById(menuId);
            MenuVO menuVO = new MenuVO();
            menuVO.setMenu(menuList.get(0));
            menusTree.add(menuVO);
        } else if (roleId != null) {
            // 根据角色id查询菜单
            menuList = menuMapper.queryByRoleId(roleId);
        } else if (userId != null) {
            // 根据用户id查询菜单
            menuList = menuMapper.queryByUserId(userId);
        } else {
            // 查询所有菜单
            menuList = menuMapper.query();
        }

        if (menuId == null) {
            // 构建菜单树
            for (Menu menu : menuList) {
                // 根节点的 level 为 1
                if (menu.getLevel() == 1) {
                    MenuVO menuVO = buildMenuTree(menu, menuList);
                    menusTree.add(menuVO);
                }
            }
        }
        return menusTree;
    }

    /**
     * 递归将菜单列表变成菜单树
     *
     * @param currentMenu
     * @param menuList
     */
    private MenuVO buildMenuTree(Menu currentMenu, List<Menu> menuList) {
        MenuVO menuVO = new MenuVO();
        List<MenuVO> children = new ArrayList<>();
        menuVO.setMenu(currentMenu);
        menuVO.setChildren(children);

        long parentId = currentMenu.getId();
        for (Menu menu : menuList) {
            if (menu.getParentId() == parentId) {
                MenuVO childMenuVO = buildMenuTree(menu, menuList);
                children.add(childMenuVO);
            }
        }
        // 对 children 列表按照 FOrder 字段排序
        children.sort((menuVo1, menuVo2) -> {
            return Math.toIntExact(menuVo1.getMenu().getOrder() - menuVo2.getMenu().getOrder());
        });
        return menuVO;
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    public int add(Menu menu) {
        menu.setCreateTime(new Date());
        menu.setUpdateTime(menu.getCreateTime());
        return menuMapper.add(menu);
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    public int update(Menu menu) {
        menu.setUpdateTime(new Date());
        return menuMapper.update(menu);
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    //TODO 事务待修改
    public int delete(List<Long> id) {
        // 删除角色_菜单关联表
        RoleMenuService.getInstance().deleteByMenuId(id);
        return menuMapper.delete(id);
    }
}
