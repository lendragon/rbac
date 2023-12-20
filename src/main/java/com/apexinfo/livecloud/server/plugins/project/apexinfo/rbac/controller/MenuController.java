package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.MenuVO;
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
     * @param roleId
     * @param menuId
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_QUERY, method = RequestMethod.GET)
    @ResponseBody
    public Response query(Long roleId, Long menuId, Long userId,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (roleId != null && roleId <= 0 || menuId != null && menuId <= 0 ||
                userId != null && userId <= 0) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_USER_NAME_REPEAT));
        }
        List<MenuVO> menusTree = MenuService.getInstance().queryToTree(roleId, menuId, userId);
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
            params = CommonConstants.PARAM_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (menu == null || menu.getName() == null ||
                menu.getLevel() == null || menu.getParentId() == null) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_USER_NAME_REPEAT));
        }
        int rows = MenuService.getInstance().add(menu);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_ADD_SUCCESS, null));
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_ADD_FAIL));
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
            params = CommonConstants.PARAM_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (menu == null || menu.getId() == null || menu.getId() <= 0 || menu.getName() == null ||
                menu.getOrder() == null || menu.getLevel() == null || menu.getParentId() == null) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_USER_NAME_REPEAT));
        }
        int rows = MenuService.getInstance().update(menu);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_UPDATE_SUCCESS), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_UPDATE_FAIL));
    }

    /**
     * 删除菜单
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_MENU,
            params = CommonConstants.PARAM_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> id, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (id == null) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_USER_NAME_REPEAT));
        }
        int rows = MenuService.getInstance().delete(id);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_DELETE_SUCCESS), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_DELETE_FAIL));
    }
}
