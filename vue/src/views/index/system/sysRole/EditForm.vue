<template>
    <div id="form">
        <el-form :model="form" ref="form" size="mini" :rules="rules">
            <el-row>
                <el-col :span="11">
                    <el-form-item label="角色名称" prop="name">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="11" :offset="2">
                    <el-form-item label="上级角色名称">
                        <el-input disabled v-model="parentData.name"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="11">
                    <el-form-item label="排序">
                        <el-input type="number" v-model="form.sort"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="11" :offset="2">
                    <el-form-item label="是否启用">
                        <el-radio-group v-model="form.isStop">
                            <el-radio :label="0">启用</el-radio>
                            <el-radio :label="1">禁用</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="描述">
                <el-input type="textarea" v-model="form.description"></el-input>
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
            parentData: {
                type: Object,
                default: () => {
                }
            },
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
                    name: [
                        {required: true, message: '请输入角色名称', trigger: 'blur'},
                        {min: 2, message: '最少两个字符', trigger: 'blur'}
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
                    let url = this.handleType === 'add' ? 'addRole' : 'updateRole';
                    that.$axios({
                        url: that.$global.URL[url],
                        method: 'post',
                        data: that.form,
                        success() {
                            that.$globalFun.successMsg('成功');
                            that.$emit('closeDialog');
                            that.$emit('renderTree');
                        }
                    })
                });
            }
        }
    }
</script>