<template>
    <div id="index">
        <wei-upload :action="$global.URL.tools.upload" :limit="3"
                    @success="success"></wei-upload>
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
            }
        }
    }
</script>