package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant;

/**
 * @ClassName: CommonConstants
 * @Description: 通用常量
 * @Author linlongyue
 * @Date 2023/12/19
 * @Version 1.0
 */
public class CommonConstants {
    /**
     * 请求接口的动作参数
     */
    public static final String PARAM_ACTION_QUERY_ALL = "action=queryAll";
    public static final String PARAM_ACTION_QUERY_BY_USER_ID = "action=queryByUserId";
    public static final String PARAM_ACTION_QUERY_BY_ROLE_ID = "action=queryByRoleId";
    public static final String PARAM_ACTION_QUERY_BY_MENU_ID = "action=queryByMenuId";
    public static final String PARAM_ACTION_ADD = "action=add";
    public static final String PARAM_ACTION_UPDATE = "action=update";
    public static final String PARAM_ACTION_UPDATE_PASSWORD = "action=updatePassword";
    public static final String PARAM_ACTION_DELETE = "action=delete";

    /**
     * 接口参数名
     */
    public static final String PARAM_COMMON_USER_ID = "userId";
    public static final String PARAM_COMMON_ROLE_ID = "roleId";
    public static final String PARAM_COMMON_MENU_ID = "menuId";

    /**
     * 响应信息的国际化key
     */
    public static final String I18N_USER_ERROR_REPEAT_CODE = "apexinfo.rbac.user.response.error.repeat.code";
    public static final String I18N_ROLE_ERROR_REPEAT_CODE = "apexinfo.rbac.role.response.error.repeat.code";
    public static final String I18N_ROLE_ERROR_ROLE_REQUIRED = "apexinfo.rbac.role.response.error.required.role";
    public static final String I18N_MENU_ERROR_MENU_REQUIRED = "apexinfo.rbac.menu.response.error.required.menu";
    public static final String I18N_COMMON_SUCCESS_ADD = "apexinfo.rbac.common.response.success.add";
    public static final String I18N_COMMON_SUCCESS_UPDATE = "apexinfo.rbac.common.response.success.update";
    public static final String I18N_COMMON_SUCCESS_DELETE = "apexinfo.rbac.common.response.success.delete";
    public static final String I18N_COMMON_FAIL_ADD = "apexinfo.rbac.common.response.fail.add";
    public static final String I18N_COMMON_FAIL_UPDATE = "apexinfo.rbac.common.response.fail.update";
    public static final String I18N_COMMON_FAIL_DELETE = "apexinfo.rbac.common.response.fail.delete";

    /**
     * 路由URI
     */
    public static final String ROUTE_URI_USER = "/livecloud/project/user.pagex";
    public static final String ROUTE_URI_ROLE = "/livecloud/project/role.pagex";
    public static final String ROUTE_URI_MENU = "/livecloud/project/menu.pagex";
    public static final String ROUTE_URI_USER_ROLE = "/livecloud/project/userRole.pagex";
    public static final String ROUTE_URI_ROLE_MENU = "/livecloud/project/roleMenu.pagex";

    /**
     * 表名
     */
    public static final String TABLE_RBAC_USER = "CT_Rbac_User";
    public static final String TABLE_RBAC_ROLE = "CT_Rbac_Role";
    public static final String TABLE_RBAC_MENU = "CT_Rbac_Menu";
    public static final String TABLE_RBAC_USER_ROLE = "CT_Rbac_User_Role";
    public static final String TABLE_RBAC_ROLE_MENU = "CT_Rbac_Role_Menu";

    /**
     * 数据常量
     */
    public static final String DATA_USER_PASSWORD = "1234";
}
