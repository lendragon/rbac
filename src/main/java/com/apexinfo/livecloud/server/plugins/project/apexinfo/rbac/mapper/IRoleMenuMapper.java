package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import java.sql.SQLException;
import java.util.List;

public interface IRoleMenuMapper {
    List<Long> queryIdByMenuIds(List<Long> menuIds) throws SQLException;
    int addMenuList(Long roleId, List<Long> addId) throws SQLException;
    int deleteByMenuList(Long roleId, List<Long> deleteId) throws SQLException;
}
