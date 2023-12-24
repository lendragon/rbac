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
     * 分页模糊查询角色
     *
     * @param pageBean 分页查询参数, pageNo, pageSize, keyword
     * @return
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
            sql.append("select ID, FRoleCode, FName, FState, FDescription, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_Role where FState != 2 ");
            // 拼接模糊查询SQL
            if (!Util.isEmpty(keyword)) {
                SQLUtil.likeContact(sql, "FRoleCode", "FName", "FDescription");
            }
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            if (!Util.isEmpty(keyword)) {
                SQLUtil.setLikeSQL(dao, keyword, 1, 3);
            }
            rs = dao.getRowSet(getDataSource(), pageNo, pageSize, null);
            pageBean.setTotal(rs.getCount());
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setRoleCode(rs.getString("FRoleCode"));
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
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<Role> queryByUserId(Long userId) throws SQLException {
        List<Role> roles = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FRoleCode, FName, FState, FDescription, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_Role where ID in ");
            sql.append("(select FRoleId from CT_Rbac_User_Role where FUserId = ?)");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, userId);
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setRoleCode(rs.getString("FRoleCode"));
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
     * 根据角色id查询角色
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public Role queryByRoleId(Long roleId) throws Exception {
        Role role = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, FRoleCode, FName, FState, FDescription, FCreateTime, FUpdateTime ");
        sql.append("from CT_Rbac_Role where ID = ? ");

        role = SQLTool.one(Role.class, sql.toString(), roleId);
        return role;
    }

    @Override
    public Long queryIdByRoleCode(String roleCode) throws Exception {
        Long roleId = null;
        String sql = "select ID from CT_Rbac_Role where FRoleCode = ? ";

        roleId = SQLTool.one(Long.class, sql.toString(), roleId);
        return roleId;
    }

    /**
     * 新增角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public int add(Role role) throws SQLException {
        int rows = 0;
        long nextId = getNextID(CommonConstants.TABLE_RBAC_ROLE);
        role.setId(nextId);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into CT_Rbac_Role(ID, FRoleCode, FName, FState, FDescription, FCreateTime, ");
        sql.append("FUpdateTime) values(?, ?, ?, ?, ?, ?, ?)");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, role.getId());
        dao.setString(2, role.getRoleCode());
        dao.setString(3, role.getName());
        dao.setInt(4, role.getState());
        dao.setString(5, role.getDescription());
        dao.setObject(6, role.getCreateTime());
        dao.setObject(7, role.getUpdateTime());

        rows = dao.executeUpdate(getDataSource());
        return rows;
    }

    /**
     * 修改角色信息
     *
     * @param role 角色
     * @return
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
     * 删除角色, 即将角色的状态改成2, 删除
     *
     * @param roleIds 角色id列表
     * @return
     */
    @Override
    public int delete(List<Long> roleIds) throws SQLException {
        int rows = 0;

        StringBuilder sql = new StringBuilder();
        sql.append("update CT_Rbac_Role set FState = 2 where ID in ");
        sql.append(SQLUtil.listToSQLList(roleIds));

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        for (int i = 0; i < roleIds.size(); i++) {
            dao.setLong(i + 1, roleIds.get(i));
        }
        rows = dao.executeUpdate(getDataSource());
        return rows;
    }
}
