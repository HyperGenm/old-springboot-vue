<template>
    <div class="upload">
        <el-upload
                ref="upload"
                :action="$global.GLOBAL.base_url + action"
                :headers="headers"
                :multiple="multiple"
                :show-file-list="showFileList"
                :on-preview="onPreview"
                :on-remove="onRemove"
                :on-change="onChange"
                :before-upload="beforeUpload"
                :list-type="listType"
                :auto-upload="autoUpload"
                :file-list="nowFileList"
                :limit="limit"
                :on-exceed="onExceed"
                :accept="accept"
                :http-request="httpRequest">
            <el-button size="small" type="primary">点击上传</el-button>
            <el-button v-if="!autoUpload" size="small" type="success"
                       @click.stop="submitUpload">上传到服务器
            </el-button>
        </el-upload>
        <div slot="tip" class="el-upload__tip">{{tip}}</div>
        <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl">
        </el-dialog>
    </div>
</template>

<script>
    //引入axios
    import axios from "axios";
    /**引入element-ui组件*/
    import {Loading} from 'element-ui';

    export default {
        name: "Index",
        props: {
            //上传的地址
            action: {
                type: String
            },
            //设置请求头
            headers: {
                type: Object,
                default() {
                    let token = this.$store.state.token;
                    return {
                        'token': token || ''
                    }
                }
            },
            //是否支持多文件上传
            multiple: {
                type: Boolean,
                default: true
            },
            //是否展示上传的文件列表
            showFileList: {
                type: Boolean,
                default: true
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
            },
            //展示的文件列表
            fileList: {
                type: Array
            }
        },
        data() {
            let that = this;
            return {
                //文件展示列表
                nowFileList: that.fileList,
                dialogImageUrl: '',
                dialogVisible: false
            }
        },
        watch: {
            nowFileList(list) {
                this.$emit('update:fileList', list);
            }
        },
        methods: {
            /**
             * 点击上传的文件--如果是图片显示缩略图
             * @param file
             */
            onPreview(file) {
                try {
                    this.dialogImageUrl = file['url'];
                    this.dialogVisible = true;
                } catch (e) {
                    console.debug('当前显示不是图片，此消息可以忽略,详情:', e);
                }
            },
            /**
             * 文件列表删除时触发的事件
             * @param file
             * @param fileList
             */
            onRemove(file, fileList) {
                this.nowFileList = fileList;
            },
            /**
             * 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
             * @param file
             * @param fileList
             */
            onChange(file, fileList) {
                let {raw} = file;
                if (!this.beforeValidateFileSize(raw)) {
                    this.nowFileList = fileList.filter(value => value['uid'] !== file['uid']);
                    this.$globalFun.errorMsg('文件超出最大尺寸');
                    console.warn('上传文件超出最大尺寸file:', file);
                    return;
                }
                if (!this.beforeValidateFileType(raw)) {
                    this.$globalFun.errorMsg('文件格式不正确');
                    this.nowFileList = fileList.filter(value => value['uid'] !== file['uid']);
                    console.warn('上传文件格式不正确file:', file);
                    return;
                }
                this.nowFileList = fileList;
            },
            /**
             * 文件尺寸是否正确
             * @param file
             * @param fileList
             */
            beforeValidateFileSize(file) {
                let {maxSize} = this;
                let {size} = file;
                //判断文件是否超出尺寸
                return size <= maxSize;
            },
            /**
             * 检验文件格式
             * @param file
             */
            beforeValidateFileType(file) {
                let {accept} = this;
                let {type} = file;
                //默认文件格式不正确
                let flag = false;
                //如果为全部类型，不做处理
                if ("*" === accept) {
                    flag = true;
                }
                //无法识别文件类型
                if (null == type || '' === type) {
                    return false;
                }
                //如果存在该类型
                if (!flag && accept.includes(type)) {
                    flag = true;
                }
                //如果有图片类型，并且是所有图片类型
                if (!flag && accept.includes("image/*")) {
                    if (0 === type.indexOf("image/")) {
                        flag = true;
                    }
                }
                //如果有音频类型，并且是所有音频类型
                if (!flag && accept.includes("audio/*")) {
                    if (0 === type.indexOf("audio/")) {
                        flag = true;
                    }
                }
                //如果有视频类型，并且是所有视频类型
                if (!flag && accept.includes("video/*")) {
                    if (0 === type.indexOf("video/")) {
                        flag = true;
                    }
                }
                //以下为自定义类型
                if (!flag && accept.includes("application/*")) {
                    if (0 === type.indexOf("application/")) {
                        flag = true;
                    }
                }
                if (!flag && accept.includes("text/*")) {
                    if (0 === type.indexOf("text/")) {
                        flag = true;
                    }
                }
                return flag;
            },
            /**
             * 文件上传之前
             * @param file
             */
            beforeUpload(file) {
                //尺寸是否正确
                if (!this.beforeValidateFileSize(file)) {
                    this.$globalFun.errorMsg('文件超出最大尺寸');
                    console.warn('上传文件超出最大尺寸file:', file);
                    return false;
                }
                //如果文件格式不正确
                if (!this.beforeValidateFileType(file)) {
                    this.$globalFun.errorMsg('文件格式不正确');
                    console.warn('上传文件格式不正确file:', file);
                    return false;
                }
                let flag = true;
                //向父组件抛出beforeUpload---接受回调
                this.$emit('beforeUpload', file, function (res) {
                    //根据回调判断是否继续上传
                    flag = null == res ? true : res;
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
            },
            /**
             * 自定义上传请求
             * @param item
             */
            httpRequest(item) {
                /**开启加载中动画*/
                let loading = Loading.service({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let that = this;
                let formData = new FormData();
                formData.append('file', item['file']);
                axios({
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        'token': that.$store.state.token || ''
                    },
                    method: 'post',
                    url: that.$global.GLOBAL.base_url + that.action,
                    data: formData,
                }).then(res => {
                    /**关闭加载中动画*/
                    loading.close();
                    //获取响应状态码
                    let {http_code, axios_result_code} = that.$global.GLOBAL;
                    let {status, data} = res;
                    /**status不是200*/
                    if (http_code['success'] !== status) {
                        item.onError();
                        that.$globalFun.errorMsg('文件上传失败');
                        console.warn('文件上传失败，status:', status, '---详情:', res);
                        return;
                    }
                    /**token过期处理*/
                    if (axios_result_code['errorToken'] === data['code']) {
                        that.$globalFun.errorMsg('登陆过期，即将跳转到登录页面');
                        that.$store.dispatch('resetState');
                        sessionStorage.setItem('loginStatus', 'logout');
                        let that = this;
                        let timer = setTimeout(() => {
                            that.$router.replace('login');
                            clearTimeout(timer);
                        }, 3000);
                        return;
                    }
                    /**code不是200*/
                    if (axios_result_code['success'] !== data['code']) {
                        item.onError();
                        that.$globalFun.errorMsg('文件上传失败');
                        console.warn('文件上传失败，code:', status, '---详情:', data);
                        return;
                    }
                    item.onSuccess();
                    let {nowFileList} = that;
                    for (let i = 0; i < nowFileList.length; i++) {
                        if (item['file']['uid'] === nowFileList[i]['uid']) {
                            nowFileList[i]['response'] = data;
                            break;
                        }
                    }
                    that.nowFileList = nowFileList;
                }).catch(error => {
                    /**关闭加载中动画*/
                    loading.close();
                    item.onError();
                    that.$globalFun.errorMsg('文件上传失败');
                    console.warn('文件上传失败，详情:', (error.response || error));
                });
            }
        }
    }
</script>