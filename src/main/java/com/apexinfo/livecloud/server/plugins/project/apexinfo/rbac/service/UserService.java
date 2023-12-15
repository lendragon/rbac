package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.UserMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.UserRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @ClassName: UserService
 * @Description: 用户业务逻辑层
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class UserService {
    private static final Logger logger = Logger.getLogger(DemoService.class);

    private static UserService instance;

    private UserMapper userMapper;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private UserService() {
        userMapper = new UserMapper();
    }

    /**
     * 分页查询/模糊查询所有用户 / 根据用户id查询用户
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param id
     * @return
     */
    public List<User> query(Long pageNo, Long pageSize, String keyword, Long id) {
        if (id == null) {
            if (pageNo == null) {
                pageNo = 1L;
            }
            if (pageSize == null) {
                pageSize = 20L;
            }
            return userMapper.query(pageNo, pageSize, keyword);
        }
        return userMapper.queryById(id);
    }


    /**
     * 新增用户
     * @param user
     * @return
     */
    public int add(User user) {
        int rows;
        user.setCreateTime(new Date());
        user.setUpdateTime(user.getCreateTime());
        rows = userMapper.add(user);
        return rows;
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    public int update(User user) {
        int rows;
        user.setUpdateTime(new Date());
        rows = userMapper.update(user);
        return rows;
    }

    /**
     * 修改用户的角色
     * @param userId
     * @param roleId
     * @return
     */
    public int updateUserRoles(Long userId, Set<Long> roleId) {
        int rows = 0;
        UserRoleMapper userRoleMapper = new UserRoleMapper();

        // 查询该用户之前拥有的所有角色lastRoleId
        List<Role> roles = RoleService.getInstance().query(null, null, null, userId);
        Set<Long> lastRoleId = new HashSet<>();
        roles.forEach((role) -> {
            lastRoleId.add(role.getId());
        });

        // lastRoleId - roleId, 进行删除操作
        Collection deleteIdCollection = CollectionUtils.subtract(lastRoleId, roleId);
        if (deleteIdCollection.size() > 0) {
            List<Long> deleteId = new ArrayList<>(deleteIdCollection);
            rows += userRoleMapper.delete(userId, deleteId);
        }

        // roleId - lastRoleId, 进行新增操作
        Collection addIdCollection = CollectionUtils.subtract(roleId, lastRoleId);
        if (addIdCollection.size() > 0) {
            List<Long> addId = new ArrayList<>(addIdCollection);
            rows += userRoleMapper.add(userId, addId);
        }

        return addIdCollection.size() == 0 && deleteIdCollection.size() == 0 ? 1 : rows;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    // TODO 同时还要删除用户_角色表中的用户
    public int delete(List<Long> id) {
        return userMapper.delete(id);
    }


}
