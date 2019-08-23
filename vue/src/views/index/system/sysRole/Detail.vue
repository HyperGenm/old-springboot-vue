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
                    {title: '角色', content: name},
                    {title: '排序', content: sort},
                    {
                        title: '是否启用', formatter() {
                            return ('0' === isStop + '') ? '启用' : '禁用';
                        }
                    },
                    {title: '描述', content: description},
                    {title: '创建时间', content: createTime}
                ];
            }
        }
    }
</script>