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
                    url: that.$global.URL.system.sysError.getPageList,
                    data: {}
                },
                tableColumns: [
                    {label: '类名', prop: 'className'},
                    {label: '方法名', prop: 'methodName'},
                    {label: '第几行', prop: 'lineNumber'},
                    {label: '备注', prop: 'remark', showOverflowTooltip: true},
                    {label: '详情', prop: 'content', showOverflowTooltip: true},
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
                    {type: 'datePicker', prop: 'createTime', placeholder: '创建时间'}
                ],
                dialogDetail: false,
                rowData: {}
            }
        }
    }
</script>