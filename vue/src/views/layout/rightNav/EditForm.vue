<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions" :formRules="rules"
                     @closeDialog="$emit('closeDialog')" @submit="submit">
        </dialog-form>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        components: {
            'dialog-form': () => import('@/components/dialog/form/Index.vue')
        },
        data() {
            let that = this;
            return {
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
                let {oldPwd, newPwd} = that.form;
                that.$axios({
                    url: that.$global.URL.system.sysUser.updateUserPassword,
                    method: 'post',
                    data: {
                        oldPwd: that.$globalFun.md5(oldPwd),
                        newPwd: that.$globalFun.md5(newPwd),
                    },
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                    }
                });
            }
        }
    }
</script>