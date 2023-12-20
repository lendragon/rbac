package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.SQLUtil;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.IMenuMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MenuMapper
 * @Description: 操作菜单表的类
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class MenuMapperImpl extends GeneralMapper implements IMenuMapper {

    /**
     * 查询所有菜单
     *
     * @return
     */
    @Override
    public List<Menu> query() {
        List<Menu> menus = new ArrayList<>();
        ApexDao dao = null;
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FOrder, FLevel, FParentId, FUrl, FCreateTime,");
            sql.append(" FUpdateTime, FDescription from CT_Rbac_Menu ");

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(rs.getLong("ID"));
                menu.setName(rs.getString("FName"));
                menu.setOrder(rs.getLong("FOrder"));
                menu.setLevel(rs.getLong("FLevel"));
                menu.setParentId(rs.getLong("FParentId"));
                menu.setUrl(rs.getString("FUrl"));
                menu.setCreateTime(rs.getDate("FCreateTime"));
                menu.setUpdateTime(rs.getDate("FUpdateTime"));
                menu.setDescription(rs.getString("FDescription"));
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao, rs);
        }
        return menus;
    }

    /**
     * 根据菜单id查询菜单树
     *
     * @param menuId
     * @return
     */
    @Override
    public List<Menu> queryById(Long menuId) {
        List<Menu> menus = new ArrayList<>();
        ApexDao dao = null;
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FOrder, FLevel, FParentId, FUrl, ");
            sql.append("FCreateTime, FUpdateTime, FDescription ");
            sql.append("from CT_Rbac_Menu where ID = ?");

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, menuId);
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(rs.getLong("ID"));
                menu.setName(rs.getString("FName"));
                menu.setOrder(rs.getLong("FOrder"));
                menu.setLevel(rs.getLong("FLevel"));
                menu.setParentId(rs.getLong("FParentId"));
                menu.setUrl(rs.getString("FUrl"));
                menu.setCreateTime(rs.getDate("FCreateTime"));
                menu.setUpdateTime(rs.getDate("FUpdateTime"));
                menu.setDescription(rs.getString("FDescription"));
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao, rs);
        }
        return menus;
    }

    /**
     * 根据角色id查询菜单
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Menu> queryByRoleId(Long roleId) {
        List<Menu> menus = new ArrayList<>();
        ApexDao dao = null;
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FOrder, FLevel, FParentId, FUrl, ");
            sql.append("FCreateTime, FUpdateTime, FDescription ");
            sql.append("from CT_Rbac_Menu ");
            sql.append("where ID in (select FMenuId from CT_Rbac_Role_Menu where FRoleId = ?)");

            // 准备sql, 开始查询
            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, roleId);
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(rs.getLong("ID"));
                menu.setName(rs.getString("FName"));
                menu.setOrder(rs.getLong("FOrder"));
                menu.setLevel(rs.getLong("FLevel"));
                menu.setParentId(rs.getLong("FParentId"));
                menu.setUrl(rs.getString("FUrl"));
                menu.setCreateTime(rs.getDate("FCreateTime"));
                menu.setUpdateTime(rs.getDate("FUpdateTime"));
                menu.setDescription(rs.getString("FDescription"));
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao, rs);
        }
        return menus;
    }

    /**
     * 根据用户id查询菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Menu> queryByUserId(Long userId) {
        List<Menu> menus = new ArrayList<>();
        ApexDao dao = null;
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select M.ID, M.FName, M.FOrder, M.FLevel, M.FParentId, M.FUrl, ");
            sql.append("M.FCreateTime, M.FUpdateTime, M.FDescription ");
            sql.append("from CT_Rbac_Menu as M ");
            sql.append("join CT_Rbac_Role_Menu as RM on M.ID = RM.FMenuId ");
            sql.append("join CT_Rbac_User_Role as UR on RM.FRoleId = UR.FRoleId ");
            sql.append("where UR.FUserId = ?");

            // 准备sql, 开始查询
            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, userId);
            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(rs.getLong("ID"));
                menu.setName(rs.getString("FName"));
                menu.setOrder(rs.getLong("FOrder"));
                menu.setLevel(rs.getLong("FLevel"));
                menu.setParentId(rs.getLong("FParentId"));
                menu.setUrl(rs.getString("FUrl"));
                menu.setCreateTime(rs.getDate("FCreateTime"));
                menu.setUpdateTime(rs.getDate("FUpdateTime"));
                menu.setDescription(rs.getString("FDescription"));
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            closeResource(dao, rs);
        }
        return menus;
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @Override
    public int add(Menu menu) {
        int rows = 0;
        ApexDao dao = null;
        try {
            long nextId = getNextID(CommonConstants.TABLE_RBAC_MENU);
            menu.setId(nextId);

            StringBuilder sql = new StringBuilder();
            sql.append("insert into CT_Rbac_Menu(ID, FName, FOrder, FLevel,");
            sql.append("FParentId, FUrl, FCreateTime, FUpdateTime, FDescription) values(?,?,?,?,?,?,?,?,?)");

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, menu.getId());
            dao.setString(2, menu.getName());
            dao.setLong(3, menu.getOrder());
            dao.setLong(4, menu.getLevel());
            dao.setObject(5, menu.getParentId());
            dao.setString(6, menu.getUrl());
            dao.setObject(7, menu.getCreateTime());
            dao.setObject(8, menu.getUpdateTime());
            dao.setString(9, menu.getDescription());

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
     * 修改菜单信息
     *
     * @param menu
     * @return
     */
    @Override
    public int update(Menu menu) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("update CT_Rbac_Menu set FName = ?, FOrder = ?, ");
            sql.append("FLevel = ?, FParentId = ?, FUrl = ?, FUpdateTime = ?, ");
            sql.append("FDescription = ? where ID = ?");

            dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setString(1, menu.getName());
            dao.setLong(2, menu.getOrder());
            dao.setLong(3, menu.getLevel());
            dao.setObject(4, menu.getParentId());
            dao.setString(5, menu.getUrl());
            dao.setObject(6, menu.getUpdateTime());
            dao.setString(7, menu.getDescription());
            dao.setLong(8, menu.getId());

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
     * 删除菜单
     *
     * @param id
     * @return
     */
    @Override
    public int delete(List<Long> id) {
        int rows = 0;
        ApexDao dao = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from CT_Rbac_Menu where ID in ");
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
        } finally {
            closeResource(dao);
        }
        return rows;
    }
}
