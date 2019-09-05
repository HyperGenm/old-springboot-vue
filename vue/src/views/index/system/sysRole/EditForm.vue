<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions" :formRules="rules"
                     @closeDialog="$emit('closeDialog')" @submit="submit">
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
            }
        },
        watch: {
            formData(formData) {
                this.form = formData;
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
                delete that.form['children'];
                that.$axios({
                    url: that.$global.URL['system']['sysRole'][that.handleType],
                    method: 'post',
                    data: that.form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTree');
                    }
                });
            }
        }
    }
</script>