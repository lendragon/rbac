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
     * @param pageBean 分页查询参数, pageNo, pageSize, keyword
     * @return PageBean<User> 用户分页结果
     * @description 分页模糊查询所有用户
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
            sql.append("select ID, FUserName, FName, FSex, FBirthDay,");
            sql.append("FPhoneNum, FState, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_User where 1 = 1 ");
            // 模糊查询拼接SQL
            if (!Util.isEmpty(keyword)) {
                SQLUtil.likeContact(sql, "FUserName", "FName", "FBirthDay", "FPhoneNum");
            }
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            if (!Util.isEmpty(keyword)) {
                SQLUtil.setLikeSQL(dao, keyword, 1, 4);
            }
            rs = dao.getRowSet(getDataSource(), pageNo, pageSize, null);
            pageBean.setTotal(rs.getCount());

            User user = null;
            while (rs.next()) {
                user = new User();
                user.setId(rs.getLong("ID"));
                user.setUserName(rs.getString("FUserName"));
                user.setName(rs.getString("FName"));
                user.setSex(rs.getInt("FSex"));
                user.setBirthDay(rs.getDate("FBirthDay"));
                user.setPhoneNum(rs.getString("FPhoneNum"));
                user.setState(rs.getInt("FState"));
                user.setCreateTime(rs.getDate("FCreateTime"));
                user.setUpdateTime(rs.getDate("FUpdateTime"));
                users.add(user);
            }
        } finally {
            closeResource(rs);
        }
        return pageBean;
    }

    /**
     * @param id 用户主键
     * @return User 用户
     * @description 根据用户主键查询用户
     */
    @Override
    public User queryByUserId(Long id) throws Exception {
        User user = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, FUserName, FName, FSex, FBirthDay, FPhoneNum, FState,");
        sql.append("FCreateTime, FUpdateTime from CT_Rbac_User where ID = ? ");

        user = SQLTool.one(User.class, sql.toString(), id);
        return user;
    }

    /**
     * @param id   用户主键
     * @param password 密码
     * @return boolean 密码是否正确
     * @description 根据用户主键和密码查询密码是否正确
     */
    @Override
    public boolean queryByUserIdAndPassword(Long id, String password) throws Exception {
        boolean isCorrect = false;
        String sql = "select ID from CT_Rbac_User where ID = ? and FPassword = ?";
        isCorrect = (SQLTool.one(User.class, sql, id, password) != null);

        return isCorrect;
    }

    /**
     * @param id 角色主键
     * @return List<User> 用户列表
     * @description 根据角色主键查询用户
     */
    @Override
    public List<User> queryByRoleId(Long id) throws SQLException {
        List<User> users = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FUserName, FName, FSex, FBirthDay, FPhoneNum, FState, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_User where ID in (select FUserId from CT_Rbac_User_Role where FRoleId = ?)");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, id);
            rs = dao.getRowSet(getDataSource());

            User user = null;
            while (rs.next()) {
                user = new User();
                user.setId(rs.getLong("ID"));
                user.setUserName(rs.getString("FUserName"));
                user.setName(rs.getString("FName"));
                user.setSex(rs.getInt("FSex"));
                user.setBirthDay(rs.getDate("FBirthDay"));
                user.setPhoneNum(rs.getString("FPhoneNum"));
                user.setState(rs.getInt("FState"));
                user.setCreateTime(rs.getDate("FCreateTime"));
                user.setUpdateTime(rs.getDate("FUpdateTime"));
                users.add(user);
            }
        } finally {
            closeResource(rs);
        }
        return users;
    }

    /**
     * @param user 用户
     * @return int 影响的行数
     * @description 新增用户
     */
    @Override
    public int add(User user) throws SQLException {
        int rows = 0;
        long nextId = getNextID(CommonConstants.TABLE_RBAC_USER);
        user.setId(nextId);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into CT_Rbac_User(ID, FUserName, FName, FPassword, FSex, FBirthDay, FPhoneNum, FState,");
        sql.append("FCreateTime, FUpdateTime) values(?,?,?,?,?,?,?,?,?,?)");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, user.getId());
        dao.setString(2, user.getUserName());
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
     * @param user 用户
     * @return int 影响的行数
     * @description 修改用户信息
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

    /**
     * @param user 用户
     * @return int 影响的行数
     * @description 修改用户密码
     */
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
     * @param id 用户主键
     * @return int 影响的行数
     * @description 删除用户, 即将用户的状态改成2, 删除
     */
    @Override
    public int delete(Long id) throws SQLException {
        int rows = 0;
        String sql = "update CT_Rbac_User set FState = 2 where ID = ? ";

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql);
        dao.setLong(1, id);
        rows = dao.executeUpdate(getDataSource());

        return rows;
    }
}
