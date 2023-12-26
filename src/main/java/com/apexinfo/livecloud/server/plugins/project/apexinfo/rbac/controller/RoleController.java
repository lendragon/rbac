package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.util.F;
import com.apexinfo.livecloud.server.common.exception.PageException;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.*;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.RoleService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.ValidUtil;
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
     * @param pageBean 分页Bean, 包含pageNo, pageSize, keyword
     * @param request
     * @param response
     * @return
     * @description 查询角色
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY_ALL, method = RequestMethod.GET)
    @ResponseBody
    public Response queryAll(PageBean<Role> pageBean,
                             HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            pageBean = RoleService.getInstance().queryAll(pageBean);
            return Response.ofSuccess(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param request
     * @param response
     * @return
     * @description 根据用户主键查询角色
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_USER_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByUserId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String userId = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);
            List<Role> roles = RoleService.getInstance().queryByUserId(F.toLong(userId));
            return Response.ofSuccess(roles);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param request
     * @param response
     * @return
     * @description 根据角色主键查询角色
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_ROLE_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByRoleId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String roleId = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);
            Role role = RoleService.getInstance().queryByRoleId(F.toLong(roleId));
            return Response.ofSuccess(role);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param role     角色
     * @param request
     * @param response
     * @return
     * @description 新增角色
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(Role.class, role, 1);

            if (RoleService.getInstance().add(role)) {
                return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_ADD), null);
            }
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_ADD));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param role     角色
     * @param request
     * @param response
     * @return
     * @description 修改角色
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(Role.class, role, 2);

            if (RoleService.getInstance().update(role)) {
                return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
            }
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param relativeBean 关联Bean, 包含role, addIds, deleteIds
     * @param request
     * @param response
     * @return
     * @description 给用户授权角色
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER_ROLE,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserRole(@RequestBody RelativeBean relativeBean,
                                   HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(RelativeBean.class, relativeBean);

            if (RoleService.getInstance().updateUserRole(relativeBean)) {
                return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
            }
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param relativeBean 关联Bean, 包含role, addIds, deleteIds
     * @param request
     * @param response
     * @return
     * @description 修改角色的菜单
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE_MENU,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateRoleMenu(@RequestBody RelativeBean relativeBean,
                                   HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(RelativeBean.class, relativeBean);
            if (RoleService.getInstance().updateRoleMenus(relativeBean)) {
                return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
            }
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param request
     * @param response
     * @return
     * @description 删除角色
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String roleId = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);

            if (RoleService.getInstance().delete(F.toLong(roleId))) {
                return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_DELETE), null);
            }
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_DELETE));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }
}
