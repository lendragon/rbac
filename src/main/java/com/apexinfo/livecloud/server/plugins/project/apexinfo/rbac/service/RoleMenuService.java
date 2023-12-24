package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.RoleMenuMapperImpl;
import org.apache.log4j.Logger;

import java.sql.SQLException;
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
     * 根据菜单id列表查询对应的角色id列表
     *
     * @param menuIds 菜单id列表
     * @return
     */
    public List<Long> queryIdByMenuIds(List<Long> menuIds) {
        List<Long> roleIds = null;
        try {
            roleIds = roleMenuMapper.queryIdByMenuIds(menuIds);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return roleIds;
    }

    /**
     * 根据角色id新增对应的菜单列表关联
     *
     * @param roleId 角色id
     * @param menuIds 要添加的菜单id列表
     * @return
     */
    public int addMenuList(Long roleId, List<Long> menuIds) throws SQLException {
        return roleMenuMapper.addMenuList(roleId, menuIds);
    }

    /**
     * 根据角色id删除对应的菜单列表关联
     *
     * @param roleId 角色id
     * @param menuIds 要添加的菜单id列表
     * @return
     */
    public int deleteByMenuList(Long roleId, List<Long> menuIds) throws SQLException {
        return roleMenuMapper.deleteByMenuList(roleId, menuIds);
    }

}

