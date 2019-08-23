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
            },
            parentData: {
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
                let {icon, type} = data;
                this.rows = [
                    {title: '功能名', content: data['name']},
                    {title: '路径', content: data['path']},
                    {title: '标题', content: data['title']},
                    {title: '上级', content: this.parentData.title},
                    {
                        title: '图标', formatter() {
                            return `<i class="${icon}"></i>`;
                        }
                    },
                    {title: '排序', content: data['sort']},
                    {
                        title: '类型', formatter() {
                            return ('0' === type + '') ? '菜单' : '按钮';
                        }
                    },
                    {title: '描述', content: data['description']}
                ];
            }
        }
    }
</script>