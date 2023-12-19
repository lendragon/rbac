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

  // 将用户的数据转换成二维数组便于表格展示
  function changeJsonToArr(data) {
    let properties = [
      "id",
      "name",
      "description",
      "createTime",
      "updateTime",
    ];
    return data.map((obj) =>
      properties.map((prop) => {
        if (prop === "createTime" || prop === "updateTime") {
          return formatDate(obj[prop], false);
        }
        return obj[prop];
      })
    );
  }

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
        // 创建表格
        createTable("#roleData", "role", changeJsonToArr(roles));

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
        rolesId.push(parseInt($(this).parent().next().text()));
      });
      deleteRoles(rolesId);
    });
  }

  // 删除角色
  function deleteRoles(rolesId) {
    let data = {};
    if (typeof rolesId === "number") {
      data.id = [];
      data.id.push(rolesId);
    } else {
      data.id = rolesId;
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
