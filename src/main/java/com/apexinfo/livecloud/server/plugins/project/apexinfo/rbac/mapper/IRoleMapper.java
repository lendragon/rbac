package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IRoleMapper {
    PageDTO<Role> query(Integer pageNo, Integer pageSize, String keyword);

    PageDTO<Role> queryByUserId(Long userId);

    PageDTO<Role> queryById(Long id);

    int add(Role role);

    int update(Role role);

    int delete(List<Long> id);
}
