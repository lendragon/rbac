<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 上部按钮和搜索框 -->
<div class="row">
  <div class="col-md-9">
    <button id="addUserBtn" class="btn btn-primary">新增</button>
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

<!-- 展示用户数据的表格 -->
<table class="table table-striped table-hover">
  <thead>
    <tr>
      <th><input type="checkbox" id="select-all" /></th>
      <th>用户标识</th>
      <th>用户名</th>
      <th>性别</th>
      <th>出生日期</th>
      <th>手机号</th>
      <th>用户状态</th>
      <th>创建时间</th>
      <th>修改时间</th>
      <th class="col-xs-2">操作</th>
    </tr>
  </thead>
  <tbody id="userData"></tbody>
</table>

<!-- 底层分页组件 -->
<nav aria-label="Page navigation">
  <ul id="pageBtnUl" class="pagination"></ul>
</nav>

<script>
  $(() => {
    pageNo = isEmpty(getURLParam("pageNo")) ? 1 : pageNo;
    keyword = getURLParam("keyword");
    queryUsers(pageNo, pageSize, keyword);
    // 监听搜索框提交事件
    searchFormEvent(queryUsers);
    $("#addUserBtn").click(addUserFormBox);
  });

  // 查询用户
  function queryUsers(pageNo = 0, pageSize = 20, keyword) {
    let params = PARAM_ACTION_QUERY_ALL;
    if (!isEmpty(pageNo)) {
      params += "&pageNo=" + pageNo;
    }
    if (!isEmpty(pageSize)) {
      params += "&pageSize=" + pageSize;
    }
    if (!isEmpty(keyword)) {
      params += "&keyword=" + keyword;
    }

    $.get(
      ROUTE_USER,
      params,
      (res) => {
        // 如果请求失败
        if (!res.success) {
          $("#userData").html(res.msg);
          return;
        }

        // 请求成功
        let users = res.data.records;
        let total = res.data.total;
        let pageSize = res.data.pageSize;
        let properties = [
          "id",
          "userName",
          "name",
          "sex",
          "birthDay",
          "phoneNum",
          "state",
          "createTime",
          "updateTime",
        ];
        // 创建表格
        createTable("#userData", "user", changeJsonToArr(properties, users));

        // 创建分页组件
        createPageBtn(Math.ceil(total / pageSize), true, "queryUsers");

        // 给多选框绑定
        tableCheckBoxEvent();
      },
      "json"
    );
  }

  // 查看用户详情
  function userDetail(userId) {
    let userData;
    $.get(
      ROUTE_USER + "?" + PARAM_ACTION_QUERY_BY_USER_ID,
      "id=" + userId,
      (res) => {
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        user = res.data;
        userDetailFormBox(user);
      }
    );
  }

  function userDetailFormBox(user) {
    // 用户数据格式
    let userFormData = [
      {
        label: "用户主键",
        type: "hidden",
        name: "id",
        value: user.id,
      },
      {
        label: "用户标识",
        type: "text",
        name: "userName",
        required: true,
        placeholder: "用户标识",
        reg: "^[0-9A-Za-z_]{5,16}$",
        regTitle: "请输入5-16位字母、数字或下划线",
        disabled: true,
        value: user.userName,
      },
      {
        label: "用户名",
        type: "text",
        name: "name",
        required: true,
        placeholder: "用户名",
        reg: "^.{2,12}$",
        regTitle: "请输入2-12位的字符",
        value: user.name,
      },
      {
        label: "性别",
        type: "radio",
        name: "sex",
        required: true,
        options: [
          {
            name: "男",
            value: "1",
            checked: user.sex === 1,
          },
          {
            name: "女",
            value: "0",
            checked: user.sex === 0,
          },
        ],
        placeholder: "性别",
      },
      {
        label: "出生日期",
        type: "date",
        name: "birthDay",
        required: false,
        placeholder: "出生日期",
        value: isEmpty(user.birthDay) ? "" : formatDate(user.birthDay, false),
      },
      {
        label: "电话",
        type: "text",
        name: "phoneNum",
        required: false,
        placeholder: "电话",
        reg: "^$|^[0-9]{7,15}$",
        regTitle: "请输入有效电话号码",
        value: user.phoneNum == null ? "" : user.phoneNum,
      },
      {
        label: "用户状态",
        type: "radio",
        name: "state",
        required: true,
        options: [
          {
            name: "正常",
            value: "1",
            checked: user.state === 1,
          },
          {
            name: "禁用",
            value: "0",
            checked: user.state === 0,
          },
        ],
        placeholder: "用户状态",
      },
      {
        label: "创建时间",
        type: "date",
        name: "createTime",
        required: false,
        disabled: true,
        value: formatDate(user.createTime, false),
      },
      {
        label: "修改时间",
        type: "date",
        name: "updateTime",
        required: false,
        disabled: true,
        value: formatDate(user.updateTime, false),
      },
    ];

    // 创建表单模态框
    createFormBox(
      "userDetail",
      "用户详情",
      userFormData,
      1,
      (data, closeFun) => {
        confirmBox("提示", "确定修改吗?", 0, true, true, () => {
          updateUser(data);
          closeFun();
        });
      },
      "修改"
    );
  }

  // 查看用户角色
  function userQueryRole(userId) {
    // 获取用户对应的角色
    $.get(
      ROUTE_USER_ROLE + "?" + PARAM_ACTION_QUERY_BY_USER_ID,
      "id=" + userId,
      (res) => {
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        let roles = res.data;
        // 展示模态框
        userQueryRoleFormBox(userId, roles);
      },
      "json"
    );
  }

  function userQueryRoleFormBox(userId, roles) {
    let tableHead = [
      "角色标识",
      "角色名",
      "角色状态",
      "角色描述"
    ];
    let properties = [
      "id",
      "roleName",
      "name",
      "state",
      "description",
    ];
    let tableData = changeJsonToArr(properties, roles);

    // 创建表格模态框
    createTableBox(
      "roleTable",
      "拥有角色",
      tableHead,
      tableData,
      (closeFun) => {
        closeFun();
      }
    );
  }

  // 查看用户菜单
  function userMenuDetail(userId) {
    $.get(
      ROUTE_USER_MENU + "?" + PARAM_ACTION_QUERY_BY_USER_ID,
      "id=" + userId,
      (res) => {
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        userMenuDetailBox(res.data);
      }
    );
  }

  function menuDetail(menuId) {
    $.get(
      ROUTE_MENU + "?" + PARAM_ACTION_QUERY_BY_MENU_ID,
      "id=" + menuId,
      (res) => {
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        menu = res.data;
        menuDetailFormBox(menu, true);
      }
    );
  }

  // 查看用户相关菜单模态框
  function userMenuDetailBox(menu) {
    createTreeBox(
      "menuTree",
      "用户菜单",
      menu,
      true,
      false,
      true,
      false,
      false
    );
  }

  // 修改用户
  function updateUser(data) {
    $.ajax({
      url: ROUTE_USER + "?" + PARAM_ACTION_UPDATE,
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
        queryUsers(pageNo, pageSize, keyword);
      },
    });
  }

  // 修改用户密码模态框
  function updatePasswordFormBox(userId) {
    let formData = [
      {
        label: "用户主键",
        type: "hidden",
        name: "id",
        value: userId,
      },
      {
        label: "旧密码",
        type: "password",
        name: "oldPassword",
        required: true,
        placeholder: "请输入旧密码",
      },
      {
        label: "新密码",
        type: "password",
        name: "password",
        required: true,
        placeholder: "请输入新密码",
        reg: "^[^\u4e00-\u9fa5]{4,16}$",
        regTitle: "请输入4-16位的数字、字母或特殊字符",
      },
      {
        label: "确认密码",
        type: "password",
        name: "checkedPassword",
        required: true,
        placeholder: "请再次输入密码",
        reg: "^[^\u4e00-\u9fa5]{4,16}$",
        regTitle: "请输入4-16位的数字、字母或特殊字符",
      },
    ];
    createFormBox(
      "updatePassword",
      "修改密码",
      formData,
      1,
      (data, closeFun) => {
        updatePassword(data);
        closeFun();
      },
      "修改"
    );
  }

  // 修改密码
  function updatePassword(data) {
    // data.oldPassword = CryptoJS.MD5(data.oldPassword + "randomSalt").toString();
    // data.password = CryptoJS.MD5(data.password).toString();
    $.ajax({
      url: ROUTE_USER + "?" + PARAM_ACTION_UPDATE_PASSWORD,
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

  // 删除用户
  function deleteUsersBox(usersId) {
    confirmBox("提示", "确定删除?", -1, true, true, () => {
      deleteUsers(usersId);
    });
  }

  // 批量删除
  function batchDelete() {
    confirmBox("提示", "确定删除?", -1, true, true, () => {
      let checked = $(".select-row:checked");
      let usersId = [];
      checked.each(function () {
        usersId.push(parseInt($(this).val()));
      });
      deleteUsers(usersId);
    });
  }

  // 删除用户
  function deleteUsers(userId) {
    $.ajax({
      url: ROUTE_USER + "?" + PARAM_ACTION_DELETE,
      type: "POST",
      data: "id=" + userId,
      success: function (res) {
        // 请求成功后的回调函数
        if (!res.success) {
          failMessageFloat(res.msg);
          return;
        }
        successMessageFloat(res.msg);
        queryUsers(pageNo, pageSize, keyword);
      },
    });
  }

  // 新增用户
  function addUser(data) {
    $.ajax({
      url: ROUTE_USER + "?" + PARAM_ACTION_ADD,
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
        queryUsers(pageNo, pageSize, keyword);
      },
    });
  }

  // 新增用户的表单
  function addUserFormBox() {
    let userFormData = [
      {
        label: "用户标识",
        type: "text",
        name: "userName",
        required: true,
        placeholder: "用户标识",
        reg: "^[0-9A-Za-z_]{5,16}$",
        regTitle: "请输入5-16位字母、数字或下划线",
      },
      {
        label: "用户名",
        type: "text",
        name: "name",
        required: true,
        placeholder: "用户名",
        reg: "^.{2,12}$",
        regTitle: "请输入2-12位的字符",
      },
      {
        label: "性别",
        type: "radio",
        name: "sex",
        required: true,
        options: [
          {
            name: "男",
            value: "1",
          },
          {
            name: "女",
            value: "0",
          },
        ],
        placeholder: "性别",
      },
      {
        label: "出生日期",
        type: "date",
        name: "birthDay",
        required: false,
        placeholder: "出生日期",
      },
      {
        label: "电话",
        type: "text",
        name: "phoneNum",
        required: false,
        placeholder: "电话",
        reg: "^$|^[0-9]{7,15}$",
        regTitle: "请输入有效电话号码",
      },
    ];

    // 创建表单模态框
    createFormBox("addUser", "新增用户", userFormData, 1, (data, closeFun) => {
      addUser(data);
      closeFun();
    });
  }
</script>
