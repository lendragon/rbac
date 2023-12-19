package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.MenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.MenuVO;
import org.apache.log4j.Logger;

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

    private MenuMapper menuMapper;

    public static MenuService getInstance() {
        if (instance == null) {
            instance = new MenuService();
        }
        return instance;
    }

    private MenuService() {
        menuMapper = new MenuMapper();
    }

    /**
     * 查询菜单树 / 根据角色id查询菜单树
     * @param roleId
     * @return
     */
    public List<MenuVO> queryToTree(Long roleId, Long menuId) {
        List<MenuVO> menusTree = new ArrayList<>();
        List<Menu> menuList = null;
        if (menuId == null) {
            menuList = query(roleId);
            for (Menu menu : menuList) {
                // 根节点的 level 为 1
                if (menu.getLevel() == 1) {
                    MenuVO menuVO = buildMenuTree(menu, menuList);
                    menusTree.add(menuVO);
                }
            }
        } else {
            menuList = menuMapper.queryById(menuId);
            MenuVO menuVO = new MenuVO();
            menuVO.setMenu(menuList.get(0));
            menusTree.add(menuVO);
        }

        // 构建菜单树
        return menusTree;
    }

    /**
     * 递归将菜单列表变成菜单树
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
     * 查询所有菜单 / 根据角色id查询菜单
     *
     * @param roleId
     * @return
     */
    public List<Menu> query(Long roleId) {
        if (roleId == null) {
            return menuMapper.query();
        }
        return menuMapper.queryByRoleId(roleId);
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
    // TODO 现有以下多种处理方式, 待定
    //  1. 如果该菜单还有角色, 则无法删除
    //  2. 如果该菜单还有角色, 同时将角色_菜单表的数据一起删除
    //  3. 如果该菜单还有角色, 但是该角色没有用户使用, 直接将菜单
    //     和角色_菜单表一起删除, 如果该角色有用户使用, 则无法删除
    //  4. 如果该菜单底下还有子菜单, 则无法删除 / 全部删除 / 将子
    //     菜单的parentId变成该菜单的parentId, 层级 - 1
    public int delete(List<Long> id) {
        return menuMapper.delete(id);
    }
}
