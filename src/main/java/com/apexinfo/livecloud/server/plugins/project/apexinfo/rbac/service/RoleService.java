package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.RoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.RoleMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.UserRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelaDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
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
     * @param roleId
     * @param userId
     * @return
     */
    public PageDTO<Role> query(Integer pageNo, Integer pageSize, String keyword, Long userId, Long roleId) {
        PageDTO<Role> pageDTO = null;
        if (roleId != null) {
            // 根据id查询角色
            pageDTO =  roleMapper.queryById(roleId);
            pageDTO.setPageNo(pageNo);
            pageDTO.setPageSize(pageSize);
        } else if (userId == null) {
            // 查询所有角色
            if (pageNo == null) {
                pageNo = 1;
            }
            if (pageSize == null) {
                pageSize = 20;
            }
            pageDTO =  roleMapper.query(pageNo, pageSize, keyword);
        } else {
            // 根据用户id查询角色
            pageDTO =  roleMapper.queryByUserId(userId);
            pageDTO.setPageNo(pageNo);
            pageDTO.setPageNo(pageSize);
        }
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
     * 修改角色的菜单
     *
     * @param relaDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateRoleMenus(RelaDTO relaDTO) {
        if (relaDTO.getAddList().size() == 0 && relaDTO.getDeleteList().size() == 0) {
            return 1;
        }
        int rows = 0;
        RoleMenuMapper roleMenuMapper = new RoleMenuMapper();
        // 进行删除操作
        rows += roleMenuMapper.deleteByList(relaDTO.getId(), relaDTO.getDeleteList());
        // 进行新增操作
        rows += roleMenuMapper.add(relaDTO.getId(), relaDTO.getAddList());
        return rows;
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> id) {
        // 删除用户_角色关联表
        UserRoleMapper userRoleMapper = new UserRoleMapper();
        userRoleMapper.deleteByRoleId(id);
        // 删除角色_菜单关联表
        RoleMenuMapper roleMenuMapper = new RoleMenuMapper();
        roleMenuMapper.deleteByRoleId(id);
        return roleMapper.delete(id);
    }
}
