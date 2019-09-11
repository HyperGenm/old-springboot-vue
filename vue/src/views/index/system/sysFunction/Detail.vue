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
                    {label: '功能名', prop: data['name']},
                    {label: '路径', prop: data['path']},
                    {label: '标题', prop: data['title']},
                    {
                        label: '对应api', prop: data['containApi'], formatter() {
                            if (null == data['containApi']) {
                                return;
                            }
                            let dom = '';
                            data['containApi'].split(',').forEach((value, index) => {
                                dom += `<div style="border-top: ${0 === index ? 0 : 1}px dashed #777;">${value}</div>`;
                            });
                            return dom;
                        }
                    },
                    {label: '上级', prop: this.parentData.title},
                    {
                        label: '图标', formatter() {
                            return `<i class="${icon}"></i>`;
                        }
                    },
                    {label: '排序', prop: data['sort']},
                    {
                        label: '类型', formatter() {
                            return ('0' === type + '') ? '菜单' : '按钮';
                        }
                    },
                    {label: '描述', prop: data['description']}
                ];
            }
        }
    }
</script>