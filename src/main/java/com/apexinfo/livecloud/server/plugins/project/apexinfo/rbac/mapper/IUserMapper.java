package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserMapper {
    PageBean<User> queryAll(PageBean<User> searchPageBean) throws SQLException;

    User queryByUserId(Long id) throws Exception;
    List<Long> queryIdByRoleId(Long roleId) throws SQLException;

    Long queryIdByUserCode(String userCode) throws Exception;

    int add(User user) throws SQLException;

    int update(User user) throws SQLException;

    int updatePassword(User user) throws SQLException;

    int delete(List<Long> userIds) throws SQLException;
}
