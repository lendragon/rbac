package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;

import java.util.List;

public interface IUserMapper {
    PageDTO<User> query(Integer pageNo, Integer pageSize, String keyword);

    PageDTO<User> queryById(Long id);

    List<User> queryByNoOrName(String no, String name);

    int add(User user);

    int update(User user);

    int delete(List<Long> id);
}
