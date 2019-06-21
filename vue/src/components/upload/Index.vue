<template>
    <div class="upload">
        <el-upload
                ref="upload"
                :action="action"
                :multiple="multiple"
                :show-file-list="showFileList"
                :drag="drag"
                :on-preview="onPreview"
                :on-remove="onRemove"
                :on-success="onSuccess"
                :on-error="onError"
                :before-upload="beforeUpload"
                :list-type="listType"
                :auto-upload="autoUpload"
                :file-list="fileList"
                :limit="limit">
            <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
        <el-button v-if="!autoUpload" style="margin-top: 10px;" size="small" type="success" @click="submitUpload">
            上传到服务器
        </el-button>
        <div slot="tip" class="el-upload__tip">{{tip}}</div>
        <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl">
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Index",
        props: {
            //上传的地址
            action: {
                type: String
            },
            //是否支持多文件上传
            multiple: {
                type: Boolean,
                default: false
            },
            //是否展示上传的文件列表
            showFileList: {
                type: Boolean,
                default: true
            },
            //是否启用拖拽上传
            drag: {
                type: Boolean,
                default: false
            },
            //文件列表的类型
            listType: {
                type: String,
                default: 'picture-card'
            },
            //是否在文件选择后立即上传
            autoUpload: {
                type: Boolean,
                default: false
            },
            //允许的最大上传个数
            limit: {
                type: Number,
                default: 3
            },
            //文件上传提示
            tip: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                //上传的文件列表
                fileList: [],
                dialogImageUrl: '',
                dialogVisible: false
            }
        },
        methods: {
            /**
             * 点击上传的文件
             * @param file
             */
            onPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            /**
             * 文件列表删除时触发的事件
             * @param file
             * @param fileList
             */
            onRemove(file, fileList) {
                this.$emit('onRemove', fileList);
            },
            /**
             * 文件上传成功
             * @param res
             * @param file
             * @param fileList
             */
            onSuccess(res, file, fileList) {
                this.$emit('onSuccess', res, file, fileList);
            },
            /**
             * 文件上传失败
             * @param error
             * @param file
             * @param fileList
             */
            onError(error, file, fileList) {
                this.$globalFun.errorMsg("文件上传失败，请重试");
                console.warn("文件上传失败---error:", error, '-----file:', file, '-----fileList', fileList);
            },
            /**
             * 文件上传之前
             * @param file
             */
            beforeUpload(file) {
                //默认文件正常上传
                let flag = true;
                //向父组件抛出beforeUpload---接受回调
                this.$emit('beforeUpload', file, function (res) {
                    //根据回调判断是否继续上传
                    flag = res;
                });
                return flag;
            },
            /**
             * 开始上传
             */
            submitUpload() {
                this.$refs.upload.submit();
            }
        }
    }
</script>