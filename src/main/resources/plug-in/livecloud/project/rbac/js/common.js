let pageNo;
let keyword;

function formatDate(timestamp, isDetail = true) {
  // 使用Date对象创建日期对象
  let date = isEmpty(timestamp) ? new Date() : new Date(timestamp);

  // 使用Date对象提供的方法获取日期的各个部分
  let year = date.getFullYear();
  let month = date.getMonth() + 1; // 注意：月份从0开始，需要加1
  let day = date.getDate();
  let hours = date.getHours();
  let minutes = date.getMinutes();
  let seconds = date.getSeconds();

  let formattedDate
  if (isDetail) {
    formattedDate = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
  } else {
    formattedDate = year + '-' + month + '-' + day;
  }

  // 格式化日期字符串

  return formattedDate;
}

function getURLParam(param) {
  var urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(param);
}

function getURLParams() {
  var params = {};
  var urlParams = new URLSearchParams(window.location.search);

  urlParams.forEach(function (value, key) {
    params[key] = value;
  });

  return params;
}


function changeURLParam(url, name, value) {
  if (typeof value === 'string') {
    value = value.toString().replace(/(^\s*)|(\s*$)/, ""); // 移除首尾空格
  }
  let url2;
  if (!value) { // remove
    let reg = eval('/(([\?|&])' + name + '=[^&]*)(&)?/i');
    let res = url.match(reg);
    if (res) {
      if (res[2] && res[2] === '?') { // before has ?
        if (res[3]) { // after has &
          url2 = url.replace(reg, '?');
        } else {
          url2 = url.replace(reg, '');
        }
      } else {
        url2 = url.replace(reg, '$3');
      }
    }
  } else {
    let reg = eval('/([\?|&]' + name + '=)[^&]*/i');
    if (url.match(reg)) { // edit
      url2 = url.replace(reg, '$1' + value);
    } else { // add
      let reg = /([?](\w+=?)?)[^&]*/i;
      let res = url.match(reg);
      url2 = url;
      if (res) {
        if (res[0] !== '?') {
          url2 += '&';
        }
      } else {
        url2 += '?';
      }
      url2 += name + '=' + value;
    }
  }
  return url2;
}


function changeURLStatic(name, value) {
  let url = changeURLParam(location.href, name, value); // 修改 URL 参数
  history.replaceState(null, null, url); // 替换地址栏
}

function isEmpty(v) {
  return typeof (v) === "undefined" || v === null || v === "";
}

// 将数据转换成二维数组便于表格展示
function changeJsonToArr(properties, data) {
  return data.map((obj) =>
    properties.map((prop) => {
      if (prop === "birthDay") {
        return isEmpty(obj[prop]) ? "null" : formatDate(obj[prop], false);
      }
      if (prop === "createTime" || prop === "updateTime") {
        return formatDate(obj[prop], false);
      }
      if (prop === "sex") {
        return obj[prop] === 0 ? "男" : "女";
      }
      if (prop === "state") {
        return obj[prop] === 0 ? "可用" : "禁用";
      }
      return obj[prop];
    })
  );
}