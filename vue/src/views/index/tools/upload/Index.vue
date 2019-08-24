<template>
    <div id="index">
        <wei-upload :action="$global.URL.tools.upload" :limit="3"
                    @success="success" @remove="remove"></wei-upload>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-upload': () => import('@/components/upload/Index.vue')
        },
        methods: {
            success(data) {
                console.log('上传成功列表---', data);
            },
            remove(data) {
                console.log('文件删除后剩余列表---', data);
            }
        }
    }
</script>