package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.livebos.console.common.util.Util;
import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.SQLUtil;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserRoleMapper;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserRoleMapper
 * @Description: 操作用户_角色关系表的类
 * @Author linlongyue
 * @Date 2023/12/14
 * @Version 1.0
 */
public class UserRoleMapperImpl extends GeneralMapper implements IUserRoleMapper {
    /**
     * 根据角色id列表查询用户id
     * @param roleIds
     * @return
     */
    @Override
    public List<Long> queryUserIdByRoleIds(List<Long> roleIds) {
        if (Util.isEmpty(roleIds)) {
            return null;
        }
        List<Long> userIds = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select FUserId from CT_Rbac_User_Role where FRoleId in ");
            sql.append(SQLUtil.listToSQLList(roleIds));
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < roleIds.size(); i++) {
                dao.setLong(i + 1, roleIds.get(i));
            }
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                userIds.add(rs.getLong("FUserId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(rs);
        }
        return userIds;
    }

    /**
     * 根据用户id新增对应的角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int addRoleList(Long userId, List<Long> roleIds) {
        if (Util.isEmpty(roleIds)) {
            return 1;
        }
        int rows = 0;
        try {
            String sql = "insert into CT_Rbac_User_Role(ID, FUserId, FRoleId) values(?, ?, ?) ";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            for (Long roleId : roleIds) {
                long nextID = getNextID(CommonConstants.TABLE_RBAC_USER_ROLE);
                dao.setLong(1, nextID);
                dao.setLong(2, userId);
                dao.setLong(3, roleId);

                rows += dao.executeUpdate(getDataSource());
            }
        } catch (SQLException e) {
            rows = 0;
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return rows;
    }

    /**
     * 根据用户id新增对应的角色
     *
     * @param roleId
     * @param userIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int addUserList(Long roleId, List<Long> userIds) {
        if (Util.isEmpty(userIds)) {
            return 1;
        }
        int rows = 0;
        try {
            String sql = "insert into CT_Rbac_User_Role(ID, FUserId, FRoleId) values(?, ?, ?) ";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            for (Long userId : userIds) {
                long nextID = getNextID(CommonConstants.TABLE_RBAC_USER_ROLE);
                dao.setLong(1, nextID);
                dao.setLong(2, userId);
                dao.setLong(3, roleId);

                rows += dao.executeUpdate(getDataSource());
            }
        } catch (SQLException e) {
            rows = 0;
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return rows;
    }

    /**
     * 根据用户id删除对应的角色列表
     *
     * @param userId
     * @param roleIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByRoleIdList(Long userId, List<Long> roleIds) {
        if (Util.isEmpty(roleIds)) {
            return 1;
        }
        int rows = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User_Role where FUserId = ? and FRoleId in ");
            sql.append(SQLUtil.listToSQLList(roleIds));

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, userId);
            for (int i = 0; i < roleIds.size(); i++) {
                dao.setLong(i + 2, roleIds.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return rows;
    }

    /**
     * 根据用户id除对应的所有用户_角色关联
     *
     * @param userIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByUserId(List<Long> userIds) {
        if (Util.isEmpty(userIds)) {
            return 1;
        }
        int rows = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User_Role where FUserId in ");
            sql.append(SQLUtil.listToSQLList(userIds));

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < userIds.size(); i++) {
                dao.setLong(i + 1, userIds.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return rows;
    }

    /**
     * 根据角色id删除对应的所有用户_角色关联
     *
     * @param roleIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByRoleId(List<Long> roleIds) {
        if (Util.isEmpty(roleIds)) {
            return 1;
        }
        int rows = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User_Role where FRoleId in ");
            sql.append(SQLUtil.listToSQLList(roleIds));

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < roleIds.size(); i++) {
                dao.setLong(i + 1, roleIds.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return rows;
    }

    /**
     * 根据角色id删除对应的用户列表
     *
     * @param roleId
     * @return
     */
    @Override
    public int deleteByUserIdList(Long roleId, List<Long> userIds) {
        if (Util.isEmpty(userIds)) {
            return 1;
        }
        int rows = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User_Role where FRoleId = ? and FUserId in ");
            sql.append(SQLUtil.listToSQLList(userIds));

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, roleId);
            for (int i = 0; i < userIds.size(); i++) {
                dao.setLong(i + 2, userIds.get(i));
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
