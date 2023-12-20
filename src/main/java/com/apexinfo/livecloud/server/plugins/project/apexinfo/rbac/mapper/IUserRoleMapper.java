package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import java.util.List;

public interface IUserRoleMapper {
    int add(Long userId, List<Long> addId);

    int deleteByIdList(Long userId, List<Long> deleteId);

    int deleteByUserId(List<Long> userId);

    int deleteByRoleId(List<Long> roleId);
}
