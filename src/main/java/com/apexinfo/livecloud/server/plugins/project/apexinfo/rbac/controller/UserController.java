package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.util.F;
import com.apexinfo.livecloud.server.common.exception.PageException;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
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
     * 查询所有用户
     *
     * @param pageBean 分页Bean, 包含pageNo, pageSize, keyword
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_QUERY_ALL, method = RequestMethod.GET)
    @ResponseBody
    public Response queryAll(PageBean<User> pageBean, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        UserService.getInstance().queryAll(pageBean);

        return Response.ofSuccess(pageBean);
    }

    /**
     * 根据用户id查询用户
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_USER_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByUserId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        try {
            String userId = verificationParameter(request, CommonConstants.PARAM_COMMON_USER_ID, true, false);
            User user = UserService.getInstance().queryByUserId(F.toLong(userId));
            return Response.ofSuccess(user);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * 根据角色id查询用户id
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_ROLE_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryIdByRoleId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        try {
            String roleId = verificationParameter(request, CommonConstants.PARAM_COMMON_ROLE_ID, true, false);
            List<Long> userIds = UserService.getInstance().queryIdByRoleId(F.toLong(roleId));
            return Response.ofSuccess(userIds);
        } catch (PageException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * 新增用户
     *
     * @param user 用户
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
        // 查看是否已经存在相同编号的用户
        Long userId = UserService.getInstance().queryIdByUserCode(user.getUserCode());
        if (userId != null) {
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_USER_ERROR_REPEAT_CODE));
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
     * @param user 用户
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
        int rows = UserService.getInstance().update(user);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 修改用户密码
     *
     * @param user 用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_UPDATE_PASSWORD, method = RequestMethod.POST)
    @ResponseBody
    public Response updatePassword(@RequestBody User user,
                           HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        int rows = UserService.getInstance().updatePassword(user);
        if (rows == 1) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_UPDATE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_UPDATE));
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> userIds,
                           HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        int rows = UserService.getInstance().delete(userIds);
        if (rows > 0) {
            return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_DELETE), null);
        }
        return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_DELETE));
    }
}
