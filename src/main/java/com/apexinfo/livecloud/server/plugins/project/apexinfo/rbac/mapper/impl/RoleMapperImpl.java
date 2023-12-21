package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apex.util.Util;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.SQLUtil;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @Override
    public PageDTO<Role> query(Integer pageNo, Integer pageSize, String keyword) {
        PageDTO<Role> pageDTO = new PageDTO<>();
        List<Role> roles = new ArrayList<>();
        pageDTO.setRecords(roles);
        pageDTO.setPageNo(pageNo);
        pageDTO.setPageSize(pageSize);
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FState, FDescription, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_Role where 1 = 1 ");
            // 拼接模糊查询SQL
            if (!Util.isEmpty(keyword)) {
                SQLUtil.likeContact(sql, "FName", "FState", "FDescription");
            }
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            if (!Util.isEmpty(keyword)) {
                SQLUtil.setLikeSQL(dao, keyword, 1, 3);
            }
            rs = dao.getRowSet(getDataSource(), pageNo, pageSize, null);
            pageDTO.setTotal(rs.getCount());
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setName(rs.getString("FName"));
                role.setState(rs.getInt("FState"));
                role.setDescription(rs.getString("FDescription"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(rs);
        }
        return pageDTO;
    }

    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    @Override
    public PageDTO<Role> queryByUserId(Long userId) {
        PageDTO<Role> pageDTO = new PageDTO<>();
        List<Role> roles = new ArrayList<>();
        pageDTO.setRecords(roles);
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FState, FDescription, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_Role where ID in ");
            sql.append("(select FRoleId from CT_Rbac_User_Role where FUserId = ?)");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, userId);
            rs = dao.getRowSet(getDataSource());
            pageDTO.setTotal(rs.getCount());
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setName(rs.getString("FName"));
                role.setState(rs.getInt("FState"));
                role.setDescription(rs.getString("FDescription"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(rs);
        }
        return pageDTO;
    }

    /**
     * 根据角色id查询角色
     *
     * @param id
     * @return
     */
    @Override
    public PageDTO<Role> queryById(Long id) {
        PageDTO<Role> pageDTO = new PageDTO<>();
        List<Role> roles = new ArrayList<>();
        pageDTO.setRecords(roles);
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FState, FDescription, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_Role where ID = ? ");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, id);
            rs = dao.getRowSet(getDataSource());
            pageDTO.setTotal(rs.getCount());
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setName(rs.getString("FName"));
                role.setState(rs.getInt("FState"));
                role.setDescription(rs.getString("FDescription"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(rs);
        }
        return pageDTO;
    }

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    @Override
    public int add(Role role) {
        int rows = 0;
        try {
            long nextId = getNextID(CommonConstants.TABLE_RBAC_ROLE);
            role.setId(nextId);
            StringBuilder sql = new StringBuilder();
            sql.append("insert into CT_Rbac_Role(ID, FName, FState, FDescription, FCreateTime, ");
            sql.append("FUpdateTime) values(?, ?, ?, ?, ?, ?)");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, role.getId());
            dao.setString(2, role.getName());
            dao.setInt(3, role.getState());
            dao.setString(4, role.getDescription());
            dao.setObject(5, role.getCreateTime());
            dao.setObject(6, role.getUpdateTime());

            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 修改角色信息
     *
     * @param role
     * @return
     */
    @Override
    public int update(Role role) {
        int rows = 0;
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 删除角色
     *
     * @param ids
     * @return
     */
    // TODO 事务待修改
    @Override
    public int delete(List<Long> ids) {
        int rows = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_Role where ID in ");
            sql.append(SQLUtil.listToSQLList(ids));

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < ids.size(); i++) {
                dao.setLong(i + 1, ids.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return rows;
    }
}
