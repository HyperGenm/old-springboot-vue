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
                // 表单每一项，具体参考 /src/components/dialog/form/Index.vue
                formOptions: [
                    {type: 'input', label: '用户名', prop: 'username'},
                    {type: 'input', label: '真实姓名', prop: 'realName', required: true},
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
                        ]
                    },
                    {type: 'input', label: '密码', prop: 'password', inputType: 'password', disabled: true}
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
                    this.rules.password[0]['required'] = true;
                    this.formOptions[0]['disabled'] = false;
                    this.formOptions[2]['disabled'] = false;
                    this.formOptions[3]['disabled'] = false;
                } else {
                    //编辑
                    this.rules.password[0]['required'] = false;
                    this.formOptions[0]['disabled'] = true;
                    this.formOptions[3]['disabled'] = true;
                    //如果当前用户被封号
                    this.formOptions[2]['disabled'] = 2 === this.formData['allowLogin'];
                }
            },
            submit(form) {
                let that = this;
                that.$axios({
                    url: that.$global.URL['system']['sysUser'][that.handleType],
                    method: 'post',
                    data: form,
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