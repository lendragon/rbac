package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apex.livebos.console.common.util.Util;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.RoleMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.*;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

/**
 * @ClassName: RoleService
 * @Description: 角色业务逻辑层
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class RoleService {
    // 日志输出对象
    private static final Logger logger = Logger.getLogger(RoleService.class);

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
     * @param pageBean 分页Bean
     * @return PageBean<Role> 角色分页结果
     * @description 分页模糊查询所有角色
     */
    public PageBean<Role> queryAll(PageBean<Role> pageBean) {
        PageBean<Role> result = null;
        try {
            // 查询所有角色
            result = roleMapper.queryAll(pageBean);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @param id 用户主键
     * @return List<Role> 角色列表
     * @description 根据用户主键查询角色
     */
    public List<Role> queryByUserId(Long id) {
        List<Role> roles = null;
        try {
            roles = roleMapper.queryByUserId(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return roles;
    }

    /**
     * @param id 角色主键
     * @return Role 角色
     * @description 根据角色主键查询角色
     */
    public Role queryByRoleId(Long id) {
        Role role = null;
        try {
            role = roleMapper.queryByRoleId(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return role;
    }

    /**
     * @param id 菜单主键
     * @return List<Long> 角色主键列表
     * @description 根据菜单主键查询对应的角色主键列表
     */
    public List<Long> queryIdsByMenuId(Long id) {
        List<Long> idsOfRole = null;
        try {
            idsOfRole = roleMapper.queryIdsByMenuId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return idsOfRole;
    }

    /**
     * @param role 角色
     * @return boolean 是否成功
     * @description 新增角色
     */
    public boolean add(Role role) {
        int rows = 0;
        try {
            role.setState(CommonConstants.DATA_COMMON_DEFAULT_STATE);
            role.setCreateTime(new Date());
            role.setUpdateTime(role.getCreateTime());
            rows = roleMapper.add(role);
        } catch (SQLIntegrityConstraintViolationException e) {
            // 约束异常 (有重复的roleName)
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new RuntimeException(CommonConstants.I18N_ROLE_ERROR_REPEAT_ROLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }

    /**
     * @param role 角色
     * @return boolean 是否成功
     * @description 修改角色
     */
    public boolean update(Role role) {
        int rows = 0;
        try {
            role.setUpdateTime(new Date());
            rows = roleMapper.update(role);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }

    /**
     * @param relativeBean 关联Bean
     * @return boolean 是否成功
     * @description 给用户授权角色
     */
    // TODO 事务待添加
    public boolean updateUserRole(RelativeBean relativeBean) {
        int rows = 0;
        try {
            // 添加对应关联
            rows += roleMapper.addUserList(relativeBean.getId(), relativeBean.getAddIds());
            // 删除对应关联
            rows += roleMapper.deleteByUserIdList(relativeBean.getId(), relativeBean.getDeleteIds());
        } catch (Exception e) {
            rows = 0;
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }

    /**
     * @param relativeBean 关联Bean
     * @return boolean 是否成功
     * @description 修改角色的菜单
     */
    // TODO 事务待添加
    public boolean updateRoleMenus(RelativeBean relativeBean) {
        int rows = 0;
        try {
            // 进行删除操作
            rows += roleMapper.deleteByMenuList(relativeBean.getId(), relativeBean.getDeleteIds());
            // 进行新增操作
            rows += roleMapper.addMenuList(relativeBean.getId(), relativeBean.getAddIds());
        } catch (Exception e) {
            rows = 0;
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }

    /**
     * @param id 角色id
     * @return boolean 是否成功
     * @description 删除角色
     */
    public boolean delete(Long id) {
        int rows = 0;
        try {
            // 如果角色有用户关联, 则不能删除
            List<User> users = UserService.getInstance().queryByRoleId(id);
            if (Util.isNotEmpty(users)) {
                throw new RuntimeException(Core.i18n().getValue(CommonConstants.I18N_ROLE_ERROR_REQUIRED_BY_USER));
            }

            // 如果角色有菜单关联, 则不能删除
            List<Menu> menus = MenuService.getInstance().queryByIdOfRole(id);
            if (Util.isNotEmpty(menus)) {
                throw new RuntimeException(Core.i18n().getValue(CommonConstants.I18N_ROLE_ERROR_REQUIRED_BY_MENU));
            }

            rows = roleMapper.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }
}
