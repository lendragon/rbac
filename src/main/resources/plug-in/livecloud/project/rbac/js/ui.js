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
function confirmBox(title, message, size, disConfirmBtn = true, disCancleBtn = true, confirmFun, cancleFun) {
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
  if (disConfirmBtn) {
    let confirmBtn = confirmModal.querySelector(".modal-footer .btn-primary");
    confirmBtn.addEventListener("click", confirmFun);
  }
  if (disCancleBtn) {
    let cancelBtn = confirmModal.querySelector(".modal-footer .btn-secondary");
    cancelBtn.addEventListener("click", cancleFun);
  }

  $(confirmModal).modal("show");

  $(confirmModal).on("hidden.bs.modal", function () {
    document.body.removeChild(confirmModal);
  });
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

    // 获取表单数据
    let formData = $(this).serialize();

    // 查询， 并展示
    if (typeof (queryFun) === "function") {
      keyword = $('#searchForm input[type="text"]').val();
      pageNo = 1;
      queryFun(pageNo, keyword);
    }
  });
}


/* ===== 分页组件 ===== */
// 创建分页
function createPageBtn(total, disLastNext, queryFunName) {
  total = total < 1 ? 1 : total;
  let btnHtml = "";
  if (disLastNext) {
    btnHtml += `
      <li id="lastPageBtn">
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
      <li id="nextPageBtn">
        <a href="#" aria-label="Next">
          <span aria-hidden="true">&raquo;</span>
        </a>
      </li>`
  }
  $("#pageBtnUl").html(btnHtml);
  $($(".pageBtn")[pageNo - 1]).addClass("active")
  // 绑定监听事件
  $("#nextPageBtn").click(nextTogglePageBtn);
  $("#lastPageBtn").click(lastTogglePageBtn);
}

// 分页组件点击切换
// function togglePageBtn(event, object, queryFun) {
//   let activeObject = $(".pageBtn.active");
//   if (object['0'] === activeObject['0']) {
//     return;
//   }
//   activeObject.removeClass("active");
//   object.addClass("active");
//   pageNo = object.text();
//   if (typeof (queryFun) === "function") {
//     queryFun(pageNo, keyword);
//   }
// }

// // 下一个按钮点击事件
// function nextTogglePageBtn() {
//   if (pageNo >= $(".pageBtn").length) {
//     return;
//   }
//   togglePageBtn(event, $(".pageBtn.active + .pageBtn"));
// }

// // 上一个按钮点击事件
// function lastTogglePageBtn() {
//   if (pageNo <= 1) {
//     return;
//   }
//   togglePageBtn(event, $($(".pageBtn")[pageNo - 2]));
// }

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
    queryFun(pageNo, keyword);
  }
}

// 下一个按钮点击事件
function nextTogglePageBtn() {
  if (pageNo >= $(".pageBtn").length) {
    return;
  }
  togglePageBtn(event, pageNo + 1);
}

// 上一个按钮点击事件
function lastTogglePageBtn() {
  if (pageNo <= 1) {
    return;
  }
  togglePageBtn(event, pageNo - 1);
}


/* ===== 自定义创建表单组件 ===== */
function createFormBox(formId, title, formData, size, submitFun, btnName) {
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
}

/**
 * 
 * @param {*} id 
 * @param {*} formArr 
 *  格式: 
 *    label: 标签名字
 *    placeholder: 显示的提示文字
 *    name: 名字, 传递给后端的参数
 *    type: 类型(text, password, radio, checkbox, date, textarea)
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
      <div class="form-group">
        <label for="${elem.name}Input" class="col-sm-2 control-label">${elem.required ? '<span class="red">*</span>' : ""}${elem.label}</label>
      <div class="col-sm-9">`;
    if (elem.type === "text" || elem.type === "password" || elem.type === "number") {
      formHtml += `
        <input id="${elem.name}Input" name="${elem.name}" type="${elem.type}" class="form-control"
         ${elem.required ? "isRequired" : ""}
         ${isEmpty(elem.reg) ? "" : 'regPattern="' + elem.reg + '"'} 
         ${isEmpty(elem.regTitle) ? "" : 'regTitle="' + elem.regTitle + '"'} 
         placeholder="${isEmpty(elem.placeholder) ? "" : elem.placeholder}" 
         ${elem.disabled ? "disabled" : ""} 
         value="${isEmpty(elem.value) ? "" : elem.value}">`
    } else if (elem.type === "checkbox" || elem.type === "radio") {
      elem.options.forEach((option) => {
        formHtml += `
          <label class="${elem.type}-inline">
            <input type="${elem.type}" name="${elem.name}" value="" ${option.checked ? "checked" : ""}> ${option.name}
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
function createTree(id, data) {
  let treeData = convertToTreeData(data);
  $(`#${id}`).treeview({
    data: treeData,
    // backColor: "green",    节点初始背景色
    // selectedBackColor: "green"  // 节点被选中时的背景色
    // selectedColor: "green"  // 节点被选中时的字体颜色
    // onNodeSelected: function (event, node) {
    //   console.log(node);
    // },
    // onNodeUnselected: function (event, node) {
    //   console.log(node);
    // }

    // $(".btn-tree").click((event) => {
    //   console.log(1);
    //   event.stopPropagation();  // 阻止事件冒泡
    // })
    showCheckbox: true,
    showTags: true,
    // searchResultColor: "#000",
    // searchResultBackColor: "#000",
    // multiSelect : true
  });
}

function convertToTreeData(data) {
  return data.map((item) => ({
    text: item.menu.name + `<span class="label label-info margin-5">${item.children.length}</span>` + treeBtnHtml(item.menu) + `<input type="hidden" value="${item.menu.id}">`,
    nodes: item.children.length >= 1 ? convertToTreeData(item.children) : null,
  }));
}

function treeBtnHtml(elem) {
  let treeBtn = `
    <div class="btn-float-right">
      <button
        class="btn btn-info btn-sm btn-tree"
        onclick="event.stopPropagation(); menuDetail(${elem.id});"
      >
        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
      </button>

      <button
        class="btn btn-primary btn-sm btn-tree"
        onclick="event.stopPropagation(); addChild(${elem.level}, ${elem.id});"
      >
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
      </button>
      <button
        class="btn btn-danger btn-sm btn-tree"
        onclick="event.stopPropagation(); deleteMenusBox(${elem.id});"
      >
        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
      </button>
    </div>`;
  return treeBtn;
}

/* ===== 自定义表格 ===== */
function createTable(selector, menuName, data, hasBtn = true, btnDataIndex = 0) {
  let displayHtml = "";
  data.forEach((items) => {
    // let keys = Object.keys(item);
    displayHtml +=
      `<tr>
        <td><input type="checkbox" class="select-row" /></td>
        <th scope="row">${items[0]}</th>`;
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
            查看详情
          </button>
          <button
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
}

function capitalizeFirstLetter(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}