package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.util.F;
import com.apexinfo.livecloud.server.common.exception.PageException;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.MenuService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.ValidUtil;
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
     * @param request
     * @param response
     * @return
     * @description 根据用户主键查询菜单树
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_USER_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryToTreeByUserId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String id = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);
            List<Menu> menuTree = MenuService.getInstance().queryToTreeByUserId(F.toLong(id));
            return Response.ofSuccess(menuTree);
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
     * @description 根据角色主键查询菜单树
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_ROLE_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_ROLE_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryToTreeByRoleId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String id = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);
            List<Menu> menuTree = MenuService.getInstance().queryToTreeByRoleId(F.toLong(id));
            return Response.ofSuccess(menuTree);
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
     * @description 根据菜单主键查询菜单
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_MENU_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByMenuId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String id = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);
            Menu menu = MenuService.getInstance().queryByMenuId(F.toLong(id));
            return Response.ofSuccess(menu);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param menu     菜单
     * @param request
     * @param response
     * @return
     * @description 新增菜单
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(Menu.class, menu, 1);
            if (MenuService.getInstance().add(menu)) {
                return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_ADD, null));
            }
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_ADD));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param menu     菜单
     * @param request
     * @param response
     * @return
     * @description 修改菜单
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(Menu.class, menu, 2);
            if (MenuService.getInstance().update(menu)) {
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
     * @description 删除菜单
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String id = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);

            if (MenuService.getInstance().delete(F.toLong(id))) {
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
