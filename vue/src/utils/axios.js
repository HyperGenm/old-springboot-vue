/**引入Vue*/
import Vue from 'vue'
/**引入axios*/
import axios from "axios";
/**引入参数处理*/
import Qs from 'qs';
/**引入element-ui组件*/
import {Loading} from 'element-ui';

//请求拦截器
axios.interceptors.request.use(
    config => {
        // 将请求放入全局变量中，便于取消请求
        config.cancelToken = new axios.CancelToken(cancel => {
            let cancelArray = window['_axiosCancelToken'] || [];
            cancelArray.push(cancel);
        });
        return config;
    },
    error => {
        // 对请求错误的处理
        return Promise.reject(error);
    }
);

/**
 * 封装axios请求
 *
 * @param allUrl 请求的url为完整url
 * @param allSuccess 返回所有成功回调,不包含status不是200的出错请求
 * @param url 请求地址
 * @param method 请求方式
 * @param headers 请求头
 * @param data 请求参数
 * @param timeout 请求超时时间---某些请求需要单独设置超时时间
 * @param timeShowLoadAnimation 多长时间之后显示加载中动画,单位毫秒
 * @param success 成功回调
 * @param fail 失败回调
 * @returns {Promise<any>}
 * @private
 */
export function weiAxios(
    {
        allUrl = false,
        allSuccess = false,
        url = '',
        method = 'get',
        header = 'application/x-www-form-urlencoded; charset=UTF-8',
        data = {},
        timeout = parseInt(process.env.VUE_APP_AXIOS_TIMEOUT),
        timeShowLoadAnimation = 555,
        success = function () {
        },
        fail = function () {
        }
    } = {}) {
    return new Promise(() => {
        /**保留this指针*/
        let that = this;
        /**timeShowLoadAnimation时间之后开启加载中动画*/
        let loading = null;
        let loadingTimer = setTimeout(() => {
            loading = Loading.service({
                lock: true,
                // text: 'Loading',
                spinner: 'none'
            });
        }, timeShowLoadAnimation);
        /**axios请求所需参数*/
        let _axios = {
            method,
            headers: {
                'Content-Type': header
            },
            timeout
        };
        //每个请求加上请求头
        _axios['headers'][that.$global.GLOBAL.token] = that.$store.state.token || '';
        /***请求的url是否为全部url***/
        if (allUrl) {
            _axios['url'] = url;
        } else {
            _axios['url'] = that.$global.GLOBAL.base_url + url;
        }
        /**axios请求参数添加随机字符串*/
        data['__t'] = (new Date()).getTime();
        /**axios请求处理不同请求方式时的参数*/
        method = method.toUpperCase();
        if (method === 'GET') {
            _axios['params'] = data;
        } else {
            _axios['data'] = Qs.stringify(data, {indices: false});
        }
        axios(_axios).then((res) => {
            /**关闭加载中动画*/
            clearTimeout(loadingTimer);
            if (null != loading) {
                loading.close();
            }
            /***请求的url如果是全部url的话,返回所有res['data']响应***/
            if (allUrl) {
                success(res['data']);
                return;
            }
            //获取响应状态码
            let {axios_result_code} = that.$global.GLOBAL;
            /**token过期处理*/
            if (axios_result_code['errorToken'] === res.data.code) {
                that.$globalFun.errorMsg('登陆过期，即将跳转到登录页面');
                that.$store.dispatch('resetState');
                sessionStorage.setItem('loginStatus', 'logout');
                let timer = setTimeout(() => {
                    clearTimeout(timer);
                    that.$router.replace('/login');
                }, 3000);
                return;
            }
            /**返回所有成功回调,不包含status不是401的出错请求*/
            if (allSuccess) {
                success(res.data);
                return;
            }
            /**处理code不为200的出错请求*/
            if (axios_result_code['success'] !== res.data.code) {
                that.$globalFun.errorMsg(res.data.msg);
                that.$globalFun.consoleWarnTable(`请求出错url:${url}`, res['data']);
                return;
            }
            /**成功回调*/
            success(res.data.data);
        }).catch((error) => {
            /**关闭加载中动画*/
            clearTimeout(loadingTimer);
            if (null != loading) {
                loading.close();
            }
            // 如果请求被取消则进入该方法
            if (axios.isCancel(error)) {
                fail(error);
                return;
            }
            if (error.response) {
                error = error['response']['data'];
            }
            //限制访问
            if (403 === error.status || 403 === error.code) {
                that.$globalFun.errorMsg('拒绝访问,请稍后重试,详情:' + JSON.stringify(error));
            } else {
                that.$globalFun.errorMsg('内部服务器错误，请稍后重试,详情:' + JSON.stringify(error));
            }
            that.$globalFun.consoleWarnTable(`请求失败url:${url}`, error);
            fail(error);
        });
    });
}

/**封装axios下载请求
 *
 * @param url
 * @param method
 * @param data
 * @param filename 文件名字
 * @param timeShowLoadAnimation 多长时间之后显示加载中动画，单位毫秒
 * @param success
 * @param fail
 * @returns {Promise<any>}
 */
export function weiAxiosDown(
    {
        url = '',
        method = 'post',
        data = {},
        filename = '新建文件',
        timeShowLoadAnimation = 555,
        success = function () {
        },
        fail = function () {
        }
    } = {}
) {
    return new Promise(() => {
        /**保留this指针*/
        let that = this;
        /**timeShowLoadAnimation时间之后开启加载中动画*/
        let loading = null;
        let loadingTimer = setTimeout(() => {
            loading = Loading.service({
                lock: true,
                text: '下载打包中...',
                spinner: 'none'
            });
        }, timeShowLoadAnimation);
        /**axios请求所需参数*/
        let _axios = {
            url: that.$global.GLOBAL.base_url + url,
            method,
            responseType: 'blob',
            headers: {},
        };
        //每个请求加上请求头
        _axios['headers'][that.$global.GLOBAL.token] = that.$store.state.token || '';
        /**axios请求参数添加随机字符串*/
        data['__t'] = (new Date()).getTime();
        /**axios请求处理不同请求方式时的参数*/
        method = method.toUpperCase();
        if (method === 'GET') {
            _axios['params'] = data;
        } else {
            _axios['data'] = Qs.stringify(data, {indices: false});
        }
        axios(_axios).then((res) => {
            /**关闭加载中动画*/
            clearTimeout(loadingTimer);
            if (null != loading) {
                loading.close();
            }
            let {data} = res;
            let fileReader = new FileReader();
            fileReader.readAsText(data);
            fileReader.onload = function () {
                // 如果JSON.parse(this.result)不报错，说明this.result是json字符串，是下载报错情况的返回值，弹框提示
                // 如果JSON.parse(this.result)报错，说明下载成功，进入catch
                try {
                    let resData = JSON.parse(this.result);
                    if (resData && resData['code']) {
                        let {code, msg} = resData;
                        //获取响应状态码
                        let {axios_result_code} = that.$global.GLOBAL;
                        /**token过期处理*/
                        if (axios_result_code['errorToken'] === code) {
                            that.$globalFun.errorMsg('登陆过期，即将跳转到登录页面');
                            that.$store.dispatch('resetState');
                            sessionStorage.setItem('loginStatus', 'logout');
                            let timer = setTimeout(() => {
                                that.$router.replace('login');
                                clearTimeout(timer);
                            }, 3000);
                            return;
                        }
                        /**处理code不为200的出错请求*/
                        if (axios_result_code['success'] !== code) {
                            that.$globalFun.errorMsg(msg);
                            that.$globalFun.consoleWarnTable(`请求出错url:${url}`, res['data']);
                            return;
                        }
                        console.log('文件下载成功回调，不是文件流', resData);
                        success();
                    }
                } catch (error) {
                    let blob = new Blob([data]);
                    // 兼容ie11
                    if (window.navigator.msSaveOrOpenBlob) {
                        window.navigator.msSaveOrOpenBlob(blob, filename);
                    } else {
                        let downloadElement = document.createElement('a');
                        //创建下载的链接
                        let href = window.URL.createObjectURL(blob);
                        downloadElement.href = href;
                        //下载后文件名
                        downloadElement.download = filename;
                        document.body.appendChild(downloadElement);
                        //点击下载
                        downloadElement.click();
                        //下载完成移除元素
                        document.body.removeChild(downloadElement);
                        //释放掉blob对象
                        window.URL.revokeObjectURL(href);
                    }
                }
            };
            success(res);
        }).catch((error) => {
            /**关闭加载中动画*/
            clearTimeout(loadingTimer);
            if (null != loading) {
                loading.close();
            }
            // 如果请求被取消则进入该方法
            if (axios.isCancel(error)) {
                fail(error);
                return;
            }
            if (error.response) {
                error = error['response']['data'];
            }
            //限制访问
            if (403 === error.status || 403 === error.code) {
                that.$globalFun.errorMsg('拒绝访问,请稍后重试,详情:' + JSON.stringify(error));
            } else {
                that.$globalFun.errorMsg('文件下载失败，请稍后重试,详情:' + JSON.stringify(error));
            }
            that.$globalFun.consoleWarnTable(`文件下载失败url:${url}`, error);
            fail(error);
        });
    });
}

