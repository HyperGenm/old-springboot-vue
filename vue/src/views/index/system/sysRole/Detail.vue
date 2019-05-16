<template>
    <div id="detail">
        <dialog-detail :show.sync="visible" :rows="rows"></dialog-detail>
    </div>
</template>

<script>
    export default {
        name: "Detail",
        components: {
            'dialog-detail': () => import('@/components/dialog/detail/Index.vue')
        },
        props: {
            show: {
                type: Boolean,
                default: false
            },
            detailData: {
                type: Object,
                default: () => {
                }
            }
        },
        watch: {
            show() {
                this.visible = this.show;
                if (!this.show) {
                    return;
                }
                let {name, sort, isStop, description, createTime} = this.detailData;
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
            },
            visible() {
                if (!this.visible) {
                    this.$emit('update:show', false);
                }
            }
        },
        data() {
            return {
                visible: false,
                rows: []
            }
        }
    }
</script>