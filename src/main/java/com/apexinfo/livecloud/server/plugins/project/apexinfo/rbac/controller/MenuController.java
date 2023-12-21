package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.livebos.console.common.util.Util;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.MenuService;
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
     * @param id
     * @param roleId
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_QUERY, method = RequestMethod.GET)
    @ResponseBody
    public Response query(Long id, Long roleId, Long userId,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        if ((roleId != null && roleId <= 0) ||
                (id != null && id <= 0) ||
                (userId != null && userId <= 0)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        List<Menu> menusTree = MenuService.getInstance().queryToTree(id, roleId, userId);
        return Response.ofSuccess(menusTree);
    }

    /**
     * 新增菜单
     *
     * @param menu
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
     * @param menu
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (Util.isEmpty(menu.getId()) || menu.getId() <= 0) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        int rows = MenuService.getInstance().update(menu);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 删除菜单
     *
     * @param ids
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> ids, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (Util.isEmpty(ids)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        int rows = MenuService.getInstance().delete(ids);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_DELETE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_DELETE));
    }
}
