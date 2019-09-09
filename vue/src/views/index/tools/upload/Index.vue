<template>
    <div id="index">
        <wei-upload :action="$global.URL.tools.upload" :limit="3"
                    :fileList.sync="fileList"></wei-upload>
        <el-button type="primary" @click="showFileList">当前的文件列表</el-button>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-upload': () => import('@/components/upload/Index.vue')
        },
        data() {
            let that = this;
            return {
                fileList: [
                    {
                        path: '原始回显图片',
                        name: '回显图片.jpeg',
                        url: that.$global.GLOBAL.base_url + '/666.jpg'
                    }
                ]
            }
        },
        methods: {
            showFileList() {
                this.$globalFun.successMsg(JSON.stringify(this.fileList));
                console.log(this.fileList);
            }
        }
    }
</script>