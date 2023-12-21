package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.livebos.console.common.util.Util;
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
     * @param menuIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int addMenuList(Long roleId, List<Long> menuIds) {
        if (menuIds.size() <= 0) {
            return 1;
        }
        int rows = 0;
        try {
            String sql = "insert into CT_Rbac_Role_Menu(ID, FRoleId, FMenuId) values(?, ?, ?) ";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            for (Long menuId : menuIds) {
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
        }
        return rows;
    }

    /**
     * 根据角色id删除对应的菜单
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByMenuList(Long roleId, List<Long> menuIds) {
        if (Util.isEmpty(menuIds)) {
            return 1;
        }
        int rows = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_Role_Menu where FRoleId = ? and FMenuId in ");
            sql.append(SQLUtil.listToSQLList(menuIds));

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, roleId);
            for (int i = 0; i < menuIds.size(); i++) {
                dao.setLong(i + 2, menuIds.get(i));
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
     * 根据角色id删除对应的所有角色_菜单关联
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
            sql.append("delete from CT_Rbac_Role_Menu where FRoleId in ");
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
     * 根据菜单id删除对应的所有角色_菜单关联
     *
     * @param menuIds
     * @return
     */
    // TODO 事务待修改
    @Override
    public int deleteByMenuId(List<Long> menuIds) {
        if (Util.isEmpty(menuIds)) {
            return 1;
        }
        int rows = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_Role_Menu where FMenuId in ");
            sql.append(SQLUtil.listToSQLList(menuIds));

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < menuIds.size(); i++) {
                dao.setLong(i + 1, menuIds.get(i));
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
