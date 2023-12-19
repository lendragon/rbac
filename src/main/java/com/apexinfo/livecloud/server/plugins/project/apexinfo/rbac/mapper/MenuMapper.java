package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common.SQLCommon;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.MenuConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import org.checkerframework.checker.units.qual.A;
import org.springframework.transaction.annotation.Transactional;

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
public class MenuMapper extends GeneralMapper {

    /**
     * 查询所有菜单
     * @return
     */
    public List<Menu> query() {
        List<Menu> menus = new ArrayList<>();
        try {
            String sql = "select ID, FName, FOrder, FLevel, FParentId, FUrl, FCreateTime," +
                    " FUpdateTime, FDescription from CT_Rbac_Menu ";

            ApexRowSet rs = ApexDao.getRowSet(getDataSource(), sql);
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
        }
        return menus;
    }

    /**
     * 根据菜单id查询菜单树
     * @return
     */
    public List<Menu> queryById(long menuId) {
        List<Menu> menus = new ArrayList<>();
        try {
            String sql = "select ID, FName, FOrder, FLevel, FParentId, FUrl, FCreateTime," +
                    " FUpdateTime, FDescription from CT_Rbac_Menu where ID = ?";

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, menuId);
            ApexRowSet rs = dao.getRowSet(getDataSource());
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
        }
        return menus;
    }

    /**
     * 根据角色id查询菜单
     * @param roleId
     * @return
     */
    public List<Menu> queryByRoleId(long roleId) {
        List<Menu> menus = new ArrayList<>();
        try {
            String sql = "select ID, FName, FOrder, FLevel, FParentId, FUrl," +
                    " FCreateTime, FUpdateTime, FDescription from CT_Rbac_Menu " +
                    "where ID in (select FMenuId from CT_Rbac_Role_Menu where " +
                    "FRoleId = ?)";

            // 准备sql, 开始查询
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            dao.setLong(1, roleId);
            ApexRowSet rs = dao.getRowSet(getDataSource());
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
        }
        return menus;
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    public int add(Menu menu) {
        int rows = 0;
        try {
            long nextId = getNextID(MenuConstants.STUDIO_RBAC_MENU);
            menu.setId(nextId);

            String sql = "insert into CT_Rbac_Menu(ID, FName, FOrder, FLevel," +
                    "FParentId, FUrl, FCreateTime, FUpdateTime, FDescription) values(?,?,?,?,?,?,?,?,?)";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
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
        }

        return rows;
    }

    /**
     * 修改菜单信息
     * @param menu
     * @return
     */
    public int update(Menu menu) {
        int rows = 0;
        try {
            String sql = "update CT_Rbac_Menu set FName = ?, FOrder = ?," +
                    "FLevel = ?, FParentId = ?, FUrl = ?, FUpdateTime = ?, " +
                    "FDescription = ? where ID = ?";

            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);

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
        }
        return rows;
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    // TODO 删除事务, 不需要的话可能要删掉
    @Transactional
    public int delete(List<Long> id) {
        int rows = 0;
        try {
            String sql = "delete from CT_Rbac_Menu where ID in " +
                    SQLCommon.listToSQLList(id);

            rows = ApexDao.executeUpdate(getDataSource(), sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
}
