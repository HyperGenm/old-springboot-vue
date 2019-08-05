<template>
    <div id="sysLog">
        <div class="wei-table">
            <wei-table ref="table" :tableDataRequest="tableDataRequest" :tableColumns="tableColumns"
                       :tableOperates="tableOperates" :tableSearch="tableSearch">
            </wei-table>
        </div>
        <div class="show">
            <detail :show.sync="dialogDetail" :detailData="rowData"></detail>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue'),
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            return {
                tableDataRequest: {
                    url: that.$global.URL.sysLogGetLogList,
                    data: {
                        username: '',
                        roleId: '',
                        createTime: ''
                    }
                },
                tableColumns: [
                    {prop: 'username', label: '用户名'},
                    {prop: 'realName', label: '真实姓名'},
                    {prop: 'roleName', label: '角色'},
                    {prop: 'description', label: '操作'},
                    {prop: 'ipAddress', label: 'ip地址'},
                    {prop: 'createTime', label: '创建时间'}
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
                    {type: 'datePicker', prop: 'createTime'},
                    {type: 'input', prop: 'description', placeholder: '操作'}
                ],
                dialogDetail: false,
                rowData: {}
            }
        },
        mounted() {
            let that = this;
            this.$axios({
                url: that.$global.URL.sysRoleGetList,
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