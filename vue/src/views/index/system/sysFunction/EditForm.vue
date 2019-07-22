<template>
    <div id="form">
        <dialog-form :show.sync="visible" :title="'add' === handleType ? '新增' : '编辑'"
                     :formData="form" :formOptions="formOptions"
                     @submit="submit">
            <template v-slot:itemHead>
                <el-form-item label="上级路由">
                    <el-input disabled v-model="parentData.title"></el-input>
                </el-form-item>
            </template>
            <template v-slot:itemTail>
                <el-form-item label="路由图标">
                    <el-input v-model="form.icon" style="display: none;"></el-input>
                    <el-button type="primary" @click="dialogIcons = true">选择图标
                    </el-button>
                    <i style="margin-left: 20px;font-size: 20px;" :class="form.icon"></i>
                </el-form-item>
            </template>
        </dialog-form>
        <div class="icons">
            <wei-icons :dialogIcons="dialogIcons" @chooseIconOk="chooseIconOk"></wei-icons>
        </div>
    </div>
</template>

<script>
    const baseOptions = [
        {type: 'input', label: '标题', prop: 'title', required: true},
        {type: 'input', label: '功能名name', prop: 'name', required: true},
        {type: 'input', label: '功能路径', prop: 'path', required: true},
        {type: 'input', label: '排序', prop: 'sort', inputType: 'number', required: true},
        {
            type: 'radio', label: '类型', prop: 'type', required: true, options: [
                {label: '菜单', value: 0},
                {label: '按钮', value: 1}
            ]
        },
        {type: 'textarea', label: '路由描述', prop: 'description'}
    ];
    export default {
        name: "EditForm",
        components: {
            'dialog-form': () => import('@/components/dialog/form/Index.vue'),
            'wei-icons': () => import('@/components/dialog/icons/Index.vue')
        },
        props: {
            parentData: {
                type: Object
            },
            handleType: {
                type: String,
                default: 'add'
            },
            formData: {
                type: Object
            },
            show: {
                type: Boolean,
                default: false
            }
        },
        watch: {
            show() {
                this.visible = this.show;
            },
            visible() {
                if (!this.visible) {
                    this.$emit('update:show', false);
                }
            },
            formData() {
                this.form = this.formData;
            },
            handleType() {
                this.formOptions = JSON.parse(JSON.stringify(baseOptions));
                if ('update' === this.handleType) {
                    this.formOptions[1]['disabled'] = true;
                }
            }
        },
        data() {
            return {
                visible: false,
                formOptions: JSON.parse(JSON.stringify(baseOptions)),
                form: this.formData,
                dialogIcons: false
            }
        },
        methods: {
            submit() {
                let that = this;
                let url = this.handleType === 'add' ? 'sysFunctionAdd' : 'sysFunctionUpdate';
                that.$axios({
                    url: that.$global.URL[url],
                    method: 'post',
                    data: that.form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('update:show', false);
                        that.$emit('renderTable');
                        that.$emit('renderTree');
                    }
                });
            },
            chooseIconOk(icon) {
                this.dialogIcons = false;
                this.form.icon = icon;
            }
        }
    }
</script>