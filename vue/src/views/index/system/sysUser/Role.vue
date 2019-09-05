<template>
    <div id="role">
        <div class="tip" v-show="null != chooseData.id">当前选中:{{chooseData.name}}</div>
        <el-tree ref="roleTree" node-key="id"
                 highlight-current default-expand-all
                 :expand-on-click-node="false" :data="roleData" :props="roleProps"
                 @node-click="handleRoleNodeClick"></el-tree>
        <el-button type="primary" @click="updateRole('save')">保存</el-button>
        <el-button type="danger" @click="updateRole('delete')">删除角色</el-button>
    </div>
</template>

<script>
    export default {
        name: "Role",
        props: {
            userData: {
                type: Object
            }
        },
        data() {
            return {
                roleData: [],
                roleProps: {
                    label: 'name'
                },
                chooseData: {}
            }
        },
        mounted() {
            let that = this;
            this.$axios({
                url: that.$global.URL.system.sysRole.getRoleTree,
                success(data) {
                    that.roleData = data;
                }
            });
        },
        methods: {
            handleRoleNodeClick(row) {
                this.chooseData = row;
            },
            updateRole(status) {
                let {id, name} = this.chooseData;
                if (null == id && 'save' === status) {
                    this.$globalFun.errorMsg('请选择角色');
                    return;
                }
                if (('1' === id + '') || '超级管理员' === name) {
                    this.$globalFun.errorMsg('超级管理员只能有一个');
                    return;
                }
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysRole.updateUserRole,
                    method: 'post',
                    data: {
                        roleId: 'save' === status ? id : 0,
                        userId: that.userData.id
                    },
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialogRole');
                        that.$emit('renderTable');
                    }
                });
            }
        }
    }
</script>

<style lang="less" scoped>
    .tip {
        margin-bottom: 20px;
        font-size: 17px;
    }

    button {
        margin: 30px auto;
    }
</style>