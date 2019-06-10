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
            },
            parentData: {
                type: Object
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
                let {name, path, title, icon, sort, type, description} = this.detailData;
                this.rows = [
                    {title: '功能名', content: name},
                    {title: '路径', content: path},
                    {title: '标题', content: title},
                    {title: '上级', content: this.parentData.title},
                    {
                        title: '图标', formatter() {
                            return `<i class="${icon}"></i>`;
                        }
                    },
                    {title: '排序', content: sort},
                    {
                        title: '类型', formatter() {
                            return ('0' === type + '') ? '菜单' : '按钮';
                        }
                    },
                    {title: '描述', content: description}
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