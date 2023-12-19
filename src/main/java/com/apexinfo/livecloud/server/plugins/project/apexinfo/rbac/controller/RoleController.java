package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.RoleConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

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
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = "action=query", method = RequestMethod.GET)
    @ResponseBody
    public Response query(Long pageNo, Long pageSize, String keyword, Long userId, Long roleId,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        PageDTO<Role> pageDTO = RoleService.getInstance().query(pageNo, pageSize, keyword, userId, roleId);
        return Response.ofSuccess(pageDTO);
    }

    /**
     * 新增角色
     * @param role
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = "action=add", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = RoleService.getInstance().add(role);
        if (rows == 1) {
            return Response.ofSuccess("新增成功", null);
        }

        return Response.ofFail("新增失败");
    }

    /**
     * 修改角色
     * @param role
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = "action=update", method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = RoleService.getInstance().update(role);
        if (rows == 1) {
            return Response.ofSuccess("修改成功", null);
        }

        return Response.ofFail("修改失败");
    }

    /**
     * 修改角色的菜单
     * @param roleId
     * @param menuId
     * @param request
     * @param response
     * @return
     */
    // TODO 可能要修改成前端发送要添加的列表和要删除的列表后再添加和删除
    @RequestMapping(value = RoleConstants.ROUTE_ROLE_MENU,
            params = "action=update", method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserRoles(@RequestParam Long roleId, @RequestParam Set<Long> menuId,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = RoleService.getInstance().updateRoleMenus(roleId, menuId);

        if (rows > 0) {
            return Response.ofSuccess("修改成功", null);
        }
        return Response.ofFail("修改失败");
    }

    /**
     * 删除角色
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = "action=delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> id, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = RoleService.getInstance().delete(id);
        if (rows > 0) {
            return Response.ofSuccess("删除成功", null);
        }

        return Response.ofFail("删除失败");
    }
}
