package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.controller;

import com.apexinfo.livecloud.server.common.exporter.Response;
import com.apexinfo.livecloud.server.core.web.AbstractController;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.mapper.Test2Mapper;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@ResponseBody
public class Test2Controller extends AbstractController {
    private Test2Mapper test2Mapper = new Test2Mapper();;
    @RequestMapping("/livecloud/project/user.pagex")
    public Response test(HttpServletRequest request, HttpServletResponse response) {
        setJsonResponse(request,response);
        List<User> users = test2Mapper.getUsers();
        System.out.println("结果:");
        System.out.println(users);
        return Response.ofSuccess(users);
    }

    @RequestMapping("/livecloud/project/test.pagex")
    public Response test2(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("test");
        return Response.ofSuccess("success");
    }
}
