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
                :on-change="onChange"
                :before-upload="beforeUpload"
                :list-type="listType"
                :auto-upload="autoUpload"
                :file-list="fileList"
                :limit="limit"
                :on-exceed="onExceed"
                :accept="accept">
            <el-button size="small" type="primary">点击上传</el-button>
            <el-button v-if="!autoUpload && listType !== 'picture-card'" style="margin-top: 10px;" size="small"
                       type="success" @click="submitUpload">
                上传到服务器
            </el-button>
        </el-upload>
        <el-button v-if="!autoUpload && listType === 'picture-card'" style="margin-top: 10px;" size="small"
                   type="success" @click="submitUpload">
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
                default: 'picture'
            },
            //是否在文件选择后立即上传
            autoUpload: {
                type: Boolean,
                default: false
            },
            //允许的最大上传个数
            limit: {
                type: Number,
                default: 1
            },
            //文件上传提示
            tip: {
                type: String,
                default: ''
            },
            //文件类型
            accept: {
                type: String,
                default: 'image/*'
            },
            //最大尺寸---默认2M
            maxSize: {
                type: Number,
                default: 2097152
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
             * 点击上传的文件--如果是图片显示缩略图
             * @param file
             */
            onPreview(file) {
                let type = file['raw']['type'];
                if (0 === type.indexOf('image/')) {
                    this.dialogImageUrl = file['url'];
                    this.dialogVisible = true;
                }
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
             * 文件改变时触发
             * @param file
             * @param fileList
             */
            onChange(file, fileList) {
                let that = this;
                for (let i = 0; i < fileList.length; i++) {
                    let {type, size} = fileList[i]['raw'];
                    //判断文件是否超出尺寸
                    if (size > that.maxSize) {
                        fileList.splice(i, 1);
                        that.$globalFun.errorMsg('图片超出最大尺寸');
                        continue;
                    }
                    //如果为全部类型，不做处理
                    if ("*" === that.accept) {
                        continue;
                    }
                    //如果存在该类型
                    if (-1 !== that.accept.indexOf(type)) {
                        continue;
                    }
                    //如果有图片类型，并且是所有图片类型
                    if (-1 !== that.accept.indexOf("image/*")) {
                        if (0 === type.indexOf("image/")) {
                            continue;
                        }
                    }
                    //如果有音频类型，并且是所有音频类型
                    if (-1 !== that.accept.indexOf("audio/*")) {
                        if (0 === type.indexOf("audio/")) {
                            continue;
                        }
                    }
                    //如果有视频类型，并且是所有视频类型
                    if (-1 !== that.accept.indexOf("video/*")) {
                        if (0 === type.indexOf("video/")) {
                            continue;
                        }
                    }
                    if (-1 !== that.accept.indexOf("application/*")) {
                        if (0 === type.indexOf("application/")) {
                            continue;
                        }
                    }
                    if (-1 !== that.accept.indexOf("text/*")) {
                        if (0 === type.indexOf("text/")) {
                            continue;
                        }
                    }
                    fileList.splice(i, 1);
                    that.$globalFun.errorMsg('文件类型错误');
                }
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
            },
            /**
             * 超出最大数量时
             * @param files
             * @param fileList
             */
            onExceed(files, fileList) {
                this.$globalFun.errorMsg("超出最大数量限制");
                console.warn("文件上传超出数量限制---files:", files, '-----fileList:', fileList);
            }
        }
    }
</script>