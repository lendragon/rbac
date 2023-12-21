<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 上部按钮和搜索框 -->
<div class="row">
  <div class="col-md-9">
    <button id="addRoleBtn" class="btn btn-primary">新增</button>
    <button id="batchDeleteBtn" class="btn btn-danger" disabled>
      批量删除
    </button>
  </div>
  <div class="col-md-3">
    <form id="searchForm" class="form-inline" role="search">
      <div class="form-group">
        <input type="text" class="form-control" placeholder="请输入搜索内容" />
      </div>
      <button type="submit" class="btn btn-default">查询</button>
    </form>
  </div>
</div>

<!-- 展示角色数据的表格 -->
<table class="table table-striped table-hover">
  <thead>
    <tr>
      <th><input type="checkbox" id="select-all" /></th>
      <th>ID</th>
      <th>角色名</th>
      <th>角色状态</th>
      <th>角色描述</th>
      <th>创建时间</th>
      <th>修改时间</th>
      <th class="col-xs-2">操作</th>
    </tr>
  </thead>
  <tbody id="roleData"></tbody>
</table>

<!-- 底层分页组件 -->
<nav aria-label="Page navigation">
  <ul id="pageBtnUl" class="pagination"></ul>
</nav>

<script>
  $(() => {
    pageNo = isEmpty(getURLParam("pageNo")) ? 1 : pageNo;
    keyword = getURLParam("keyword");
    queryRoles(pageNo, keyword);
    // 监听搜索框提交事件
    searchFormEvent(queryRoles);
    $("#addRoleBtn").click(addRoleFormBox);
    $("#batchDeleteBtn").click(batchDelete);
  });

  // 查询角色
  function queryRoles(pageNo, keyword) {
    let params = "action=query";
    if (!isEmpty(pageNo)) {
      params += "&pageNo=" + pageNo;
    }
    if (!isEmpty(keyword)) {
      params += "&keyword=" + keyword;
    }

    $.get(
      ROUTE_ROLE,
      params,
      (res) => {
        // 如果请求失败
        if (!res.success) {
          $("#roleData").html(res.msg);
          return;
        }

        // 请求成功
        let roles = res.data.records;
        let total = res.data.total;
        let pageSize = res.data.pageSize;
        let properties = [
          "id",
          "name",
          "state",
          "description",
          "createTime",
          "updateTime",
        ];
        // 创建表格
        createTable("#roleData", "role", changeJsonToArr(properties, roles));

        // 创建分页组件
        createPageBtn(Math.ceil(total / pageSize), true, "queryRoles");

        // 给多选框绑定
        tableCheckBoxEvent();
      },
      "json"
    );
  }

  // 查看角色详情
  function roleDetail(roleId) {
    $.get(ROUTE_ROLE + "?action=query", "roleId=" + roleId, (res) => {
      if (!res.success) {
        failMessageFloat(res.msg);
        return;
      }
      role = res.data.records[0];
      roleDetailFormBox(role);
    });
  }

  function roleDetailFormBox(role) {
    createFormBox(
      "roleDetail",
      "角色详情",
      [
        {
          label: "ID",
          type: "text",
          name: "id",
          required: true,
          disabled: true,
          value: role.id,
        },
        {
          label: "角色名",
          type: "text",
          name: "name",
          required: true,
          placeholder: "角色名",
          reg: "^.{2,12}$",
          regTitle: "请输入2-12位的字符",
          value: role.name,
        },
        {
          label: "角色状态",
          type: "radio",
          name: "state",
          required: true,
          options: [
            {
              name: "正常",
              value: "0",
              checked: role.state === 0,
            },
            {
              name: "禁用",
              value: "1",
              checked: role.state === 1,
            },
          ],
          placeholder: "角色状态",
        },
        {
          label: "角色描述",
          type: "textarea",
          name: "description",
          placeholder: "角色描述",
          required: false,
          value: role.description,
          rows: 4,
        },
        {
          label: "创建时间",
          type: "date",
          name: "createTime",
          required: false,
          disabled: true,
          value: formatDate(role.createTime, false),
        },
        {
          label: "修改时间",
          type: "date",
          name: "updateTime",
          required: false,
          disabled: true,
          value: formatDate(role.updateTime, false),
        },
      ],
      1,
      (data, closeFun) => {
        confirmBox("提示", "确定修改吗?", 0, true, true, () => {
          updateRole(data);
          closeFun();
        });
      },
      "修改"
    );
  }

  // 查看角色有的用户
  function roleQueryUser(roleId) {
    // 获取角色对应的用户
    $.get(
      ROUTE_USER + "?action=query",
      "roleId=" + roleId,
      (res) => {
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        let userRoles = res.data.records;
        // 查询所有用户
        $.get(
          ROUTE_USER + "?action=query",
          (res) => {
            if (!res.success) {
              failMessageFloat(res.msg);
              return;
            }
            let users = res.data.records;
            // 展示模态框
            roleQueryUserTableBox(roleId, userRoles, users);
          },
          "json"
        );
      },
      "json"
    );
  }

  function roleQueryUserTableBox(roleId, userRoles, users) {
    let tableHead = [
      "ID",
      "用户编号",
      "用户名",
      "性别",
      "出生日期",
      "手机号",
      "用户状态",
    ];
    let properties = [
      "id",
      "no",
      "name",
      "sex",
      "birthDay",
      "phoneNum",
      "state",
    ];
    let tableData = changeJsonToArr(properties, users);

    let deleteList = []; // 要删除的列表
    let addList = []; // 要添加的列表

    // 创建表格模态框
    createTableBox(
      "userTable",
      "授权用户",
      tableHead,
      tableData,
      (closeFun) => {
        updateUserRole(roleId, addList, deleteList);
        closeFun();
      },
      () => {
        let userRolesId = $.map(userRoles, function (r) {
          return r.id;
        });
        // 角色有的用户多选框要自动勾选
        $(`#userTable input[type='checkbox']`).each(function () {
          if ($.inArray(parseInt($(this).val()), userRolesId) !== -1) {
            $(this).prop("checked", "true");
          }
        });

        // 为多选框绑定事件
        checkBoxEvent("userTable", addList, deleteList);
      }
    );
  }

  // 为多选框绑定事件
  function checkBoxEvent(tableId, addList, deleteList) {
    $("#" + tableId + " input[type='checkbox']").click(function () {
      let id = $(this).val();
      if ($(this).is(":checked")) {
        // 多选框被勾选
        if ($.inArray(id, deleteList) !== -1) {
          // 如果deleteList中已经有该id，则从deleteList中删除
          deleteList = $.grep(deleteList, function (value) {
            return value != id;
          });
        } else {
          // 否则，将该id添加到addList中
          addList.push(id);
        }
      } else {
        // 多选框被取消勾选
        if ($.inArray(id, addList) !== -1) {
          // 如果addList中已经有该id，则从addList中删除
          addList = $.grep(addList, function (value) {
            return value != id;
          });
        } else {
          // 否则，将该id添加到deleteList中
          deleteList.push(id);
        }
      }
    });
  }

  // 修改用户角色关联
  function updateUserRole(roleId, addList, deleteList) {
    let data = {};
    data.roleId = roleId;
    data.addIds = addList;
    data.deleteIds = deleteList;
    $.ajax({
      url: ROUTE_USER_ROLE + "?action=update",
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
      },
    });
  }

  // 查询角色相关的菜单
  function roleMenuDetail(roleId) {
    // 查询角色菜单
    $.get(ROUTE_MENU + "?action=query", "roleId=" + roleId, (res) => {
      if (!res.success) {
        failMessageFloat(res.msg);
        return;
      }
      let roleMenus = res.data;
      // 查询所有菜单
      $.get(ROUTE_MENU + "?action=query", (res) => {
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        let menus = res.data;
        // 创建角色菜单模态框
        roleMenuDetailBox(roleId, roleMenus, menus);
      });
    });
  }

  // 角色相关菜单表单
  function roleMenuDetailBox(roleId, roleMenus, menus) {
    let addList = [];
    let deleteList = [];
    let originList = getIdsToArray(roleMenus);
    let confirmModal = confirmBox(
      "角色菜单",
      `<div id="roleMenu"></div>
      <div class="col-sm-offset-8 col-sm-3 btn-group-box">
          <button id="cancleBtn" type="button" class="btn btn-primary">取消</button>
          <button id="confirmBtn" type="button" class="btn btn-primary">确定</button>
      </div>`,
      1,
      false,
      false,
      null,
      null,
      () => {
        $("#confirmBtn").click(() => {
          // 点击确定后
          confirmBox("提示", "确定修改?", 0, true, true, () => {
            // 修改数据
            updateRoleMenu(roleId, addList, deleteList);
            // 关闭模态框
            $(confirmModal).modal("hide");
          });
        });

        $("#cancleBtn").click(() => {
          $(confirmModal).modal("hide");
        });

        // 模态框创建完后创建菜单树
        if (menus.length === 0) {
          $(`#roleMenu`).text("暂无菜单");
          return;
        }
        let treeData = convertToTreeDataOverride(originList, menus);
        $(`#roleMenu`).treeview({
          data: treeData,
          showCheckbox: true,
          onNodeChecked: function (event, node) {
            // 选中父节点
            checkParentNodes(node, addList, deleteList);
          },
          onNodeUnchecked: function (event, node) {
            let nodeId = $($(node.text)[0]).val();
            if ($.inArray(nodeId, addList) === -1) {
              deleteList.push(nodeId);
            } else {
              addList = $.grep(addList, function (value) {
                return value != nodeId;
              });
            }
            // 取消选中所有孩子节点
            uncheckAllChildNodes(node, addList, deleteList);
          },
        });

        // 
        $('#roleMenu').treeview('expandAll');
      }
    );
  }

  // 递归选中所有父节点
  function checkParentNodes(node, addList, deleteList) {
    let nodeId = $($(node.text)[0]).val();
    if ($.inArray(nodeId, deleteList) === -1) {
      addList.push(nodeId);
    } else {
      deleteList = $.grep(deleteList, function (value) {
        return value != nodeId;
      });
    }

    if (node.parentId) {
      // 通过父节点ID获取父节点
      var parentNode = $("#roleMenu").treeview("getNode", node.parentId);
      // 如果父节点已经选中, 则不用
      if (parentNode.state.checked) {
        return;
      }

      // 选中父节点
      $("#roleMenu").treeview("checkNode", [
        parentNode.nodeId,
        { silent: true },
      ]);

      // 递归选中更上层的父节点
      checkParentNodes(parentNode, addList, deleteList);
    }
  }

  // 递归取消选中所有子节点
  function uncheckAllChildNodes(node, addList, deleteList) {
    if (node.nodes) {
      node.nodes.forEach(function (childNode) {
        // 如果本来就没选中, 则直接下一个
        if (!childNode.state.checked) {
          return;
        }

        let nodeId = $($(childNode.text)[0]).val();
        if ($.inArray(nodeId, addList) === -1) {
          deleteList.push(nodeId);
        } else {
          addList = $.grep(addList, function (value) {
            return value != nodeId;
          });
        }

        // 取消选中子节点
        $("#roleMenu").treeview("uncheckNode", [
          childNode.nodeId,
          { silent: true },
        ]);

        // 递归取消选中更深层的子节点
        uncheckAllChildNodes(childNode, addList, deleteList);
      });
    }
  }

  function getIdsToArray(data) {
    let ids = [];
    $.each(data, function (index, value) {
      ids.push(value.id);
      if (value.children) {
        $.each(value.children, function (index, value) {
          ids.push(value.id);
          if (value.children) {
            $.each(value.children, function (index, value) {
              ids.push(value.id);
            });
          }
        });
      }
    });
    return ids;
  }

  function convertToTreeDataOverride(originList, menus) {
    return menus.map((item) => ({
      text:
        `<input type="hidden" value="` +
        item.id +
        `" parentId="` +
        item.parentId +
        `">` +
        item.name +
        `<span class="label label-info margin-5">` +
        item.children.length +
        `</span>
        <div class="btn-float-right">
          <button
            class="btn btn-info btn-sm btn-tree"
            onclick="event.stopPropagation(); menuDetail(` +
        item.id +
        `);"
          >
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
          </button>
        </div>`,
      nodes:
        item.children.length >= 1
          ? convertToTreeDataOverride(originList, item.children)
          : null,
      state: {
        checked: $.inArray(item.id, originList) !== -1,
        // checked: true,
      },
    }));
  }

  function updateRoleMenu(roleId, addList, deleteList) {
    let data = {};
    data.roleId = roleId;
    data.addIds = addList;
    data.deleteIds = deleteList;
    $.ajax({
      url: ROUTE_ROLE_MENU + "?action=update",
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
      },
    });
  }

  function menuDetail(menuId) {
    $.get(ROUTE_MENU + "?action=query", "menuId=" + menuId, (res) => {
      if (!res.success) {
        failMessageFloat(res.msg);
        return;
      }
      menu = res.data[0];
      menuDetailFormBox(menu, true);
    });
  }

  // 修改角色
  function updateRole(data) {
    $.ajax({
      url: ROUTE_ROLE + "?action=update",
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
        queryRoles(pageNo, keyword);
      },
    });
  }

  // 删除角色
  function deleteRolesBox(rolesId) {
    confirmBox("提示", "确定删除?", -1, true, true, () => {
      deleteRoles(rolesId);
    });
  }

  // 批量删除
  function batchDelete() {
    confirmBox("提示", "确定删除?", -1, true, true, () => {
      let checked = $(".select-row:checked");
      let rolesId = [];
      checked.each(function () {
        rolesId.push(parseInt($(this).val()));
      });
      deleteRoles(rolesId);
    });
  }

  // 删除角色
  function deleteRoles(rolesId) {
    let data = {};
    if (typeof rolesId === "number") {
      data.ids = [];
      data.ids.push(rolesId);
    } else {
      data.ids = rolesId;
    }
    let param = $.param(data).replaceAll("%5B%5D", "");
    $.post(ROUTE_ROLE + "?action=delete", param, (res) => {
      if (!res.success) {
        failMessageFloat(res.msg);
        return;
      }
      successMessageFloat(res.msg);
      queryRoles(pageNo, keyword);
    });
  }

  // 新增角色
  function addRole(data) {
    $.ajax({
      url: ROUTE_ROLE + "?action=add",
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
        queryRoles(pageNo, keyword);
      },
    });
  }

  // 新增角色的表单
  function addRoleFormBox() {
    createFormBox(
      "addRole",
      "新增角色",
      [
        {
          label: "角色名",
          type: "text",
          name: "name",
          required: true,
          placeholder: "角色名",
          reg: "^.{2,12}$",
          regTitle: "请输入2-12位的字符",
        },
        {
          label: "角色状态",
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
          placeholder: "角色状态",
        },
        {
          label: "角色描述",
          type: "textarea",
          name: "description",
          placeholder: "角色描述",
          required: false,
          rows: 4,
        },
      ],
      1,
      (data, closeFun) => {
        addRole(data);
        closeFun();
      }
    );
  }
</script>
