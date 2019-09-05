<template>
    <div id="index">
        <div class="left">
            <div class="box">
                <div class="item">
                    <el-button size="mini" :type="type === 'white' ? 'primary' : 'info'"
                               @click="typeChange('white')">白名单
                    </el-button>
                </div>
                <div class="item">
                    <el-button size="mini" :type="type === 'black' ? 'primary' : 'info'"
                               @click="typeChange('black')">黑名单
                    </el-button>
                </div>
            </div>
        </div>
        <div class="right">
            <div class="wei-table">
                <wei-table ref="table" :tableDataRequest="tableDataRequest" :tableColumns="tableColumns"
                           :tableHeaderButtons="tableHeaderButtons" :tableOperates="tableOperates">
                </wei-table>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue')
        },
        data() {
            let that = this;
            let SUPER_ADMIN_ID = that.$global.GLOBAL.super_admin_id;
            let SUPER_ADMIN_ROLE_ID = that.$global.GLOBAL.super_admin_role_id;
            let showIpEditButton = (SUPER_ADMIN_ID === that.$store.state.userInfo['id']
                || SUPER_ADMIN_ROLE_ID === that.$store.state.userInfo['roleId']);
            return {
                type: 'white',
                tableDataRequest: {
                    url: that.$global.URL.dataDictionary.ipFilter.getPageList,
                    data: {
                        type: that.type
                    }
                },
                tableColumns: [
                    {prop: 'name', label: 'ip'},
                    {prop: 'remark', label: '备注'},
                    {prop: 'createTime', label: '创建时间'}
                ],
                tableHeaderButtons: [
                    {
                        name: '新增', icon: 'el-icon-plus', show: showIpEditButton, handleClick() {
                            that.addIp();
                        }
                    }
                ],
                tableOperates: {
                    buttons:
                        [
                            {
                                name: '删除', type: 'danger',
                                showFormatter(row) {
                                    if (!showIpEditButton) {
                                        return false;
                                    }
                                    if (null == row) {
                                        return false;
                                    }
                                    return '127.0.0.1' !== row['name'];
                                },
                                handleClick(row, index) {
                                    that.deleteIp([row.id], index);
                                }
                            }
                        ]
                }
            }
        },
        methods: {
            typeChange(type) {
                this.type = type;
                this.tableDataRequest['data']['type'] = type;
                this.$refs['table'].renderTable();
            },
            addIp() {
                let that = this;
                this.$globalFun.messageBoxInput({
                    message: '请输入ip',
                    confirm(instance, done) {
                        let value = instance.inputValue;
                        if (that.$globalFun.isBlank(value)) {
                            that.$globalFun.errorMsg('ip不能为空');
                            return;
                        }
                        that.$axios({
                            method: 'post',
                            url: that.$global.URL.dataDictionary.ipFilter.add,
                            data: {
                                ip: value,
                                type: that.type
                            },
                            success() {
                                that.$globalFun.successMsg('ip添加成功');
                                that.$refs['table'].renderTable();
                                done();
                            }
                        });
                    }
                });
            },
            deleteIp(id, index) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定删除,该操作无法撤销',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.dataDictionary.ipFilter.delete,
                            method: 'post',
                            data: {
                                id,
                                type: that.type
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                let {tableData, total} = that.$refs['table'];
                                tableData.splice(index, 1);
                                total -= 1;
                                that.$refs['table'].tableData = tableData;
                                that.$refs['table'].total = total;
                            }
                        });
                    }
                });
            }
        }
    }
</script>

<style lang="less" scoped>
    #index {
        display: flex;
        box-sizing: border-box;
        .left {
            flex: 1;
            margin-right: 20px;
            .box {
                .item {
                    margin-top: 20px;
                }
            }
        }
        .right {
            flex: 10;
        }
    }
</style>