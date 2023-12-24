package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.MenuMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.StateEnum;
import org.apache.log4j.Logger;

import java.sql.SQLException;
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
     * 递归将菜单列表变成菜单树
     *
     * @param currentMenu 当前菜单
     * @param menuList    所有菜单列表
     */
    private List<Menu> buildMenuTree(Menu currentMenu, List<Menu> menuList) {
        if (currentMenu == null) {
            List<Menu> menuTree = new ArrayList<>();
            // 构建菜单树
            for (Menu menu : menuList) {
                // 父菜单id为0, 表示第一层级的菜单
                if (menu.getParentId() == 0) {
                    menu.setChildren(buildMenuTree(menu, menuList));
                    menuTree.add(menu);
                }
            }
            return menuTree;
        }


        List<Menu> children = new ArrayList<>();
        currentMenu.setChildren(children);
        long currentMenuId = currentMenu.getId();
        for (Menu menu : menuList) {
            if (menu.getParentId() == currentMenuId) {
                children.add(menu);
                menu.setChildren(buildMenuTree(menu, menuList));
            }
        }
        // 对 children 列表按照 FOrder 字段排序
        children.sort(Comparator.comparingInt(Menu::getOrder));
        return children;
    }

    /**
     * 查询菜单树
     *
     * @return
     */
    public List<Menu> queryAllToTree() {
        List<Menu> menusTree = null;
        try {
            // 查询所有菜单
            List<Menu> menuList = menuMapper.queryAll();
            // 构建菜单树
            menusTree = buildMenuTree(null, menuList);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menusTree;
    }

    /**
     * 根据用户id查询菜单树
     *
     * @param userId 用户id
     * @return
     */
    public List<Menu> queryToTreeByUserId(Long userId) {
        List<Menu> menusTree = null;
        try {
            // 根据用户id查询菜单
            List<Menu> menuList = menuMapper.queryByUserId(userId);
            // 构建菜单树
            menusTree = buildMenuTree(null, menuList);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menusTree;
    }

    /**
     * 根据角色id查询菜单树
     *
     * @param roleId 角色id
     * @return
     */
    public List<Menu> queryToTreeByRoleId(Long roleId) {
        List<Menu> menusTree = null;
        try {
            // 根据角色id查询菜单
            List<Menu> menuList = menuMapper.queryByRoleId(roleId);
            // 构建菜单树
            menusTree = buildMenuTree(null, menuList);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menusTree;
    }

    /**
     * 根据菜单id查询菜单
     *
     * @param menuId 菜单id
     * @return
     */
    public Menu queryByMenuId(Long menuId) {
        Menu menu = null;
        try {
            menu = menuMapper.queryByMenuId(menuId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menu;
    }

    /**
     * 新增菜单
     *
     * @param menu 菜单
     * @return
     */
    public int add(Menu menu) {
        int rows = 0;
        try {
            // 根据parentId查询菜单, 获取菜单层级
            if (menu.getParentId() == null) {
                menu.setLevel(1);
            } else {
                Menu parentMenu = menuMapper.queryByMenuId(menu.getParentId());
                menu.setLevel(parentMenu.getLevel() + 1);
            }

            menu.setState(StateEnum.正常.ordinal());
            menu.setCreateTime(new Date());
            menu.setUpdateTime(menu.getCreateTime());
            rows = menuMapper.add(menu);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 修改菜单
     *
     * @param menu 菜单
     * @return
     */
    public int update(Menu menu) {
        int rows = 0;
        try {
            // 根据parentId查询菜单, 获取菜单层级
            Menu parentMenu = menuMapper.queryByMenuId(menu.getParentId());
            menu.setLevel(parentMenu.getLevel() + 1);
            menu.setUpdateTime(new Date());
            rows = menuMapper.update(menu);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 删除菜单
     *
     * @param menuIds 菜单id列表
     * @return
     */
    public int delete(List<Long> menuIds) {
        int rows = 0;
        try {
            rows = menuMapper.delete(menuIds);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }
}
