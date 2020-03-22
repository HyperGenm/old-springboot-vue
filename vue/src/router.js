import Vue from 'vue'
import Router from 'vue-router'
import store from './store'

/*浏览器上面进度条*/
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({
    showSpinner: false, // 是否显示加载ico
});

if (process.env.NODE_ENV !== 'production') {
    Vue.use(Router);
    //解决点击当前路由报错问题
    const originalPush = Router.prototype.push;
    Router.prototype.push = function push(location) {
        return originalPush.call(this, location).catch(err => err)
    };
}
const router = new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {path: '/login', name: 'login', component: () => import('@/views/login/Index')}
    ]
});

/**
 *取消上一个页面的请求
 */
function clearAxiosCancelToken() {
    let cancelArray = window['_axiosCancelToken'] || [];
    cancelArray.forEach(value => {
        value();
    });
    window['_axiosCancelToken'] = [];
}

/**
 * 重新生成路由
 * @returns {{children: Array, haveHomePage: boolean}}
 */
function handleRouterChildren() {
    let routersTree = store.state.routers.routersTree;
    let routers = [];
    let haveHomePage = false;
    routersTree.forEach((value) => {
        if ('home' === value['name']) {
            haveHomePage = true;
        }
        let router = {
            path: "/" + value.path,
            name: value.path,
            meta: {
                title: value.title,
                icon: value.icon
            }
        };
        let children = value.children;
        //如果存在子级
        if (null != children && 0 < children.length) {
            router['components'] = require('@/views/station/Index.vue');
            router['redirect'] = `/${value.path}/${children[0]['path']}`;
            router['children'] = childrenRouter(value.path, children);
        } else {
            try {
                router['components'] = require(`@/views/index/${value.path}/Index.vue`);
            } catch (e) {
                console.debug(value.path, '没有找到对应组件', '---详情', e);
                router['components'] = require(`@/views/errorPage/404.vue`);
            }
        }
        routers.push(router);
    });
    return {
        haveHomePage,
        children: routers
    };
}

function childrenRouter(parentPath, data) {
    let routers = [];
    data.forEach((value) => {
        let router = {
            path: value.path,
            name: value.path,
            meta: {
                title: value.title,
                icon: value.icon
            }
        };
        let children = value.children;
        //如果存在子级
        if (null != children && 0 < children.length) {
            router['components'] = require('@/views/station/Index.vue');
            router['redirect'] = `/${parentPath}/${value.path}/${children[0]['path']}`;
            router['children'] = childrenRouter(parentPath + '/' + value.path, children);
        } else {
            try {
                router['components'] = require(`@/views/index/${parentPath}/${value.path}/Index.vue`);
            } catch (e) {
                console.debug(value.path, '没有找到对应组件', '---详情', e);
                router['components'] = require(`@/views/errorPage/404.vue`);
            }
        }
        routers.push(router);
    });
    return routers;
}

router.beforeEach((to, from, next) => {
    clearAxiosCancelToken();
    //浏览器上方显示进度条
    NProgress.start();
    const token = store.state.token;
    if ((null == token || '' === token) && 'login' !== to.name) {
        next('/login');
        return;
    }
    //刷新页面，重新初始化路由
    if ((null == from.name || '' === from.name) && (null == to.name || '' === to.name)) {
        let {children, haveHomePage} = handleRouterChildren();
        let parentRouters = [{
            path: '/',
            components: require('@/views/layout/Index.vue'),
            name: 'index',
            children
        }];
        router.addRoutes(parentRouters);
        next(haveHomePage ? '/home' : children[0]['path'] || '/');
        return;
    }
    //正常放行
    next();
});

router.afterEach(() => {
    //关闭浏览器上方的进度条
    NProgress.done();
});

export default router;