<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions"
                     @closeDialog="$emit('closeDialog')" @submit="submit">
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
                this.changeOptions();
            },
            handleType() {
                this.changeOptions();
            }
        },
        data() {
            let SUPER_ADMIN_ID = this.$global.GLOBAL.super_admin_id;
            let SUPER_ADMIN_ROLE_ID = this.$global.GLOBAL.super_admin_role_id;
            //是否可以编辑 功能对应的api
            let isEditApi = (SUPER_ADMIN_ID === this.$globalFun.getSessionStorage('userInfo')['id']
                || SUPER_ADMIN_ROLE_ID === this.$globalFun.getSessionStorage('userInfo')['roleId']);
            let that = this;
            return {
                formOptions: [
                    {
                        items: [
                            {type: 'input', label: '标题', prop: 'title', required: true},
                            {
                                type: 'input',
                                label: '功能名name',
                                prop: 'name',
                                required: true,
                                disabled: 'update' === that.handleType
                            }
                        ]
                    },
                    {
                        items: [
                            {
                                type: 'cascader',
                                label: '上级菜单',
                                prop: 'parentId',
                                required: true,
                                options: [],
                                hidden: !isEditApi,
                                disabled: 'update' === that.handleType
                            },
                            {type: 'input', label: '功能路径', prop: 'path', required: true, hidden: !isEditApi},
                        ]
                    },
                    {type: 'textarea', label: '功能对应的api', prop: 'containApi', hidden: !isEditApi},
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number', required: true},
                    {
                        type: 'radio', label: '类型', prop: 'type', required: true, hidden: !isEditApi, options: [
                            {label: '菜单', value: 0},
                            {label: '按钮', value: 1}
                        ]
                    },
                    {type: 'textarea', label: '路由描述', prop: 'description'}
                ],
                form: that.formData,
                dialogIcons: false,
                funTreeList: []
            }
        },
        mounted() {
            this.initFunTreeSelect();
        },
        methods: {
            initFunTreeSelect() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysFunction.getAllFunctionTreeNotButton,
                    success(data) {
                        let result = [];
                        data.forEach(value => {
                            let {id, title, children} = value;
                            let item = {
                                label: title,
                                value: id
                            };
                            if (null != children && 0 < children.length) {
                                item['children'] = that.handleFunTreeChildrenList(children);
                            }
                            result.push(item);
                        });
                        that.formOptions[1]['items'][0]['options'] = [{
                            label: '最高级',
                            value: 0,
                            children: result
                        }];
                    }
                });
            },
            handleFunTreeChildrenList(list) {
                let that = this;
                let result = [];
                list.forEach(value => {
                    let {id, title, children} = value;
                    let item = {
                        label: title,
                        value: id
                    };
                    if (null != children && 0 < children.length) {
                        item['children'] = that.handleFunTreeChildrenList(children);
                    }
                    result.push(item);
                });
                return result;
            },
            changeOptions() {
                this.form = this.formData;
                this.formOptions[0]['items'][1]['disabled'] = 'update' === this.handleType;
                this.formOptions[1]['items'][0]['disabled'] = 'update' === this.handleType;
            },
            submit(form) {
                //`el-select`级联选择器是个数组
                let {parentId} = form;
                if (Array.isArray(parentId)) {
                    let max = 0;
                    parentId.forEach(value => {
                        max = max > value ? max : value;
                    });
                    form['parentId'] = max;
                }
                form['icon'] = this.form['icon'];
                let that = this;
                that.$axios({
                    url: that.$global.URL['system']['sysFunction'][that.handleType],
                    method: 'post',
                    data: form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTable');
                        that.initFunTreeSelect();
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