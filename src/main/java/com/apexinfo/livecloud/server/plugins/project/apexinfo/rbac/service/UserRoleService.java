package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.UserRoleMapperImpl;
import org.apache.log4j.Logger;

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
     * 根据用户id新增对应的角色
     *
     * @param userId
     * @param addId
     * @return
     */
    int add(Long userId, List<Long> addId) {
        return userRoleMapper.add(userId, addId);
    }

    /**
     * 根据用户id删除对应的角色
     *
     * @param userId
     * @param deleteId
     * @return
     */
    int deleteByIdList(Long userId, List<Long> deleteId) {
        return userRoleMapper.deleteByIdList(userId, deleteId);
    }

    /**
     * 根据用户id除对应的所有用户_角色关联
     *
     * @param userId
     * @return
     */
    int deleteByUserId(List<Long> userId) {
        return userRoleMapper.deleteByUserId(userId);
    }

    /**
     * 根据角色id删除对应的所有用户_角色关联
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(List<Long> roleId) {
        return userRoleMapper.deleteByRoleId(roleId);
    }
}
