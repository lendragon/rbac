package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import java.util.List;

public interface IRoleMenuMapper {
    int add(Long roleId, List<Long> addId);

    int deleteByList(Long roleId, List<Long> deleteId);

    int deleteByRoleId(List<Long> roleId);

    int deleteByMenuId(List<Long> menuId);
}
