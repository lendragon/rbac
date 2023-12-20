package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.common;

import com.apex.util.ApexDao;

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
     *
     * @param sql
     * @param pageNo
     * @param pageSize
     */
    public static void limitContact(StringBuilder sql, long pageNo, long pageSize) {
        sql.append(" limit ");
        sql.append(pageNo - 1);
        sql.append(",");
        sql.append(pageSize);
    }

    /**
     * ?拼接like查询
     * @param sql
     * @param params
     */
    public static void likeContact(StringBuilder sql, String... params) {
        sql.append(" and (");
        for (int i = 0; i < params.length - 1; i++) {
            sql.append(params[i]);
            sql.append(" like ? or ");
        }

        sql.append(params[params.length - 1]);
        sql.append(" like ? )");
    }

    /**
     * 将keyword预编译到sql中
     * @param dao
     * @param keyword
     * @param offset
     * @param length
     */
    public static void setLikeSQL(ApexDao dao, String keyword, int offset, int length) {
        StringBuilder likeSQL = new StringBuilder();
        likeSQL.append("%");
        likeSQL.append(keyword);
        likeSQL.append("%");
        String keywordLike = likeSQL.toString();
        for (int i = offset; i < offset + length; i++) {
            dao.setString(i, keywordLike);
        }
    }

    /**
     * 将List对象变成(?,?,?)的格式
     *
     * @param list
     * @return
     */
    public static String listToSQLList(List<?> list) {
        StringJoiner sj = new StringJoiner(",", "(", ")");
        list.forEach((v) -> {
            sj.add("?");
        });
        return sj.toString();
    }
}
