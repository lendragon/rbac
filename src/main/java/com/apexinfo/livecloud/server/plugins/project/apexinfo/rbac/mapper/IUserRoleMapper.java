package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import java.util.List;

public interface IUserRoleMapper {
    List<Long> queryUserIdByRoleIds(List<Long> roleIds);
    int addRoleList(Long userId, List<Long> roleIds);
    int addUserList(Long roleId, List<Long> userIds);

    int deleteByRoleIdList(Long userId, List<Long> roleIds);

    int deleteByUserId(List<Long> userId);

    int deleteByRoleId(List<Long> roleId);

    int deleteByUserIdList(Long roleId, List<Long> userIds);
}
