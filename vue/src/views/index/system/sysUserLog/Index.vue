<template>
    <div id="index">
        <div class="wei-table">
            <wei-table ref="table" :tableDataRequest="tableDataRequest" :tableColumns="tableColumns"
                       :tableOperates="tableOperates" :tableSearch="tableSearch">
            </wei-table>
        </div>
        <div class="show">
            <wei-dialog :show.sync="dialogDetail">
                <detail :detailData="rowData"></detail>
            </wei-dialog>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue'),
            'wei-dialog': () => import('@/components/dialog/index/Index.vue'),
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.sysUserLog.getPageList,
                    data: {}
                },
                tableColumns: [
                    {label: '用户名', prop: 'username', showOverflowTooltip: true, fixed: 'left'},
                    {label: '真实姓名', prop: 'realName', fixed: 'left'},
                    {label: '角色', prop: 'roleName', fixed: 'left'},
                    {label: 'url', prop: 'url', showOverflowTooltip: true},
                    {label: '参数', prop: 'param', showOverflowTooltip: true},
                    {
                        label: '类型', prop: 'type', type: 'tag',
                        element({type}) {
                            let result = [
                                {content: '查询'},
                                {content: '新增', type: 'success'},
                                {content: '修改', type: 'warning'},
                                {content: '删除', type: 'danger'}
                            ];
                            return result[type - 1] || {
                                content: '未知类型',
                                type: 'danger'
                            };
                        }
                    },
                    {label: '操作', prop: 'description', showOverflowTooltip: true},
                    {label: 'ip地址', prop: 'ipAddress', showOverflowTooltip: true},
                    {
                        label: '创建时间', prop: 'createTime', showOverflowTooltip: true, type: 'icon', element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['createTime']
                            };
                        }
                    }
                ],
                tableOperates: {
                    buttons:
                        [
                            {
                                name: '查看', type: 'primary', show: true, handleClick(row) {
                                    that.rowData = row;
                                    that.dialogDetail = true;
                                }
                            }
                        ]
                },
                tableSearch: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {type: 'select', prop: 'roleId', placeholder: '角色', options: []},
                    {type: 'input', prop: 'url', placeholder: '请求的url'},
                    {
                        type: 'select', prop: 'type', placeholder: '类型',
                        options: [
                            {label: '查询', value: 1},
                            {label: '新增', value: 2},
                            {label: '修改', value: 3},
                            {label: '删除', value: 4}
                        ]
                    },
                    {type: 'input', prop: 'description', placeholder: '操作'},
                    {type: 'input', prop: 'ipAddress', placeholder: 'ip地址'},
                    {type: 'dateTimePicker', prop: 'startTime', placeholder: '开始时间'},
                    {type: 'dateTimePicker', prop: 'endTime', placeholder: '结束时间'}
                ],
                dialogDetail: false,
                rowData: {}
            }
        },
        mounted() {
            let that = this;
            this.$axios({
                url: that.$global.URL.system.sysRole.getList,
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
        }
    }
</script>