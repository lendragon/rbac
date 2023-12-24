package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.livebos.console.common.util.Util;
import com.apex.util.F;
import com.apexinfo.livecloud.server.common.exception.PageException;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelativeBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.RoleService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.UserRoleService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: RoleController
 * @Description: 角色控制器
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
@Controller
public class RoleController extends AbstractController {
    /**
     * 查询角色
     *
     * @param pageBean 分页Bean, 包含pageNo, pageSize, keyword
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY_ALL, method = RequestMethod.GET)
    @ResponseBody
    public Response queryAll(PageBean<Role> pageBean,
                             HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        pageBean = RoleService.getInstance().queryAll(pageBean);

        return Response.ofSuccess(pageBean);
    }

    /**
     * 根据用户id查询角色
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_USER_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByUserId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        try {
            String userId = verificationParameter(request, CommonConstants.PARAM_COMMON_USER_ID, true, false);
            List<Role> roles = RoleService.getInstance().queryByUserId(F.toLong(userId));
            return Response.ofSuccess(roles);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * 根据角色id查询角色
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_ROLE_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByRoleId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        try {
            String roleId = verificationParameter(request, CommonConstants.PARAM_COMMON_ROLE_ID, true, false);
            Role role = RoleService.getInstance().queryByRoleId(F.toLong(roleId));
            return Response.ofSuccess(role);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * 新增角色
     *
     * @param role 角色
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        // 查看是否已经存在相同编号的角色
        Long roleId = RoleService.getInstance().queryIdByRoleCode(role.getRoleCode());
        if (roleId != null) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_ROLE_ERROR_REPEAT_CODE));
        }

        int rows = RoleService.getInstance().add(role);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_ADD), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_ADD));
    }

    /**
     * 修改角色
     *
     * @param role 角色
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = RoleService.getInstance().update(role);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 给用户授权角色
     *
     * @param relativeBean 关联Bean, 包含role, addIds, deleteIds
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER_ROLE,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserRole(@RequestBody RelativeBean relativeBean,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = RoleService.getInstance().updateUserRole(relativeBean);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 修改角色的菜单
     *
     * @param relativeBean 关联Bean, 包含role, addIds, deleteIds
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE_MENU,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateRoleMenus(@RequestBody RelativeBean relativeBean,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = RoleService.getInstance().updateRoleMenus(relativeBean);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestBody List<Long> roleIds, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        // 如果角色有用户关联, 则不能删除
        List<Long> userIds = UserRoleService.getInstance().queryByRoleIds(roleIds);
        if (Util.isNotEmpty(userIds)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_ROLE_ERROR_ROLE_REQUIRED));
        }

        int rows = RoleService.getInstance().delete(roleIds);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_DELETE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_DELETE));
    }
}
