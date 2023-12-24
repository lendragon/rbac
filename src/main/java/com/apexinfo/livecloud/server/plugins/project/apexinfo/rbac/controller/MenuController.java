package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.livebos.console.common.util.Util;
import com.apex.util.F;
import com.apexinfo.livecloud.server.common.exception.PageException;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.MenuService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.RoleMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: MenuController
 * @Description: 菜单控制器
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
@Controller
public class MenuController extends AbstractController {
    /**
     * 查询菜单树
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY_ALL, method = RequestMethod.GET)
    @ResponseBody
    public Response queryAllToTree(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        List<Menu> menusTree = MenuService.getInstance().queryAllToTree();
        return Response.ofSuccess(menusTree);
    }

    /**
     * 根据用户id查询菜单树
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_USER_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryToTreeByUserId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String userId = verificationParameter(request, CommonConstants.PARAM_COMMON_USER_ID, true, false);
            List<Menu> menuTree = MenuService.getInstance().queryToTreeByUserId(F.toLong(userId));
            return Response.ofSuccess(menuTree);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * 根据角色id查询菜单树
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_ROLE_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryToTreeByRoleId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String roleId = verificationParameter(request, CommonConstants.PARAM_COMMON_ROLE_ID, true, false);
            List<Menu> menuTree = MenuService.getInstance().queryToTreeByRoleId(F.toLong(roleId));
            return Response.ofSuccess(menuTree);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * 根据菜单id查询菜单
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_MENU_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByMenuId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String menuId = verificationParameter(request, CommonConstants.PARAM_COMMON_MENU_ID, true, false);
            Menu menu = MenuService.getInstance().queryByMenuId(F.toLong(menuId));
            return Response.ofSuccess(menu);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * 新增菜单
     *
     * @param menu 菜单
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = MenuService.getInstance().add(menu);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_ADD, null));
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_ADD));
    }

    /**
     * 修改菜单
     *
     * @param menu 菜单
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = MenuService.getInstance().update(menu);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 删除菜单
     *
     * @param menuIds 菜单id列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestBody List<Long> menuIds, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        // 如果菜单有角色关联, 则不能删除
        List<Long> roleIds = RoleMenuService.getInstance().queryIdByMenuIds(menuIds);
        if (Util.isNotEmpty(roleIds)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_MENU_ERROR_MENU_REQUIRED));
        }

        int rows = MenuService.getInstance().delete(menuIds);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_DELETE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_DELETE));
    }
}
