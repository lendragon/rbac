/* ===== 浮动消息 ===== */
function messageFloat(type, title, message) {
  let alertBox = $(`
    <div class="alert alert-${type} center-fade">
      <strong>${title}</strong>${message}
    </div>`);
  $("body").append(alertBox);
  setTimeout(function () {
    alertBox.fadeOut("slow", function () {
      alertBox.remove();
    });
  }, 800); // 延迟3秒后隐藏警告框
}

function successMessageFloat(message) {
  messageFloat("success", "成功！", message);
}

function warnMessageFloat(message) {
  messageFloat("warning", "警告！", message);
}

function failMessageFloat(message) {
  messageFloat("danger", "失败！", message);
}

function infoMessageFloat(message) {
  messageFloat("info", "信息！", message);
}

/* ===== 确认框 ===== */
function confirmBox(title, message, size, disConfirmBtn = true, disCancleBtn = true, confirmFun, cancleFun, createdFun) {
  let confirmModal = document.createElement("div");
  confirmModal.className = "modal modal-vertical-center";
  confirmModal.innerHTML = `
    <div class="modal-dialog ${size === 1 ? "modal-lg" : (size === -1 ? "modal-sm" : "")} modal-dialog-vertical-center">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">
            &times;
          </button>
          <h4 class="modal-title">${title}</h4>
        </div>
        <div class="modal-body">${message}</div>
        ${!disConfirmBtn && !disCancleBtn ? "" : (
        '<div class="modal-footer">' + 
        (disConfirmBtn ? '<button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>' : "") + 
        (disCancleBtn ? '<button type="button" class="btn btn-primary" data-dismiss="modal">确认</button>' : "") +
        '</div>')}
      </div>
    </div>`;


  document.body.appendChild(confirmModal);
  if (disConfirmBtn && typeof (confirmFun) === "function") {
    let confirmBtn = confirmModal.querySelector(".modal-footer .btn-primary");
    confirmBtn.addEventListener("click", confirmFun);
  }
  if (disCancleBtn && typeof (confirmFun) === "function") {
    let cancelBtn = confirmModal.querySelector(".modal-footer .btn-secondary");
    cancelBtn.addEventListener("click", cancleFun);
  }

  $(confirmModal).modal("show");

  $(confirmModal).on("hidden.bs.modal", function () {
    document.body.removeChild(confirmModal);
  });

  if (typeof createdFun === "function") {
    createdFun();
  }
  return confirmModal;
}


/* ===== 消息框 ===== */
function messageBox(title, message, size, confirmFun) {
  let confirmModal = document.createElement("div");
  confirmModal.className = "modal modal-vertical-center";
  confirmModal.innerHTML = `
    <div class="modal-dialog ${size === 1 ? "modal-lg" : (size === -1 ? "modal-sm" : "")} modal-dialog-vertical-center">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">
            &times;
          </button>
          <h4 class="modal-title">${title}</h4>
        </div>
        <div class="modal-body">${message}</div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" data-dismiss="modal"">确认</button>
        </div>
      </div>
    </div>`;

  document.body.appendChild(confirmModal);
  let confirmBtn = confirmModal.querySelector(".modal-footer .btn-primary");
  confirmModal.addEventListener("click", confirmFun);

  $(confirmModal).modal("show");

  $(confirmModal).on("hidden.bs.modal", function () {
    document.body.removeChild(confirmModal);
  });
}


/* ===== 表格多选框 ===== */
// 全选/取消全选
function tableCheckBoxAll() {
  $(".select-row").each(function () {
    $(this).prop("checked", $("#select-all").prop("checked"));
  });
  $("#batchDeleteBtn").prop("disabled", !$(this).prop("checked"));
}

function tableCheckBox() {
  let allChecked = true;
  let noneChecked = true;

  // 检查每个多选框的状态
  $(".select-row").each(function () {
    if ($(this).prop("checked")) {
      noneChecked = false;
    } else {
      allChecked = false;
    }
  });

  // 根据多选框的状态设置全选框的状态
  if (allChecked) {
    $("#select-all").prop("checked", true);
    $("#batchDeleteBtn").prop("disabled", false);
  } else if (noneChecked) {
    $("#select-all").prop("checked", false);
    $("#select-all").prop("indeterminate", false);
    $("#batchDeleteBtn").prop("disabled", true);
  } else {
    // 半选
    $("#select-all").prop("checked", false);
    $("#select-all").prop("indeterminate", true);
    $("#batchDeleteBtn").prop("disabled", false);
  }
}

function tableCheckBoxEvent() {
  $("#select-all").click(tableCheckBoxAll);
  $(".select-row").click(tableCheckBox);
}



/* ===== 搜索框 ===== */
function searchFormEvent(queryFun) {
  $("#searchForm").submit(function (event) {
    // 阻止表单的默认提交行为
    event.preventDefault();

    // 查询， 并展示
    if (typeof (queryFun) === "function") {
      keyword = $('#searchForm input[type="text"]').val();
      pageNo = 1;
      queryFun(pageNo, pageSize, keyword);
    }
  });
}


/* ===== 分页组件 ===== */
// 创建分页
function createPageBtn(total, disLastNext, queryFunName, ulId = "pageBtnUl", lastId = "lastPageBtn", nextId = "nextPageBtn", ) {
  total = total < 1 ? 1 : total;
  let btnHtml = "";
  if (disLastNext) {
    btnHtml += `
      <li id="${lastId}">
        <a href="#" aria-label="Previous">
          <span aria-hidden="true">&laquo;</span>
        </a>
      </li>`;

  }
  for (let i = 0; i < total; i++) {
    btnHtml += `
      <li class="pageBtn" onclick="togglePageBtn(event, ${i + 1}, ${queryFunName})">
        <a href="#">${i + 1}</a>
      </li>`;
  }
  if (disLastNext) {
    btnHtml += `
      <li id="${nextId}">
        <a href="#" aria-label="Next">
          <span aria-hidden="true">&raquo;</span>
        </a>
      </li>`
  }
  $(`#${ulId}`).html(btnHtml);
  $($(".pageBtn")[pageNo - 1]).addClass("active")
  // 绑定监听事件
  $(`#${nextId}`).click(() => {
    nextTogglePageBtn(window[queryFunName])
  });
  $(`#${lastId}`).click(() => {
    lastTogglePageBtn(window[queryFunName])
  });
}

function togglePageBtn(event, pageNoTmp, queryFun) {
  let activeObject = $(".pageBtn.active")[0];
  let object = $(".pageBtn")[pageNoTmp - 1];
  if (object === activeObject) {
    return;
  }

  activeObject.classList.remove("active");
  object.classList.add("active");
  pageNo = pageNoTmp;
  if (typeof (queryFun) === "function") {
    queryFun(pageNoTmp, pageSize, keyword);
  }
}

// 下一个按钮点击事件
function nextTogglePageBtn(queryFun) {
  if (pageNo >= $(".pageBtn").length) {
    return;
  }
  togglePageBtn(event, pageNo + 1, queryFun);
}

// 上一个按钮点击事件
function lastTogglePageBtn(queryFun) {
  if (pageNo <= 1) {
    return;
  }
  togglePageBtn(event, pageNo - 1, queryFun);
}


/* ===== 自定义创建表单模态框组件 ===== */
function createFormBox(formId, title, formData, size, submitFun, btnName, createdFun) {
  // 创建表单html
  let formHtml = createFormHtml(formId, formData, btnName);
  // 展示确认表单框
  let confirmModal = confirmBox(title, formHtml, size, false, false);

  // 正则表达式匹配所有需要匹配的元素
  $(`#${formId} input`).on('input', function () {
    let input = $(this);
    let formGroup = input.closest('.form-group');
    let pattern = new RegExp(input.attr('regPattern'));

    if (pattern.test(input.val())) {
      formGroup.removeClass('has-error');
      formGroup.addClass('has-success');
      input.next().text("");
    } else {
      formGroup.removeClass('has-success');
      formGroup.addClass('has-error');
      input.next().text(input.attr('regTitle'));
    }
  });

  // 绑定提交按钮事件
  $(`#${formId}`).submit(function (event) {
    // 阻止表单的默认提交行为
    event.preventDefault();

    // 必填字段须填
    let canSubmit = true;
    $(`#${formId} input[isRequired]`).toArray().forEach(function (elem) {
      let elemJQ = $(elem);
      if (elemJQ.val() === "") {
        elemJQ.closest('.form-group').addClass('has-error');
        elemJQ.next().text("请填写此字段");
        canSubmit = false;
      }
    });

    // 所有正则表达式需通过
    if ($(`#${formId} input`).next().text() !== "") {
      canSubmit = false;
    }

    if (!canSubmit) {
      return;
    }

    // 提交表单
    // 将所有的disabled的input全部开启
    let disabledInput = $('input[disabled]');
    disabledInput.removeAttr('disabled');
    let formData = $(this).serializeArray()
    disabledInput.prop('disabled', 'disabled');

    let jsonData = {};
    $.each(formData, function () {
      jsonData[this.name] = this.value === "" ? null : this.value;
    });
    submitFun(jsonData, () => {
      // 关闭窗口
      $(confirmModal).modal("hide");
      $("body > .datetimepicker").remove();
    });


  });
  // 绑定日期选择器
  dateChooseEvent(".date-choose-input");

  // 表单创建完成后事件
  if (typeof createdFun === "function") {
    createdFun();
  }
}

/**
 * 
 * @param {*} id 
 * @param {*} formArr 
 *  格式: 
 *    label: 标签名字
 *    placeholder: 显示的提示文字
 *    name: 名字, 传递给后端的参数
 *    type: 类型(text, password, radio, checkbox, date, textarea, hidden)
 *    disabled: boolean, 是否禁用
 *    value: 默认值
 *    options: 选项名和选项内容(当type为radio和checkbox时生效)
 *      name: 选项名
 *      value: 选项值
 *      checked: 是否默认选中
 *    rows: 当type为textarea时生效, 支持多少行
 *    required: boolean, 是否必填
 *    reg: 正则表达式验证
 *    regTitle: 正则表达式没通过时报的错误
 */
function createFormHtml(formId, formArr, btnName = "确定") {
  let formHtml = `<form id="${formId}" class="form-horizontal">`;
  formArr.forEach((elem) => {
    formHtml += `
      <div class="form-group">`
    if (elem.type !== "hidden") {
      formHtml += `<label for="${elem.name}Input" class="col-sm-2 control-label">${elem.required ? '<span class="red">*</span>' : ""}${elem.label}</label>`
    }
    formHtml += `<div class="col-sm-9">`;
    if (elem.type === "text" || elem.type === "password" || elem.type === "number") {
      formHtml += `
        <input id="${elem.name}Input" name="${elem.name}" type="${elem.type}" class="form-control"
         ${elem.required ? "isRequired" : ""}
         ${isEmpty(elem.reg) ? "" : 'regPattern="' + elem.reg + '"'} 
         ${isEmpty(elem.regTitle) ? "" : 'regTitle="' + elem.regTitle + '"'} 
         placeholder="${isEmpty(elem.placeholder) ? "" : elem.placeholder}" 
         ${elem.disabled ? "disabled" : ""} 
         value="${isEmpty(elem.value) ? "" : elem.value}" >`
    } else if (elem.type === "checkbox" || elem.type === "radio") {
      elem.options.forEach((option) => {
        formHtml += `
          <label class="${elem.type}-inline">
            <input type="${elem.type}" name="${elem.name}" value="${option.value}" ${option.checked ? "checked" : ""} ${elem.disabled ? "disabled" : ""}> ${option.name}
          </label>`
      })
    } else if (elem.type === "textarea") {
      formHtml += `
        <textarea id="${elem.name}Input" name="${elem.name}" class="form-control" rows="${elem.rows}"
         ${elem.required ? "isRequired" : ""}
         ${isEmpty(elem.reg) ? "" : 'regPattern="' + elem.reg + '"'} 
         ${isEmpty(elem.regTitle) ? "" : 'regTitle="' + elem.regTitle + '"'} 
         placeholder="${isEmpty(elem.placeholder) ? "" : elem.placeholder}" 
         ${elem.disabled ? "disabled" : ""} >${isEmpty(elem.value) ? "" : elem.value}</textarea>`
    } else if (elem.type === "hidden") {
      formHtml += `
      <input id="${elem.name}Input" name="${elem.name}" type="${elem.type}"
       value="${isEmpty(elem.value) ? "" : elem.value}" >`
    } else { // date
      formHtml += `
        <input name="${elem.name}" type="text" class="form-control date-choose-input"  ${elem.required ? "required" : ""} 
        placeholder="${isEmpty(elem.placeholder) ? "" : elem.placeholder}"
        ${elem.disabled ? "disabled" : ""} 
        value="${isEmpty(elem.value) ? "" : elem.value}">`
    }

    formHtml += `
        <div class="error-text"></div>
      </div>
    </div>
    `;
  })
  formHtml += `
      <div class="form-group">
        <div class="col-sm-offset-8 col-sm-3">
          <button type="submit" class="btn btn-primary btn-block">${btnName}</button>
        </div>
      </div>
    </form>
  `

  return formHtml;
}

function passwordChanged(event, object, originalPassword) {
  let newPassword = $(object).val();

  // 检查密码是否发生变化
  // 检查密码是否发生变化
  let isPasswordChanged = (newPassword !== originalPassword);

  // 存储变化状态
  $(object).data('isPasswordChanged', isPasswordChanged);
}

// 日期选择器绑定
function dateChooseEvent(elemCss) {
  $(`${elemCss}`).datetimepicker({
    language: 'zh-CN',
    timepicker: false, // 禁用时间选择
    format: 'yyyy-mm-dd', // 日期格式
    autoclose: true, // 选择后自动关闭
    minView: "month", //设置选择时 只显示到月份 ----day， year --不设置默认带时间 秒   日历的显示时间
    maxDate: new Date(), // 最多只能选到今天
    todayHighlight: true // 当天高亮显示
  });
}


/* ===== 自定义菜单树 ===== */
function createTree(id, data, hasBtn = true, hasCheckBox = true, hasDetail = true, hasAdd = true, hasDelete = true, noneDataDis = "暂无菜单") {
  if (data.length === 0) {
    $(`#${id}`).text(noneDataDis);
    return;
  }
  let treeData = convertToTreeData(data, hasBtn, hasCheckBox, hasDetail, hasAdd, hasDelete);
  $(`#${id}`).treeview({
    data: treeData,
    showCheckbox: hasCheckBox,
  });
  $(`#${id}`).treeview('expandAll');
}

function convertToTreeData(data, hasBtn, hasCheckBox = true, detailBtn = true, addBtn = true, deleteBtn = true) {
  return data.map((item) => ({
    text: (hasCheckBox ? `<input type="hidden" value="${item.menuId}" parentId="${item.parentId}">` : "") +
      item.name + `<span class="label label-info margin-5">${item.children.length}</span>` +
      (hasBtn ? treeBtnHtml(item, detailBtn, addBtn, deleteBtn) : ""),
    nodes: item.children.length >= 1 ? convertToTreeData(item.children, hasBtn, hasCheckBox, detailBtn, addBtn, deleteBtn) : null,

  }));
}

function treeBtnHtml(elem, detailBtn = true, addBtn = true, deleteBtn = true) {
  let treeBtn = `<div class="btn-float-right">`;
  if (detailBtn) {
    treeBtn += `
      <button
        class="btn btn-info btn-sm btn-tree"
        onclick="event.stopPropagation(); menuDetail(${elem.menuId});"
      >
        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
      </button>`;
  }
  if (addBtn) {
    treeBtn += `<button
        class="btn btn-primary btn-sm btn-tree"
        onclick="event.stopPropagation(); addChild(${elem.menuId});"
      >
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
      </button>`
  }
  if (deleteBtn) {
    treeBtn += `<button
      class="btn btn-danger btn-sm btn-tree"
      onclick="event.stopPropagation(); deleteMenusBox(${elem.menuId});"
    > 
      <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
      </button>`;
  }
  treeBtn += `</div>`;

  return treeBtn;
}

/* ===== 自定义表格 ===== */
function createTable(selector, menuName, data, hasBtn = true, btnDataIndex = 0) {
  let displayHtml = "";
  data.forEach((items) => {
    displayHtml +=
      `<tr>
        <td><input type="checkbox" class="select-row" value="${items[0]}" /></td>`
    for (let i = 1; i < items.length; i++) {
      displayHtml += `<td>${items[i]}</td>`;
    }
    if (hasBtn) {
      displayHtml +=
        `<td>
          <button
            class="btn btn-link btn-no-underline btn-text-size"
            onclick="${menuName}Detail(${items[btnDataIndex]})"
          >
          详情
          </button>`
      if (menuName === "user") {
        displayHtml +=
          `<button
            class="btn btn-link btn-no-underline btn-text-size"
            onclick="userQueryRole(${items[btnDataIndex]})"
          >
            角色
          </button>
          <button
            class="btn btn-link btn-no-underline btn-text-size"
            onclick="userMenuDetail(${items[btnDataIndex]})"
          >
            菜单
          </button>`
      } else if (menuName === "role") {
        displayHtml +=
          `<button
            class="btn btn-link btn-no-underline btn-text-size"
            onclick="roleQueryUser(${items[btnDataIndex]})"
          >
            用户
          </button>
          <button
            class="btn btn-link btn-no-underline btn-text-size"
            onclick="roleMenuDetail(${items[btnDataIndex]})"
          >
            菜单
          </button>`
      }

      displayHtml +=
        `<button
            class="btn btn-link btn-no-underline btn-text-size"
            onclick="delete${capitalizeFirstLetter(menuName)}sBox(${items[btnDataIndex]})"
          >
            删除
          </button>
        </td>
      </tr>`
    }

  });
  $(selector).html(displayHtml);

  // 创建分页组件
}

// 首字母大写
function capitalizeFirstLetter(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}


/* ===== 自定义菜单树模态框 ===== */
function createTreeBox(id, title, treeData, hasBtn = true, hasCheckBox = true, hasDetail = true, hasAdd = true, hasDelete = true) {
  let menuTree = `<div id="${id}"></div>`;
  confirmBox(title, menuTree, 1, false, true, null, null, () => {
    createTree(id, treeData, hasBtn, hasCheckBox, hasDetail, hasAdd, hasDelete);
    // 给模态框限制高度， 并添加滚动条
    $(`#${id}`).css({
      "height": "350px",
      "overflow-y": "auto",
      "font-size": "16px",
    });
  });
}



function menuDetailFormBox(menu, readonly = false) {
  createFormBox(
    "menuDetail",
    "菜单详情",
    [{
        label: "ID",
        type: "hidden",
        name: "menuId",
        value: menu.menuId,
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
        disabled: readonly
      },
      {
        label: "菜单显示顺序",
        type: "number",
        name: "order",
        required: true,
        placeholder: "菜单显示顺序",
        reg: "^$|^[1-9]\\d*$",
        regTitle: "请输入有效数字",
        value: menu.order,
        disabled: readonly
      },
      {
        label: "菜单层级",
        type: "number",
        name: "level",
        required: true,
        reg: "^$|^[1-9]\\d*$",
        regTitle: "请输入有效数字",
        placeholder: "菜单层级",
        value: menu.level,
        disabled: true
      },
      {
        label: "父菜单id",
        type: "number",
        name: "parentId",
        required: true,
        reg: "^$|^\\d*$",
        regTitle: "请输入有效数字",
        placeholder: "父菜单id",
        value: menu.parentId,
        disabled: readonly
      },
      {
        label: "菜单路径",
        type: "text",
        name: "url",
        required: false,
        placeholder: "菜单路径",
        value: menu.url,
        disabled: readonly
      },
      {
        label: "菜单状态",
        type: "radio",
        name: "state",
        required: true,
        disabled: readonly,
        options: [{
            name: "正常",
            value: "0",
            checked: menu.state === 0,
          },
          {
            name: "禁用",
            value: "1",
            checked: menu.state === 1,
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
        value: menu.description,
        disabled: readonly
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
      if (readonly) {
        closeFun();
        return;
      }
      confirmBox("提示", "确定修改吗?", 0, true, true, () => {
        updateMenu(data);
        closeFun();
      });
    },
    "修改"
  );
}

/* ===== 自定义创建表格模态框组件 ===== */
function createTableBox(tableId, title, tableHead, tableData, confirmFun, createdFun) {
  // 创建表头html
  let tableHtml =
    `<table class="table table-striped table-hover">
      <thead>
        <tr><th></th>`;
  tableHead.forEach((item) => {
    tableHtml += `<th>${item}</th>`
  });
  tableHtml +=
    `</tr>
      </thead>
      <tbody id="${tableId}"></tbody>
    </table>
    <nav aria-label="Page navigation">
      <ul id="tableBtnUl" class="pagination"></ul>
    </nav>
    <div class="col-sm-offset-8 col-sm-3 btn-group-box">
          <button id="cancleBtn" type="button" class="btn btn-primary">取消</button>
          <button id="confirmBtn" type="button" class="btn btn-primary">确定</button>
    </div>`;
  // 展示确认框
  let confirmModal = confirmBox(title, tableHtml, 1, false, false, null, null, () => {
    // 展示表格数据
    createTable(`#${tableId}`, null, tableData, false);
    // 为按钮绑定事件
    $("#confirmBtn").click(() => {
      confirmFun(() => {
        $(confirmModal).modal("hide");
      });
    });
    $("#cancleBtn").click(() => {
      $(confirmModal).modal("hide");
    });
    // 创建分页组件
    createPageBtn(1, true, "queryUsers", "tableBtnUl", "TableLastPageBtn", "TableNextPageBtn");
  });

  // 表格创建完成后事件
  if (typeof createdFun === "function") {
    createdFun();
  }
}