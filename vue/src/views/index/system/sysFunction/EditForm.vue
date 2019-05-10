<template>
    <div id="form">
        <el-form ref="form" size="mini" :model="form" :rules="rules" :disabled="handleType === 'detail'">
            <el-row>
                <el-col :span="11">
                    <el-form-item label="路由标题" prop="title">
                        <el-input v-model="form.title"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="11" :offset="2">
                    <el-form-item label="路由名name" prop="name">
                        <el-input :disabled="handleType === 'update'" v-model="form.name"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="11">
                    <el-form-item label="上级路由">
                        <el-input disabled v-model="parentData.title"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="11" :offset="2">
                    <el-form-item label="排序">
                        <el-input type="number" v-model="form.sort"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="11">
                    <el-form-item label="路由图标">
                        <el-input v-model="form.icon" style="display: none;"></el-input>
                        <el-button type="primary" @click="dialogIcons = true">选择图标
                        </el-button>
                        <i style="margin-left: 20px;font-size: 20px;" :class="form.icon"></i>
                    </el-form-item>
                </el-col>
                <el-col :span="11" :offset="2">
                    <el-form-item label="类型">
                        <el-radio-group v-model="form.type">
                            <el-radio :label="0">菜单</el-radio>
                            <el-radio :label="1">按钮</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="路由描述">
                <el-input type="textarea" v-model="form.description"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="$emit('closeDialog')">取 消</el-button>
            <el-button v-show="handleType !== 'detail'" type="primary" @click="ok">确 定</el-button>
        </div>
        <div class="icons">
            <wei-icons :dialogIcons="dialogIcons" @chooseIconOk="chooseIconOk"></wei-icons>
        </div>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        components: {
            'wei-icons': () => import('@/components/dialog/icons/Index.vue')
        },
        props: {
            parentData: {
                type: Object
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
                    title: [
                        {required: true, message: '请输入标题', trigger: 'blur'},
                        {min: 2, message: '最少两个字符', trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入name', trigger: 'blur'}
                    ]
                },
                form: this.formData,
                dialogIcons: false
            }
        },
        methods: {
            ok() {
                let that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                    let url = this.handleType === 'add' ? 'addFunction' : 'updateFunction';
                    that.$axios({
                        url: that.$global.URL[url],
                        method: 'post',
                        data: that.form,
                        success() {
                            that.$globalFun.successMsg('成功');
                            that.$emit('closeDialog');
                            that.$emit('renderTable');
                            that.$emit('renderTree');
                        }
                    })
                });
            },
            chooseIconOk(icon) {
                this.dialogIcons = false;
                this.form.icon = icon;
            }
        }
    }
</script>