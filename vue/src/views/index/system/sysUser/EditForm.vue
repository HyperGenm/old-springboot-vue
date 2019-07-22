<template>
    <div id="form">
        <dialog-form :show.sync="visible" :title="'add' === handleType ? '新增' : '编辑'"
                     :formData="form" :formOptions="formOptions" :formRules="rules"
                     @submit="submit">
        </dialog-form>
    </div>
</template>

<script>
    const baseOptions = [
        {type: 'input', label: '用户名', prop: 'username'},
        {type: 'input', label: '真实姓名', prop: 'realName'},
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
            },
            show: {
                type: Boolean,
                default: false
            }
        },
        watch: {
            show() {
                this.visible = this.show;
            },
            visible() {
                if (!this.visible) {
                    this.$emit('update:show', false);
                }
            },
            formData() {
                this.form = this.formData;
            },
            handleType() {
                this.formOptions = JSON.parse(JSON.stringify(baseOptions));
                if ('add' === this.handleType) {
                    this.formOptions.push({type: 'input', label: '密码', prop: 'password', inputType: 'password'});
                } else {
                    this.formOptions[0]['disabled'] = true;
                }
            }
        },
        data() {
            return {
                visible: false,
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
                let url = this.handleType === 'add' ? 'sysUserAdd' : 'sysUserUpdate';
                that.$axios({
                    url: that.$global.URL[url],
                    method: 'post',
                    data: that.form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('update:show', false);
                        that.$emit('renderTable');
                    }
                });
            }
        }
    }
</script>