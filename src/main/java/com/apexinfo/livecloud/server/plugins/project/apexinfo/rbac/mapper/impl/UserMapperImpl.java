package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apex.util.Util;
import com.apexinfo.livecloud.server.common.SQLTool;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.SQLUtil;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserMapper
 * @Description: 操作用户表的类
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class UserMapperImpl extends GeneralMapper implements IUserMapper {

    /**
     * 分页模糊查询所有用户
     *
     * @param pageBean 分页查询参数, pageNo, pageSize, keyword
     * @return
     */
    @Override
    public PageBean<User> queryAll(PageBean<User> pageBean) throws SQLException {
        int pageNo = pageBean.getPageNo();
        int pageSize = pageBean.getPageSize();
        String keyword = pageBean.getKeyword();

        List<User> users = new ArrayList<>();
        pageBean.setRecords(users);

        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FUserCode, FName, FSex, FBirthDay,");
            sql.append("FPhoneNum, FState, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_User where FState != 2 ");
            // 模糊查询拼接SQL
            if (!Util.isEmpty(keyword)) {
                SQLUtil.likeContact(sql, "FUserCode", "FName", "FBirthDay", "FPhoneNum");
            }
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            if (!Util.isEmpty(keyword)) {
                SQLUtil.setLikeSQL(dao, keyword, 1, 4);
            }
            rs = dao.getRowSet(getDataSource(), pageNo, pageSize, null);
            pageBean.setTotal(rs.getCount());
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("ID"));
                user.setUserCode(rs.getString("FUserCode"));
                user.setName(rs.getString("FName"));
                user.setSex(rs.getInt("FSex"));
                user.setBirthDay(rs.getDate("FBirthDay"));
                user.setPhoneNum(rs.getString("FPhoneNum"));
                user.setState(rs.getInt("FState"));
                user.setCreateTime(rs.getDate("FCreateTime"));
                user.setUpdateTime(rs.getDate("FUpdateTime"));
                users.add(user);
            }
        }  finally {
            closeResource(rs);
        }
        return pageBean;
    }

    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public User queryByUserId(Long userId) throws Exception {
        User user = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, FUserCode, FName, FSex, FBirthDay, FPhoneNum, FState,");
        sql.append("FCreateTime, FUpdateTime from CT_Rbac_User where FState != 2 and ID = ? ");

        user = SQLTool.one(User.class, sql.toString(), userId);
        return user;
    }

    /**
     * 根据角色id查询用户
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public List<Long> queryIdByRoleId(Long roleId) throws SQLException {
        List<Long> userIds = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID from CT_Rbac_User where ID in ");
            sql.append("(select FUserId from CT_Rbac_User_Role where FState != 2 and FRoleId = ?)");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, roleId);
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                userIds.add(rs.getLong("ID"));
            }
        } finally {
            closeResource(rs);
        }
        return userIds;
    }

    /**
     * 根据用户编号查找用户id
     *
     * @param userCode 用户编号
     * @return
     */
    @Override
    public Long queryIdByUserCode(String userCode) throws Exception {
        Long userId = null;
        String sql = "select ID from CT_Rbac_User where FState != 2 and FUserCode = ?";
        userId = SQLTool.one(Long.class, sql, userCode);

        return userId;
    }

    /**
     * 新增用户
     *
     * @param user 用户
     * @return
     */
    @Override
    public int add(User user) throws SQLException {
        int rows = 0;
        long nextId = getNextID(CommonConstants.TABLE_RBAC_USER);
        user.setId(nextId);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into CT_Rbac_User(ID, FUserCode, FName, FPassword, FSex, FBirthDay, FPhoneNum, FState,");
        sql.append("FCreateTime, FUpdateTime) values(?,?,?,?,?,?,?,?,?,?)");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, user.getId());
        dao.setString(2, user.getUserCode());
        dao.setString(3, user.getName());
        dao.setString(4, user.getPassword());
        dao.setObject(5, user.getSex());
        dao.setObject(6, user.getBirthDay());
        dao.setString(7, user.getPhoneNum());
        dao.setInt(8, user.getState());
        dao.setObject(9, user.getCreateTime());
        dao.setObject(10, user.getUpdateTime());

        rows = dao.executeUpdate(getDataSource());
        return rows;
    }

    /**
     * 修改用户信息
     *
     * @param user 用户
     * @return
     */
    @Override
    public int update(User user) throws SQLException {
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("update CT_Rbac_User set FName = ?, FSex = ?, FBirthDay = ?,");
        sql.append("FPhoneNum = ?, FState = ?, FUpdateTime = ? where ID = ?");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setString(1, user.getName());
        dao.setObject(2, user.getSex());
        dao.setObject(3, user.getBirthDay());
        dao.setString(4, user.getPhoneNum());
        dao.setInt(5, user.getState());
        dao.setObject(6, user.getUpdateTime());
        dao.setLong(7, user.getId());

        rows = dao.executeUpdate(getDataSource());

        return rows;
    }

    @Override
    public int updatePassword(User user) throws SQLException {
        int rows = 0;
        String sql = "update CT_Rbac_User set FPassword = ?, FUpdateTime = ? where ID = ?";

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql);
        dao.setString(1, user.getPassword());
        dao.setObject(2, user.getUpdateTime());
        dao.setLong(3, user.getId());

        rows = dao.executeUpdate(getDataSource());

        return rows;
    }

    /**
     * 删除用户, 即将用户的状态改成2, 删除
     *
     * @param userIds 用户id列表
     * @return
     */
    @Override
    public int delete(List<Long> userIds) throws SQLException {
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("update CT_Rbac_User set FState = 2 where ID in ");
        sql.append(SQLUtil.listToSQLList(userIds));

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        for (int i = 0; i < userIds.size(); i++) {
            dao.setLong(i + 1, userIds.get(i));
        }
        rows = dao.executeUpdate(getDataSource());

        return rows;
    }
}
