package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.RoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.RoleMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @ClassName: RoleService
 * @Description: 角色业务逻辑层
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class RoleService {
    private static final Logger logger = Logger.getLogger(DemoService.class);

    private static RoleService instance;

    private RoleMapper roleMapper;

    public static RoleService getInstance() {
        if (instance == null) {
            instance = new RoleService();
        }
        return instance;
    }

    private RoleService() {
        roleMapper = new RoleMapper();
    }

    /**
     * 分页查询/模糊查询所有角色 / 根据用户id查询角色
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param userId
     * @return
     */
    public List<Role> query(Long pageNo, Long pageSize, String keyword, Long userId) {
        if (userId == null) {
            if (pageNo == null) {
                pageNo = 1L;
            }
            if (pageSize == null) {
                pageSize = 20L;
            }
            return roleMapper.query(pageNo, pageSize, keyword);
        }
        return roleMapper.queryByUserId(userId);
    }

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    public int add(Role role) {
        int rows;
        role.setCreateTime(new Date());
        role.setUpdateTime(role.getCreateTime());
        rows = roleMapper.add(role);
        return rows;
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    public int update(Role role) {
        int rows;
        role.setUpdateTime(new Date());
        rows = roleMapper.update(role);
        return rows;
    }

    /**
     * 修改角色的菜单
     *
     * @param roleId
     * @param menuId
     * @return
     */
    public int updateRoleMenus(Long roleId, Set<Long> menuId) {
        int rows = 0;
        RoleMenuMapper roleMenuMapper = new RoleMenuMapper();

        // 查询该角色之前拥有的所有菜单lastMenuId
        List<Menu> menus = MenuService.getInstance().query(roleId);
        Set<Long> lastMenuId = new HashSet<>();
        menus.forEach((menu) -> {
            lastMenuId.add(menu.getId());
        });

        // lastMenuId - menuId, 进行删除操作
        Collection deleteIdCollection = CollectionUtils.subtract(lastMenuId, menuId);
        if (deleteIdCollection.size() > 0) {
            List<Long> deleteId = new ArrayList<>(deleteIdCollection);
            rows += roleMenuMapper.delete(roleId, deleteId);
        }

        // menuId - lastMenuId, 进行新增操作
        Collection addIdCollection = CollectionUtils.subtract(menuId, lastMenuId);
        if (addIdCollection.size() > 0) {
            List<Long> addId = new ArrayList<>(addIdCollection);
            rows += roleMenuMapper.add(roleId, addId);
        }

        return addIdCollection.size() == 0 && deleteIdCollection.size() == 0 ? 1 : rows;
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    // TODO 现有一下两种处理方式, 待定
    //  1. 如果该角色还有用户, 则无法删除
    //  2. 如果该角色还有用户, 同时将用户_角色表的数据一起删除
    public int delete(List<Long> id) {
        return roleMapper.delete(id);
    }
}
