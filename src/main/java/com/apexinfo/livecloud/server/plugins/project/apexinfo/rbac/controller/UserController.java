package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.livebos.console.common.util.Util;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

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
     * @param roleId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_QUERY, method = RequestMethod.GET)
    @ResponseBody
    public Response query(Integer pageNo, Integer pageSize, String keyword, Long id, Long roleId,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if ((pageNo != null && pageNo < 1) ||
                (pageSize != null && pageSize < 0) ||
                (id != null && id <= 0)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }

        PageDTO<User> pageDTO = UserService.getInstance().query(pageNo, pageSize, keyword, id, roleId);
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
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody User user,
                        HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        // 查看是否已经存在相同编号或相同名称的用户
        List<User> queryUsers = UserService.getInstance().queryByNoOrName(user.getNo(), user.getName());
        if (queryUsers != null) {
            for (User queryUser : queryUsers) {
                if (Objects.equals(user.getNo(), queryUser.getNo())) {
                    return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_USER_ERROR_REPEAT_NO));
                }
                if (Objects.equals(user.getName(), queryUser.getName())) {
                    return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_USER_ERROR_REPEAT_NAME));
                }
            }
        }
        int rows = UserService.getInstance().add(user);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_ADD), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_ADD));
    }

    /**
     * 修改用户
     *
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody User user,
                           HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (Util.isEmpty(user.getId()) || user.getId() <= 0) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        int rows = UserService.getInstance().update(user);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

//    /**
//     * 修改用户的角色
//     *
//     * @param relativeDTO
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = CommonConstants.ROUTE_URI_USER_ROLE,
//            params = CommonConstants.PARAM_UPDATE, method = RequestMethod.POST)
//    @ResponseBody
//    public Response updateUserRoles(@RequestBody RelativeDTO relativeDTO,
//                                    HttpServletRequest request, HttpServletResponse response) {
//        setJsonResponse(request, response);
//        if (relativeDTO == null || relativeDTO.getId() == null || relativeDTO.getId() <= 0) {
//            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_DATA_ERROR));
//        }
//        int rows = UserService.getInstance().updateUserRoles(relativeDTO);
//        if (rows > 0) {
//            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_UPDATE_SUCCESS), null);
//        }
//        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_UPDATE_FAIL));
//    }

    /**
     * 删除用户
     *
     * @param ids
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> ids, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        if (Util.isEmpty(ids)) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_ERROR_DATA));
        }
        int rows = UserService.getInstance().delete(ids);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_DELETE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_DELETE));
    }
}
