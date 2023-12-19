package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common.SQLCommon;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.RoleConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RoleMapper
 * @Description: 操作角色表的类
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class RoleMapper extends GeneralMapper {
    /**
     * 分页模糊查询角色
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    public List<Role> query(Long pageNo, Long pageSize, String keyword) {
        List<Role> roles = new ArrayList<>();
        try {
            StringBuffer sql = new StringBuffer("select ID, FName, FCreateTime, FUpdateTime, " +
                    "FDescription from CT_Rbac_Role where 1 = 1 ");
            // 拼接模糊查询SQL
            if (keyword != null && !keyword.isEmpty()) {
                SQLCommon.likeContact(sql, keyword, "FName", "FDescription");
            }
            // 分页查询
            ApexRowSet rs = ApexDao.getRowSet(getDataSource(), sql.toString(),
                    Math.toIntExact(pageNo), Math.toIntExact(pageSize));
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setName(rs.getString("FName"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                role.setDescription(rs.getString("FDescription"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    public List<Role> queryByUserId(long userId) {
        List<Role> roles = new ArrayList<>();
        try {
            String sql = "select ID, FName, FCreateTime, FUpdateTime, FDescription " +
                    "from CT_Rbac_Role where ID in " +
                    "(select FRoleId from CT_Rbac_User_Role where FUserId = ?)";

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, userId);
            ApexRowSet rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setName(rs.getString("FName"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                role.setDescription(rs.getString("FDescription"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 根据角色id查询角色
     *
     * @return
     */
    public List<Role> queryById(Long id) {
        List<Role> roles = new ArrayList<>();
        try {
            String sql = "select ID, FName, FDescription, FCreateTime, FUpdateTime " +
                    "from CT_Rbac_Role where ID = ? ";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, id);
            ApexRowSet rs = dao.getRowSet(getDataSource());

            while (rs != null && rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("ID"));
                role.setName(rs.getString("FName"));
                role.setDescription(rs.getString("FDescription"));
                role.setCreateTime(rs.getDate("FCreateTime"));
                role.setUpdateTime(rs.getDate("FUpdateTime"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    public int add(Role role) {
        int rows = 0;
        try {
            long nextId = getNextID(RoleConstants.STUDIO_RBAC_ROLE);
            role.setId(nextId);

            String sql = "insert into CT_Rbac_Role(ID, FName, FCreateTime, FUpdateTime, FDescription)" +
                    " values(?, ?, ?, ?, ?)";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, role.getId());
            dao.setString(2, role.getName());
            dao.setObject(3, role.getCreateTime());
            dao.setObject(4, role.getUpdateTime());
            dao.setString(5, role.getDescription());

            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    /**
     * 修改角色信息
     *
     * @param role
     * @return
     */
    public int update(Role role) {
        int rows = 0;
        try {
            String sql = "update CT_Rbac_Role set FName = ?, FUpdateTime = ?, " +
                    "FDescription = ?  where ID = ? ";

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setString(1, role.getName());
            dao.setObject(2, role.getUpdateTime());
            dao.setString(3, role.getDescription());
            dao.setLong(4, role.getId());

            rows = dao.executeUpdate(getDataSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    // TODO 删除事务, 不需要的话可能要删掉
    @Transactional
    public int delete(List<Long> id) {
        int rows = 0;
        try {
            String sql = "delete from CT_Rbac_Role where ID in " +
                    SQLCommon.listToSQLList(id);

            rows = ApexDao.executeUpdate(getDataSource(), sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    /**
     * 获取数据库中数据的数量
     *
     * @return
     */
    public int count(String keyword) {
        int count = 0;
        try {
            StringBuffer sql = new StringBuffer("select count(*) from CT_Rbac_Role where 1 = 1 ");
            if (keyword != null && !keyword.isEmpty()) {
                SQLCommon.likeContact(sql, keyword, "FName", "FDescription");
            }
            ApexRowSet rs = ApexDao.getRowSet(getDataSource(), sql.toString());
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


}
