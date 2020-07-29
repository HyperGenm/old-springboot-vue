<template>
    <div id="index">
        <wei-table ref="table"
                   :tableDataRequest="tableDataRequest" :tableHeaderButtons="tableHeaderButtons"
                   :tableColumns="tableColumns" :tableOperates="tableOperates"></wei-table>
        <div class="show">
            <wei-dialog :show.sync="dialogDetail">
                <detail :detailData="formData"></detail>
            </wei-dialog>
        </div>
        <div class="edit">
            <wei-dialog :show.sync="dialogEditForm" :title="'add' === handleType ? '新增' : '编辑'">
                <edit-form :handleType="handleType" :formData="formData"
                           @closeDialog="dialogEditForm = false" :isShow="dialogEditForm"
                           @renderTable="$refs.table.renderTable()"></edit-form>
            </wei-dialog>
        </div>
        <div class="tree">
            <wei-dialog :show.sync="dialogFunList">
                <el-tree ref="funTree" node-key="id"
                         highlight-current default-expand-all show-checkbox
                         :expand-on-click-node="false" :data="[{title:'最高级',id:0,children:funData}]"
                         :props="{label: 'title'}"
                         :default-checked-keys="checkedRoleFunctionData"></el-tree>
                <el-button v-if="haveButtonMap['sysRole_save']" type="primary" size="mini"
                           @click="saveRole">保存
                </el-button>
            </wei-dialog>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-dialog': () => import('@/components/dialog/index/Index.vue'),
            'edit-form': () => import('./EditForm.vue'),
            'detail': () => import('./Detail.vue'),
            'wei-table': () => import('@/components/table/Index.vue')
        },
        data() {
            let that = this;
            //用户角色拥有的按钮
            let {
                sysRole_add, sysRole_update, sysRole_delete, sysRole_save, sysRole_status
            } = this.$globalFun.getSessionStorage('buttonMap');
            //超级管理员角色id
            let SUPER_ADMIN_ROLE_ID = this.$global.GLOBAL.super_admin_role_id;
            return {
                //拥有的按钮
                haveButtonMap: that.$globalFun.getSessionStorage('buttonMap'),
                //功能树的数据
                funData: [],
                funParentData: [],
                //选中的功能树的数据
                checkedRoleFunctionData: [],
                //表单弹窗
                dialogEditForm: false,
                //表单类型
                handleType: 'add',
                //表单数据
                formData: {},
                //详情
                dialogDetail: false,
                //选择功能
                dialogFunList: false,
                tableDataRequest: {
                    url: that.$global.URL.system.sysRole.getAllRoleTreePageList
                },
                tableHeaderButtons: [
                    {
                        name: '新增',
                        icon: 'el-icon-plus',
                        type: 'success',
                        show: sysRole_add,
                        handleClick() {
                            that.handleType = 'add';
                            that.formData = {
                                parentId: SUPER_ADMIN_ROLE_ID,
                                name: '',
                                sort: 0,
                                isStop: 0,
                                description: ''
                            };
                            that.dialogEditForm = true;
                        }
                    }
                ],
                tableColumns: [
                    {label: '名称', prop: 'name', width: 210, fixed: 'left'},
                    {
                        label: '状态', prop: 'isStop', type: 'tag',
                        element({isStop}) {
                            let result = [
                                {content: '正常'},
                                {content: '禁用', type: 'danger'}
                            ];
                            return result[isStop] || {
                                content: '未知状态',
                                type: 'danger'
                            };
                        }
                    },
                    {label: '排序', prop: 'sort'},
                    {label: '描述', prop: 'description', showOverflowTooltip: true},
                    {
                        label: '创建时间', prop: 'createTime', showOverflowTooltip: true, type: 'icon',
                        element({createTime}) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: createTime
                            };
                        }
                    }
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
                            name: '编辑', type: 'success',
                            showFormatter(row) {
                                if (!sysRole_update) {
                                    return false;
                                }
                                if (null == row) {
                                    return true;
                                }
                                return SUPER_ADMIN_ROLE_ID !== row['id'];
                            },
                            handleClick(row) {
                                that.handleType = 'update';
                                that.formData = row;
                                that.dialogEditForm = true;
                            }
                        },
                        {
                            name: '修改功能', type: 'primary',
                            showFormatter(row) {
                                if (!sysRole_save) {
                                    return false;
                                }
                                if (null == row) {
                                    return true;
                                }
                                //角色被禁用不显示该按钮
                                return 0 === row['isStop'];
                            },
                            handleClick(row) {
                                that.formData = row;
                                that.getRoleFunList(row['id']);
                                that.dialogFunList = true;
                            }
                        },
                        {
                            name: '删除', type: 'danger',
                            showFormatter(row) {
                                if (!sysRole_delete) {
                                    return false;
                                }
                                if (null == row) {
                                    return true;
                                }
                                return SUPER_ADMIN_ROLE_ID !== row['id'];
                            },
                            handleClick(row) {
                                that.deleteRole(row);
                            }
                        },
                        {
                            name: '启用', type: 'success',
                            showFormatter(row) {
                                if (!sysRole_status) {
                                    return false;
                                }
                                if (null == row) {
                                    return true;
                                }
                                if (SUPER_ADMIN_ROLE_ID === row['id']) {
                                    return false;
                                }
                                return 0 !== row['isStop'];
                            },
                            handleClick(row) {
                                that.formData = row;
                                that.changeRoleIsStop(row, 0);
                            }
                        },
                        {
                            name: '禁用', type: 'danger',
                            showFormatter(row) {
                                if (!sysRole_status) {
                                    return false;
                                }
                                if (null == row) {
                                    return true;
                                }
                                if (SUPER_ADMIN_ROLE_ID === row['id']) {
                                    return false;
                                }
                                return 1 !== row['isStop'];
                            },
                            handleClick(row) {
                                that.formData = row;
                                that.changeRoleIsStop(row, 1);
                            }
                        }
                    ]
                },
            }
        },
        mounted() {
            let that = this;
            //获取功能树形列表
            this.$axios({
                url: that.$global.URL.system.sysFunction.getAllFunctionTree,
                success(data) {
                    that.funData = data;
                    that.handleFunParent(data);
                }
            });
        },
        methods: {
            //获取功能树所有的父级id
            handleFunParent(data) {
                let funParent = [];
                let that = this;
                data.forEach((value) => {
                    if (null !== value.children && 0 < value.children.length) {
                        funParent.push(value.id);
                        funParent = funParent.concat(that.findFunParentChildren(value.children));
                    }
                });
                this.funParentData = funParent;
            },
            findFunParentChildren(data) {
                let result = [];
                let that = this;
                data.forEach((value) => {
                    if (null !== value.children && 0 < value.children.length) {
                        result.push(value.id);
                        result = result.concat(that.findFunParentChildren(value.children));
                    }
                });
                return result;
            },
            //获取角色的功能列表
            getRoleFunList(roleId) {
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysRole.getRoleFunList,
                    data: {
                        roleId
                    },
                    success(data) {
                        that.$refs['funTree'].setCheckedKeys([]);
                        let checkedRoleFunctionData = JSON.parse(JSON.stringify(data));
                        let funParentData = JSON.parse(JSON.stringify(that.funParentData));
                        for (let i = 0; i < checkedRoleFunctionData.length; i++) {
                            let value = checkedRoleFunctionData[i];
                            for (let j = 0; j < funParentData.length; j++) {
                                let funParent = funParentData[j];
                                if (value === funParent) {
                                    checkedRoleFunctionData.splice(i, 1);
                                    i--;
                                    funParentData.splice(j, 1);
                                    j--;
                                }
                            }
                        }
                        that.checkedRoleFunctionData = checkedRoleFunctionData;
                    }
                });
            },
            deleteRole(row) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定删除,该操作无法撤销',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysRole.delete,
                            method: 'post',
                            data: {
                                roleId: row.id
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                that.$refs.table.renderTable();
                            }
                        })
                    }
                });
            },
            //保存角色的功能
            saveRole() {
                let getCheckedKeys = this.$refs['funTree'].getCheckedKeys();
                let getHalfCheckedKeys = this.$refs['funTree'].getHalfCheckedKeys();
                let funIds = getCheckedKeys.concat(getHalfCheckedKeys);
                funIds.forEach((value, index) => {
                    if ('0' === value + '') {
                        funIds.splice(index, 1);
                    }
                });
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysRole.addRoleFun,
                    method: 'post',
                    data: {
                        roleId: that.formData['id'],
                        funIds
                    },
                    success() {
                        that.$globalFun.successMsg('保存成功');
                    }
                });
            },
            changeRoleIsStop(row, isStop) {
                let that = this;
                this.$globalFun.messageBox({
                    message: `是否${0 === isStop ? '启用' : '禁用'}角色,该操作将会同时改变下级状态`,
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysRole.changeRoleIsStop,
                            method: 'post',
                            data: {
                                roleId: that.formData.id,
                                isStop
                            },
                            success() {
                                that.$globalFun.successMsg('成功');
                                that.$refs.table.renderTable();
                            }
                        });
                    }
                });
            }
        }
    }
</script>