import Vue from 'vue'

/**引入iconfont字体库*/
import "./assets/font/iconfont/iconfont.css";

import ElementUI from "element-ui";
//引入覆盖element-ui的css
import './assets/sass/element-variables.scss'

if (process.env.NODE_ENV !== 'production') {
    Vue.use(ElementUI);
}

/**引入封装的axios*/
import {weiAxios, weiAxiosDown} from './utils/axios';

Vue.prototype.$axios = weiAxios;
Vue.prototype.$axiosDown = weiAxiosDown;

/**引入全局变量和方法*/
import globalVariable from './utils/global_variable'
import globalFunction from './utils/global_function'

Vue.prototype.$global = globalVariable;
Vue.prototype.$globalFun = globalFunction;

//加载路由，vuex
import App from './App.vue'
import router from './router'
import store from './store'

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
