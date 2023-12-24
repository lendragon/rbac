package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;

import java.sql.SQLException;
import java.util.List;

public interface IRoleMapper {
    PageBean<Role> queryAll(PageBean<Role> pageBean) throws SQLException;

    Role queryByRoleId(Long id) throws Exception;

    List<Role> queryByUserId(Long userId) throws SQLException;

    Long queryIdByRoleCode(String roleCode) throws Exception;

    int add(Role role) throws SQLException;

    int update(Role role) throws SQLException;

    int delete(List<Long> id) throws SQLException;
}
