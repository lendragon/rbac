package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.UserConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.RelaDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description: 用户控制器
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
@Controller
public class UserController extends AbstractController {
    /**
     * 查询用户
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = UserConstants.ROUTE_USER,
            params = CommonConstants.ACTION_QUERY, method = RequestMethod.GET)
    @ResponseBody
    public Response query(Integer pageNo, Integer pageSize, String keyword, Long id,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (pageNo != null && pageNo < 1 || pageSize != null && pageSize < 0 ||
                id != null && id <= 0) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }

        PageDTO<User> pageDTO = UserService.getInstance().query(pageNo, pageSize, keyword, id);
        return Response.ofSuccess(pageDTO);
    }

    /**
     * 新增用户
     *
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = UserConstants.ROUTE_USER,
            params = CommonConstants.ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody User user,
                        HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (user == null || user.getPassword() == null ||
                user.getName() == null || user.getNo() == null) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = UserService.getInstance().add(user);
        if (rows == 1) {
            return Response.ofSuccess(CommonConstants.ADD_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.ADD_FAIL);
    }

    /**
     * 修改用户
     *
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = UserConstants.ROUTE_USER,
            params = CommonConstants.ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody User user,
                           HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (user == null || user.getId() == null || user.getId() <= 0 ||
                user.getName() == null || user.getNo() == null) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = UserService.getInstance().update(user);
        if (rows == 1) {
            return Response.ofSuccess(CommonConstants.UPDATE_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.UPDATE_FAIL);
    }

    /**
     * 修改用户的角色
     *
     * @param relaDTO
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = UserConstants.ROUTE_USER_ROLE,
            params = CommonConstants.ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserRoles(@RequestBody RelaDTO relaDTO,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (relaDTO == null || relaDTO.getId() == null || relaDTO.getId() <= 0) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = UserService.getInstance().updateUserRoles(relaDTO);
        if (rows > 0) {
            return Response.ofSuccess(CommonConstants.UPDATE_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.UPDATE_FAIL);
    }

    /**
     * 删除用户
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = UserConstants.ROUTE_USER,
            params = CommonConstants.ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> id, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (id == null) {
            return Response.ofFail(CommonConstants.DATA_ERROR);
        }
        int rows = UserService.getInstance().delete(id);
        if (rows > 0) {
            return Response.ofSuccess(CommonConstants.DELETE_SUCCESS, null);
        }
        return Response.ofFail(CommonConstants.DELETE_FAIL);
    }
}
