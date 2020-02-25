<template>
    <div id="index">
        <el-divider content-position="left">ip规则</el-divider>
        <div style="margin-left: 50px;">
            <el-radio-group v-model="ipRole" :disabled="!isAddAndDelete">
                <el-radio label="all">允许所有</el-radio>
                <el-radio label="white">只允许白名单</el-radio>
                <el-radio label="black">只禁止黑名单</el-radio>
            </el-radio-group>
            <el-button v-if="isAddAndDelete" type="primary" size="small" style="margin-left: 77px;"
                       @click="updateIpRole">设置
            </el-button>
        </div>
        <el-divider content-position="left">ip列表</el-divider>
        <div class="wei-table" style="height: 67%;">
            <wei-table ref="table" :isPagination="false"
                       :tableDataRequest="tableDataRequest" :tableColumns="tableColumns"
                       :tableSearch="tableSearch" :tableHeaderButtons="tableHeaderButtons"
                       :tableOperates="tableOperates"></wei-table>
        </div>
        <div class="edit">
            <wei-dialog :show.sync="dialogEditForm" title="新增">
                <edit-form @closeDialog="dialogEditForm = false"
                           @renderTable="$refs.table.renderTable()"></edit-form>
            </wei-dialog>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index'),
            'wei-dialog': () => import('@/components/dialog/index/Index.vue'),
            'edit-form': () => import('./EditForm.vue'),
        },
        data() {
            let that = this;
            let SUPER_ADMIN_ID = this.$global.GLOBAL.super_admin_id;
            let SUPER_ADMIN_ROLE_ID = this.$global.GLOBAL.super_admin_role_id;
            //是否可以编辑 功能对应的api
            let isAddAndDelete = (SUPER_ADMIN_ID === this.$store.state.userInfo['id']
                || SUPER_ADMIN_ROLE_ID === this.$store.state.userInfo['roleId']);
            return {
                ipRole: '',
                //是否可以编辑 功能对应的api
                isAddAndDelete: isAddAndDelete,
                tableDataRequest: {
                    url: that.$global.URL.dataDictionary.ipManager.getIpList,
                    data: {
                        type: ''
                    }
                },
                tableHeaderButtons: [
                    {
                        name: '新增', icon: 'el-icon-plus', type: 'success', show: isAddAndDelete, handleClick() {
                            that.dialogEditForm = true;
                        }
                    }
                ],
                tableColumns: [
                    {label: 'ip', prop: 'value'},
                    {
                        label: '类型', prop: 'dictionaryCode', type: 'tag',
                        element({dictionaryCode}) {
                            let result = {
                                ipListWhite: {
                                    content: '白名单',
                                    type: 'success'
                                },
                                ipListBlack: {
                                    content: '黑名单',
                                    type: 'danger'
                                },
                                ipListAbnormal: {
                                    content: '异常名单',
                                    type: 'warning'
                                }
                            };
                            return result[dictionaryCode] || {
                                content: '未知类型',
                                type: 'danger'
                            };
                        }
                    },
                    {label: '标题', prop: 'name'},
                    {label: '备注', prop: 'remark'},
                    {
                        label: '创建时间', prop: 'createTime', type: 'icon', element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['createTime']
                            };
                        }
                    }
                ],
                tableOperates: {
                    buttons: [
                        {
                            name: '删除', type: 'danger', show: isAddAndDelete, handleClick(row) {
                                that.deleteIp(row.id);
                            }
                        }
                    ]
                },
                tableSearch: [
                    {type: 'input', prop: 'ipAddress', placeholder: 'ip地址'},
                    {
                        type: 'select', prop: 'type', placeholder: '类型',
                        options: [
                            {label: '白名单', value: 'ipListWhite'},
                            {label: '黑名单', value: 'ipListBlack'},
                            {label: '异常名单', value: 'ipListAbnormal'}
                        ]
                    },
                ],
                //新增弹出框
                dialogEditForm: false
            }
        },
        mounted() {
            this.getIpRole();
        },
        methods: {
            getIpRole() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.dataDictionary.ipManager.getIpRole,
                    success(data) {
                        that.ipRole = data;
                    }
                });
            },
            updateIpRole() {
                let that = this;
                this.$globalFun.messageBox({
                    message: '是否更新ip规则',
                    confirm() {
                        if (that.$globalFun.isBlank(that.ipRole)) {
                            that.$globalFun.errorMsg("请选择ip规则");
                            return;
                        }
                        that.$axios({
                            url: that.$global.URL.dataDictionary.ipManager.updateIpRole,
                            method: 'post',
                            data: {
                                role: that.ipRole
                            },
                            success() {
                                that.$globalFun.successMsg('更新成功');
                            }
                        })
                    }
                })
            },
            deleteIp(id) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定删除，该操作无法撤销',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.dataDictionary.ipManager.deleteIp,
                            method: 'post',
                            data: {
                                id
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                that.$refs['table'].renderTable();
                            }
                        })
                    }
                })
            }
        }
    }
</script>