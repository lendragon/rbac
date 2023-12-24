package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.product.sql.query.util.MD5Tools;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.UserMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.StateEnum;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.apache.log4j.Logger;

import java.sql.SQLException;
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

    private IUserMapper userMapper;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private UserService() {
        userMapper = new UserMapperImpl();
    }

    /**
     * 分页模糊查询所有用户
     *
     * @param pageBean 分页Bean
     * @return
     */
    public PageBean<User> queryAll(PageBean<User> pageBean) {
        PageBean<User> result = null;
        try {
            result = userMapper.queryAll(pageBean);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return
     */
    public User queryByUserId(Long userId) {
        User user = null;
        try {
            user = userMapper.queryByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return user;
    }


    /**
     * 根据用户标识查找用户
     *
     * @param userCode 用户编码
     * @return
     */
    public Long queryIdByUserCode(String userCode) {
        Long userId = null;
        try {
            userId = userMapper.queryIdByUserCode(userCode);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return userId;
    }

    /**
     * 根据角色id查询用户id
     *
     * @param roleId 角色id
     * @return
     */
    public List<Long> queryIdByRoleId(Long roleId) {
        List<Long> userIds = null;
        try {
            userIds = userMapper.queryIdByRoleId(roleId);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return userIds;
    }

    /**
     * 新增用户
     *
     * @param user 用户
     * @return
     */
    public int add(User user) {
        int rows = 0;
        user.setState(StateEnum.正常.ordinal());
        user.setPassword(new MD5Tools().stringToMD5(CommonConstants.DATA_USER_PASSWORD));
        user.setCreateTime(new Date());
        user.setUpdateTime(user.getCreateTime());
        try {
            rows = userMapper.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 修改用户
     *
     * @param user 用户
     * @return
     */
    public int update(User user) {
        int rows = 0;
        user.setUpdateTime(new Date());
        try {
            rows = userMapper.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 修改用户密码
     *
     * @param user 用户
     * @return
     */
    public int updatePassword(User user) {
        int rows = 0;
        user.setUpdateTime(new Date());
        try {
            rows = userMapper.updatePassword(user);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id列表
     * @return
     */
    public int delete(List<Long> userIds) {
        int rows = 0;
        try {
            rows += userMapper.delete(userIds);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows;
    }
}
