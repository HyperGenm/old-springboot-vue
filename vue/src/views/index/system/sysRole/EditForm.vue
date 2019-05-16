<template>
    <div id="form">
        <dialog-form :show.sync="visible" :title="'add' === handleType ? '新增' : '编辑'"
                     :formData="form" :formOptions="formOptions" :formRules="rules"
                     @submit="submit">
            <template v-if="'add' === handleType" v-slot:itemHead>
                <el-form-item label="上级角色名称">
                    <el-input disabled v-model="parentData.name"></el-input>
                </el-form-item>
            </template>
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
            }
        },
        data() {
            return {
                visible: false,
                rules: {
                    name: [
                        {required: true, message: '请输入角色名称', trigger: 'blur'},
                        {min: 2, message: '最少两个字符', trigger: 'blur'}
                    ]
                },
                formOptions: [
                    {type: 'input', label: '角色名', prop: 'name'},
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number'},
                    {
                        type: 'radio', label: '是否启用', prop: 'isStop', options: [
                            {label: '启用', value: 0},
                            {label: '禁用', value: 1}
                        ]
                    },
                    {type: 'textarea', label: '描述', prop: 'description'}
                ],
                form: this.formData
            }
        },
        methods: {
            submit() {
                let that = this;
                let url = this.handleType === 'add' ? 'addRole' : 'updateRole';
                delete that.form['children'];
                that.$axios({
                    url: that.$global.URL[url],
                    method: 'post',
                    data: that.form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('update:show', false);
                        that.$emit('renderTree');
                    }
                });
            }
        }
    }
</script>