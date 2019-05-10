<template>
    <div id="form">
        <el-form ref="form" :model="form" :rules="rules">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item label="是否允许登录">
                <el-radio-group v-model="form.allowLogin">
                    <el-radio :label="0">允许</el-radio>
                    <el-radio :label="1">禁止</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item v-if="handleType === 'add'" label="密码" prop="password">
                <el-input type="password" v-model="form.password"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="$emit('closeDialog')">取 消</el-button>
            <el-button type="primary" @click="ok">确 定</el-button>
        </div>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
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
            formData() {
                this.form = this.formData;
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
                form: this.formData
            }
        },
        methods: {
            ok() {
                let that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                    let url = this.handleType === 'add' ? 'addUser' : 'updateUser';
                    that.$axios({
                        url: that.$global.URL[url],
                        method: 'post',
                        data: that.form,
                        success() {
                            that.$globalFun.successMsg('成功');
                            that.$emit('closeDialog');
                            that.$emit('renderTable');
                        }
                    })
                });
            }
        }
    }
</script>