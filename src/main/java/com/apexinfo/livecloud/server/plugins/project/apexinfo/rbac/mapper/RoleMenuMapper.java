package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apex.util.ApexDao;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common.SQLCommon;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.RoleMenuConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.UserRoleConstants;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: RoleMenuMapper
 * @Description: 操作角色_菜单关系表的类
 * @Author linlongyue
 * @Date 2023/12/14
 * @Version 1.0
 */
public class RoleMenuMapper extends GeneralMapper {
    /**
     * 根据角色id新增对应的菜单
     * @param roleId
     * @param addId
     * @return
     */
    // TODO 批量新增事务, 不需要可能要删掉
    @Transactional
    public int add(Long roleId, List<Long> addId) {
        int rows = 0;

        try {
            ApexDao dao = new ApexDao();
            for (Long menuId : addId) {
                long nextID = getNextID(RoleMenuConstants.STUDIO_RBAC_ROLE_MENU);
                String sql = "insert into CT_Rbac_Role_Menu(ID, FRoleId, FMenuId) values(?, ?, ?) ";
                dao.prepareStatement(sql);
                dao.setLong(1, nextID);
                dao.setLong(2, roleId);
                dao.setLong(3, menuId);
                rows += dao.executeUpdate(getDataSource());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 根据角色id删除对应的菜单
     * @param roleId
     * @param deleteId
     * @return
     */
    // TODO 批量删除事务, 不需要可能要删掉
    @Transactional
    public int delete(Long roleId, List<Long> deleteId) {
        int rows = 0;
        try {
            String sql = "delete from CT_Rbac_Role_Menu where FRoleId = ? and FMenuId in " +
                    SQLCommon.listToSQLList(deleteId);
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, roleId);
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }


}
