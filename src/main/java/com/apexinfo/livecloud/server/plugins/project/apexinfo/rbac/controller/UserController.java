package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apex.util.F;
import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.Core;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.CommonConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageBean;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.UserService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.ValidUtil;
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
     * @param pageBean 分页Bean, 包含pageNo, pageSize, keyword
     * @return
     * @description 查询所有用户
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_QUERY_ALL, method = RequestMethod.GET)
    @ResponseBody
    public Response queryAll(PageBean<User> pageBean, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            PageBean<User> result = UserService.getInstance().queryAll(pageBean);
            return Response.ofSuccess(result);
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
     * @description 根据用户id查询用户
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_USER_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByUserId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String id = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);
            User user = UserService.getInstance().queryByUserId(F.toLong(id));
            return Response.ofSuccess(user);
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
     * @description 根据角色id查询用户
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER_ROLE,
            params = CommonConstants.PARAM_ACTION_QUERY_BY_ROLE_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryByRoleId(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String id = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);
            List<User> users = UserService.getInstance().queryByRoleId(F.toLong(id));
            return Response.ofSuccess(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param user     用户
     * @param request
     * @param response
     * @return
     * @description 新增用户
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_ADD, method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody User user,
                        HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(User.class, user, 1);

            if (UserService.getInstance().add(user)) {
                return Response.ofSuccess(Core.i18n().getValue(CommonConstants.I18N_COMMON_SUCCESS_ADD), null);
            }
            return Response.ofFail(Core.i18n().getValue(CommonConstants.I18N_COMMON_FAIL_ADD));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    /**
     * @param user     用户
     * @param request
     * @param response
     * @return
     * @description 修改用户
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody User user,
                           HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            ValidUtil.valid(User.class, user, 2);

            if (UserService.getInstance().update(user)) {
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
     * @param user     用户
     * @param request
     * @param response
     * @return
     * @description 修改用户密码
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_UPDATE_PASSWORD, method = RequestMethod.POST)
    @ResponseBody
    public Response updatePassword(@RequestBody User user,
                                   HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            if (UserService.getInstance().updatePassword(user)) {
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
     * @description 删除用户
     */
    @RequestMapping(value = CommonConstants.ROUTE_URI_USER,
            params = CommonConstants.PARAM_ACTION_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public Response delete(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        try {
            String userId = verificationParameter(request, CommonConstants.PARAM_COMMON_ID, true, false);

            if (UserService.getInstance().delete(F.toLong(userId))) {
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
