<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="css/bootstrap-treeview.min.css" />
<script src="js/bootstrap-treeview.min.js"></script>

<!-- 上部按钮 -->
<button id="addMenuBtn" class="btn btn-primary">新增</button>
<button id="batchDeleteBtn" class="btn btn-danger" disabled>批量删除</button>

<div id="menuTree"></div>

<script>
  $(() => {
    queryMenus();
    $("#addMenuBtn").click(addMenuFormBox);
    $("#batchDeleteBtn").click(batchDelete);
  });

  // 查询菜单树
  function queryMenus() {
    $.get(
      ROUTE_MENU + "?action=query",
      (res) => {
        // 如果请求失败
        if (!res.success) {
          $("#menuData").html(res.msg);
          return;
        }
        // 请求成功
        let menus = res.data;
        // 创建菜单树并展示
        createTree("menuTree", menus);
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

  function menuDetailFormBox(menu) {
    createFormBox(
      "menuDetail",
      "菜单详情",
      [
        {
          label: "ID",
          type: "text",
          name: "id",
          required: true,
          disabled: true,
          value: menu.id,
        },
        {
          label: "菜单名",
          type: "text",
          name: "name",
          required: true,
          placeholder: "菜单名",
          reg: "^.{2,12}$",
          regTitle: "请输入2-12位的字符",
          value: menu.name,
        },
        {
          label: "菜单显示顺序",
          type: "number",
          name: "order",
          required: true,
          placeholder: "菜单显示顺序",
          reg: "^[1-9]\d*$",
          regTitle: "请输入有效数字",
          value: menu.order,
        },
        {
          label: "菜单层级",
          type: "number",
          name: "level",
          required: true,
          reg: "^[1-9]\d*$",
          regTitle: "请输入有效数字",
          placeholder: "菜单层级",
          value: menu.level,
        },
        {
          label: "父菜单id",
          type: "number",
          name: "parentId",
          required: false,
          reg: "^[1-9]\d*$",
          regTitle: "请输入有效数字",
          placeholder: "父菜单id",
          value: menu.parentId,
        },
        {
          label: "菜单路径",
          type: "text",
          name: "url",
          required: false,
          placeholder: "菜单路径",
          value: menu.url,
        },
        {
          label: "菜单描述",
          type: "textarea",
          name: "description",
          placeholder: "菜单描述",
          required: false,
          rows: 4,
          value: menu.description
        },
        {
          label: "创建时间",
          type: "date",
          name: "createTime",
          required: false,
          disabled: true,
          value: formatDate(menu.createTime, false),
        },
        {
          label: "修改时间",
          type: "date",
          name: "updateTime",
          required: false,
          disabled: true,
          value: formatDate(menu.updateTime, false),
        },
      ],
      1,
      (data, closeFun) => {
        confirmBox("提示", "确定修改吗?", 0, true, true, () => {
          updateMenu(data);
          closeFun();
        });
      },
      "修改"
    );
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

  // 批量删除
  function batchDelete() {
    confirmBox("提示", "确定删除?", -1, true, true, () => {
      let checked = $(".select-row:checked");
      let menusId = [];
      checked.each(function () {
        menusId.push(parseInt($(this).parent().next().text()));
      });
      deleteMenus(menusId);
    });
  }

  // 删除菜单
  function deleteMenus(menusId) {
    let data = {};
    if (typeof menusId === "number") {
      data.id = [];
      data.id.push(menusId);
    } else {
      data.id = menusId;
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
          disabled: !isEmpty(level)
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
          disabled: !isEmpty(level)
        },
        {
          label: "菜单路径",
          type: "text",
          name: "url",
          required: false,
          placeholder: "菜单路径",
        },
        {
          label: "菜单描述",
          type: "textarea",
          name: "description",
          placeholder: "菜单描述",
          required: false,
          rows: 4,
        }
      ],
      1,
      (data, closeFun) => {
        addMenu(data);
        closeFun();
      }
    );
  }
</script>
