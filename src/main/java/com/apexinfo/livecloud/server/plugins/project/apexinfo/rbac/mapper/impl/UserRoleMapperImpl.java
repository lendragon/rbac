package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.livebos.console.common.util.Util;
import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.common.SQLTool;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.UserRole;
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
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Long> queryUserIdByRoleIds(List<Long> roleIds) throws SQLException {
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
        } finally {
            closeResource(rs);
        }
        return userIds;
    }

    /**
     * 根据角色id新增对应的用户id列表关联
     *
     * @param roleId  角色id
     * @param userIds 用户id列表
     * @return
     */
    @Override
    public int addUserList(Long roleId, List<Long> userIds) throws Exception {
        int rows = 0;

        List<UserRole> userRoles = new ArrayList<>();
        for (Long userId : userIds) {
            long nextId = getNextID(CommonConstants.TABLE_RBAC_USER_ROLE);
            userRoles.add(new UserRole(nextId, userId, roleId));
        }
        rows += SQLTool.insert(userRoles);

        return rows;
    }

    /**
     * 根据角色id删除对应的用户id列表关联
     *
     * @param roleId  角色id
     * @param userIds 用户id列表
     * @return
     */
    @Override
    public int deleteByUserIdList(Long roleId, List<Long> userIds) throws SQLException {
        int rows = 0;
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
        return rows;
    }
}
