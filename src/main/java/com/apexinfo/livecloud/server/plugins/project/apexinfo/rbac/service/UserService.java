package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.UserMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.UserRoleMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelaDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
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
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param id
     * @return
     */
    public PageDTO<User> query(Integer pageNo, Integer pageSize, String keyword, Long id) {
        PageDTO<User> pageDTO = null;
        if (id == null) {
            if (pageNo == null) {
                pageNo = 1;
            }
            if (pageSize == null) {
                pageSize = 20;
            }
            pageDTO = userMapper.query(pageNo, pageSize, keyword);
        } else {
            pageDTO = userMapper.queryById(id);
            pageDTO.setPageNo(1);
            pageDTO.setPageSize(1);
        }
        return pageDTO;
    }

    /**
     * 新增用户
     *
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
     *
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
     *
     * @param relaDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateUserRoles(RelaDTO relaDTO) {
        if (relaDTO.getAddList().size() == 0 && relaDTO.getDeleteList().size() == 0) {
            return 1;
        }
        int rows = 0;
        UserRoleMapper userRoleMapper = new UserRoleMapper();
        // 进行删除操作
        rows += userRoleMapper.deleteByIdList(relaDTO.getId(), relaDTO.getDeleteList());
        // 进行新增操作
        rows += userRoleMapper.add(relaDTO.getId(), relaDTO.getAddList());
        return rows;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> id) {
        UserRoleMapper userRoleMapper = new UserRoleMapper();
        userRoleMapper.deleteByUserId(id);
        return userMapper.delete(id);
    }
}
