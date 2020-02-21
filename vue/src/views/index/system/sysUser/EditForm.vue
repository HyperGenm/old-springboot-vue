<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions" :formRules="rules"
                     @closeDialog="$emit('closeDialog')" @submit="submit">
        </dialog-form>
    </div>
</template>

<!--简单表单可以参考/src/views/index/system/sysRole/EditForm.vue-->

<script>
    export default {
        name: "EditForm",
        components: {
            'dialog-form': () => import('@/components/dialog/form/Index.vue')
        },
        props: {
            handleType: {
                type: String,
                default: 'add'
            },
            formData: {
                type: Object
            }
        },
        watch: {
            formData(data) {
                this.form = data;
                this.changeFormOptions();
            },
            handleType() {
                this.changeFormOptions();
            }
        },
        data() {
            return {
                //表单验证的规则
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 2, message: '最少两个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 6, message: '密码最少6位', trigger: 'blur'}
                    ]
                },
                form: this.formData,
                // 表单每一项，具体参考 /src/components/dialog/form/Item.vue
                formOptions: [
                    {
                        //如果一行展示两项
                        items: [
                            {type: 'input', label: '用户名', prop: 'username'},
                            {type: 'input', label: '真实姓名', prop: 'realName', required: true},
                        ]
                    },
                    {
                        //类型
                        type: 'radio',
                        //标题
                        label: '是否允许登录',
                        //对应字段
                        prop: 'allowLogin',
                        //下拉框、单选按钮的选项
                        options: [
                            {
                                //选项名称
                                label: '允许',
                                //选项值
                                value: 0
                            },
                            {label: '禁止', value: 1}
                        ],
                        //是否隐藏，不设置默认显示
                        hidden: false
                    },
                    {type: 'input', label: '密码', prop: 'password', inputType: 'password'}
                ]
            }
        },
        mounted() {
            this.changeFormOptions();
        },
        methods: {
            changeFormOptions() {
                if ('add' === this.handleType) {
                    //新增
                    this.formOptions[0]['items'][0]['disabled'] = false;
                    this.formOptions[1]['disabled'] = false;
                    this.formOptions[2]['hidden'] = false;
                } else {
                    //编辑
                    this.formOptions[0]['items'][0]['disabled'] = true;
                    //如果当前用户被封号------2:封号状态码
                    this.formOptions[1]['hidden'] = 2 === this.formData['allowLogin'];
                    this.formOptions[2]['hidden'] = true;
                }
            },
            submit(form) {
                let that = this;
                let {username, realName, allowLogin, password} = form;
                that.$axios({
                    url: that.$global.URL['system']['sysUser'][that.handleType],
                    method: 'post',
                    data: {
                        username, realName, allowLogin,
                        password: that.$globalFun.md5(password)
                    },
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTable');
                    }
                });
            }
        }
    }
</script>