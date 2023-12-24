package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;

import java.sql.SQLException;
import java.util.List;

public interface IMenuMapper {

    List<Menu> queryAll() throws SQLException;

    Menu queryByMenuId(Long menuId) throws Exception;

    List<Menu> queryByRoleId(Long roleId) throws SQLException;

    List<Menu> queryByUserId(Long userId) throws SQLException;

    int add(Menu menu) throws SQLException;

    int update(Menu menu) throws SQLException;

    int delete(List<Long> id) throws SQLException;
}
