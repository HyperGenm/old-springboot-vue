<template>
    <div id="login">
        <el-form class="login-form" ref="form" :model="form" :rules="rules" label-position="left">
            <h3 class="title">WeiziPlus</h3>
            <el-form-item prop="username">
                <el-input name="username" type="text" clearable
                          v-model="form.username" placeholder="用户名"/>
            </el-form-item>
            <el-form-item prop="password">
                <el-input name="password" type="password" show-password @change="handleLogin"
                          v-model="form.password" placeholder="密码"/>
            </el-form-item>
            <el-row>
                <el-col :span="16">
                    <el-form-item prop="code">
                        <el-input @change="handleLogin" v-model="form.code" placeholder="验证码"/>
                    </el-form-item>
                </el-col>
                <el-col :span="6" :offset="2">
                    <div class="code" @click="changeCode">
                        <img :src="imgCodeUrl" alt="验证码">
                    </div>
                </el-col>
            </el-row>
            <el-form-item>
                <el-button v-loading="loginLoad" type="primary" class="btn" @click="handleLogin">
                    登录
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
                                that.$globalFun.errorMsg("您还没有可用菜单，请联系管理员添加")
                                return;
                            }
                            that.$store.dispatch('setUserInfo', userInfo);
                            that.$store.dispatch('setToken', token);
                            let roleButtons = {};
                            res.data['roleButtons'].forEach((value) => {
                                roleButtons[value.name] = true;
                            });
                            that.$store.dispatch('setRole', {
                                name: role,
                                buttons: roleButtons
                            });
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
                    if ('home' === value['name']) {
                        haveHomePage = true;
                    }
                    let router = {
                        path: "/" + value.name,
                        name: value.name,
                        meta: {
                            title: value.title,
                            icon: value.icon
                        }
                    };
                    let children = value.children;
                    if (null == children || 0 >= children.length) {
                        try {
                            router['components'] = require(`@/views/index/${value.path}/Index`);
                        } catch (e) {
                            console.debug(value.path, '没有找到对应组件');
                            router['components'] = require(`@/views/errorPage/404`);
                        }
                        router['components_bak'] = value.path;
                        routers.push(router);
                    } else {
                        let childrenRouters = that.childrenRouter(value.path, children);
                        routers = routers.concat(childrenRouters);
                    }
                });
                let parentRouters = [{
                    path: '/',
                    components: require('@/views/layout/Index'),
                    name: 'index',
                    children: routers
                }];
                this.$store.dispatch('setRouters', {
                    routers: routers,
                    routersTree: data
                });
                this.$router.addRoutes(parentRouters);
                this.$router.push(haveHomePage ? '/home' : routers[0]['path'] || '/');
            },
            childrenRouter(parentName, data) {
                let routers = [];
                let that = this;
                data.forEach((value) => {
                    let router = {
                        path: "/" + value.name,
                        name: value.name,
                        meta: {
                            title: value.title,
                            icon: value.icon
                        }
                    };
                    let children = value.children;
                    if (null == children || 0 >= children.length) {
                        try {
                            router['components'] = require(`@/views/index/${parentName}/${value.path}/Index`);
                        } catch (e) {
                            console.debug(value.path, '没有找到对应组件');
                            router['components'] = require(`@/views/errorPage/404`);
                        }
                        router['components_bak'] = parentName + '/' + value.path;
                        routers.push(router);
                    } else {
                        let childrenRouters = that.childrenRouter(parentName + '/' + value.path, children);
                        routers = routers.concat(childrenRouters);
                    }
                });
                return routers;
            }
        }
    };
</script>

<style lang="scss">
    /* reset element-ui css */
    #login {
        .el-input {
            display: inline-block;
            height: 47px;
            width: 90%;
            input {
                background: transparent;
                border: 0;
                -webkit-appearance: none;
                border-radius: 0;
                padding: 12px 5px 12px 15px;
                color: #eee;
                height: 47px;
                &:-webkit-autofill {
                    -webkit-box-shadow: 0 0 0 1000px #2d3a4b inset !important;
                    -webkit-text-fill-color: #fff !important;
                }
            }
        }
        .el-form-item {
            border: 1px solid rgba(255, 255, 255, 0.1);
            background: rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            color: #454545;
        }
    }
</style>

<style lang="scss" scoped>
    #login {
        position: fixed;
        height: 100%;
        width: 100%;
        background: url("../../assets/loginBg.jpg") no-repeat;
        background-size: 100% 100%;
        .login-form {
            position: absolute;
            left: 0;
            right: 0;
            width: 520px;
            padding: 35px 35px 15px 35px;
            margin: 120px auto;
            .btn {
                width: 100%;
            }
        }
        .title {
            font-size: 26px;
            color: #eee;
            margin: 0 auto 40px auto;
            text-align: center;
            font-weight: bold;
        }
        .code {
            border-radius: 5px;
            height: 47px;
            line-height: 47px;
            color: white;
            border: 1px solid rgba(255, 255, 255, 0.1);
            overflow: hidden;
            img {
                width: 100%;
                height: 47px;
                line-height: 47px;
            }
        }
    }

    @media screen and (min-width: 666px) {
        #login .login-form {
            width: 520px;
        }
    }

    @media screen and (max-width: 666px) {
        #login .login-form {
            width: 72%;
        }
    }

</style>
