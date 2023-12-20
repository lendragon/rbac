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
     * @param addId
     * @return
     */
    int add(Long roleId, List<Long> addId) {
        return roleMenuMapper.add(roleId, addId);
    }

    /**
     * 根据角色id删除对应的菜单
     *
     * @param roleId
     * @param deleteId
     * @return
     */
    int deleteByList(Long roleId, List<Long> deleteId) {
        return roleMenuMapper.deleteByList(roleId, deleteId);
    }

    /**
     * 根据角色id删除对应的所有角色_菜单关联
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(List<Long> roleId) {
        return roleMenuMapper.deleteByRoleId(roleId);
    }

    /**
     * 根据菜单id删除对应的所有角色_菜单关联
     *
     * @param menuId
     * @return
     */
    int deleteByMenuId(List<Long> menuId) {
        return roleMenuMapper.deleteByMenuId(menuId);
    }
}

