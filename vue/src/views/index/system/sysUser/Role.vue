<template>
    <div id="role">
        <div class="tip" v-show="null != chooseData.id">当前选中:{{chooseData.name}}</div>
        <el-tree ref="roleTree" node-key="id"
                 highlight-current default-expand-all
                 :expand-on-click-node="false" :data="roleData" :props="roleProps"
                 @node-click="handleRoleNodeClick"></el-tree>
        <el-button type="primary" @click="saveRole">保存</el-button>
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
                url: that.$global.URL.getRoleTree,
                success(data) {
                    that.roleData = data;
                }
            });
        },
        methods: {
            handleRoleNodeClick(row) {
                this.chooseData = row;
            },
            saveRole() {
                let chooseData = this.chooseData;
                if (null == chooseData.id) {
                    this.$globalFun.errorMsg('请选择角色');
                    return;
                }
                if (('1' === chooseData.id + '') || '超级管理员' === chooseData.name) {
                    this.$globalFun.errorMsg('超级管理员只能有一个');
                    return;
                }
                let that = this;
                this.$axios({
                    url: that.$global.URL.addUserRole,
                    method: 'post',
                    data: {
                        roleId: chooseData.id,
                        userId: that.userData.id
                    },
                    success() {
                        that.$globalFun.successMsg('角色保存成功');
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