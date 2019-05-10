<template>
    <div id="sysRole">
        <div class="role">
            <div class="btn">
                <el-button type="primary" size="mini"
                           @click="initGetRoleTree">
                    刷新
                </el-button>
                <el-button v-if="$store.state.role['buttons']['sysRole_add']" type="success" size="mini"
                           @click="addRole">
                    新增角色
                </el-button>
                <el-button v-if="$store.state.role['buttons']['sysRole_update']" type="primary" size="mini"
                           @click="updateRole">修改角色
                </el-button>
                <el-button v-if="$store.state.role['buttons']['sysRole_delete']" type="danger" size="mini"
                           @click="deleteRole">
                    删除角色
                </el-button>
                <el-button v-if="$store.state.role['buttons']['sysRole_status']" type="danger" size="mini"
                           @click="changeRoleIsStop(1)">
                    禁用角色
                </el-button>
                <el-button v-if="$store.state.role['buttons']['sysRole_status']" type="success" size="mini"
                           @click="changeRoleIsStop(0)">
                    启用角色
                </el-button>
            </div>
            <el-tree ref="roleTree" node-key="id"
                     highlight-current default-expand-all
                     :expand-on-click-node="false" :data="roleData" :props="roleProps"
                     @node-click="handleRoleNodeClick"></el-tree>
            <div class="handle">
                <div class="edit">
                    <wei-dialog :show.sync="dialogEditForm" :title="handleType === 'add' ? '新增' : '编辑'">
                        <edit-form :parentData="parentData" :handleType="handleType" :formData="formData"
                                   @closeDialog="dialogEditForm = false" @renderTree="initGetRoleTree"></edit-form>
                    </wei-dialog>
                </div>
            </div>
        </div>
        <div class="function">
            <div class="title">
                <span>功能列表</span>
                <el-button v-if="$store.state.role['buttons']['sysRole_save']" type="primary" size="mini"
                           @click="saveRole">
                    保存
                </el-button>
            </div>
            <el-tree ref="funTree" node-key="id"
                     highlight-current default-expand-all show-checkbox
                     :expand-on-click-node="false" :data="[{title:'最高级',id:0,children:funData}]" :props="funProps"
                     :default-checked-keys="checkedRoleFunctionData"></el-tree>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'edit-form': () => import('./EditForm.vue'),
            'wei-dialog': () => import('@/components/dialog/index/Index.vue')
        },
        data() {
            return {
                //角色树的数据
                roleData: [],
                roleProps: {
                    label(data) {
                        let {name, isStop} = data;
                        return name + '-----' + (('0' === isStop + '') ? '正常' : "禁用");
                    },
                    disabled(data) {
                        return '0' !== data.isStop + '';
                    }
                },
                //功能树的数据
                funData: [],
                funProps: {
                    label: 'title'
                },
                funParentData: [],
                //选中的功能树的数据
                checkedRoleFunctionData: [],
                //表单弹窗
                dialogEditForm: false,
                //表单类型
                handleType: 'add',
                //表单数据
                formData: {},
                //父级功能信息,当前选中的功能信息
                parentData: {
                    id: 1,
                    name: '超级管理员',
                    isStop: 0
                }
            }
        },
        mounted() {
            let that = this;
            this.initGetRoleTree();
            //获取功能树形列表
            this.$axios({
                url: that.$global.URL.getAllFunctionTree,
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
            //获取角色树形列表
            initGetRoleTree() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.getRoleTree,
                    success(data) {
                        that.roleData = data;
                        that.$nextTick(() => {
                            that.$refs['roleTree'].setCurrentKey(1);
                        });
                        that.parentData = {
                            id: 1,
                            name: '超级管理员',
                            isStop: 0
                        };
                        that.getRoleFunList();
                    }
                });
            },
            //获取角色的功能列表
            getRoleFunList() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.getRoleFunList,
                    data: {
                        roleId: that.parentData.id
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
            //新增角色
            addRole() {
                let isStop = this.parentData.isStop;
                if ('0' !== isStop + '') {
                    this.$globalFun.errorMsg('角色禁用中,禁止添加下级');
                    return;
                }
                this.formData = {
                    name: '',
                    isStop: 0,
                    sort: 0,
                    description: '',
                    parentId: this.parentData.id
                };
                this.dialogEditForm = true;
                this.handleType = 'add';
            },
            //修改角色
            updateRole() {
                let data = this.parentData;
                if (null != data && (1 === data.id || '超级管理员' === data.name)) {
                    this.$globalFun.errorMsg('超级管理员不能修改');
                    return;
                }
                let isStop = this.parentData.isStop;
                if ('0' !== isStop + '') {
                    this.$globalFun.errorMsg('角色禁用中,禁止修改');
                    return;
                }
                this.formData = data;
                this.dialogEditForm = true;
                this.handleType = 'update';
            },
            deleteRole() {
                let data = this.parentData;
                if (null != data && (1 === data.id || '超级管理员' === data.name)) {
                    this.$globalFun.errorMsg('超级管理员不能删除');
                    return;
                }
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定删除',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.deleteRole,
                            method: 'post',
                            data: {
                                roleId: data.id
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                that.initGetRoleTree();
                            }
                        })
                    }
                });
            },
            handleRoleNodeClick(data) {
                this.parentData = data;
                this.getRoleFunList();
            },
            //保存角色的功能
            saveRole() {
                let isStop = this.parentData.isStop;
                if ('0' !== isStop + '') {
                    this.$globalFun.errorMsg('角色禁用中,禁止保存功能');
                    return;
                }
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
                    url: that.$global.URL.addRoleFun,
                    method: 'post',
                    data: {
                        roleId: that.parentData.id,
                        funIds
                    },
                    success() {
                        that.$globalFun.successMsg('保存成功');
                    }
                });
            },
            changeRoleIsStop(isStop) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '是否' + ('0' === isStop + '' ? '启用' : '禁用') + '角色,该操作将会同时改变下级状态',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.changeRoleIsStop,
                            method: 'post',
                            data: {
                                roleId: that.parentData.id,
                                isStop
                            },
                            success() {
                                that.$globalFun.successMsg('成功');
                                that.initGetRoleTree();
                            }
                        });
                    }
                });
            }
        }
    }
</script>

<style lang="less" scoped>
    #sysRole {
        overflow: hidden;
        display: flex;
        .role {
            flex: 1;
            button {
                margin-bottom: 20px;
            }
        }
        .function {
            flex: 2.5;
            padding-left: 2%;
            border-left: 1px solid #eee;
            .title {
                margin-bottom: 20px;
                span {
                    color: #666;
                }
                button {
                    margin-left: 20px;
                }
            }
        }
    }
</style>