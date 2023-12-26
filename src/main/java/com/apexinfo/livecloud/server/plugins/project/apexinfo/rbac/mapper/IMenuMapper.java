package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: Valid
 * @Description: 数据校验注解
 * @Author linlongyue
 * @Date 2023/12/25
 * @Version 1.0
 */
public interface IMenuMapper {

    /**
     * @return List<Menu> 菜单列表
     * @description 查询所有菜单
     */
    List<Menu> queryAll() throws SQLException;

    /**
     * @param id 菜单主键
     * @return Menu 菜单
     * @description 根据菜单主键查询菜单
     */
    Menu queryByMenuId(Long id) throws Exception;

    /**
     * @param id 角色主键
     * @return List<Menu> 菜单列表
     * @description 根据角色主键查询菜单
     */
    List<Menu> queryByRoleId(Long id) throws SQLException;

    /**
     * @param id 用户主键
     * @return List<Menu> 菜单列表
     * @description 根据用户主键查询菜单
     */
    List<Menu> queryByUserId(Long id) throws SQLException;

    /**
     * @param id 菜单主键列表
     * @return List<Long> 子菜单主键列表
     * @description 根据菜单主键列表查询子菜单主键列表
     */
    List<Long> queryChildrenIdsByMenuId(Long id) throws SQLException;

    /**
     * @param menu 菜单
     * @return int 影响的行数
     * @description 新增菜单
     */
    int add(Menu menu) throws SQLException;

    /**
     * @param menu 菜单
     * @return int 影响的行数
     * @description 修改菜单信息
     */
    int update(Menu menu) throws SQLException;

    /**
     * @param id 菜单主键
     * @return int 影响的行数
     * @description 删除菜单, 即将菜单的状态改为2, 删除
     */
    int delete(Long id) throws SQLException;
}
