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
            },
            visible() {
                if (!this.visible) {
                    this.$emit('update:show', false);
                }
            },
            detailData() {
                let {username, realName, roleName, allowLogin, lastActiveTime, createTime} = this.detailData;
                this.rows = [
                    {title: '用户名', content: username},
                    {title: '真实姓名', content: realName},
                    {title: '角色名', content: roleName},
                    {
                        title: '是否允许登录', formatter() {
                            return ('0' === allowLogin + '') ? '允许' : '禁止';
                        }
                    },
                    {title: '最后活跃时间', content: lastActiveTime},
                    {title: '用户创建时间', content: createTime}
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