package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.RoleConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelaDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.Role;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.RoleService;
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
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = CommonConstants.ACTION_QUERY, method = RequestMethod.GET)
    @ResponseBody
    public Response query(Integer pageNo, Integer pageSize, String keyword, Long userId, Long roleId,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (pageNo != null && pageNo < 1 || pageSize != null && pageSize < 0 ||
                userId != null && userId <= 0 || roleId != null && roleId <= 0) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
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
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = CommonConstants.ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (role == null || role.getName() == null) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = RoleService.getInstance().add(role);
        if (rows == 1) {
            return Response.ofSuccess(CommonConstants.ADD_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.ADD_FAIL);
    }

    /**
     * 修改角色
     *
     * @param role
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = CommonConstants.ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (role == null || role.getId() == null || role.getId() <= 0 || role.getName() == null) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = RoleService.getInstance().update(role);
        if (rows == 1) {
            return Response.ofSuccess(CommonConstants.UPDATE_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.UPDATE_FAIL);
    }

    /**
     * 修改角色的菜单
     *
     * @param relaDTO
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = RoleConstants.ROUTE_ROLE_MENU,
            params = CommonConstants.ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateRoleMenus(@RequestBody RelaDTO relaDTO,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (relaDTO == null || relaDTO.getId() <= 0) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = RoleService.getInstance().updateRoleMenus(relaDTO);
        if (rows > 0) {
            return Response.ofSuccess(CommonConstants.UPDATE_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.UPDATE_FAIL);
    }

    /**
     * 删除角色
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = RoleConstants.ROUTE_ROLE,
            params = CommonConstants.ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> id, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (id == null) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = RoleService.getInstance().delete(id);
        if (rows > 0) {
            return Response.ofSuccess(CommonConstants.DELETE_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.DELETE_FAIL);
    }
}
