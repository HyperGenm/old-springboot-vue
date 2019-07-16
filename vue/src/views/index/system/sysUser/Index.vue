<template>
    <div id="sysUser">
        <div class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest" :tableHeaderButtons="tableHeaderButtons"
                       :tableColumns="tableColumns" :tableOperates="tableOperates" :tableSearch="tableSearch">
            </wei-table>
        </div>
        <div class="edit">
            <edit-form :handleType="handleType" :formData="formData" :show.sync="dialogEditForm"
                       @renderTable="$refs.table.renderTable()"></edit-form>
            <wei-dialog :show.sync="dialogRole" :title="userData.username + ': 请选择角色'">
                <role :userData="userData"
                      @closeDialogRole="dialogRole = false"
                      @renderTable="$refs.table.renderTable()"></role>
            </wei-dialog>
        </div>
        <div class="show">
            <detail :show.sync="dialogDetail" :detailData="formData"></detail>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue'),
            'edit-form': () => import('./EditForm.vue'),
            'detail': () => import('./Detail.vue'),
            'role': () => import('./Role.vue'),
            'wei-dialog': () => import('@/components/dialog/index/Index.vue')
        },
        data() {
            let that = this;
            let {
                sysUser_add, sysUser_update, sysUser_delete, sysUser_role, sysUser_resetPwd
            } = that.$store.state.role['buttons'];
            return {
                tableDataRequest: {
                    url: that.$global.URL.getUserList,
                    data: {
                        userName: '',
                        allowLogin: '',
                        createTime: ''
                    }
                },
                tableHeaderButtons: [
                    {
                        name: '新增', icon: 'el-icon-plus', show: sysUser_add, handleClick() {
                            that.handleType = 'add';
                            that.formData = {
                                username: '',
                                realName: '',
                                password: '',
                                allowLogin: 0
                            };
                            that.dialogEditForm = true;
                        }
                    },
                    {
                        name: '删除', icon: 'el-icon-delete', show: sysUser_delete, handleClick(rows) {
                            if (0 >= rows.length) {
                                that.$globalFun.errorMsg('请选择行');
                                return;
                            }
                            let ids = [];
                            for (let i = 0; i < rows.length; i++) {
                                let value = rows[i];
                                if (('0' === value.id + '') || '超级管理员' === value['roleName']) {
                                    that.$globalFun.errorMsg('不能删除超级管理员');
                                    return;
                                }
                                ids.push(value.id);
                            }
                            that.deleteUsers(ids);
                        }
                    }
                ],
                tableColumns: [
                    {prop: 'username', label: '用户名'},
                    {prop: 'roleName', label: '角色'},
                    {prop: 'realName', label: '真实姓名'},
                    {
                        prop: 'title', label: '是否允许登录', formatter(row) {
                            return ('0' === row['allowLogin'] + '') ? '允许' : '禁止'
                        }
                    },
                    {prop: 'lastActiveTime', label: '用户最后活跃时间'},
                    {prop: 'createTime', label: '用户创建时间'}
                ],
                tableOperates: {
                    width: 370,
                    buttons:
                        [
                            {
                                name: '查看', type: 'primary', show: true, handleClick(row) {
                                    that.formData = row;
                                    that.dialogDetail = true;
                                }
                            },
                            {
                                name: '编辑', type: 'success', show: sysUser_update, handleClick(row) {
                                    if (('0' === row.id + '') || '超级管理员' === row['roleName']) {
                                        that.$globalFun.errorMsg('不能编辑超级管理员');
                                        return;
                                    }
                                    that.handleType = 'update';
                                    that.formData = row;
                                    that.dialogEditForm = true;
                                }
                            },
                            {
                                name: '角色', type: 'primary', show: sysUser_role, handleClick(row) {
                                    if (('0' === row.id + '') || '超级管理员' === row['roleName']) {
                                        that.$globalFun.errorMsg('不能操作超级管理员');
                                        return;
                                    }
                                    that.dialogRole = true;
                                    that.userData = row;
                                }
                            },
                            {
                                name: '重置密码', type: 'warning', show: sysUser_resetPwd, handleClick(row) {
                                    if ((('0' === row.id + '') || '超级管理员' === row['roleName']) && that.$store.state.role.name !== '超级管理员') {
                                        that.$globalFun.errorMsg('不能编辑超级管理员');
                                        return;
                                    }
                                    that.resetPassword(row);
                                }
                            },
                            {
                                name: '删除', type: 'danger', show: sysUser_delete, handleClick(row) {
                                    if (('0' === row.id + '') || '超级管理员' === row['roleName']) {
                                        that.$globalFun.errorMsg('不能删除超级管理员');
                                        return;
                                    }
                                    that.deleteUsers([row.id]);
                                }
                            }
                        ]
                },
                tableSearch: [
                    {type: 'input', prop: 'userName', placeholder: '用户名'},
                    {
                        type: 'select', prop: 'allowLogin', placeholder: '是否允许登录', options: [
                            {label: '允许', value: 0},
                            {label: '禁止', value: 1}
                        ]
                    },
                    {type: 'datePicker', prop: 'createTime'}
                ],
                //操作弹出框
                dialogEditForm: false,
                //操作类型新增还是修改
                handleType: null,
                //操作的form表单
                formData: {},
                dialogRole: false,
                userData: {},
                dialogDetail: false
            }
        },
        methods: {
            deleteUsers(ids) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定删除,该操作无法撤销',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.deleteUser,
                            method: 'post',
                            data: {
                                ids
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                that.$refs['table'].renderTable();
                            }
                        });
                    }
                });
            },
            resetPassword(row) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '是否要重置密码!!!',
                    confirm() {
                        that.$globalFun.messageBoxInput({
                            message: '请输入密码',
                            inputType: 'password',
                            confirm(instance, done) {
                                let value = instance.inputValue;
                                if (that.$globalFun.isBlank(value)) {
                                    that.$globalFun.errorMsg('密码不能为空');
                                    return;
                                }
                                if (6 > value.length) {
                                    that.$globalFun.errorMsg('密码不能少于6位');
                                    return;
                                }
                                that.$axios({
                                    url: that.$global.URL.resetUserPassword,
                                    method: 'post',
                                    data: {
                                        userId: row.id,
                                        password: value
                                    },
                                    success() {
                                        that.$globalFun.successMsg('成功');
                                        done();
                                    }
                                })
                            },
                        });
                    }
                });
            }
        }
    }
</script>