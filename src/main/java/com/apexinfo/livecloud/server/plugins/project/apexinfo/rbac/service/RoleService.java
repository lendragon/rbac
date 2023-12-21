package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.RoleMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelativeDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
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

    private IRoleMapper roleMapper;

    public static RoleService getInstance() {
        if (instance == null) {
            instance = new RoleService();
        }
        return instance;
    }

    private RoleService() {
        roleMapper = new RoleMapperImpl();
    }

    /**
     * 分页查询/模糊查询所有角色 / 根据用户id查询角色
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param roleId
     * @param userId
     * @return
     */
    public PageDTO<Role> query(Integer pageNo, Integer pageSize, String keyword, Long userId, Long roleId) {
        PageDTO<Role> pageDTO = null;
        if (roleId != null) {
            // 根据角色id查询角色
            pageDTO = roleMapper.queryById(roleId);
            pageDTO.setPageNo(pageNo);
            pageDTO.setPageSize(pageSize);
            return pageDTO;
        }
        if (userId != null) {
            // 根据用户id查询角色
            pageDTO = roleMapper.queryByUserId(userId);
            pageDTO.setPageNo(pageNo);
            pageDTO.setPageNo(pageSize);
            return pageDTO;
        }
        // 查询所有角色
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        pageDTO = roleMapper.query(pageNo, pageSize, keyword);
        return pageDTO;
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
     * 给用户授权角色
     *
     * @param relativeDTO
     * @return
     */
    // TODO 事务
    public int updateUserRole(RelativeDTO relativeDTO) {
        int rows = 0;
        // 添加对应关联
        rows += UserRoleService.getInstance().addUserList(relativeDTO.getRoleId(), relativeDTO.getAddIds());
        // 删除对应关联
        rows += UserRoleService.getInstance().deleteByUserIdList(relativeDTO.getRoleId(), relativeDTO.getDeleteIds());
        return rows;
    }

    /**
     * 修改角色的菜单
     *
     * @param relativeDTO
     * @return
     */
    // TODO 事务待修改
    public int updateRoleMenus(RelativeDTO relativeDTO) {
        int rows = 0;
        // 进行删除操作
        rows += RoleMenuService.getInstance().deleteByMenuList(relativeDTO.getRoleId(), relativeDTO.getDeleteIds());
        // 进行新增操作
        rows += RoleMenuService.getInstance().addMenuList(relativeDTO.getRoleId(), relativeDTO.getAddIds());
        return rows;
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    // TODO 事务待修改
    public int delete(List<Long> id) {

        // 删除角色_菜单关联表
        RoleMenuService.getInstance().deleteByRoleId(id);
        return roleMapper.delete(id);
    }
}
