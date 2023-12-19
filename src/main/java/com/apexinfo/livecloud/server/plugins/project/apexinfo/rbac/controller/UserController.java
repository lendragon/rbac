package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.product.sql.query.util.MD5Tools;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.constant.UserConstants;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.PageDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.UserDTO;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.service.UserService;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util.AesDecryptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

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
            params = "action=query", method = RequestMethod.GET)
    @ResponseBody
    public Response query(Long pageNo, Long pageSize, String keyword, Long id,
                          HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        PageDTO<User> pageDTO = UserService.getInstance().query(pageNo, pageSize, keyword, id);
        return Response.ofSuccess(pageDTO);
    }

    /**
     * 新增用户
     *
     * @param userDTO
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = UserConstants.ROUTE_USER,
            params = "action=add", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@RequestBody UserDTO userDTO,
                        HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);
        String iv = userDTO.getIv();
        User user = userDTO.getUser();

        String password = null;
        // 验证合法性
        try {
            // 对传输过来的密码进行aes解密并再使用md5加密得到加密后的密码
            password = new MD5Tools().stringToMD5(
                    AesDecryptor.aesDecrypt(user.getPassword(), UserConstants.AES_KEY, iv));
        } catch (Exception e) {
            return Response.ofFail("数据异常, 请重新提交");
        }

        user.setPassword(password);
        int rows = UserService.getInstance().add(user);
        if (rows == 1) {
            return Response.ofSuccess("新增成功", null);
        }
        return Response.ofFail("新增失败");
    }

    /**
     * 修改用户
     *
     * @param userDTO
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = UserConstants.ROUTE_USER,
            params = "action=update", method = RequestMethod.POST)
    @ResponseBody
    public Response update(@RequestBody UserDTO userDTO,
                           HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        String iv = userDTO.getIv();
        User user = userDTO.getUser();

        String password = null;
        // 验证合法性
        try {
            // 对传输过来的密码进行aes解密并再使用md5加密得到加密后的密码
            password = new MD5Tools().stringToMD5(
                    AesDecryptor.aesDecrypt(user.getPassword(), UserConstants.AES_KEY, iv));
        } catch (Exception e) {
            return Response.ofFail("数据异常, 请重新提交");
        }

        int rows = UserService.getInstance().update(user);
        if (rows == 1) {
            return Response.ofSuccess("修改成功", null);
        }
        return Response.ofFail("修改失败");
    }


    /**
     * 修改用户的角色
     *
     * @param userId
     * @param roleId
     * @param request
     * @param response
     * @return
     */
    // TODO 可能要修改成前端发送要添加的列表和要删除的列表后再添加和删除
    @RequestMapping(value = UserConstants.ROUTE_USER_ROLE,
            params = "action=update", method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserRoles(@RequestParam Long userId, @RequestParam Set<Long> roleId,
                                    HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = UserService.getInstance().updateUserRoles(userId, roleId);

        if (rows > 0) {
            return Response.ofSuccess("修改成功", null);
        }
        return Response.ofFail("修改失败");
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
            params = "action=delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam List<Long> id, HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request, response);

        int rows = UserService.getInstance().delete(id);
        if (rows > 0) {
            return Response.ofSuccess("删除成功", null);
        }
        return Response.ofFail("删除失败");
    }

}
