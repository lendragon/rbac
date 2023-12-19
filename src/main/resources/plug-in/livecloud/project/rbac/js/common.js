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


function aesEncrypt(data, key, iv) {
  // 将密钥和向量转换为WordArray对象
  const keyBytes = CryptoJS.enc.Utf8.parse(key);
  const ivBytes = CryptoJS.enc.Utf8.parse(iv);

  // 加密
  const encrypted = CryptoJS.AES.encrypt(data, keyBytes, {
    iv: ivBytes,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  });

  // 返回加密后的数据，使用Base64编码
  return encrypted.toString();
}

// 随机生成一个16位的16进制数
function generateHex() {
  let hex = '';
  let characters = '0123456789ABCDEF';
  
  for (var i = 0; i < 16; i++) {
    let randomIndex = Math.floor(Math.random() * characters.length);
    hex += characters[randomIndex];
  }
  
  return hex;
}