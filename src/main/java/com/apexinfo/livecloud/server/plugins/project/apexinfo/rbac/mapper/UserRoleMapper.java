package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apex.util.ApexDao;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common.SQLCommon;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.UserRoleConstants;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: UserRoleMapper
 * @Description: 操作用户_角色关系表的类
 * @Author linlongyue
 * @Date 2023/12/14
 * @Version 1.0
 */
public class UserRoleMapper extends GeneralMapper {
    /**
     * 根据用户id新增对应的角色
     *
     * @param userId
     * @param addId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(Long userId, List<Long> addId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            String sql = "insert into CT_Rbac_Role_Menu(ID, FRoleId, FMenuId) values(?, ?, ?) ";
            dao = new ApexDao();
            dao.prepareStatement(sql);
            for (Long roleId : addId) {
                long nextID = getNextID(UserRoleConstants.STUDIO_RBAC_USER_ROLE);
                dao.setLong(1, nextID);
                dao.setLong(2, userId);
                dao.setLong(3, roleId);

                rows += dao.executeUpdate(getDataSource());
            }
        } catch (SQLException e) {
            rows = 0;
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 根据用户id删除对应的角色
     *
     * @param userId
     * @param deleteId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIdList(Long userId, List<Long> deleteId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User_Role where FUserId = ? and FRoleId in ");
            sql.append(SQLCommon.listToSQLList(deleteId));

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, userId);
            for (int i = 0; i < deleteId.size(); i++) {
                dao.setLong(i + 2, deleteId.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 根据用户id除对应的所有用户_角色关联
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByUserId(List<Long> userId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User_Role where FUserId in ");
            sql.append(SQLCommon.listToSQLList(userId));

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < userId.size(); i++) {
                dao.setLong(i + 1, userId.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 根据角色id删除对应的所有用户_角色关联
     *
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByRoleId(List<Long> roleId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User_Role where FRoleId in ");
            sql.append(SQLCommon.listToSQLList(roleId));

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < roleId.size(); i++) {
                dao.setLong(i + 1, roleId.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }
}
