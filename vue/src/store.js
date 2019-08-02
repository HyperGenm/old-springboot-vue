import Vue from 'vue'
import Vuex from 'vuex'

// Vue.use(Vuex);

// 获取session中的state信息，如果有说明是浏览器刷新，然后赋值，如果没有，重新声明
let state = sessionStorage.getItem('state') ? JSON.parse(sessionStorage.getItem('state')) : {
    token: '',
    userInfo: {},
    routers: {
        //用户路由列表
        routers: [],
        //用户路由树
        routersTree: []
    },
    role: {
        //用户权限名称
        name: '',
        //用户权限的按钮
        buttons: []
    }
};
//赋值后将session中的state信息清空
sessionStorage.removeItem('state');

export default new Vuex.Store({
    state,
    mutations: {
        setToken(state, token) {
            state.token = token;
        },
        clearToken(state) {
            state.token = '';
        },
        setUserInfo(state, userInfo) {
            state.userInfo = userInfo;
        },
        clearUserInfo(state) {
            state.userInfo = {};
        },
        setRouters(state, routers) {
            state.routers = routers;
        },
        clearRouters(state) {
            state.routers = {
                routers: [],
                routersTree: []
            };
        },
        setRole(state, role) {
            state.role = role;
        },
        clearRole(state) {
            state.role = {
                name: '',
                buttons: []
            };
        },
        resetState(state) {
            state = {
                token: '',
                userInfo: {},
                routers: {
                    routers: [],
                    routersTree: []
                }
            };
        }
    },
    actions: {
        setToken(context, token) {
            context.commit('setToken', token);
        },
        clearToken(context) {
            context.commit('clearToken');
        },
        setUserInfo(context, userInfo) {
            context.commit('setUserInfo', userInfo);
        },
        clearUserInfo(context) {
            context.commit('clearUserInfo');
        },
        setRouters(context, routers) {
            context.commit('setRouters', routers);
        },
        clearRouters(context) {
            context.commit('clearRouters');
        },
        setRole(context, role) {
            context.commit('setRole', role);
        },
        clearRole(context) {
            context.commit('clearRole');
        },
        resetState(context) {
            context.commit('resetState');
        }
    }
});
