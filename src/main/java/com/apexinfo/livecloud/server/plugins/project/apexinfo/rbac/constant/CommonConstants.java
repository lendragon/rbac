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
     * 请求接口的动作
     */
    public static final String PARAM_QUERY = "action=query";
    public static final String PARAM_ADD = "action=add";
    public static final String PARAM_UPDATE = "action=update";
    public static final String PARAM_DELETE = "action=delete";
    /**
     * 国际化响应信息
     */
    public static final String I18N_USER_NO_REPEAT = "apexinfo.rbac.user.responseNoRepeat";

    public static final String I18N_USER_NAME_REPEAT = "apexinfo.rbac.user.responseDataError";

    public static final String I18N_DATA_ERROR = "apexinfo.rbac.responseDataError";
    public static final String I18N_ADD_SUCCESS = "apexinfo.rbac.responseAddSuccess";
    public static final String I18N_UPDATE_SUCCESS = "apexinfo.rbac.responseUpdateSuccess";
    public static final String I18N_DELETE_SUCCESS = "apexinfo.rbac.responseDeleteSuccess";
    public static final String I18N_ADD_FAIL = "apexinfo.rbac.responseAddFail";
    public static final String I18N_UPDATE_FAIL = "apexinfo.rbac.responseUpdateFail";
    public static final String I18N_DELETE_FAIL = "apexinfo.rbac.responseDeleteFail";

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
}
