package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apex.util.ApexDao;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common.SQLCommon;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.UserRoleConstants;
import org.springframework.transaction.annotation.Transactional;

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
     * @param userId
     * @param addId
     * @return
     */
    // TODO 批量新增事务, 不需要可能要删掉
    @Transactional
    public int add(Long userId, List<Long> addId) {
        int rows = 0;

        try {
            ApexDao dao = new ApexDao();
            for (Long roleId : addId) {
                long nextID = getNextID(UserRoleConstants.STUDIO_RBAC_USER_ROLE);
                String sql = "insert into CT_Rbac_User_Role(ID, FUserId, FRoleId) values(?, ?, ?) ";
                dao.prepareStatement(sql);
                dao.setLong(1, nextID);
                dao.setLong(2, userId);
                dao.setLong(3, roleId);
                rows += dao.executeUpdate(getDataSource());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 根据用户id删除对应的角色
     * @param userId
     * @param deleteId
     * @return
     */
    // TODO 批量删除事务, 不需要可能要删掉
    @Transactional
    public int delete(Long userId, List<Long> deleteId) {
        int rows = 0;
        try {
            String sql = "delete from CT_Rbac_User_Role where FUserId = ? and FRoleId in " +
                    SQLCommon.listToSQLList(deleteId);
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, userId);
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }


}
