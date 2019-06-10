<template>
    <div id="login">
        <el-form class="login-form" ref="form" :model="form" :rules="rules" label-position="left">
            <h3 class="title">WeiziPlus</h3>
            <el-form-item prop="username">
                <el-input name="username" type="text" clearable v-model="form.username" placeholder="用户名"/>
            </el-form-item>
            <el-form-item prop="password">
                <el-input name="password" type="password" show-password v-model="form.password" placeholder="密码"/>
            </el-form-item>
            <el-row>
                <el-col :span="16">
                    <el-form-item prop="code">
                        <el-input v-model="form.code" placeholder="验证码"/>
                    </el-form-item>
                </el-col>
                <el-col :span="6" :offset="2">
                    <div class="code" @click="changeCode">
                        <img :src="imgCodeUrl" alt="验证码">
                    </div>
                </el-col>
            </el-row>
            <el-form-item>
                <el-button type="primary" class="btn" @click="handleLogin">
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
            let that = this;
            return {
                imgCodeUrl: that.$global.GLOBAL.base_url + that.$global.URL.loginValidateCode,
                form: {
                    username: '',
                    password: '',
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
                }
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
        },
        methods: {
            changeCode() {
                this.imgCodeUrl = this.$global.GLOBAL.base_url + this.$global.URL.loginValidateCode + '?__t' + Math.random();
            },
            handleLogin() {
                const that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                    this.$axios({
                        allSuccess: true,
                        url: that.$global.URL.login,
                        method: 'post',
                        data: that.form,
                        success(res) {
                            if ('200' !== res.code + '') {
                                that.$globalFun.errorMsg(res.msg);
                                that.changeCode();
                                return;
                            }
                            let data = res.data;
                            that.$store.dispatch('setUserInfo', data['userInfo']);
                            that.$store.dispatch('setToken', data['token']);
                            let roleButtons = {};
                            data['roleButtons'].forEach((value) => {
                                roleButtons[value.name] = true;
                            });
                            that.$store.dispatch('setRole', {
                                name: data['role'],
                                buttons: roleButtons
                            });
                            that.handleRouter(data['routerTree']);
                        }
                    });
                });
            },
            handleRouter(data) {
                let that = this;
                let routers = [];
                data.forEach((value) => {
                    let router = {};
                    router['path'] = "/" + value.name;
                    router['name'] = value.name;
                    router['meta'] = {
                        title: value.title,
                        icon: value.icon
                    };
                    let children = value.children;
                    if (null == children || 0 >= children.length) {
                        router['components'] = require(`@/views/index/${value.path}/Index`);
                        router['components_bak'] = value.path;
                        routers.push(router);
                    } else {
                        let childrenRouters = that.childrenRouter(value.path, children);
                        routers = routers.concat(childrenRouters);
                    }
                });
                let parentRouters = [{
                    path: '/',
                    components: require('@/views/common/layout/Index'),
                    name: 'index',
                    children: routers
                }];
                that.$store.dispatch('setRouters', {
                    routers: routers,
                    routersTree: data
                });
                this.$router.addRoutes(parentRouters);
                this.$router.push('/');
            },
            childrenRouter(parentName, data) {
                let routers = [];
                let that = this;
                data.forEach((value) => {
                    let router = {};
                    router['path'] = "/" + value.name;
                    router['name'] = value.name;
                    router['meta'] = {
                        title: value.title,
                        icon: value.icon
                    };
                    let children = value.children;
                    if (null == children || 0 >= children.length) {
                        router['components'] = require(`@/views/index/${parentName}/${value.path}/Index`);
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

<style lang="less">
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

<style lang="less" scoped>
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
</style>
