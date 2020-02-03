<template>
    <div id="index">
        <div class="wei-table">
            <wei-table ref="table" :tableDataRequest="tableDataRequest" :tableColumns="tableColumns"
                       :tableOperates="tableOperates"></wei-table>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue'),
        },
        data() {
            let that = this;
            let SUPER_ADMIN_ID = that.$global.GLOBAL.super_admin_id;
            let SUPER_ADMIN_ROLE_ID = that.$global.GLOBAL.super_admin_role_id;
            let showIpEditButton = (SUPER_ADMIN_ID === that.$store.state.userInfo['id']
                || SUPER_ADMIN_ROLE_ID === that.$store.state.userInfo['roleId']);
            return {
                tableDataRequest: {
                    url: that.$global.URL.dataDictionary.abnormalIp.getPageList
                },
                tableColumns: [
                    {label: 'ip', prop: 'value'},
                    {
                        label: '异常次数', prop: 'order', type: 'tag',
                        element(row) {
                            let {order} = row;
                            let type = 'info';
                            if (5 >= order && 0 < order) {
                                type = 'warning';
                            } else if (5 < order) {
                                type = 'danger';
                            }
                            return {
                                type,
                                content: order
                            };
                        }
                    },
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
                    width: 150,
                    buttons:
                        [
                            {
                                name: '添加到黑名单', type: 'danger', show: showIpEditButton, handleClick(row) {
                                    that.setBlack(row);
                                }
                            }
                        ]
                }
            }
        },
        methods: {
            setBlack(row) {
                let {value} = row;
                let that = this;
                this.$globalFun.messageBox({
                    message: '是否将改ip添加到黑名单',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.dataDictionary.ipFilter.add,
                            method: 'post',
                            data: {
                                ip: value,
                                type: 1
                            },
                            success() {
                                that.$globalFun.successMsg('添加成功');
                            }
                        });
                    }
                });
            }
        }
    }
</script>