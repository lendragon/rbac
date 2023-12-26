package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: Valid
 * @Description: 数据校验注解
 * @Author linlongyue
 * @Date 2023/12/25
 * @Version 1.0
 */
public interface IRoleMapper {
    /**
     * @param pageBean 分页查询参数, pageNo, pageSize, keyword
     * @return PageBean<Role> 角色分页结果
     * @description 分页模糊查询角色
     */
    PageBean<Role> queryAll(PageBean<Role> pageBean) throws SQLException;

    /**
     * @param id 用户主键
     * @return List<Role> 角色列表
     * @description 根据用户主键查询角色
     */
    List<Role> queryByUserId(Long id) throws SQLException;

    /**
     * @param id 角色主键
     * @return Role 角色
     * @description 根据角色主键查询角色
     */
    Role queryByRoleId(Long id) throws Exception;

    /**
     * @param id 菜单主键列表
     * @return List<Long> 角色主键列表
     * @description 根据菜单主键查询对应的角色主键列表
     */
    List<Long> queryIdsByMenuId(Long id) throws SQLException;

    /**
     * @param role 角色
     * @return int 影响的行数
     * @description 新增角色
     */
    int add(Role role) throws SQLException;

    /**
     * @param id  角色主键
     * @param menuIds 要添加的菜单主键列表
     * @return int 影响的行数
     * @description 根据角色主键新增对应的菜单列表关联
     */
    int addMenuList(Long id, List<Long> menuIds) throws SQLException;

    /**
     * @param role 角色
     * @return int 影响的行数
     * @description 修改角色信息
     */
    int update(Role role) throws SQLException;

    /**
     * @param id  角色主键
     * @param userIds 用户主键列表
     * @return 影响的行数
     * @description 根据角色主键新增对应的用户主键列表关联
     */
    int addUserList(Long id, List<Long> userIds) throws Exception;

    /**
     * @param id 角色主键
     * @return int 影响的行数
     * @description 删除角色, 即将角色的状态改成2, 删除
     */
    int delete(Long id) throws SQLException;

    /**
     * @param id  角色主键
     * @param menuIds 要删除的菜单主键列表
     * @return int 影响的行数
     * @description 根据角色主键删除对应的菜单列表关联
     */
    int deleteByMenuList(Long id, List<Long> menuIds) throws SQLException;

    /**
     * @param id  角色主键
     * @param userIds 用户主键列表
     * @return int 影响的行数
     * @description 根据角色主键删除对应的用户主键列表关联
     */
    int deleteByUserIdList(Long id, List<Long> userIds) throws SQLException;
}
