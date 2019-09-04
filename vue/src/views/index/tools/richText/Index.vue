<template>
    <div id="index">
        <wang-editor :html.sync="html"></wang-editor>
        <button @click="ceshi">测试</button>
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
                html: `<h1>hello wangEditor</h1>`
            }
        },
        methods: {
            ceshi() {
                console.log(this.html);
            }
        }
    }
</script>