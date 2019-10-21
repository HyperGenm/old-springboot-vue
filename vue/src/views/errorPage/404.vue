<template>
    <div style="width: 87%;margin: 0 auto;">
        <img src="../../assets/image/404.png" alt="404">
    </div>
</template>

<script>
    export default {
        name: "errorPage404",
        mounted() {
            this.$globalFun.errorMsg('请创建路径对应组件');
        }
    }
</script>