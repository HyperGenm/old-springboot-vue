<template>
    <div id="login">
        <el-form ref="form" class="container" :model="form" :rules="rules">
            <h3 class="title">系统登录</h3>
            <el-form-item>
                <el-input autocomplete placeholder="用户名"
                          v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item>
                <el-input type="password" placeholder="密码"
                          v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item>
                <el-row>
                    <el-col :span="12">
                        <el-input v-model="form.code" placeholder="验证码"
                                  @change="handleLogin"/>
                    </el-col>
                    <el-col :span="10" :offset="2">
                        <img alt="验证码"
                             :src="imgCodeUrl"
                             @click="changeCode">
                    </el-col>
                </el-row>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width: 100%;"
                           :loading="loginLoad"
                           @click="handleLogin">登录
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "login",
        data() {
            return {
                imgUUID: '',
                imgCodeUrl: '',
                form: {
                    username: 'superadmin',
                    password: '111111',
                    code: ''
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 2, message: '最少两个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 6, message: '密码最少6位', trigger: 'blur'}
                    ],
                    code: [
                        {required: true, message: '请输入验证码', trigger: 'blur'},
                        {min: 4, max: 4, message: '验证码为4位', trigger: 'blur'}
                    ]
                },
                loginLoad: false
            };
        },
        mounted() {
            /**
             * 判断是否是用户退出登录，如果是用户退出登录就重新刷新页面，解决路由bug
             * @type {string}
             */
            let status = sessionStorage.getItem("loginStatus");
            if (null != status && 'logout' === status) {
                sessionStorage.removeItem('loginStatus');
                window.location.reload();
            }
            this.changeCode();
        },
        methods: {
            changeCode() {
                let uuid = this.$globalFun.createUUID();
                let {GLOBAL, URL} = this.$global;
                this.imgCodeUrl = GLOBAL.base_url + URL.loginValidateCode + `?uuid=${uuid}&__t=${new Date().getTime()}`;
                this.imgUUID = uuid;
                this.loginLoad = false;
            },
            handleLogin() {
                const that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                    that.loginLoad = true;
                    let {username, password, code} = that.form;
                    let uuid = that.imgUUID;
                    that.$axios({
                        allSuccess: true,
                        url: that.$global.URL.login,
                        method: 'post',
                        data: {
                            uuid, code,
                            username: username,
                            password: that.$globalFun.md5(password)
                        },
                        success(res) {
                            that.loginLoad = false;
                            //获取响应状态码
                            let {axios_result_code} = that.$global.GLOBAL;
                            if (axios_result_code['success'] !== res.code) {
                                that.$globalFun.errorMsg(res.msg);
                                that.changeCode();
                                return;
                            }
                            let {userInfo, token, role, routerTree} = res.data;
                            if (null == routerTree || 0 >= routerTree.length) {
                                that.$globalFun.errorMsg("您还没有可用菜单，请联系管理员添加");
                                return;
                            }
                            that.$globalFun.setSessionStorage(`token`, token);
                            that.$globalFun.setSessionStorage('userInfo', userInfo);
                            let roleButtons = {};
                            res.data['roleButtons'].forEach((value) => {
                                roleButtons[value.name] = true;
                            });
                            that.$globalFun.setSessionStorage('buttonMap', roleButtons);
                            that.$globalFun.setSessionStorage('role', role);
                            that.$globalFun.setSessionStorage('menuTree', routerTree);
                            that.handleRouter(routerTree);
                        }
                    });
                });
            },
            handleRouter(data) {
                let that = this;
                let routers = [];
                let haveHomePage = false;
                data.forEach((value) => {
                    //如果有首页优先展示首页
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
                        router['components'] = require('@/views/station/Index');
                        router['redirect'] = `/${value.path}/${children[0]['path']}`;
                        router['children'] = that.childrenRouter(value.path, children);
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
                let parentRouters = [{
                    path: '/',
                    components: require('@/views/layout/Index.vue'),
                    name: 'index',
                    children: routers
                }];
                this.$store.dispatch('setRouters', {
                    routersTree: data
                });
                this.$router.addRoutes(parentRouters);
                this.$router.push(haveHomePage ? '/home' : routers[0]['path'] || '/');
            },
            childrenRouter(parentPath, data) {
                let routers = [];
                let that = this;
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
                        router['children'] = that.childrenRouter(parentPath + '/' + value.path, children);
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
        }
    };
</script>

<style lang="scss" scoped>
    #login {
        position: fixed;
        top: 0;
        width: 100%;
        bottom: 0;
        background: url("../../assets/loginBg.jpg") no-repeat;
        background-size: 100% 100%;
        display: flex;
        align-items: center;

        img {
            width: 100%;
        }

        .container {
            border-radius: 15px;
            margin: 0 auto;
            width: 350px;
            padding: 35px 35px 15px 35px;
            background: #fff;
            border: 1px solid #eaeaea;
            box-shadow: 0 0 7px #cac6c6;
        }

        .title {
            margin: 0 auto 40px auto;
            text-align: center;
            color: #505458;
        }

    }

</style>