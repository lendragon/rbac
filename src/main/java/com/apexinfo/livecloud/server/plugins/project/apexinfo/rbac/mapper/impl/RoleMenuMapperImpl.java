package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.livebos.console.common.util.Util;
import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
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
     * 根据菜单id列表查询对应的角色id
     *
     * @param menuIds 菜单id列表
     * @return
     */
    @Override
    public List<Long> queryIdByMenuIds(List<Long> menuIds) throws SQLException {
        List<Long> roleIds = null;
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select FRoleID from CT_Rbac_Role_Menu where FMenuId in ");
            sql.append(SQLUtil.listToSQLList(menuIds));
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < menuIds.size(); i++) {
                dao.setLong(i + 1, menuIds.get(i));
            }
            rs = dao.getRowSet(getDataSource());
        } finally {
            closeResource(rs);
        }
        return roleIds;
    }

    /**
     * 根据角色id新增对应的菜单列表关联
     *
     * @param roleId 角色id
     * @param menuIds 要添加的菜单id列表
     * @return
     */
    @Override
    public int addMenuList(Long roleId, List<Long> menuIds) throws SQLException {
        int rows = 0;
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
        return rows;
    }

    /**
     * 根据角色id删除对应的菜单列表关联
     *
     * @param roleId 角色id
     * @param menuIds 要删除的菜单id列表
     * @return
     */
    @Override
    public int deleteByMenuList(Long roleId, List<Long> menuIds) throws SQLException {
        int rows = 0;
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
        return rows;
    }
}
