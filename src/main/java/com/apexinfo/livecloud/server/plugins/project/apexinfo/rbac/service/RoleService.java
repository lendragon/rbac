package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apex.form.transaction.TransactionManagerFactory;
import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.RoleMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelativeBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.StateEnum;
import org.apache.log4j.Logger;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.sql.SQLException;
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
     * 分页模糊查询所有角色
     *
     * @param pageBean 分页Bean
     * @return
     */
    public PageBean<Role> queryAll(PageBean<Role> pageBean) {
        try {
            // 查询所有角色
            pageBean = roleMapper.queryAll(pageBean);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return pageBean;
    }

    /**
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return
     */
    // TODO 查询角色还是只需要查询角色id, 如果用户不需要直接查看角色的话, 可以删除
    public List<Role> queryByUserId(Long userId) {
        List<Role> roles = null;
        try {
            roles = roleMapper.queryByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return roles;
    }

    /**
     * 根据角色id查询角色
     *
     * @param roleId 角色id
     * @return
     */
    public Role queryByRoleId(Long roleId) {
        Role role = null;
        try {
            role = roleMapper.queryByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return role;
    }

    /**
     * 根据角色编号查询角色id
     *
     * @param roleCode 角色id
     * @return
     */
    public Long queryIdByRoleCode(String roleCode) {
        Long roleId = null;
        try {
            roleId = roleMapper.queryIdByRoleCode(roleCode);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return roleId;
    }

    /**
     * 新增角色
     *
     * @param role 角色
     * @return
     */
    public int add(Role role) {
        int rows = 0;
        role.setState(StateEnum.正常.ordinal());
        role.setCreateTime(new Date());
        role.setUpdateTime(role.getCreateTime());
        try {
            rows = roleMapper.add(role);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 修改角色
     *
     * @param role 角色
     * @return
     */
    public int update(Role role) {
        int rows = 0;
        role.setUpdateTime(new Date());
        try {
            rows = roleMapper.update(role);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 给用户授权角色
     *
     * @param relativeBean 关联Bean
     * @return
     */
    public int updateUserRole(RelativeBean relativeBean) {
        int rows = 0;
        TransactionManager tm = TransactionManagerFactory.INSTANCE.getTransactionManager();
        try {
            tm.begin();
            // 添加对应关联
            rows += UserRoleService.getInstance().addUserList(relativeBean.getRoleId(), relativeBean.getAddIds());
            // 删除对应关联
            rows += UserRoleService.getInstance().deleteByUserIdList(relativeBean.getRoleId(), relativeBean.getDeleteIds());
            tm.commit();
        } catch (Exception e) {
            rows = 0;
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            try {
                tm.rollback();
            } catch (SystemException ex) {
                ex.printStackTrace();
                logger.error(ex.getMessage(), ex);
            }
        }
        return rows;
    }

    /**
     * 修改角色的菜单
     *
     * @param relativeBean 关联Bean
     * @return
     */
    public int updateRoleMenus(RelativeBean relativeBean) {
        int rows = 0;
        TransactionManager tm = TransactionManagerFactory.INSTANCE.getTransactionManager();
        try {
            tm.begin();
            // 进行删除操作
            rows += RoleMenuService.getInstance().deleteByMenuList(relativeBean.getRoleId(), relativeBean.getDeleteIds());
            // 进行新增操作
            rows += RoleMenuService.getInstance().addMenuList(relativeBean.getRoleId(), relativeBean.getAddIds());
        } catch (Exception e) {
            rows = 0;
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            try {
                tm.rollback();
            } catch (SystemException ex) {
                ex.printStackTrace();
                logger.error(ex.getMessage(), ex);
            }
        }
        return rows;
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id列表
     * @return
     */
    public int delete(List<Long> roleIds) {
        int rows = 0;
        try {
            rows = roleMapper.delete(roleIds);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }
}
