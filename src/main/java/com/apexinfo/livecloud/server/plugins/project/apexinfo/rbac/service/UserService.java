package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service;

import com.apexinfo.livecloud.server.plugins.product.mobile.extend.DemoService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl.UserMapperImpl;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
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
     * 分页查询/模糊查询所有用户 / 根据用户id查询用户 / 根据角色id查询用户
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param id
     * @param roleId
     * @return
     */
    public PageDTO<User> query(Integer pageNo, Integer pageSize, String keyword, Long id, Long roleId) {
        PageDTO<User> pageDTO = null;
        if (id != null) {
            // 根据用户id查询
            pageDTO = userMapper.queryById(id);
            pageDTO.setPageNo(1);
            pageDTO.setPageSize(1);
            return pageDTO;
        }
        if (roleId != null) {
            // 根据角色id查询
            pageDTO = userMapper.queryByRoleId(roleId);
            return pageDTO;
        }
        // 查询全部
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        pageDTO = userMapper.query(pageNo, pageSize, keyword);
        return pageDTO;
    }

    /**
     * 根据用户编号或用户名查找用户
     *
     * @param no
     * @param name
     * @return
     */
    public List<User> queryByNoOrName(String no, String name) {
        return userMapper.queryByNoOrName(no, name);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    public int add(User user) {
        int rows;
        user.setPassword(CommonConstants.DATA_USER_PASSWORD);
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

//    /**
//     * 修改用户的角色
//     *
//     * @param relativeDTO
//     * @return
//     */
//    // TODO 事务待修改
//    public int updateUserRoles(RelativeDTO relativeDTO) {
//        if (relativeDTO.getAddList().size() == 0 && relativeDTO.getDeleteList().size() == 0) {
//            return 1;
//        }
//        int rows = 0;
//        // 进行删除操作
//        rows += UserRoleService.getInstance().deleteByRoleIdList(relativeDTO.getId(), relativeDTO.getDeleteList());
//        // 进行新增操作
//        rows += UserRoleService.getInstance().addRoleList(relativeDTO.getId(), relativeDTO.getAddList());
//        return rows;
//    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    // TODO 事务待修改
    public int delete(List<Long> id) {
        UserRoleService.getInstance().deleteByUserId(id);
        return userMapper.delete(id);
    }
}
