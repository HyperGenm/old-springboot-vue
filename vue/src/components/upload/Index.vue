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
                :before-upload="beforeUpload"
                :list-type="listType"
                :auto-upload="autoUpload"
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
            }
        },
        data() {
            return {
                //当前成功上传的文件数组
                successFile: [],
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
                for (let i = 0; i < this.successFile.length; i++) {
                    if (file['uid'] === this.successFile[i]['uid']) {
                        this.successFile.splice(i, 1);
                        break;
                    }
                }
                this.$emit('success', this.successFile);
            },
            /**
             * 文件上传之前
             * @param file
             */
            beforeUpload(file) {
                let {maxSize, accept} = this;
                let {type, size} = file;
                //判断文件是否超出尺寸
                if (size > maxSize) {
                    this.$globalFun.errorMsg('图片超出最大尺寸');
                    return false;
                }
                //默认文件格式不正确
                let flag = false;
                //如果为全部类型，不做处理
                if ("*" === accept) {
                    flag = true;
                }
                //无法识别文件类型
                if (null == type || '' === type) {
                    this.$globalFun.errorMsg('图片文件格式不正确');
                    console.warn('图片上传文件格式不正确file:', file);
                    return false;
                }
                //如果存在该类型
                if (!flag && -1 !== accept.indexOf(type)) {
                    flag = true;
                }
                //如果有图片类型，并且是所有图片类型
                if (!flag && -1 !== accept.indexOf("image/*")) {
                    if (0 === type.indexOf("image/")) {
                        flag = true;
                    }
                }
                //如果有音频类型，并且是所有音频类型
                if (!flag && -1 !== accept.indexOf("audio/*")) {
                    if (0 === type.indexOf("audio/")) {
                        flag = true;
                    }
                }
                //如果有视频类型，并且是所有视频类型
                if (!flag && -1 !== accept.indexOf("video/*")) {
                    if (0 === type.indexOf("video/")) {
                        flag = true;
                    }
                }
                if (!flag && -1 !== accept.indexOf("application/*")) {
                    if (0 === type.indexOf("application/")) {
                        flag = true;
                    }
                }
                if (!flag && -1 !== accept.indexOf("text/*")) {
                    if (0 === type.indexOf("text/")) {
                        flag = true;
                    }
                }
                //如果文件格式不正确
                if (!flag) {
                    this.$globalFun.errorMsg('图片文件格式不正确');
                    console.warn('图片上传文件格式不正确file:', file);
                    return false;
                }
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
                    let {status, data} = res;
                    /**status不是200*/
                    if (200 !== status) {
                        item.onError();
                        that.$globalFun.errorMsg('文件上传失败');
                        console.warn('文件上传失败，status:', status, '---详情:', res);
                        return;
                    }
                    /**token过期处理*/
                    if (401 === data['code']) {
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
                    if (200 !== data['code']) {
                        item.onError();
                        that.$globalFun.errorMsg('文件上传失败');
                        console.warn('文件上传失败，code:', status, '---详情:', data);
                        return;
                    }
                    item.onSuccess();
                    that.successFile.push({
                        uid: item['file']['uid'],
                        data: data['data']
                    });
                    that.$emit('success', that.successFile);
                }).catch(error => {
                    item.onError();
                    that.$globalFun.errorMsg('文件上传失败');
                    console.warn('文件上传失败，详情:', (error.response || error));
                });
            }
        }
    }
</script>