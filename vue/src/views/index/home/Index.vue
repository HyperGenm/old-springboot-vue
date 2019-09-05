<template>
    <div id="index">
        <el-divider content-position="left">首页展示</el-divider>
        <el-calendar v-model="date"></el-calendar>
    </div>
</template>

<script>
    export default {
        name: "Index",
        data() {
            return {
                date: new Date()
            }
        }
    }
</script>