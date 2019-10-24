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
            handleType(type) {
                this.formOptions[0]['hidden'] = 'update' === type;
                this.formOptions[3]['hidden'] = 'update' === type;
            },
            formData(formData) {
                this.form = formData;
            }
        },
        data() {
            let that = this;
            return {
                rules: {
                    name: [
                        {required: true, message: '请输入角色名称', trigger: 'blur'},
                        {min: 2, message: '最少两个字符', trigger: 'blur'}
                    ]
                },
                formOptions: [
                    {
                        type: 'select', label: '上级', prop: 'parentId', required: true,
                        options: [], hidden: 'update' === that.handleType
                    },
                    {type: 'input', label: '角色名', prop: 'name', required: true},
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number'},
                    {
                        type: 'radio', label: '是否启用', prop: 'isStop', required: true, options: [
                            {label: '启用', value: 0},
                            {label: '禁用', value: 1}
                        ],
                        hidden: 'update' === that.handleType
                    },
                    {type: 'textarea', label: '描述', prop: 'description'}
                ],
                form: that.formData
            }
        },
        mounted() {
            this.getRoleList();
        },
        methods: {
            getRoleList() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysRole.getList,
                    success(data) {
                        let options = [];
                        data.forEach(value => {
                            options.push({
                                label: value['name'],
                                value: value['id']
                            });
                        });
                        that.formOptions[0]['options'] = options;
                    }
                });
            },
            submit(form) {
                let that = this;
                that.$axios({
                    url: that.$global.URL['system']['sysRole'][that.handleType],
                    method: 'post',
                    data: form,
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