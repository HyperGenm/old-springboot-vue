/**引入全局Vue*/
import Vue from 'vue'

let _Vue = Vue.prototype;
/**引入axios*/
import axios from "axios";
/**引入参数处理*/
import Qs from 'qs';
/**引入element-ui组件*/
import {Loading} from 'element-ui';

/**axios默认配置*/
axios.defaults.timeout = 30000;//设置超时时间，规定时间内没有响应则执行失败回调
axios.defaults.withCredentials = true;

/**
 * 封装axios请求
 *
 * @param allSuccess 返回所有成功回调,不包含status不是200的出错请求
 * @param url 请求地址
 * @param method 请求方式
 * @param headers 请求头
 * @param data 请求参数
 * @param success 成功回调
 * @param fail 失败回调
 * @returns {Promise<any>}
 * @private
 */
export function weiAxios({
                             allSuccess = false,
                             url = '',
                             method = 'get',
                             header = 'application/x-www-form-urlencoded; charset=UTF-8',
                             data = {},
                             success = function () {
                             },
                             fail = function () {
                             }
                         } = {}) {
    return new Promise(() => {
        /**保留this指针*/
        let that = this;
        /**开启加载中动画*/
        let loading = Loading.service({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        /**axios请求所需参数*/
        let _axios = {
            url: _Vue.$global.GLOBAL.base_url + url,
            method,
            headers: {
                'Content-Type': header,
                'token': that.$store.state.token || ''
            }
        };
        /**axios请求参数添加随机字符串*/
        data['__t'] = (new Date()).getTime();
        /**axios请求处理不同请求方式时的参数*/
        if (method === 'get' || method === 'GET') {
            _axios['params'] = data;
        } else {
            _axios['data'] = Qs.stringify(data, {indices: false});
        }
        axios(_axios).then((res) => {
            /**关闭加载中动画*/
            loading.close();
            /**处理status不为200的出错请求*/
            if (200 !== res.status) {
                _Vue.$globalFun.errorMsg('请求出错:' + res.status);
                console.warn(url, '-----status:', res.status, '------请求出错-----res:', res);
                return;
            }
            /**token过期处理*/
            if (401 === res.data.code) {
                _Vue.$globalFun.errorMsg('登陆过期，即将跳转到登录页面');
                that.$store.dispatch('resetState');
                sessionStorage.setItem('loginStatus', 'logout');
                let timer = setTimeout(() => {
                    clearTimeout(timer);
                    that.$router.replace('login');
                }, 3000);
                return;
            }
            /**返回所有成功回调,不包含status不是200的出错请求*/
            if (allSuccess) {
                success(res.data);
                return;
            }
            /**处理code不为200的出错请求*/
            if (200 !== res.data.code) {
                _Vue.$globalFun.errorMsg(res.data.msg);
                console.warn(url, '-----code:', res.data.code, '------请求出错-----res:', res);
                return;
            }
            /**成功回调*/
            success(res.data.data);
        }).catch((error) => {
            /**关闭加载中动画*/
            loading.close();
            _Vue.$globalFun.errorMsg('请求失败');
            if (error.response) {
                console.warn(url, '------请求失败-----error:', error, '---失败详情:', error.response);
            } else {
                console.warn(url, '------请求失败-----error:', error);
            }
            fail(error);
        });
    });
}
