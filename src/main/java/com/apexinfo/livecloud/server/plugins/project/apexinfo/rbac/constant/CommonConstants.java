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
    public static final String PARAM_COMMON_ID = "id";

    /**
     * 响应信息的国际化key
     */
    public static final String I18N_USER_ERROR_WRONG_PASSWORD = "apexinfo.rbac.user.response.error.wrong.password";
    public static final String I18N_USER_ERROR_WRONG_CHECKED_PASSWORD = "apexinfo.rbac.user.response.error.wrong.checked.password";
    public static final String I18N_USER_ERROR_REPEAT_USER_NAME = "apexinfo.rbac.user.response.error.repeat.user.name";
    public static final String I18N_ROLE_ERROR_REPEAT_ROLE_NAME = "apexinfo.rbac.role.response.error.repeat.role.name";
    public static final String I18N_ROLE_ERROR_REQUIRED_BY_USER = "apexinfo.rbac.role.response.error.required.by.user";
    public static final String I18N_ROLE_ERROR_REQUIRED_BY_MENU = "apexinfo.rbac.role.response.error.required.by.menu";
    public static final String I18N_MENU_ERROR_REQUIRED_BY_ROLE = "apexinfo.rbac.menu.response.error.required.by.role";
    public static final String I18N_MENU_ERROR_REQUIRED_BY_CHILDREN = "apexinfo.rbac.menu.response.error.required.by.children";
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
    public static final String ROUTE_URI_USER_MENU = "/livecloud/project/userMenu.pagex";

    /**
     * 表名
     */
    public static final String TABLE_RBAC_USER = "CT_Rbac_User";
    public static final String TABLE_RBAC_ROLE = "CT_Rbac_Role";
    public static final String TABLE_RBAC_MENU = "CT_Rbac_Menu";
    public static final String TABLE_RBAC_USER_ROLE = "CT_Rbac_User_Role";
    public static final String TABLE_RBAC_ROLE_MENU = "CT_Rbac_Role_Menu";


    /**
     * 默认密码
     */
    public static final String DATA_USER_DEFAULT_PASSWORD = "1234";
    /**
     * 默认状态
     */
    public static final Integer DATA_COMMON_DEFAULT_STATE = 1;
    /**
     * 顶层菜单主键
     */
    public static final Long DATA_COMMON_ROOT_MENU = 0L;
    /**
     * 超级管理员主键
     */
    public static final Long DATA_COMMON_ROOT_ADMIN = 1L;
}
