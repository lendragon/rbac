package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import java.sql.SQLException;
import java.util.List;

public interface IUserRoleMapper {
    List<Long> queryUserIdByRoleIds(List<Long> roleIds) throws SQLException;
    int addUserList(Long roleId, List<Long> userIds) throws Exception;
    int deleteByUserIdList(Long roleId, List<Long> userIds) throws SQLException;
}
