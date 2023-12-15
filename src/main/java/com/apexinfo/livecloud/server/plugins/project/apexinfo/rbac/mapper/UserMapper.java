package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apex.form.FormObject;
import com.apex.util.ApexDao;
import com.apex.util.ApexDaoResult;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.common.SQLTool;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common.SQLCommon;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.UserConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.checkerframework.checker.units.qual.A;
import org.springframework.transaction.annotation.Transactional;

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
public class UserMapper extends GeneralMapper {

    /**
     * 分页查询用户
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    public List<User> query(long pageNo, long pageSize, String keyword) {
        List<User> users = new ArrayList<>();
        try {
            StringBuffer sql = new StringBuffer(
                    "select ID, FNo, FName, FPassword, FSex, FBirthDay," +
                            "       FPhoneNum, FCreateTime, FUpdateTime " +
                            "from CT_Rbac_User where 1 = 1 ");
            // 模糊查询拼接SQL
            if (keyword != null && !keyword.isEmpty()) {
                SQLCommon.likeContact(sql, keyword, "FNo","FName",
                        "FSex", "FBirthDay", "FPhoneNum");
            }
                // 分页查询多个用户
            ApexRowSet rs = ApexDao.getRowSet(getDataSource(), sql.toString(), (int) pageNo, (int) pageSize);

            while (rs != null && rs.next()) {
                User user = new User();
                user.setId(rs.getLong("ID"));
                user.setNo(rs.getString("FNo"));
                user.setName(rs.getString("FName"));
                user.setPassword(rs.getString("FPassword"));
                user.setSex(rs.getLong("FSex"));
                user.setBirthDay(rs.getDate("FBirthDay"));
                user.setPhoneNum(rs.getString("FPhoneNum"));
                user.setCreateTime(rs.getDate("FCreateTime"));
                user.setUpdateTime(rs.getDate("FUpdateTime"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    public List<User> queryById(long id) {
        List<User> users = new ArrayList<>();
        try {
            String sql = "select ID, FNo, FName, FPassword, FSex, FBirthDay," +
                            "       FPhoneNum, FCreateTime, FUpdateTime " +
                            "from CT_Rbac_User where ID = ? ";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, id);
            ApexRowSet rs = dao.getRowSet(getDataSource());

            while (rs != null && rs.next()) {
                User user = new User();
                user.setId(rs.getLong("ID"));
                user.setNo(rs.getString("FNo"));
                user.setName(rs.getString("FName"));
                user.setPassword(rs.getString("FPassword"));
                user.setSex(rs.getLong("FSex"));
                user.setBirthDay(rs.getDate("FBirthDay"));
                user.setPhoneNum(rs.getString("FPhoneNum"));
                user.setCreateTime(rs.getDate("FCreateTime"));
                user.setUpdateTime(rs.getDate("FUpdateTime"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    public int add(User user) {
        int rows = 0;
        try {
            long nextId = getNextID(UserConstants.STUDIO_RBAC_USER);
            user.setId(nextId);

            String sql = "insert into CT_Rbac_User(ID, FNo, FName, FPassword, FSex," +
                    "FBirthDay, FPhoneNum, FCreateTime, FUpdateTime) values(?,?,?,?,?,?,?,?,?)";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, user.getId());
            dao.setString(2, user.getNo());
            dao.setString(3, user.getName());
            dao.setString(4, user.getPassword());
            dao.setObject(5, user.getSex());
            dao.setObject(6, user.getBirthDay());
            dao.setString(7, user.getPhoneNum());
            dao.setObject(8, user.getCreateTime());
            dao.setObject(9, user.getUpdateTime());

            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    public int update(User user) {
        int rows = 0;
        try {
            String sql = "update CT_Rbac_User set FNo = ?, FName = ?, FPassword = ?," +
                    "FSex = ?, FBirthDay = ?, FPhoneNum = ?, FUpdateTime = ? " +
                    " where ID = ?";

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setString(1, user.getNo());
            dao.setString(2, user.getName());
            dao.setString(3, user.getPassword());
            dao.setObject(4, user.getSex());
            dao.setObject(5, user.getBirthDay());
            dao.setString(6, user.getPhoneNum());
            dao.setObject(7, user.getUpdateTime());
            dao.setLong(8, user.getId());

            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    // TODO 删除事务, 不需要的话可能要删掉
    @Transactional
    public int delete(List<Long> id) {
        int rows = 0;
        try {
            String sql = "delete from CT_Rbac_User where ID in " +
                    SQLCommon.listToSQLList(id);

            rows = ApexDao.executeUpdate(getDataSource(), sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
}
