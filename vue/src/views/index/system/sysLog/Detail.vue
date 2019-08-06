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
            show(show) {
                this.visible = show;
            },
            visible(visible) {
                if (!visible) {
                    this.$emit('update:show', false);
                }
            },
            detailData(data) {
                this.rows = [
                    {title: '用户名', content: data['username']},
                    {title: '真实姓名', content: data['realName']},
                    {title: '角色名', content: data['roleName']},
                    {title: '日志记录', content: data['description']},
                    {title: 'ip地址', content: data['ipAddress']},
                    {title: '操作时间', content: data['createTime']}
                ];
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