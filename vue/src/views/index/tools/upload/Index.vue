<template>
    <div id="index">
        <!--文件上传,必须绑定 action 和 fileList ,具体属性参考 /src/components/upload/Index.vue-->
        <wei-upload :action="$global.URL.tools.upload" :limit="3"
                    :fileList.sync="fileList"></wei-upload>
        <el-button type="primary" @click="showFileList">当前的文件列表</el-button>
        <el-button type="success" @click="downTemp">文件下载</el-button>
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
                        url: 'http://39.96.52.201/github/temp/logo.jpg'
                    }
                ]
            }
        },
        methods: {
            showFileList() {
                this.$globalFun.successMsg(JSON.stringify(this.fileList));
                //this.fileList里面有所有上传成功(有绿色的对号图标)的图片数组
                console.log(this.fileList);
            },
            //文件下载
            downTemp() {
                let that = this;
                this.$axiosDown({
                    url: that.$global.URL.tools.downTemp,
                    method: 'post',
                    filename: 'logo.jpg'
                })
            }
        }
    }
</script>