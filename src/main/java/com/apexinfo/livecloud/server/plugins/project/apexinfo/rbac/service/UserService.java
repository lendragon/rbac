package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apex.util.Util;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.plugins.product.sql.query.util.MD5Tools;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.UserMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

/**
 * @ClassName: UserService
 * @Description: 用户业务逻辑层
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class UserService {
    // 日志输出对象
    private static final Logger logger = Logger.getLogger(UserService.class);

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
     * @param pageBean 分页Bean
     * @return PageBean<User> 用户分页结果
     * @description 分页模糊查询所有用户
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
     * @param id 用户主键
     * @return User 用户
     * @description 根据用户主键查询用户
     */
    public User queryByUserId(Long id) {
        User user = null;
        try {
            user = userMapper.queryByUserId(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return user;
    }

    /**
     * @param id   用户主键
     * @param password 密码
     * @return boolean 是否正确
     * @description 根据用户名和密码查看密码是否正确
     */
    public boolean queryByUserIdAndPassword(Long id, String password) {
        boolean isCorrect = false;
        try {
            password = new MD5Tools().stringToMD5(password);
            isCorrect = userMapper.queryByUserIdAndPassword(id, password);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return isCorrect;
    }

    /**
     * @param id 角色主键
     * @return List<User> 用户列表
     * @description 根据角色主键查询用户
     */
    public List<User> queryByRoleId(Long id) {
        List<User> users = null;
        try {
            users = userMapper.queryByRoleId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return users;
    }

    /**
     * @param user 用户
     * @return boolean 是否成功
     * @description 新增用户
     */
    public boolean add(User user) {
        int rows = 0;
        try {
            user.setState(CommonConstants.DATA_COMMON_DEFAULT_STATE);
            user.setPassword(Util.getMD5Digest(CommonConstants.DATA_USER_DEFAULT_PASSWORD));
            user.setCreateTime(new Date());
            user.setUpdateTime(user.getCreateTime());
            rows = userMapper.add(user);
        } catch (SQLIntegrityConstraintViolationException e) {
            // 约束异常 (有重复的userId)
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new RuntimeException(CommonConstants.I18N_USER_ERROR_REPEAT_USER_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        return rows > 0;
    }

    /**
     * @param user 用户
     * @return boolean 是否成功
     * @description 修改用户
     */
    public boolean update(User user) {
        int rows = 0;
        try {
            user.setUpdateTime(new Date());
            rows = userMapper.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }

    /**
     * @param user 用户
     * @return boolean 是否成功
     * @description 修改用户密码
     */
    public boolean updatePassword(User user) {
        int rows = 0;
        try {
            // 查看新密码和确认密码是否一致
            if (!Objects.equals(user.getPassword(), user.getCheckedPassword())) {
                throw new RuntimeException(Core.i18n().getValue(CommonConstants.I18N_USER_ERROR_WRONG_CHECKED_PASSWORD));
            }

            // 查看用户id和旧密码是否正确
            boolean isCorrect = UserService.getInstance().queryByUserIdAndPassword(user.getId(), user.getOldPassword());
            if (!isCorrect) {
                throw new RuntimeException(Core.i18n().getValue(CommonConstants.I18N_USER_ERROR_WRONG_PASSWORD));
            }

            user.setUpdateTime(new Date());
            user.setPassword(Util.getMD5Digest(user.getPassword()));
            rows = userMapper.updatePassword(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        return rows > 0;
    }

    /**
     * @param userId 用户id
     * @return boolean 是否成功
     * @description 删除用户
     */
    public boolean delete(Long userId) {
        int rows = 0;
        try {
            rows += userMapper.delete(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return rows > 0;
    }
}
