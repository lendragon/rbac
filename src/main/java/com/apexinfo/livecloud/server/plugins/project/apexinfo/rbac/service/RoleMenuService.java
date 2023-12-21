package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.RoleMenuMapperImpl;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @ClassName: RoleMenuService
 * @Description: 角色菜单表业务逻辑层
 * @Author linlongyue
 * @Date 2023/12/20
 * @Version 1.0
 */
public class RoleMenuService {
    private static final Logger logger = Logger.getLogger(DemoService.class);
    private static RoleMenuService instance;
    private IRoleMenuMapper roleMenuMapper;
    public static RoleMenuService getInstance() {
        if (instance == null) {
            instance = new RoleMenuService();
        }
        return instance;
    }
    private RoleMenuService() {
        roleMenuMapper = new RoleMenuMapperImpl();
    }

    /**
     * 根据角色id新增对应的菜单
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    int addMenuList(Long roleId, List<Long> menuIds) {
        return roleMenuMapper.addMenuList(roleId, menuIds);
    }

    /**
     * 根据角色id删除对应的菜单
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    int deleteByMenuList(Long roleId, List<Long> menuIds) {
        return roleMenuMapper.deleteByMenuList(roleId, menuIds);
    }

    /**
     * 根据角色id删除对应的所有角色_菜单关联
     *
     * @param roleIds
     * @return
     */
    int deleteByRoleId(List<Long> roleIds) {
        return roleMenuMapper.deleteByRoleId(roleIds);
    }

    /**
     * 根据菜单id删除对应的所有角色_菜单关联
     *
     * @param menuIds
     * @return
     */
    int deleteByMenuId(List<Long> menuIds) {
        return roleMenuMapper.deleteByMenuId(menuIds);
    }
}

