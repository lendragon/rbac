package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apex.util.Util;
import com.apexinfo.livecloud.server.common.SQLTool;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.SQLUtil;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RoleMapper
 * @Description: 操作角色表的类
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class RoleMapperImpl extends GeneralMapper implements IRoleMapper {
    /**
     * @param pageBean 分页查询参数, pageNo, pageSize, keyword
     * @return PageBean<Role> 角色分页结果
     * @description 分页模糊查询角色
     */
    @Override
    public PageBean<Role> queryAll(PageBean<Role> pageBean) throws SQLException {
        int pageNo = pageBean.getPageNo();
        int pageSize = pageBean.getPageSize();
        String keyword = pageBean.getKeyword();

        List<Role> roles = new ArrayList<>();
        pageBean.setRecords(roles);

        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FRoleName, FName, FState, FDescription, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_Role where 1 = 1 ");
            // 拼接模糊查询SQL
            if (!Util.isEmpty(keyword)) {
                SQLUtil.likeContact(sql, "FRoleName", "FName", "FDescription");
            }
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            if (!Util.isEmpty(keyword)) {
                SQLUtil.setLikeSQL(dao, keyword, 1, 3);
            }
            rs = dao.getRowSet(getDataSource(), pageNo, pageSize, null);
            pageBean.setTotal(rs.getCount());

            Role role = null;
            while (rs.next()) {
                role = new Role();
                role.setId(rs.getLong("ID"));
                role.setRoleName(rs.getString("FRoleName"));
                role.setName(rs.getString("FName"));
                role.setState(rs.getInt("FState"));
                role.setDescription(rs.getString("FDescription"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                roles.add(role);
            }
        } finally {
            closeResource(rs);
        }
        return pageBean;
    }

    /**
     * @param id 用户主键
     * @return List<Role> 角色列表
     * @description 根据用户主键查询角色
     */
    @Override
    public List<Role> queryByUserId(Long id) throws SQLException {
        List<Role> roles = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FRoleName, FName, FState, FDescription, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_Role where ID in ");
            sql.append("(select FRoleId from CT_Rbac_User_Role where FUserId = ?)");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, id);
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setRoleName(rs.getString("FRoleName"));
                role.setName(rs.getString("FName"));
                role.setState(rs.getInt("FState"));
                role.setDescription(rs.getString("FDescription"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                roles.add(role);
            }
        } finally {
            closeResource(rs);
        }
        return roles;
    }

    /**
     * @param id 角色主键
     * @return Role 角色
     * @description 根据角色id查询角色
     */
    @Override
    public Role queryByRoleId(Long id) throws Exception {
        Role role = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, FRoleName, FName, FState, FDescription, FCreateTime, FUpdateTime ");
        sql.append("from CT_Rbac_Role where ID = ? ");

        role = SQLTool.one(Role.class, sql.toString(), id);
        return role;
    }

    /**
     * @param id 菜单主键列表
     * @return List<Long> 角色主键列表
     * @description 根据菜单主键查询对应的角色主键列表
     */
    @Override
    public List<Long> queryIdsByMenuId(Long id) throws SQLException {
        List<Long> roleIds = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select FRoleID from CT_Rbac_Role_Menu where FMenuId = ");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, id);

            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                roleIds.add(rs.getLong("FRoleId"));
            }
        } finally {
            closeResource(rs);
        }
        return roleIds;
    }

    /**
     * @param role 角色
     * @return int 影响的行数
     * @description 新增角色
     */
    @Override
    public int add(Role role) throws SQLException {
        int rows = 0;
        long nextId = getNextID(CommonConstants.TABLE_RBAC_ROLE);
        role.setId(nextId);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into CT_Rbac_Role(ID, FRoleName, FName, FState, FDescription, FCreateTime, ");
        sql.append("FUpdateTime) values(?, ?, ?, ?, ?, ?, ?)");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, role.getId());
        dao.setString(2, role.getRoleName());
        dao.setString(3, role.getName());
        dao.setInt(4, role.getState());
        dao.setString(5, role.getDescription());
        dao.setObject(6, role.getCreateTime());
        dao.setObject(7, role.getUpdateTime());

        rows = dao.executeUpdate(getDataSource());
        return rows;
    }

    /**
     * @param id  角色主键
     * @param menuIds 要添加的菜单主键列表
     * @return int 影响的行数
     * @description 根据角色主键新增对应的菜单列表关联
     */
    @Override
    public int addMenuList(Long id, List<Long> menuIds) throws SQLException {
        if (Util.isEmpty(menuIds)) {
            return 0;
        }
        int rows = 0;
        String sql = "insert into CT_Rbac_Role_Menu(ID, FRoleId, FMenuId) values(?, ?, ?) ";
        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql);
        for (Long menuId : menuIds) {
            long nextId = getNextID(CommonConstants.TABLE_RBAC_ROLE_MENU);
            dao.setLong(1, nextId);
            dao.setLong(2, id);
            dao.setLong(3, menuId);

            rows += dao.executeUpdate(getDataSource());
        }
        return rows;
    }

    /**
     * @param role 角色
     * @return int 影响的行数
     * @description 修改角色信息
     */
    @Override
    public int update(Role role) throws SQLException {
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("update CT_Rbac_Role set FName = ?, FState = ?, FUpdateTime = ?, FDescription = ? ");
        sql.append("where ID = ? ");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setString(1, role.getName());
        dao.setInt(2, role.getState());
        dao.setObject(3, role.getUpdateTime());
        dao.setString(4, role.getDescription());
        dao.setLong(5, role.getId());

        rows = dao.executeUpdate(getDataSource());
        return rows;
    }

    /**
     * @param id  角色id
     * @param userIds 用户id列表
     * @return 影响的行数
     * @description 根据角色id新增对应的用户id列表关联
     */
    @Override
    public int addUserList(Long id, List<Long> userIds) throws Exception {
        if (Util.isEmpty(userIds)) {
            return 0;
        }
        int rows = 0;

        String sql = "insert into CT_Rbac_User_Role(ID, FUserId, FRoleId) values(?, ?, ?) ";
        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql);
        for (Long userId : userIds) {
            long nextId = getNextID(CommonConstants.TABLE_RBAC_USER_ROLE);
            dao.setLong(1, nextId);
            dao.setLong(2, userId);
            dao.setLong(3, id);

            rows += dao.executeUpdate(getDataSource());
        }
        return rows;
    }

    /**
     * @param id 角色主键
     * @return int 影响的行数
     * @description 删除角色, 即将角色的状态改成2, 删除
     */
    @Override
    public int delete(Long id) throws SQLException {
        int rows = 0;

        StringBuilder sql = new StringBuilder();
        sql.append("update CT_Rbac_Role set FState = 2 where ID = ");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, id);
        rows = dao.executeUpdate(getDataSource());

        return rows;
    }

    /**
     * @param id  角色主键
     * @param menuIds 要删除的菜单主键列表
     * @return int 影响的行数
     * @description 根据角色主键删除对应的菜单列表关联
     */
    @Override
    public int deleteByMenuList(Long id, List<Long> menuIds) throws SQLException {
        if (Util.isEmpty(menuIds)) {
            return 0;
        }
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("delete from CT_Rbac_Role_Menu where FRoleId = ? and FMenuId in ");
        sql.append(SQLUtil.listToSQLList(menuIds));

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, id);
        for (int i = 0; i < menuIds.size(); i++) {
            dao.setLong(i + 2, menuIds.get(i));
        }
        rows = dao.executeUpdate(getDataSource());
        return rows;
    }

    /**
     * @param id  角色主键
     * @param userIds 用户主键列表
     * @return int 影响的行数
     * @description 根据角色主键删除对应的用户主键列表关联
     */
    @Override
    public int deleteByUserIdList(Long id, List<Long> userIds) throws SQLException {
        if (Util.isEmpty(userIds)) {
            return 0;
        }
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("delete from CT_Rbac_User_Role where FRoleId = ? and FUserId in ");
        sql.append(SQLUtil.listToSQLList(userIds));

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, id);
        for (int i = 0; i < userIds.size(); i++) {
            dao.setLong(i + 2, userIds.get(i));
        }
        rows = dao.executeUpdate(getDataSource());
        return rows;
    }
}
