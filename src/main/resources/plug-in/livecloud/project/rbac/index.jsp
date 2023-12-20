<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>test</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/bootstrap-datetimepicker.min.css" />
    <link rel="stylesheet" href="css/bootstrap-treeview.min.css" />
    <link rel="stylesheet" href="css/common.css" />

    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/bootstrap-datetimepicker.min.js"></script>
    <script src="js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="js/bootstrap-treeview.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.9-1/crypto-js.js"></script>
    <script src="js/common.js"></script>
    <script src="js/ui.js"></script>
  </head>
  <body>
    <div class="container-fluid">
      <!-- 头部 -->
      <header>
        <div class="page-header">
          <h2>系统管理 <small>/用户管理</small></h2>
        </div>
      </header>

      <!-- 导航栏 -->
      <nav>
        <ul class="nav nav-tabs" role="tablist">
          <li id="userNav" role="presentation">
            <a href="#user" aria-controls="user" role="tab" data-toggle="tab"
              >用户管理</a
            >
          </li>
          <li id="roleNav" role="presentation">
            <a href="#role" aria-controls="role" role="tab" data-toggle="tab"
              >角色管理</a
            >
          </li>
          <li id="menuNav" role="presentation">
            <a href="#menu" aria-controls="menu" role="tab" data-toggle="tab"
              >菜单管理</a
            >
          </li>
        </ul>
      </nav>

      <!-- 内容 -->
      <content id="content"></content>
    </div>

    <script>
      const ROUTE_USER =
        "${pageContext.request.contextPath}/livecloud/project/user.pagex";
      const ROUTE_USER_ROLE =
        "${pageContext.request.contextPath}/livecloud/project/userRole.pagex";
      const ROUTE_ROLE =
        "${pageContext.request.contextPath}/livecloud/project/role.pagex";
      const ROUTE_ROLE_MENU =
        "${pageContext.request.contextPath}/livecloud/project/roleMenu.pagex";
      const ROUTE_MENU =
        "${pageContext.request.contextPath}/livecloud/project/menu.pagex";
      $(() => {
        // 根据地址栏的参数page, 值为user、role、menu来展示不同的页面， 没有填写时自动加上user
        let page = getURLParam("page");
        page = isEmpty(page) ? "user" : page;

        // 展示对应页面
        showPage(page);

        // 对应菜单活跃
        $("#" + page + "Nav").addClass("active");

        // 绑定监听事件
        bindNav("user");
        bindNav("role");
        bindNav("menu");
      });

      // 展示页面并修改地址栏
      function showPage(pageName) {
        $("#content").load("./" + pageName + ".jsp");
        $(".page-header small").text("/ " + getMenuName(pageName));

        // 修改地址栏
        changeURLStatic("page", pageName);
      }

      // 根据user、role、menu获取菜单名称， 如用户管理， 角色管理， 菜单管理
      function getMenuName(menuName) {
        return $("#" + menuName + "Nav > a").text();
      }

      // 根据user、role、menu绑定对应的跳转事件
      function bindNav(pageName) {
        $("#" + pageName + "Nav").click(() => {
          pageNo = 1;
          showPage(pageName);
        });
      }
    </script>
  </body>
</html>
