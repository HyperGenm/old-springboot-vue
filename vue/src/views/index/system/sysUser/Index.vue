<template>
    <div id="index">
        <!--表格展示，如果外面包裹div一定要设置class="wei-table"-->
        <div class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest" :tableHeaderButtons="tableHeaderButtons"
                       :tableColumns="tableColumns" :tableOperates="tableOperates" :tableSearch="tableSearch">
            </wei-table>
        </div>
        <div class="edit">
            <!--新增、编辑弹出框，对应EditForm.vue文件-->
            <wei-dialog :show.sync="dialogEditForm" :title="'add' === handleType ? '新增' : '编辑'">
                <edit-form :handleType="handleType" :formData="formData"
                           @closeDialog="dialogEditForm = false"
                           @renderTable="$refs.table.renderTable()"></edit-form>
            </wei-dialog>
            <wei-dialog :show.sync="dialogRole" :title="userData.username + ': 请选择角色'">
                <role :userData="userData"
                      @closeDialogRole="dialogRole = false"
                      @renderTable="$refs.table.renderTable()"></role>
            </wei-dialog>
        </div>
        <div class="show">
            <!--展示详情弹出框，对应Detail.vue文件-->
            <wei-dialog :show.sync="dialogDetail">
                <detail :detailData="formData"></detail>
            </wei-dialog>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        //引入组件
        components: {
            'wei-table': () => import('@/components/table/Index.vue'),
            'edit-form': () => import('./EditForm.vue'),
            'detail': () => import('./Detail.vue'),
            'role': () => import('./Role.vue'),
            'wei-dialog': () => import('@/components/dialog/index/Index.vue')
        },
        data() {
            let that = this;
            //获取用户角色所拥有的按钮
            let {
                sysUser_add, sysUser_update, sysUser_delete, sysUser_role, sysUser_resetPwd
            } = that.$store.state.role['buttons'];
            //获取超级用户id(一般用不到)
            let SUPER_ADMIN_ID = that.$global.GLOBAL.super_admin_id;
            //获取超级管理员id(一般用不到)
            let SUPER_ADMIN_ROLE_ID = that.$global.GLOBAL.super_admin_role_id;
            //当前页面 解除封号 按钮，只有超级管理员才拥有(其它页面一般用不到)
            let showRelieveSuspend = (SUPER_ADMIN_ID === that.$store.state.userInfo['id']
                || SUPER_ADMIN_ROLE_ID === that.$store.state.userInfo['roleId']);
            return {
                //表格请求
                tableDataRequest: {
                    //请求的url
                    url: that.$global.URL.system.sysUser.getPageList,
                    //data默认添加,不然报错
                    data: {}
                },
                //表格上面按钮
                tableHeaderButtons: [
                    {
                        //按钮名字
                        name: '新增',
                        //按钮前面图标样式
                        icon: 'el-icon-plus',
                        //按钮的颜色样式
                        type: 'success',
                        //是否展示该按钮,对应上面用户角色所对应的按钮
                        show: sysUser_add,
                        //按钮的点击事件,内置参数 rows ,当前所有选中的表格行数组
                        handleClick() {
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
                        name: '删除', icon: 'el-icon-delete', type: 'danger', show: sysUser_delete, handleClick(rows) {
                            if (0 >= rows.length) {
                                that.$globalFun.errorMsg('请选择行');
                                return;
                            }
                            let ids = [];
                            for (let i = 0; i < rows.length; i++) {
                                let value = rows[i];
                                if ((SUPER_ADMIN_ID === value.id) || '超级管理员' === value['roleName']) {
                                    that.$globalFun.errorMsg('不能删除超级管理员');
                                    return;
                                }
                                ids.push(value.id);
                            }
                            that.deleteUsers(ids);
                        }
                    }
                ],
                //表格展示对应的字段
                tableColumns: [
                    {
                        //表头名字
                        label: '用户名',
                        //对应的字段
                        prop: 'username',
                        //固定列
                        fixed: 'left'
                        /**
                         * 当前没有用到formatter(),如果同时存在 formatter() 和 prop  以formatter()为主
                         * formatter()方法可以返回一个dom，也可以返回String、Number
                         * 示例，内置row参数，当前行数据
                         * formatter(row) {
                         *   return `<i class="${row.icon}"></i>`;
                         * }
                         */
                    },
                    {label: '真实姓名', prop: 'realName', fixed: 'left'},
                    {label: '角色', prop: 'roleName'},
                    {
                        label: '是否允许登录', prop: 'title',
                        //自定义单元格样式类型,具体参考 /src/components/table/Index.vue 123行左右
                        type: 'tag',
                        //element元素为自定义单元格element-ui组件具体属性
                        element({allowLogin}) {
                            let result = [
                                {content: '允许'},
                                {content: '禁止', type: 'warning'},
                                {content: '封号中', type: 'danger'}
                            ];
                            return result[allowLogin] || {
                                content: '未知数据',
                                type: 'danger'
                            };
                        }
                    },
                    {
                        label: '头像', prop: 'icon', type: 'avatar', element({icon}) {
                            return {
                                src: icon
                            }
                        }
                    },
                    {label: '封号次数', prop: 'suspendNum'},
                    {label: '用户最后活跃ip地址', prop: 'laseIpAddress'},
                    {
                        label: '用户最后活跃时间', prop: 'lastActiveTime', type: 'icon', element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['lastActiveTime']
                            };
                        }
                    },
                    {
                        label: '用户创建时间', prop: 'createTime', type: 'icon', element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['createTime']
                            };
                        }
                    }
                ],
                //表格对应每一行按钮
                tableOperates: {
                    //还有个width字段，展示按钮的宽度
                    //按钮数组
                    buttons:
                        [
                            {
                                //按钮名字
                                name: '查看',
                                //按钮的样式类型
                                type: 'primary',
                                //是否展示该按钮，对应上面用户角色所对应的按钮
                                show: true,
                                //按钮点击事件，内置 row 参数，当前行的数据
                                handleClick(row) {
                                    that.formData = row;
                                    that.dialogDetail = true;
                                }
                                /**
                                 * showFormatter(),如果同时存在 showFormatter() 和 show  showFormatter()为主
                                 * showFormatter()方法可以返回一个Boolean,显示隐藏该按钮
                                 * 示例，内置row参数，当前行数据
                                 * showFormatter(row) {
                                 *    if (!showIpEditButton) {
                                 *        return false;
                                 *    }
                                 *    if (null == row) {
                                 *        return false;
                                 *    }
                                 *    return '127.0.0.1' !== row['name'];
                                 * }
                                 */
                            },
                            {
                                name: '编辑', type: 'success', show: sysUser_update, handleClick(row) {
                                    if ((SUPER_ADMIN_ID === row.id) || '超级管理员' === row['roleName']) {
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
                                    if ((SUPER_ADMIN_ID === row.id) || '超级管理员' === row['roleName']) {
                                        that.$globalFun.errorMsg('不能操作超级管理员');
                                        return;
                                    }
                                    that.dialogRole = true;
                                    that.userData = row;
                                }
                            },
                            {
                                name: '重置密码', type: 'warning', show: sysUser_resetPwd, handleClick(row) {
                                    if (((SUPER_ADMIN_ID === row.id) || '超级管理员' === row['roleName'])) {
                                        that.$globalFun.errorMsg('不能编辑超级管理员');
                                        return;
                                    }
                                    that.resetPassword(row);
                                }
                            },
                            {
                                name: '解除封号',
                                type: 'danger',
                                show: showRelieveSuspend,
                                handleClick(row) {
                                    that.relieveSuspend(row['id']);
                                }
                            },
                            {
                                name: '删除', type: 'danger', show: sysUser_delete, handleClick(row) {
                                    if ((SUPER_ADMIN_ID === row.id) || '超级管理员' === row['roleName']) {
                                        that.$globalFun.errorMsg('不能删除超级管理员');
                                        return;
                                    }
                                    that.deleteUsers([row.id]);
                                }
                            }
                        ]
                },
                //表格顶部搜索
                tableSearch: [
                    {
                        //类型,具体参考 /src/components/table/Index.vue 5行左右， 示例,输入框:input、下拉框:select
                        type: 'input',
                        //请求的字段,自定添加到当前请求的 data 参数中
                        prop: 'userName',
                        //输入框空白时展示内容
                        placeholder: '用户名'
                    },
                    {type: 'select', prop: 'roleId', placeholder: '角色', options: []},
                    {
                        type: 'select', prop: 'allowLogin', placeholder: '是否允许登录',
                        //下拉框每一项
                        options: [
                            {
                                //下拉框项对应的问题
                                label: '允许',
                                //下拉框对应的值
                                value: 0
                            },
                            {label: '禁止', value: 1},
                            {label: '封号中', value: 2},
                        ]
                    },
                    {type: 'datePicker', prop: 'lastActiveTime', placeholder: '最后活跃时间'},
                    {type: 'datePicker', prop: 'createTime', placeholder: '创建时间'}
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
        //可以看做入口函数
        mounted() {
            //保存当前 this 指针
            let that = this;
            //发送请求,详细属性请看/src/utils/axios.js
            this.$axios({
                //请求的url，默认不写域名前缀
                url: that.$global.URL.system.sysRole.getList,
                //成功的回调，默认 res['data']['data']
                success(data) {
                    let options = [];
                    data.forEach((value) => {
                        options.push({
                            label: value.name,
                            value: value.id
                        });
                    });
                    that.tableSearch[1].options = options;
                }
            });
        },
        //方法
        methods: {
            deleteUsers(ids) {
                let that = this;
                //对话框，详细属性请看/src/utils/global_function.js
                this.$globalFun.messageBox({
                    //对话框显示文字
                    message: '确定删除,该操作无法撤销',
                    //点击确认按钮的回调
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysUser.delete,
                            method: 'post',
                            data: {
                                ids
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                let {tableData, total} = that.$refs['table'];
                                for (let i = 0; i < ids.length; i++) {
                                    tableData = tableData.filter(value => value['id'] !== ids[i]);
                                }
                                total -= ids.length;
                                that.$refs['table']['tableData'] = tableData;
                                that.$refs['table']['total'] = total;
                            }
                        });
                    }
                });
            },
            relieveSuspend(id) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定解封该账号,该操作无法撤销',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysUser.relieveSuspend,
                            method: 'post',
                            data: {
                                userId: id
                            },
                            success() {
                                that.$globalFun.successMsg('解封成功');
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
                        //输入对话框，详细属性请看/src/utils/global_function.js
                        that.$globalFun.messageBoxInput({
                            message: '请输入密码',
                            //输入框类型，默认text，一般不填
                            inputType: 'password',
                            //点击确认的回调,
                            //注意:如果操作成功，调用done()方法关闭该对话框
                            confirm(value, done) {
                                if (that.$globalFun.isBlank(value)) {
                                    that.$globalFun.errorMsg('密码不能为空');
                                    return;
                                }
                                if (6 > value.length) {
                                    that.$globalFun.errorMsg('密码不能少于6位');
                                    return;
                                }
                                that.$axios({
                                    url: that.$global.URL.system.sysUser.resetUserPassword,
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