import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementUI from "element-ui";
//引入覆盖element-ui的css
import './assets/sass/element-variables.scss'

if (process.env.NODE_ENV !== 'production') {
    Vue.use(ElementUI);
}

/**引入iconfont字体库*/
import "./assets/font/iconfont/iconfont.css";

/**引入封装的axios*/
import {weiAxios, weiAxiosDown} from './utils/axios';

Vue.prototype.$axios = weiAxios;
Vue.prototype.$axiosDown = weiAxiosDown;

/**引入全局变量和方法*/
import globalVariable from './utils/global_variable'
import globalFunction from './utils/global_function'

Vue.prototype.$global = globalVariable;
Vue.prototype.$globalFun = globalFunction;

window.onkeyup = function (event) {
    // 判断是否按下F12，F12键码为123
    if (123 === event['keyCode']) {
        globalFunction.errorMsg('不是粉红，是品红哒');
        console.log('element-ui样式覆盖:/src/assets/sass/element-variables.scss文件');
    }
};

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
