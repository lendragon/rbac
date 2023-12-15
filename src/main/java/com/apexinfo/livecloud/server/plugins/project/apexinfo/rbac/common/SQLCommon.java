package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common;

import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @ClassName: SQLCommon
 * @Description: SQL通用方法
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class SQLCommon {
    /**
     * 拼接分页查询
     * @param sql
     * @param pageNo
     * @param pageSize
     */
    public static void limitContact(StringBuffer sql, long pageNo, long pageSize) {
        sql.append(" limit ");
        sql.append(pageNo);
        sql.append(",");
        sql.append(pageSize);
    }

    public static void likeContact(StringBuffer sql, String keyword, String... params) {
        sql.append(" and (");
        for (int i = 0; i < params.length - 1; i++) {
            sql.append(params[i]);
            sql.append(" like '%");
            sql.append(keyword);
            sql.append("%' or ");
        }

        sql.append(params[params.length - 1]);
        sql.append(" like '%");
        sql.append(keyword);
        sql.append("%') ");
    }

    /**
     * 将List对象变成(x,y,z)的格式
     * @param list
     * @return
     */
    public static String listToSQLList(List<?> list) {
        StringJoiner sj = new StringJoiner(",", "(", ")");
        list.forEach((v) -> {
            sj.add(v.toString());
        });
        return sj.toString();
    }

    /**
     * 将一个类的所有成员变量的变量名提取出来变成list数组
     * @param c
     * @return
     */
    public static List<String> ClassFieldNameToList(Class<?> c) {
        List<String> fieldNames = null;

        Field[] fields = User.class.getFields();
        fieldNames = new ArrayList<>(fields.length);
        for (Field f : fields) {
            fieldNames.add(f.getName());
        }
        return fieldNames;
    }
}
