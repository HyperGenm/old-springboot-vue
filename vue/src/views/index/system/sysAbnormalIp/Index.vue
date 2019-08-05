<template>
    <div id="sysAbnormalIp">
        <div class="wei-table">
            <wei-table ref="table" :tableDataRequest="tableDataRequest" :tableColumns="tableColumns"
                       :tableOperates="tableOperates">
            </wei-table>
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
                    url: that.$global.URL.sysAbnormalIpGetPageList,
                    data: {}
                },
                tableColumns: [
                    {prop: 'ip', label: 'ip'},
                    {prop: 'number', label: '异常次数'},
                    {prop: 'laseTime', label: '最后一次异常事件'},
                    {prop: 'createTime', label: '创建时间'}
                ],
                tableOperates: {
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
                let {ip} = row;
                let that = this;
                this.$globalFun.messageBox({
                    message: '是否将改ip添加到黑名单',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.ipFilterAdd,
                            method: 'post',
                            data: {
                                ip,
                                type: 'black'
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