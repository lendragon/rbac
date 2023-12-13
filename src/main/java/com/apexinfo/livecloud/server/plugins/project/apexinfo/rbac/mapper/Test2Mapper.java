package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper;

import com.apex.util.ApexDao;
import com.apex.util.ApexRowSet;
import com.apexinfo.livecloud.server.core.GeneralMapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DemoMapper
 * @Description:
 * @Author fanguangyu
 * @Date 2023/07/05
 * @Version 1.0
 */
public class Test2Mapper extends GeneralMapper {

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "select ID, FName from t_hr_employee";
            ApexDao dao = new ApexDao();
            dao.prepareStatement(sql);
            ApexRowSet rs = dao.getRowSet(getDataSource());
            while (rs.next()){
                User user = new User();
                user.setID((int)rs.getLong("ID"));
                user.setFName(rs.getString("FName"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
