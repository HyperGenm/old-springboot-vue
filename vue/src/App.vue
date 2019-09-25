<template>
    <div id="app">
        <router-view/>
    </div>
</template>

<script>
    export default {
        name: 'App',
        mounted() {
            let that = this;
            // 监听浏览器刷新,刷新时保存state数据到session
            window.addEventListener('unload', function () {
                sessionStorage.setItem('state', JSON.stringify(that.$store.state));
            });
        }
    }
</script>

<style lang="scss">
    html, body {
        margin: 0;
        padding: 0;
    }

    img{
        width: 100%;
    }

    .wei-table {
        height: 100%;
    }
</style>
