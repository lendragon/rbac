package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.impl;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.common.SQLTool;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
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
     * @return List<Menu> 菜单列表
     * @description 查询所有菜单
     */
    @Override
    public List<Menu> queryAll() throws SQLException {
        List<Menu> menus = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FOrder, FLevel, FParentId, FUrl, FState, FDescription ");
            sql.append("from CT_Rbac_Menu where 1 = 1 ");

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            rs = dao.getRowSet(getDataSource());

            Menu menu = null;
            while (rs.next()) {
                menu = new Menu();
                menu.setId(rs.getLong("ID"));
                menu.setName(rs.getString("FName"));
                menu.setOrder(rs.getInt("FOrder"));
                menu.setLevel(rs.getInt("FLevel"));
                menu.setParentId(rs.getLong("FParentId"));
                menu.setUrl(rs.getString("FUrl"));
                menu.setState(rs.getInt("FState"));
                menu.setDescription(rs.getString("FDescription"));
                menus.add(menu);
            }
        } finally {
            closeResource(rs);
        }
        return menus;
    }

    /**
     * @param id 菜单主键
     * @return Menu 菜单
     * @description 根据菜单主键查询菜单
     */
    @Override
    public Menu queryByMenuId(Long id) throws Exception {
        Menu menu = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, FName, FOrder, FLevel, FParentId, FUrl, ");
        sql.append("FState, FDescription, FCreateTime, FUpdateTime ");
        sql.append("from CT_Rbac_Menu where ID = ?");
        menu = SQLTool.one(Menu.class, sql.toString(), id);
        return menu;
    }

    /**
     * @param id 角色主键
     * @return List<Menu> 菜单列表
     * @description 根据角色主键查询菜单
     */
    @Override
    public List<Menu> queryByRoleId(Long id) throws SQLException {
        List<Menu> menus = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ID, FName, FOrder, FParentId, FUrl, ");
            sql.append("FState, FDescription from CT_Rbac_Menu ");
            sql.append("where ID in (select FMenuId from CT_Rbac_Role_Menu where FRoleId = ?)");

            // 准备sql, 开始查询
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, id);
            rs = dao.getRowSet(getDataSource());

            Menu menu = null;
            while (rs.next()) {
                menu = new Menu();
                menu.setId(rs.getLong("ID"));
                menu.setName(rs.getString("FName"));
                menu.setOrder(rs.getInt("FOrder"));
                menu.setParentId(rs.getLong("FParentId"));
                menu.setUrl(rs.getString("FUrl"));
                menu.setState(rs.getInt("FState"));
                menu.setDescription(rs.getString("FDescription"));
                menus.add(menu);
            }
        } finally {
            closeResource(rs);
        }
        return menus;
    }

    /**
     * @param id 用户主键
     * @return List<Menu> 菜单列表
     * @description 根据用户主键查询菜单
     */
    @Override
    public List<Menu> queryByUserId(Long id) throws SQLException {
        List<Menu> menus = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct M.ID, M.FName, M.FOrder, M.FParentId, M.FUrl, ");
            sql.append("M.FState, M.FDescription ");
            sql.append("from CT_Rbac_Menu as M left join CT_Rbac_Role_Menu as RM ");
            sql.append("on M.ID = RM.FMenuId ");
            sql.append("left join CT_Rbac_User_Role as UR on RM.FRoleId = UR.FRoleId ");
            sql.append("where UR.FUserId = ?");

            // 准备sql, 开始查询
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql.toString());
            dao.setLong(1, id);
            rs = dao.getRowSet(getDataSource());

            Menu menu = null;
            while (rs.next()) {
                menu = new Menu();
                menu.setId(rs.getLong("ID"));
                menu.setName(rs.getString("FName"));
                menu.setOrder(rs.getInt("FOrder"));
                menu.setParentId(rs.getLong("FParentId"));
                menu.setUrl(rs.getString("FUrl"));
                menu.setState(rs.getInt("FState"));
                menu.setDescription(rs.getString("FDescription"));
                menus.add(menu);
            }
        } finally {
            closeResource(rs);
        }
        return menus;
    }

    /**
     * @param id 菜单主键列表
     * @return List<Long> 子菜单主键列表
     * @description 根据菜单主键列表查询子菜单主键列表
     */
    @Override
    public List<Long> queryChildrenIdsByMenuId(Long id) throws SQLException {
        List<Long> menuChildrenIds = new ArrayList<>();
        ApexRowSet rs = null;
        try {
            String sql = "select ID from CT_Rbac_Menu where FParentId = ?";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, id);

            rs = dao.getRowSet(getDataSource());
            while (rs.next()) {
                menuChildrenIds.add(rs.getLong("ID"));
            }
        } finally {
            closeResource(rs);
        }
        return menuChildrenIds;
    }

    /**
     * @param menu 菜单
     * @return int 影响的行数
     * @description 新增菜单
     */
    @Override
    public int add(Menu menu) throws SQLException {
        int rows = 0;
        long nextId = getNextID(CommonConstants.TABLE_RBAC_MENU);
        menu.setId(nextId);

        StringBuilder sql = new StringBuilder();
        sql.append("insert into CT_Rbac_Menu(ID, FName, FOrder, FLevel, FParentId, FUrl,");
        sql.append("FState, FDescription, FCreateTime, FUpdateTime) values(?,?,?,?,?,?,?,?,?,?)");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setLong(1, menu.getId());
        dao.setString(2, menu.getName());
        dao.setLong(3, menu.getOrder());
        dao.setLong(4, menu.getLevel());
        dao.setObject(5, menu.getParentId());
        dao.setString(6, menu.getUrl());
        dao.setInt(7, menu.getState());
        dao.setString(8, menu.getDescription());
        dao.setObject(9, menu.getCreateTime());
        dao.setObject(10, menu.getUpdateTime());

        rows = dao.executeUpdate(getDataSource());
        return rows;
    }

    /**
     * @param menu 菜单
     * @return int 影响的行数
     * @description 修改菜单信息
     */
    @Override
    public int update(Menu menu) throws SQLException {
        int rows = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("update CT_Rbac_Menu set FName = ?, FOrder = ?, FLevel = ?, FParentId = ?,");
        sql.append("FUrl = ?,FState = ?, FDescription = ?, FUpdateTime = ? where ID = ?");

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql.toString());
        dao.setString(1, menu.getName());
        dao.setLong(2, menu.getOrder());
        dao.setLong(3, menu.getLevel());
        dao.setObject(4, menu.getParentId());
        dao.setString(5, menu.getUrl());
        dao.setInt(6, menu.getState());
        dao.setString(7, menu.getDescription());
        dao.setObject(8, menu.getUpdateTime());
        dao.setLong(9, menu.getId());

        rows = dao.executeUpdate(getDataSource());
        return rows;
    }

    /**
     * @param id 菜单主键
     * @return int 影响的行数
     * @description 删除菜单, 即将菜单的状态改为2, 删除
     */
    @Override
    public int delete(Long id) throws SQLException {
        int rows = 0;
        String sql = "update CT_Rbac_Menu set FState = 2 where ID = ?";

        ApexDao dao = new ApexDao();
        dao.prepareStatement(sql);
        dao.setLong(1, id);

        rows = dao.executeUpdate(getDataSource());
        return rows;
    }
}
