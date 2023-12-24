package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.UserRoleMapperImpl;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: UserRoleService
 * @Description: 用户角色表业务逻辑层
 * @Author linlongyue
 * @Date 2023/12/20
 * @Version 1.0
 */
public class UserRoleService {
    private static final Logger logger = Logger.getLogger(DemoService.class);
    private static UserRoleService instance;
    private IUserRoleMapper userRoleMapper;

    public static UserRoleService getInstance() {
        if (instance == null) {
            instance = new UserRoleService();
        }
        return instance;
    }

    private UserRoleService() {
        userRoleMapper = new UserRoleMapperImpl();
    }

    /**
     * 根据角色id列表查询关联的用户id列表
     *
     * @param roleIds 角色id列表
     * @return
     */
    public List<Long> queryByRoleIds(List<Long> roleIds) {
        List<Long> userIds = null;
        try {
            userIds = userRoleMapper.queryUserIdByRoleIds(roleIds);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return userIds;
    }

    /**
     * 根据用户id新增对应的角色列表
     *
     * @param roleId 角色id
     * @param userIds 用户id列表
     * @return
     */
    public int addUserList(Long roleId, List<Long> userIds) throws Exception {
        return userRoleMapper.addUserList(roleId, userIds);
    }

    /**
     * 根据角色id删除对应的用户列表
     *
     * @param roleId 用户id
     * @param deleteId 角色id列表
     * @return
     */
    public int deleteByUserIdList(Long roleId, List<Long> deleteId) throws SQLException {
        return userRoleMapper.deleteByUserIdList(roleId, deleteId);
    }
}
