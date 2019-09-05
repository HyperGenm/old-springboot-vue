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
                    url: that.$global.URL.system.sysLog.getPageList,
                    data: {
                        username: '',
                        roleId: '',
                        createTime: ''
                    }
                },
                tableColumns: [
                    {label: '用户名', prop: 'username'},
                    {label: '真实姓名', prop: 'realName'},
                    {label: '角色', prop: 'roleName'},
                    {label: '操作', prop: 'description'},
                    {label: 'ip地址', prop: 'ipAddress'},
                    {label: '创建时间', prop: 'createTime'}
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
                    {type: 'input', prop: 'description', placeholder: '操作'},
                    {type: 'input', prop: 'ipAddress', placeholder: 'ip地址'},
                    {type: 'datePicker', prop: 'createTime', placeholder: '创建时间'}
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