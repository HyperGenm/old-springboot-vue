/**引入element-ui组件*/
import {Message, MessageBox, prompt} from 'element-ui';

/**引入base64*/
const Base64 = require('js-base64').Base64;

/**引入Md5*/
import Md5 from 'js-md5';

/**
 * 判断是否为空
 * @param str
 * @returns {boolean}
 */
function isBlank(str) {
    if (null == str) {
        return true;
    }
    return '' === ('' + str).replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 判断不是手机号
 * @param phone
 * @returns {boolean}
 */
function notPhone(phone) {
    return !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(phone));
}

/**
 * 错误警告
 * @param msg
 * @param time
 */
function errorMsg(msg = '失败', time = 3000) {
    Message({
        message: msg,
        type: 'error',
        duration: time
    });
}

/**
 * 成功提示
 * @param msg
 * @param time
 */
function successMsg(msg = '成功', time = 3000) {
    Message({
        message: msg,
        type: 'success',
        duration: time
    });
}

/**
 * 对话框
 * @param title  标题
 * @param message  内容
 * @param type  类型
 * @param center  居中布局
 * @param showCancelButton  是否显示取消按钮
 * @param cancelButtonText  取消按钮文字
 * @param confirmButtonText  确定按钮文字
 * @param confirm 确定回调
 * @param cancel 取消回调
 * @returns {Promise<MessageBoxData>}
 */
function messageBox({
                        title = '警告',
                        message = '',
                        type = 'warning',
                        center = false,
                        showCancelButton = true,
                        cancelButtonText = '取消',
                        confirmButtonText = '确定',
                        confirm = function () {

                        },
                        cancel = function () {

                        }
                    } = {}) {
    return MessageBox({
        title,
        message,
        type,
        center,
        showCancelButton,
        cancelButtonText,
        confirmButtonText,
        callback(action, instance) {
            if (action === 'confirm') {
                confirm(instance);
            } else {
                cancel(instance);
            }
        }
    });
}

/**
 * 对话框input输入框-----确定后需要调用done()关闭对话框
 * @param title  标题
 * @param message  内容
 * @param type  类型
 * @param center  居中布局
 * @param showCancelButton  是否显示取消按钮
 * @param cancelButtonText  取消按钮文字
 * @param confirmButtonText  确定按钮文字
 * @param inputType  输入框类型
 * @param confirm 确定回调
 * @returns {Promise<MessageBoxData>}
 */
function messageBoxInput({
                             title = '警告',
                             message = '',
                             type = 'warning',
                             center = false,
                             showCancelButton = true,
                             cancelButtonText = '取消',
                             confirmButtonText = '确定',
                             inputType = 'primary',
                             confirm = function () {

                             }
                         } = {}) {
    return MessageBox({
        title,
        message,
        type,
        center,
        showCancelButton,
        cancelButtonText,
        confirmButtonText,
        showInput: true,
        inputType,
        beforeClose(action, instance, done) {
            if (action === 'cancel') {
                done();
                return;
            }
            confirm(instance.inputValue, done);
        },
        callback(action, instance) {
        }
    });
}

/**
 * 生成uuid
 * @returns {string}
 */
function createUUID() {
    let s = [];
    let hexDigits = "0123456789abcdef";
    for (let i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    // bits 12-15 of the time_hi_and_version field to 0010
    s[14] = "4";
    // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
    s[8] = s[13] = s[18] = s[23] = "";
    return s.join("");
}

/**
 * 将错误信息以console.table打印出来
 * @param msg
 * @param object
 */
function consoleWarnTable(msg, object = {}) {
    console.warn(msg);
    try {
        if (object instanceof Object) {
            console.table(object);
        } else {
            console.log(object);
        }
    } catch (e) {
        console.log('此浏览器不支持console.table()', e, '---错误详情:', object);
    }
    console.warn('↑↑以上为错误详情↑↑↑↑↑');
}

/**
 * 获取session存储的数据
 * @param key
 * @returns {*}
 */
function getSessionStorage(key) {
    if (null == key) {
        return null;
    }
    key += `weiziplus-${key}`;
    return JSON.parse(sessionStorage.getItem(key));
}

/**
 * 将数据保存到session中
 * @param key
 * @param value
 */
function setSessionStorage(key, value = '') {
    if (null == key) {
        return;
    }
    key += `weiziplus-${key}`;
    sessionStorage.setItem(key, JSON.stringify(value));
}

/**
 * 字符串反转
 * @param value
 */
function reverse(value) {
    if (null == value) {
        return null;
    }
    return value.split('').reverse().join('');
}

/**
 * base64加密
 * @param value
 */
function encode(value) {
    if (null == value) {
        return null;
    }
    //对内容进行加密
    let encode = Base64.encode(value);
    //对加密内容进行反转
    return reverse(encode);
}

/**
 * base64解密
 * @param value
 */
function decode(value) {
    if (null == value) {
        return null;
    }
    //先对加密内容反转
    let str = reverse(value);
    //解密
    return Base64.decode(str);
}

/**
 * 进行md5加密
 * @param value
 */
function md5(value) {
    if (null == value) {
        return null;
    }
    return Md5(value + 'weiziplus');
}

/**
 * 格式化时间戳
 * @param timestamp
 * @param format
 * @returns {string}
 */
function timestampFormat(timestamp, format = 'yyyy-MM-dd') {
    if (null == timestamp || 0 > timestamp) {
        return '';
    }
    let date = new Date(parseInt(timestamp));
    let o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (date.getFullYear() + ""));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return format;
}

/**
 * 将方法暴露出去
 */
export default {
    isBlank,
    notPhone,
    errorMsg,
    successMsg,
    messageBox,
    messageBoxInput,
    createUUID,
    consoleWarnTable,
    getSessionStorage,
    setSessionStorage,
    reverse,
    encode,
    decode,
    md5,
    timestampFormat
};