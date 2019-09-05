import Vue from 'vue'
import Router from 'vue-router'
import store from './store'

if (process.env.NODE_ENV !== 'production') {
    Vue.use(Router);
}

const baseRouters = [
    {path: '/login', name: 'login', component: () => import('@/views/login/Index')}
];

const router = new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: baseRouters
});

router.beforeEach((to, from, next) => {
    const token = store.state.token;
    if ((null == token || '' === token) && 'login' !== to.name) {
        next('/login');
        return;
    }
    //刷新页面，重新初始化路由
    if ((null == from.name || '' === from.name) && (null == to.name || '' === to.name)) {
        let routers = store.state.routers.routers;
        let children = [];
        let haveHomePage = false;
        routers.forEach((value) => {
            if ('home' === value['name']) {
                haveHomePage = true;
            }
            let router = {};
            router['path'] = value.path;
            router['name'] = value.name;
            router['meta'] = {
                title: value.meta.title,
                icon: value.meta.icon
            };
            router['components'] = require('@/views/index/' + value.components_bak + '/Index');
            router['components_bak'] = value.components_bak;
            children.push(router);
        });
        let parentRouters = [{
            path: '/',
            components: require('@/views/common/layout/Index'),
            name: 'index',
            children
        }];
        router.addRoutes(parentRouters);
        next(haveHomePage ? '/home' : '/');
        return;
    }
    next();
});

export default router;