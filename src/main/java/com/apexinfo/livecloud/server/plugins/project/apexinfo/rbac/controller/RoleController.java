package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.livebos.console.common.util.Util;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelativeDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.RoleService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.UserRoleService;
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
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param userId
     * @param roleId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY, method = RequestMethod.GET)
    @ResponseBody
    public Response query(Integer pageNo, Integer pageSize, String keyword, Long userId, Long roleId,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if ((pageNo != null && pageNo < 1) ||
                (pageSize != null && pageSize < 0) ||
                (userId != null && userId <= 0) ||
                (roleId != null && roleId <= 0)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        PageDTO<Role> pageDTO = RoleService.getInstance().query(pageNo, pageSize, keyword, userId, roleId);
        return Response.ofSuccess(pageDTO);
    }

    /**
     * 新增角色
     *
     * @param role
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        int rows = RoleService.getInstance().add(role);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_ADD), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_ADD));
    }

    /**
     * 修改角色
     *
     * @param role
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (Util.isEmpty(role.getId()) || role.getId() <= 0) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        int rows = RoleService.getInstance().update(role);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 给用户授权角色
     *
     * @param relativeDTO
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER_ROLE,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserRole(@RequestBody RelativeDTO relativeDTO,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (relativeDTO.getRoleId() <= 0) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        int rows = RoleService.getInstance().updateUserRole(relativeDTO);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 修改角色的菜单
     *
     * @param relativeDTO
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE_MENU,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateRoleMenus(@RequestBody RelativeDTO relativeDTO,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (relativeDTO.getRoleId() <= 0) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        int rows = RoleService.getInstance().updateRoleMenus(relativeDTO);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 删除角色
     *
     * @param ids
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> ids, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (Util.isEmpty(ids)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        // 如果角色有用户关联, 则不能删除
        List<Long> userIds = UserRoleService.getInstance().queryByRoleIds(ids);
        if (Util.isNotEmpty(userIds)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_ROLE_ERROR_REQUIRED));
        }

        int rows = RoleService.getInstance().delete(ids);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_DELETE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_DELETE));
    }
}
