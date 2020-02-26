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
                this.rows = [
                    {label: '用户名', prop: data['username']},
                    {label: '真实姓名', prop: data['realName']},
                    {label: '角色', prop: data['roleName']},
                    {label: '操作', prop: data['description']},
                    {label: '参数', prop: data['param']},
                    {
                        label: '类型', prop: 'type', type: 'tag',
                        element() {
                            let result = [
                                {content: '查询'},
                                {content: '新增', type: 'success'},
                                {content: '修改', type: 'warning'},
                                {content: '删除', type: 'danger'}
                            ];
                            return result[data['type'] - 1] || {
                                content: '未知类型',
                                type: 'danger'
                            };
                        }
                    },
                    {label: 'ip地址', prop: data['ipAddress']},
                    {
                        label: '操作时间', prop: data['createTime'], type: 'icon', element() {
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