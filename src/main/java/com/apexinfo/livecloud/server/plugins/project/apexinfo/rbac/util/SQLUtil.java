package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util;

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
public class SQLUtil {
    /**
     * SQL常用关键字
     */
    private static final String SQL_KEYWORD_LIKE = " like ";
    private static final String SQL_KEYWORD_AND = " and ";
    private static final String SQL_KEYWORD_OR = " or ";
    /**
     * SQL常用符号
     */
    private static final String SQL_SYMBOL_QUESTION = " ? ";
    private static final String SQL_SYMBOL_PERCENT = "%";
    private static final String SQL_SYMBOL_COMMA = ",";
    private static final String SQL_SYMBOL_LEFT = " ( ";
    private static final String SQL_SYMBOL_RIGHT = " ) ";

    /**
     * @param sql    sql语句
     * @param params 参数列表
     * @description ?拼接like查询
     */
    public static void likeContact(StringBuilder sql, String... params) {
        // and (
        sql.append(SQL_KEYWORD_AND);
        sql.append(SQL_SYMBOL_LEFT);
        for (int i = 0; i < params.length - 1; i++) {
            sql.append(params[i]);
            // like ? or
            sql.append(SQL_KEYWORD_LIKE);
            sql.append(SQL_SYMBOL_QUESTION);
            sql.append(SQL_KEYWORD_OR);
        }
        sql.append(params[params.length - 1]);
        // like ? )
        sql.append(SQL_KEYWORD_LIKE);
        sql.append(SQL_SYMBOL_QUESTION);
        sql.append(SQL_SYMBOL_RIGHT);
    }

    /**
     * @param dao     dao
     * @param keyword 查询关键字
     * @param offset  起始位置
     * @param length  长度
     * @description 将keyword预编译到sql中
     */
    public static void setLikeSQL(ApexDao dao, String keyword, int offset, int length) {
        StringBuilder likeSQL = new StringBuilder();
        likeSQL.append(SQL_SYMBOL_PERCENT);
        likeSQL.append(keyword);
        likeSQL.append(SQL_SYMBOL_PERCENT);
        String keywordLike = likeSQL.toString();
        for (int i = offset; i < offset + length; i++) {
            dao.setString(i, keywordLike);
        }
    }

    /**
     * @param list 列表
     * @return String 结果
     * @description 将List对象变成(?, ?, ?)的格式
     */
    public static String listToSQLList(List<?> list) {
        StringJoiner sj = new StringJoiner(SQL_SYMBOL_COMMA, SQL_SYMBOL_LEFT, SQL_SYMBOL_RIGHT);
        list.forEach((v) -> {
            sj.add(SQL_SYMBOL_QUESTION);
        });
        return sj.toString();
    }
}
