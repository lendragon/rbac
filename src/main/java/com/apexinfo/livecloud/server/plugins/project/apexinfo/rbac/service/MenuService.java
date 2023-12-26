package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apex.livebos.console.common.util.Util;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.MenuMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.ValidUtil;
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
    // 日志输出对象
    private static final Logger logger = Logger.getLogger(MenuService.class);

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
     * @param currentMenu 当前菜单
     * @param menuList    所有菜单列表
     * @return List<Menu> 菜单树列表
     * @description 递归将菜单列表变成菜单树
     */
    private List<Menu> buildMenuTree(Menu currentMenu, List<Menu> menuList) {
        if (currentMenu == null) {
            List<Menu> menuTree = new ArrayList<>();
            // 构建菜单树
            for (Menu menu : menuList) {
                // 父菜单主键为0, 表示第一层级的菜单
                if (Objects.equals(menu.getParentId(), CommonConstants.DATA_COMMON_ROOT_MENU)) {
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
     * @param id 用户主键
     * @return List<Menu> 菜单树
     * @description 根据用户主键查询菜单树
     */
    public List<Menu> queryToTreeByUserId(Long id) {
        List<Menu> menusTree = null;
        try {
            List<Menu> menuList = null;
            if (Objects.equals(id, CommonConstants.DATA_COMMON_ROOT_ADMIN)) {
                // 如果是超级管理员直接全部查询
                menuList = menuMapper.queryAll();
            } else {
                // 根据用户主键查询菜单
                menuList = menuMapper.queryByUserId(id);
            }
            // 构建菜单树
            menusTree = buildMenuTree(null, menuList);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menusTree;
    }

    /**
     * @param id 角色主键
     * @return List<Menu> 菜单树
     * @description 根据角色主键查询菜单树
     */
    public List<Menu> queryToTreeByRoleId(Long id) {
        List<Menu> menusTree = null;
        try {
            // 根据角色主键查询菜单
            List<Menu> menuList = menuMapper.queryByRoleId(id);
            // 构建菜单树
            menusTree = buildMenuTree(null, menuList);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menusTree;
    }

    /**
     * @param id 菜单主键
     * @return Menu 菜单
     * @description 根据菜单主键查询菜单
     */
    public Menu queryByMenuId(Long id) {
        Menu menu = null;
        try {
            menu = menuMapper.queryByMenuId(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menu;
    }

    /**
     * @param id 菜单主键
     * @return List<Long> 子菜单主键列表
     * @description 根据菜单主键查询子菜单主键列表
     */
    public List<Long> queryChildrenIdsByMenuId(Long id) {
        List<Long> menuChildrenIds = null;
        try {
            menuChildrenIds = menuMapper.queryChildrenIdsByMenuId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menuChildrenIds;
    }

    /**
     * @param id 角色主键
     * @return List<Menu> 菜单列表
     * @description 根据角色主键查询菜单
     */
    public List<Menu> queryByIdOfRole(Long id) {
        List<Menu> menus = null;
        try {
            // 根据角色主键查询菜单
            menus = menuMapper.queryByRoleId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return menus;
    }

    /**
     * @param menu 菜单
     * @return boolean 是否成功
     * @description 新增菜单
     */
    public boolean add(Menu menu) {
        int rows = 0;
        try {
            // 根据parentId查询菜单, 获取菜单层级
            if (menu.getParentId().equals(CommonConstants.DATA_COMMON_ROOT_MENU)) {
                menu.setLevel(1);
            } else {
                Menu parentMenu = menuMapper.queryByMenuId(menu.getParentId());
                menu.setLevel(parentMenu.getLevel() + 1);
            }

            menu.setState(CommonConstants.DATA_COMMON_DEFAULT_STATE);
            menu.setCreateTime(new Date());
            menu.setUpdateTime(menu.getCreateTime());
            rows = menuMapper.add(menu);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }

    /**
     * @param menu 菜单
     * @return boolean 是否成功
     * @description 修改菜单
     */
    public boolean update(Menu menu) {
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
        return rows > 0;
    }

    /**
     * @param id 菜单主键
     * @return boolean 是否成功
     * @description 删除菜单
     */
    public boolean delete(Long id) {
        int rows = 0;
        try {
            // 如果菜单有角色关联, 则不能删除
            List<Long> ids = RoleService.getInstance().queryIdsByMenuId(id);
            if (Util.isNotEmpty(ids)) {
                throw new RuntimeException(Core.i18n().getValue(CommonConstants.I18N_MENU_ERROR_REQUIRED_BY_ROLE));
            }

            // 如果菜单有子菜单, 则不能删除
            List<Long> menuChildrenIds = MenuService.getInstance().queryChildrenIdsByMenuId(id);
            if (Util.isNotEmpty(menuChildrenIds)) {
                throw new RuntimeException(Core.i18n().getValue(CommonConstants.I18N_MENU_ERROR_REQUIRED_BY_CHILDREN));
            }

            rows = menuMapper.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        return rows > 0;
    }
}
