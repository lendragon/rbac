package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant;

import com.apexinfo.livecloud.server.plugins.product.liveid.client.util.AESUtil;
import com.sun.crypto.provider.AESKeyGenerator;

/**
 * @ClassName: UserConstants
 * @Description: 用户常量
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class UserConstants {
//    public static final String PARAM_ID = "id";
//    public static final String PARAM_PAGE_NO = "pageNo";
//    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String AES_KEY = "0123456789abcdef";
    /**
     * 用户列表路由
     */
    public static final String ROUTE_USER = "/livecloud/project/user.pagex";
    public static final String ROUTE_USER_ROLE = "/livecloud/project/userRole.pagex";

    /**
     * 用户列表插件标识
     */
//    public static final String PLUGIN_USER = "livecloud.server.plugins.project.rbac.user";

    public static final String STUDIO_RBAC_USER = "CT_Rbac_User";
}
