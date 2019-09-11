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
                    {label: '角色名', prop: data['roleName']},
                    {
                        label: '是否允许登录', type: 'tag',
                        element() {
                            let result = [
                                {content: '允许'},
                                {content: '禁止', type: 'warning'},
                                {content: '封号中', type: 'danger'}
                            ];
                            return result[data['allowLogin']];
                        }
                    },
                    {label: '最后活跃ip', prop: data['laseIpAddress']},
                    {label: '最后活跃时间', prop: data['lastActiveTime']},
                    {label: '用户创建时间', prop: data['createTime']}
                ];
            }
        }
    }
</script>