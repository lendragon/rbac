package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.MenuConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.RoleConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Menu;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.MenuVO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.MenuService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.RoleService;
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
     * 查询菜单
     * @param roleId
     * @param menuId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = MenuConstants.ROUTE_MENU,
            params = "action=query", method = RequestMethod.GET)
    @ResponseBody
    public Response query(Long roleId, Long menuId,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        List<MenuVO> menusTree = MenuService.getInstance().queryToTree(roleId, menuId);
        return Response.ofSuccess(menusTree);
    }

    /**
     * 新增菜单
     * @param menu
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = MenuConstants.ROUTE_MENU,
            params = "action=add", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = MenuService.getInstance().add(menu);
        if (rows == 1) {
            return Response.ofSuccess("新增成功", null);
        }
        return Response.ofFail("新增失败");
    }

    /**
     * 修改菜单
     * @param menu
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = MenuConstants.ROUTE_MENU,
            params = "action=update", method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Menu menu, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = MenuService.getInstance().update(menu);
        if (rows == 1) {
            return Response.ofSuccess("修改成功", null);
        }
        return Response.ofFail("修改失败");
    }

    /**
     * 删除菜单
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = MenuConstants.ROUTE_MENU,
            params = "action=delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> id, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = MenuService.getInstance().delete(id);
        if (rows > 0) {
            return Response.ofSuccess("删除成功", null);
        }
        return Response.ofFail("删除失败");
    }
}
