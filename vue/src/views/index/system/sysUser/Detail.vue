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
                    {title: '用户名', content: data['username']},
                    {title: '真实姓名', content: data['realName']},
                    {title: '角色名', content: data['roleName']},
                    {
                        title: '是否允许登录', formatter() {
                            return ('0' === data['allowLogin'] + '') ? '允许' : '禁止';
                        }
                    },
                    {title: '最后活跃ip', content: data['laseIpAddress']},
                    {title: '最后活跃时间', content: data['lastActiveTime']},
                    {title: '用户创建时间', content: data['createTime']}
                ];
            }
        }
    }
</script>