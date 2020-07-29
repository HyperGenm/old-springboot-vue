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

    img {
        width: 100%;
    }

    .wei-table {
        height: 100%;
    }

    /*指令加载中动画*/
    .el-loading-mask.is-fullscreen {
        background-color: #3b2d47 !important;
        opacity: 0.7;
        .el-loading-spinner {
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            i {
                background: url("./assets/image/loading.gif") no-repeat;
                background-size: 100% 100%;
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                &:before {
                    content: none;
                }
            }
            p {
                position: absolute;
                top: 72%;
                width: 100%;
                font-size: 17px;
            }
        }
    }
</style>
