package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: Valid
 * @Description: 数据校验注解
 * @Author linlongyue
 * @Date 2023/12/25
 * @Version 1.0
 */
public interface IUserMapper {
    /**
     * @param pageBean 分页查询参数, pageNo, pageSize, keyword
     * @return PageBean<User> 用户分页结果
     * @description 分页模糊查询所有用户
     */
    PageBean<User> queryAll(PageBean<User> pageBean) throws SQLException;

    /**
     * @param id 用户主键
     * @return User 用户
     * @description 根据用户主键查询用户
     */
    User queryByUserId(Long id) throws Exception;

    /**
     * @param id   用户主键
     * @param password 密码
     * @return boolean 密码是否正确
     * @description 根据用户主键和密码查询密码是否正确
     */
    boolean queryByUserIdAndPassword(Long id, String password) throws Exception;

    /**
     * @param id 角色主键
     * @return List<User> 用户列表
     * @description 根据角色主键查询用户
     */
    List<User> queryByRoleId(Long id) throws SQLException;

    /**
     * @param user 用户
     * @return int 影响的行数
     * @description 新增用户
     */
    int add(User user) throws SQLException;

    /**
     * @param user 用户
     * @return int 影响的行数
     * @description 修改用户信息
     */
    int update(User user) throws SQLException;

    /**
     * @param user 用户
     * @return int 影响的行数
     * @description 修改用户密码
     */
    int updatePassword(User user) throws SQLException;

    /**
     * @param id 用户主键
     * @return int 影响的行数
     * @description 删除用户, 即将用户的状态改成2, 删除
     */
    int delete(Long id) throws SQLException;
}
