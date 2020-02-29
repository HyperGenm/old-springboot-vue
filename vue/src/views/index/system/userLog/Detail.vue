<template>
    <div id="detail">
        <dialog-detail :rows="rows"></dialog-detail>
    </div>
</template>

<script>
    export default {
        name: "Detail",
        components: {
            'dialog-detail': () => import('@/components/dialog/detail/Index.vue')
        },
        props: {
            detailData: {
                type: Object
            }
        },
        watch: {
            detailData(data) {
                this.initData(data);
            }
        },
        data() {
            return {
                rows: []
            }
        },
        mounted() {
            this.initData(this.detailData);
        },
        methods: {
            initData(data) {
                let that = this;
                this.rows = [
                    {label: '用户名', prop: data['username']},
                    {label: '操作', prop: data['description']},
                    {
                        label: '参数', type: 'table',
                        element() {
                            if (that.$globalFun.isBlank(data['param'])) {
                                return {
                                    tableData: [],
                                    tableColumns: [
                                        {label: '字段', prop: 'field'},
                                        {label: '参数', prop: 'value'},
                                    ]
                                }
                            }
                            let paramMap = JSON.parse(data['param']);
                            let paramArr = [];
                            for (let key in paramMap) {
                                if (!paramMap.hasOwnProperty(key)) {
                                    continue;
                                }
                                paramArr.push({
                                    field: key,
                                    value: paramMap[key]
                                });
                            }
                            return {
                                tableData: paramArr,
                                tableColumns: [
                                    {label: '字段', prop: 'field'},
                                    {label: '参数', prop: 'value'},
                                ]
                            }
                        }
                    },
                    {
                        label: '类型', prop: 'type', type: 'tag',
                        element() {
                            let result = [
                                null,
                                {content: '查询'},
                                {content: '新增', type: 'success'},
                                {content: '修改', type: 'warning'},
                                {content: '删除', type: 'danger'}
                            ];
                            return result[data['type']] || {
                                content: '未知类型',
                                type: 'danger'
                            };
                        }
                    },
                    {label: 'ip地址', prop: data['ipAddress']},
                    {
                        label: '创建时间', prop: 'createTime', type: 'icon', element() {
                            return {
                                leftIcon: 'el-icon-time',
                                content: data['createTime']
                            };
                        }
                    }
                ];
            }
        }
    }
</script>