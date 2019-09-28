<template>
    <div id="index">
        <!--富文本输入框,必须绑定 html ,具体属性参考 /src/components/wangEditor/Index.vue-->
        <wang-editor :html.sync="html"></wang-editor>
        <el-button type="primary" @click="showContent">展示输入框内容</el-button>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wang-editor': () => import('@/components/wangEditor/Index.vue')
        },
        data() {
            return {
                //富文本输入框中的内容
                html: `<h1 style="color: #666;">hello wangEditor</h1>`
            }
        },
        methods: {
            showContent() {
                this.$globalFun.successMsg(this.html);
                console.log(this.html);
            }
        }
    }
</script>