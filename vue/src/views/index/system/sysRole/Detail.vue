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
                let {name, sort, isStop, description, createTime} = data;
                this.rows = [
                    {label: '角色', prop: name},
                    {label: '排序', prop: sort},
                    {
                        label: '状态', type: 'tag',
                        element() {
                            let result = [
                                {content: '正常'},
                                {content: '禁用', type: 'danger'}
                            ];
                            return result[isStop] || {
                                content: '未知状态',
                                type: 'danger'
                            };
                        }
                    },
                    {label: '描述', prop: description},
                    {label: '创建时间', prop: createTime}
                ];
            }
        }
    }
</script>