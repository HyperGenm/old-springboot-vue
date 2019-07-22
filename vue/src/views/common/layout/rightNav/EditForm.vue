<template>
    <div id="form">
        <dialog-form :show.sync="visible" title="修改密码"
                     :formData="form" :formOptions="formOptions" :formRules="rules"
                     @submit="submit">
        </dialog-form>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        components: {
            'dialog-form': () => import('@/components/dialog/form/Index.vue')
        },
        props: {
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
            }
        },
        data() {
            let that = this;
            return {
                visible: false,
                formOptions: [
                    {type: 'input', label: '旧密码', prop: 'oldPwd', inputType: 'password'},
                    {type: 'input', label: '新密码', prop: 'newPwd', inputType: 'password'},
                    {type: 'input', label: '重复密码', prop: 'againPwd', inputType: 'password', required: true}
                ],
                rules: {
                    oldPwd: [
                        {required: true, message: '请输入原密码', trigger: 'blur'},
                        {min: 6, message: '密码最少6位', trigger: 'blur'}
                    ],
                    newPwd: [
                        {required: true, message: '请输入新密码', trigger: 'blur'},
                        {min: 6, message: '密码最少6位', trigger: 'blur'}
                    ],
                    againPwd: [
                        {
                            validator(rule, value, callback) {
                                let newPwd = that.form['newPwd'];
                                if (newPwd !== value) {
                                    callback(new Error('两次输入密码不一致!'));
                                    return;
                                }
                                callback();
                            }, trigger: 'blur'
                        }
                    ]
                },
                form: {
                    oldPwd: '',
                    newPwd: ''
                }
            }
        },
        methods: {
            submit() {
                let that = this;
                that.$axios({
                    url: that.$global.URL.sysUserUpdateUserPassword,
                    method: 'post',
                    data: that.form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('update:show', false);
                    }
                });
            }
        }
    }
</script>