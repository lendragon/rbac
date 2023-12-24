<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 上部按钮 -->
<div class="row">
  <div class="col-md-9">
    <button id="addMenuBtn" class="btn btn-primary">新增</button>
  </div>
  <div class="col-md-3">
    <form id="searchForm" class="form-inline" role="search">
      <div class="form-group">
        <input type="text" class="form-control" placeholder="请输入角色id" />
      </div>
      <button type="submit" class="btn btn-default">查询</button>
    </form>
  </div>
</div>

<div id="menuTree"></div>

<script>
  $(() => {
    queryMenus();
    $("#addMenuBtn").click(() => {
      addMenuFormBox(event, 0);
    });
    // 监听搜索框提交事件
    searchFormEvent((pageNo, pageSize, roleId) => {
      queryMenus("roleId=" + roleId, "role");
    });
  });

  // 查询菜单树
  function queryMenus(params = "", type = "") {
    $.get(
      ROUTE_MENU + "?" + (type === "" ? PARAM_ACTION_QUERY_ALL : PARAM_ACTION_QUERY_BY_ROLE_ID),
      params,
      (res) => {
        // 如果请求失败
        if (!res.success) {
          $("#menuData").html(res.msg);
          return;
        }
        // 请求成功
        let menus = res.data;
        // 创建菜单树并展示
        createTree("menuTree", menus, true, false);
      },
      "json"
    );
  }

  // 查看菜单详情
  function menuDetail(menuId) {
    $.get(
      ROUTE_MENU + "?" + PARAM_ACTION_QUERY_BY_MENU_ID,
      "menuId=" + menuId,
      (res) => {
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        menu = res.data;
        menuDetailFormBox(menu);
      }
    );
  }

  // 修改菜单
  function updateMenu(data) {
    $.ajax({
      url: ROUTE_MENU + "?" + PARAM_ACTION_UPDATE,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(data),
      success: function (res) {
        // 请求成功后的回调函数
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        successMessageFloat(res.msg);
        queryMenus();
      },
    });
  }

  // 删除菜单
  function deleteMenusBox(menusId) {
    confirmBox("提示", "确定删除?", -1, true, true, () => {
      deleteMenus(menusId);
    });
  }

  // 删除菜单
  function deleteMenus(menuIds) {
    let data = [];
    if (typeof menuIds === "number") {
      data.push(menuIds);
    } else {
      data = menuIds;
    }
    $.ajax({
      url: ROUTE_MENU + "?" + PARAM_ACTION_DELETE,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(data),
      success: function (res) {
        // 请求成功后的回调函数
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        successMessageFloat(res.msg);
        queryMenus();
      },
    });
  }

  // 新增菜单
  function addMenu(data) {
    $.ajax({
      url: ROUTE_MENU + "?" + PARAM_ACTION_ADD,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(data),
      success: function (res) {
        // 请求成功后的回调函数
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        successMessageFloat(res.msg);
        queryMenus();
      },
    });
  }

  function addChild(parentId) {
    addMenuFormBox(null, parentId);
  }

  // 新增菜单的表单
  function addMenuFormBox(event, parentId) {
    createFormBox(
      "addMenu",
      "新增菜单",
      [
        {
          label: "菜单名",
          type: "text",
          name: "name",
          required: true,
          placeholder: "菜单名",
          reg: "^.{2,12}$",
          regTitle: "请输入2-12位的字符",
        },
        {
          label: "菜单显示顺序",
          type: "number",
          name: "order",
          required: true,
          placeholder: "菜单显示顺序",
          reg: "^$|^[1-9]\\d*$",
          regTitle: "请输入有效数字",
        },
        {
          label: "父菜单id",
          type: "number",
          name: "parentId",
          required: true,
          reg: "^$|^[1-9]\\d*$",
          regTitle: "请输入有效数字",
          placeholder: "父菜单id",
          value: parentId,
          disabled: !isEmpty(parentId),
        },
        {
          label: "菜单路径",
          type: "text",
          name: "url",
          required: false,
        },
        {
          label: "菜单描述",
          type: "textarea",
          name: "description",
          placeholder: "菜单描述",
          required: false,
          rows: 4,
        },
      ],
      1,
      (data, closeFun) => {
        addMenu(data);
        closeFun();
      }
    );
  }
</script>
