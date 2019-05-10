/**引入element-ui组件*/
import {Message, MessageBox, prompt} from 'element-ui';

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
 */
function errorMsg(msg) {
    Message({
        message: msg,
        type: 'error'
    });
}

/**
 * 成功提示
 * @param msg
 */
function successMsg(msg) {
    Message({
        message: msg,
        type: 'success'
    });
}

/**
 * 对话框
 * @param title  标题
 * @param message  内容
 * @param type  类型
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
 * 对话框input输入框
 * @param title  标题
 * @param message  内容
 * @param type  类型
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
            confirm(instance, done);
        },
        callback(action, instance) {
        }
    });
}

/**
 * 将方法暴露出去
 */
export default {
    isBlank, notPhone, errorMsg, successMsg, messageBox, messageBoxInput
};