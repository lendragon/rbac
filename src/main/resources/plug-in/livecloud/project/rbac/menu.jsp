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
    $("#addMenuBtn").click(addMenuFormBox);
    // 监听搜索框提交事件
    searchFormEvent((pageNo, roleId) => {
      queryMenus("roleId=" + roleId);
    });
  });

  // 查询菜单树
  function queryMenus(params = "") {
    $.get(
      ROUTE_MENU + "?action=query",
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
    $.get(ROUTE_MENU + "?action=query", "menuId=" + menuId, (res) => {
      if (!res.success) {
        failMessageFloat(res.msg);
        return;
      }
      menu = res.data[0].menu;
      menuDetailFormBox(menu);
    });
  }

  // 修改菜单
  function updateMenu(data) {
    $.ajax({
      url: ROUTE_MENU + "?action=update",
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
  function deleteMenus(menusId) {
    let data = {};
    if (typeof menusId === "number") {
      data.ids = [];
      data.ids.push(menusId);
    } else {
      data.ids = menusId;
    }
    let param = $.param(data).replaceAll("%5B%5D", "");
    $.post(ROUTE_MENU + "?action=delete", param, (res) => {
      if (!res.success) {
        failMessageFloat(res.msg);
        return;
      }
      successMessageFloat(res.msg);
      queryMenus();
    });
  }

  // 新增菜单
  function addMenu(data) {
    $.ajax({
      url: ROUTE_MENU + "?action=add",
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

  function addChild(level, parentId) {
    addMenuFormBox(null, level + 1, parentId);
  }

  // 新增菜单的表单
  function addMenuFormBox(event, level, parentId) {
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
          reg: "^[1-9]\d*$",
          regTitle: "请输入有效数字",
        },
        {
          label: "菜单层级",
          type: "number",
          name: "level",
          required: true,
          reg: "^[1-9]\d*$",
          regTitle: "请输入有效数字",
          placeholder: "菜单层级",
          value: level,
          disabled: !isEmpty(level),
        },
        {
          label: "父菜单id",
          type: "number",
          name: "parentId",
          required: false,
          reg: "^[1-9]\d*$",
          regTitle: "请输入有效数字",
          placeholder: "父菜单id",
          value: parentId,
          disabled: !isEmpty(level),
        },
        {
          label: "菜单路径",
          type: "text",
          name: "url",
          required: false,
          placeholder: "菜单路径",
        },
        {
          label: "菜单状态",
          type: "radio",
          name: "state",
          required: true,
          options: [
            {
              name: "正常",
              value: "0",
              checked: true,
            },
            {
              name: "禁用",
              value: "1",
            },
          ],
          placeholder: "菜单状态",
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
