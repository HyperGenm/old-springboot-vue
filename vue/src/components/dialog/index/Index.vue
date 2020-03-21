<template>
    <div id="dialog">
        <el-dialog :title="title" :visible.sync="visible"
                   :modal-append-to-body="modelAppendToBody" :append-to-body="appendToBody"
                   @close="$emit('update:show', false)">
            <slot></slot>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Index",
        props: {
            //是否展示
            show: {
                type: Boolean,
                default: false
            },
            //标题
            title: {
                type: String,
                default: '标题'
            },
            modelAppendToBody: {
                type: Boolean,
                default: false
            },
            appendToBody: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
                visible: this.show
            };
        },
        watch: {
            show() {
                this.visible = this.show;
            }
        }
    }
</script>