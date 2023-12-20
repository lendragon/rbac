package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.SQLUtil;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IUserMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
     * 分页查询用户
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @Override
    public PageDTO<User> query(Integer pageNo, Integer pageSize, String keyword) {
        PageDTO<User> pageDTO = new PageDTO<>();
        List<User> users = new ArrayList<>();
        pageDTO.setRecords(users);
        pageDTO.setPageNo(pageNo);
        pageDTO.setPageSize(pageSize);
        ApexDao dao = null;
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FNo, FName, FPassword, FSex, FBirthDay,");
            sql.append(" FPhoneNum, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_User where 1 = 1 ");
            // 模糊查询拼接SQL
            if (keyword != null && !keyword.isEmpty()) {
                SQLUtil.likeContact(sql, "FNo", "FName", "FSex", "FBirthDay", "FPhoneNum");
            }
            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            if (keyword != null && !keyword.isEmpty()) {
                SQLUtil.setLikeSQL(dao, keyword, 1, 5);
            }
            rs = dao.getRowSet(getDataSource(), pageNo, pageSize, null);
            pageDTO.setTotal(rs.getCount());
            while (rs.next()) {
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
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao, rs);
        }
        return pageDTO;
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public PageDTO<User> queryById(Long id) {
        PageDTO<User> pageDTO = new PageDTO<>();
        List<User> users = new ArrayList<>();
        pageDTO.setRecords(users);
        ApexDao dao = null;
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FNo, FName, FPassword, FSex, FBirthDay,");
            sql.append(" FPhoneNum, FCreateTime, FUpdateTime ");
            sql.append("from CT_Rbac_User where ID = ? ");

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, id);
            rs = dao.getRowSet(getDataSource());
            pageDTO.setTotal(rs.getCount());
            while (rs.next()) {
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
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao, rs);
        }
        return pageDTO;
    }

    /**
     * 根据用户编号或用户名查找用户
     *
     * @param no
     * @param name
     * @return
     */
    @Override
    public List<User> queryByNoOrName(String no, String name) {
        List<User> users = new ArrayList<>();
        ApexDao dao = null;
        ApexRowSet rs = null;
        try {
            String sql = "select ID, FNo, FName from CT_Rbac_User where FNo = ? or FName = ?";

            dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setString(1, no);
            dao.setString(2, name);
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("ID"));
                user.setNo(rs.getString("FNo"));
                user.setName(rs.getString("FName"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao, rs);
        }
        return users;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Override
    public int add(User user) {
        int rows = 0;
        ApexDao dao = null;
        try {
            long nextId = getNextID(CommonConstants.TABLE_RBAC_USER);
            user.setId(nextId);
            StringBuilder sql = new StringBuilder();
            sql.append("insert into CT_Rbac_User(ID, FNo, FName, FSex, FBirthDay, FPhoneNum, FCreateTime, FUpdateTime ");
            if (user.getPassword() == null) {
                sql.append(") values(?,?,?,?,?,?,?,?)");
            } else {
                sql.append(", FPassword) values(?,?,?,?,?,?,?,?,?)");
            }
            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, user.getId());
            dao.setString(2, user.getNo());
            dao.setString(3, user.getName());
            dao.setObject(4, user.getSex());
            dao.setObject(5, user.getBirthDay());
            dao.setString(6, user.getPhoneNum());
            dao.setObject(7, user.getCreateTime());
            dao.setObject(8, user.getUpdateTime());
            if (user.getPassword() != null) {
                dao.setObject(9, user.getPassword());
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @Override
    public int update(User user) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("update CT_Rbac_User set FNo = ?, FName = ?, FSex = ?, FBirthDay = ?, FPhoneNum = ?, FUpdateTime = ? ");
            if (user.getPassword() != null) {
                sql.append(", FPassword = ? ");
            }
            sql.append(" where ID = ?");

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setString(1, user.getNo());
            dao.setString(2, user.getName());
            dao.setObject(3, user.getSex());
            dao.setObject(4, user.getBirthDay());
            dao.setString(5, user.getPhoneNum());
            dao.setObject(6, user.getUpdateTime());
            if (user.getPassword() == null) {
                dao.setLong(7, user.getId());
            } else {
                dao.setObject(7, user.getBirthDay());
                dao.setLong(8, user.getId());
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao);
        }
        return rows;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    // TODO 事务待修改
    @Override
    public int delete(List<Long> id) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_User where ID in ");
            sql.append(SQLUtil.listToSQLList(id));
            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            for (int i = 0; i < id.size(); i++) {
                dao.setLong(i + 1, id.get(i));
            }
            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            closeResource(dao);
        }
        return rows;
    }
}
