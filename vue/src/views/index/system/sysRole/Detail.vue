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
            isShow: {
                type: Boolean,
                default: false
            },
            detailData: {
                type: Object
            }
        },
        watch: {
            isShow(show) {
                if (!show) {
                    return;
                }
                this.initData(this.detailData);
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
                        label: '是否启用', formatter() {
                            return ('0' === isStop + '') ? '启用' : '禁用';
                        }
                    },
                    {label: '描述', prop: description},
                    {label: '创建时间', prop: createTime}
                ];
            }
        }
    }
</script>