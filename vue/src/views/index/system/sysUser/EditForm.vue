<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions" :formRules="rules"
                     @closeDialog="$emit('closeDialog')" @submit="submit">
        </dialog-form>
    </div>
</template>

<script>
    const baseOptions = [
        {type: 'input', label: '用户名', prop: 'username'},
        {type: 'input', label: '真实姓名', prop: 'realName', required: true},
        {
            type: 'radio', label: '是否允许登录', prop: 'allowLogin', options: [
                {label: '允许', value: 0},
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
                this.formOptions = JSON.parse(JSON.stringify(baseOptions));
                this.formOptions[0]['disabled'] = true;
                //如果当前用户被封号
                if ('update' === this.handleType && 2 === data['allowLogin']) {
                    this.formOptions.splice(2);
                }
            },
            handleType(handleType) {
                this.formOptions = JSON.parse(JSON.stringify(baseOptions));
                if ('add' === handleType) {
                    this.formOptions.push({type: 'input', label: '密码', prop: 'password', inputType: 'password'});
                } else {
                    this.formOptions[0]['disabled'] = true;
                    //如果当前用户被封号
                    if (2 === this.formData['allowLogin']) {
                        this.formOptions.splice(2);
                    }
                }
            }
        },
        data() {
            return {
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
                formOptions: JSON.parse(JSON.stringify(baseOptions))
            }
        },
        methods: {
            submit() {
                let that = this;
                that.$axios({
                    url: that.$global.URL[url]['system']['sysUser'][that.handleType],
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