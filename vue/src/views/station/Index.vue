<template>
    <div id="Station">
        <router-view/>
    </div>
</template>

<!--仅做多级路由站位-->
<script>
    export default {
        name: "Station"
    }
</script>

<style lang="scss" scoped>
    #Station {
        & > div {
            height: 100%;
        }
    }
</style>