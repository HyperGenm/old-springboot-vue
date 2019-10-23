<template>
    <div id="index">
        <wei-table ref="table"
                   :tableDataRequest="tableDataRequest" :tableHeaderButtons="tableHeaderButtons"
                   :tableColumns="tableColumns" :tableOperates="tableOperates"></wei-table>
        <div class="handle">
            <div class="show">
                <wei-dialog :show.sync="dialogDetail">
                    <detail :detailData="formData"></detail>
                </wei-dialog>
            </div>
            <div class="edit">
                <wei-dialog :show.sync="dialogEditForm" :title="'add' === handleType ? '新增' : '编辑'">
                    <edit-form :handleType="handleType" :formData="formData"
                               @closeDialog="dialogEditForm = false"
                               @renderTable="$refs.table.renderTable()"></edit-form>
                </wei-dialog>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue'),
            'wei-dialog': () => import('@/components/dialog/index/Index.vue'),
            'edit-form': () => import('./EditForm.vue'),
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            let {
                sysFunc_update
            } = that.$store.state.role['buttons'];
            let SUPER_ADMIN_ID = this.$global.GLOBAL.super_admin_id;
            let SUPER_ADMIN_ROLE_ID = this.$global.GLOBAL.super_admin_role_id;
            //是否可以编辑 功能对应的api
            let isAddAndDelete = (SUPER_ADMIN_ID === this.$store.state.userInfo['id']
                || SUPER_ADMIN_ROLE_ID === this.$store.state.userInfo['roleId']);
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.sysFunction.getAllFunctionTreePageList
                },
                tableHeaderButtons: [
                    {
                        name: '新增', icon: 'el-icon-plus', type: 'success', show: isAddAndDelete, handleClick() {
                            that.handleType = 'add';
                            that.formData = {
                                name: '',
                                path: '',
                                title: '',
                                icon: '',
                                sort: 0,
                                type: 0,
                                description: '',
                                parentId: 0
                            };
                            that.dialogEditForm = true;
                        }
                    },
                    {
                        name: '删除', icon: 'el-icon-delete', type: 'danger', show: isAddAndDelete, handleClick(rows) {
                            let ids = [];
                            rows.forEach((value) => {
                                ids.push(value.id);
                            });
                            if (0 >= ids.length) {
                                that.$globalFun.errorMsg('请选择行');
                                return;
                            }
                            that.deleteFunction(ids);
                        }
                    }
                ],
                tableColumns: [
                    {label: '路由标题', prop: 'title', width: 210},
                    {label: '路径', prop: 'path'},
                    {label: '功能名', prop: 'name'},
                    {label: '对应api', prop: 'containApi'},
                    {
                        label: '类型', prop: 'type', type: 'tag',
                        element({type}) {
                            let result = [
                                {content: '菜单'},
                                {content: '按钮', type: 'success'}
                            ];
                            return result[type];
                        }
                    },
                    {
                        label: '路由图标', prop: 'icon', formatter(row) {
                            return `<i class="${row.icon}"></i>`;
                        }
                    },
                    {label: '排序', prop: 'sort'},
                    {label: '路由描述', prop: 'description'}
                ],
                tableOperates: {
                    buttons: [
                        {
                            name: '查看', type: 'primary', show: true, handleClick(row) {
                                that.formData = row;
                                that.dialogDetail = true;
                            }
                        },
                        {
                            name: '编辑', type: 'success', show: sysFunc_update, handleClick(row) {
                                that.handleType = 'update';
                                that.formData = row;
                                that.dialogEditForm = true;
                            }
                        },
                        {
                            name: '删除', type: 'danger', show: isAddAndDelete, handleClick(row) {
                                that.deleteFunction([row.id]);
                            }
                        }
                    ]
                },
                //表单弹窗
                dialogEditForm: false,
                //是新增还是修改
                handleType: 'add',
                //表单数据
                formData: {},
                //详情弹窗
                dialogDetail: false
            };
        },
        methods: {
            //删除方法
            deleteFunction(ids) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定删除，该操作无法撤销',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysFunction.delete,
                            method: 'post',
                            data: {
                                ids
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                that.$refs['table'].renderTable();
                            }
                        })
                    }
                });
            }
        }
    }
</script>