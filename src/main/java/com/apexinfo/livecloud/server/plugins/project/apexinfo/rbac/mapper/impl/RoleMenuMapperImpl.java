package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.util.ApexDao;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.SQLUtil;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IRoleMenuMapper;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: RoleMenuMapper
 * @Description: 操作角色_菜单关系表的类
 * @Author linlongyue
 * @Date 2023/12/14
 * @Version 1.0
 */
public class RoleMenuMapperImpl extends GeneralMapper implements IRoleMenuMapper {
    /**
     * 根据角色id新增对应的菜单
     *
     * @param roleId
     * @param addId
     * @return
     */
    // TODO 事务待修改
    @Override
    public int add(Long roleId, List<Long> addId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            String sql = "insert into CT_Rbac_Role_Menu(ID, FRoleId, FMenuId) values(?, ?, ?) ";
            dao = new ApexDao();
            dao.prepareStatement(sql);
            for (Long menuId : addId) {
                long nextID = getNextID(CommonConstants.TABLE_RBAC_ROLE_MENU);
                dao.setLong(1, nextID);
                dao.setLong(2, roleId);
                dao.setLong(3, menuId);

                rows += dao.executeUpdate(getDataSource());
            }
        } catch (SQLException e) {
            rows = 0;
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 根据角色id删除对应的菜单
     *
     * @param roleId
     * @param deleteId
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByList(Long roleId, List<Long> deleteId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_Role_Menu where FRoleId = ? and FMenuId in ");
            sql.append(SQLUtil.listToSQLList(deleteId));

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, roleId);
            for (int i = 0; i < deleteId.size(); i++) {
                dao.setLong(i + 2, deleteId.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 根据角色id删除对应的所有角色_菜单关联
     *
     * @param roleId
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByRoleId(List<Long> roleId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_Role_Menu where FRoleId in ");
            sql.append(SQLUtil.listToSQLList(roleId));

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < roleId.size(); i++) {
                dao.setLong(i + 1, roleId.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 根据菜单id删除对应的所有角色_菜单关联
     *
     * @param menuId
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByMenuId(List<Long> menuId) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_Role_Menu where FMenuId in ");
            sql.append(SQLUtil.listToSQLList(menuId));

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < menuId.size(); i++) {
                dao.setLong(i + 1, menuId.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }
}
