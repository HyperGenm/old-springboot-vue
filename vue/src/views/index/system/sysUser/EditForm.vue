<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions" :formRules="rules"
                     @closeDialog="$emit('closeDialog')" @submit="submit">
        </dialog-form>
    </div>
</template>

<!--简单表单可以参考/src/views/index/system/sysRole/EditForm.vue-->

<script>
    // 表单每一项，具体参考 /src/components/dialog/form/Index.vue
    const baseOptions = [
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
        }
    ];
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
                formOptions: []
            }
        },
        mounted() {
            this.changeFormOptions();
        },
        methods: {
            //本次，新增和编辑有不同表单项，所以作此判断
            changeFormOptions() {
                this.formOptions = JSON.parse(JSON.stringify(baseOptions));
                if ('add' === this.handleType) {
                    this.formOptions.push({type: 'input', label: '密码', prop: 'password', inputType: 'password'});
                } else {
                    this.formOptions[0]['disabled'] = true;
                    //如果当前用户被封号
                    if (2 === this.formData['allowLogin']) {
                        this.formOptions.splice(2);
                    }
                }
            },
            submit() {
                let that = this;
                that.$axios({
                    url: that.$global.URL['system']['sysUser'][that.handleType],
                    method: 'post',
                    data: that.form,
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