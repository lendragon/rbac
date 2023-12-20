package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;

import java.util.List;

public interface IMenuMapper {

    List<Menu> query();

    List<Menu> queryById(Long menuId);

    List<Menu> queryByRoleId(Long roleId);

    List<Menu> queryByUserId(Long userId);

    int add(Menu menu);

    int update(Menu menu);

    int delete(List<Long> id);
}
