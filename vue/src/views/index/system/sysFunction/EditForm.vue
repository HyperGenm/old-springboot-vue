<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions"
                     @closeDialog="$emit('closeDialog')" @submit="submit">
            <template v-slot:itemHead>
                <el-form-item label="上级路由">
                    <el-input disabled v-model="parentData.title"></el-input>
                </el-form-item>
            </template>
            <template v-slot:itemTail>
                <el-form-item label="路由图标">
                    <el-input v-model="form.icon" style="display: none;"></el-input>
                    <el-button type="primary" @click="dialogIcons = true">选择图标
                    </el-button>
                    <i style="margin-left: 20px;font-size: 20px;" :class="form.icon"></i>
                </el-form-item>
            </template>
        </dialog-form>
        <div class="icons">
            <wei-icons :dialogIcons="dialogIcons" @chooseIconOk="chooseIconOk"></wei-icons>
        </div>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        components: {
            'dialog-form': () => import('@/components/dialog/form/Index.vue'),
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
            formData(formData) {
                this.form = formData;
                this.formOptions[1]['disabled'] = 'update' === this.handleType;
            },
            handleType(handleType) {
                this.formOptions[1]['disabled'] = 'update' === handleType;
            }
        },
        data() {
            let SUPER_ADMIN_ID = this.$global.GLOBAL.super_admin_id;
            let SUPER_ADMIN_ROLE_ID = this.$global.GLOBAL.super_admin_role_id;
            //是否可以编辑 功能对应的api
            let isEditApi = (SUPER_ADMIN_ID === this.$store.state.userInfo['id']
                || SUPER_ADMIN_ROLE_ID === this.$store.state.userInfo['roleId']);
            let that = this;
            return {
                formOptions: [
                    {type: 'input', label: '标题', prop: 'title', required: true},
                    {
                        type: 'input',
                        label: '功能名name',
                        prop: 'name',
                        required: true,
                        disabled: 'update' === that.handleType
                    },
                    {type: 'input', label: '功能路径', prop: 'path', required: true, hidden: !isEditApi},
                    {type: 'textarea', label: '功能对应的api', prop: 'containApi', hidden: !isEditApi},
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number', required: true},
                    {
                        type: 'radio', label: '类型', prop: 'type', required: true, options: [
                            {label: '菜单', value: 0},
                            {label: '按钮', value: 1}
                        ]
                    },
                    {type: 'textarea', label: '路由描述', prop: 'description'}
                ],
                form: this.formData,
                dialogIcons: false
            }
        },
        methods: {
            submit(form) {
                if ('add' === this.handleType) {
                    form['parentId'] = this.parentData['id'];
                } else {
                    form['parentId'] = this.formData['parentId'];
                }
                let that = this;
                that.$axios({
                    url: that.$global.URL['system']['sysFunction'][that.handleType],
                    method: 'post',
                    data: form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTable');
                        that.$emit('renderTree');
                    }
                });
            },
            chooseIconOk(icon) {
                this.dialogIcons = false;
                this.form.icon = icon;
            }
        }
    }
</script>