<template>
    <div id="dialogForm" style="overflow: hidden;">
        <el-form ref="form" size="mini" :label-width="labelWidth"
                 :model="formData || {}" :rules="formRules || {}">
            <slot name="itemHead"></slot>
            <div v-for="item in formOptions" :key="item.prop"
                 v-if="!item['hidden']">
                <!--一行展示多个-->
                <div v-if="null != item.items && 0 < item.items.length">
                    <div style="overflow: hidden">
                        <wei-item v-for="(i,index) in item.items" :key="i.prop"
                                  :item="i" :formData="formData"
                                  v-if="!i['hidden']"
                                  :style="itemsType(item.items)"></wei-item>
                    </div>
                </div>
                <!--一行展示一个-->
                <div v-else>
                    <wei-item :item="item" :formData="formData"
                              @selectChange="selectChange"></wei-item>
                </div>
            </div>
            <slot name="itemTail"></slot>
        </el-form>
        <div slot="footer">
            <div style="float: right;">
                <el-button @click="$emit('closeDialog')">取 消</el-button>
                <slot name="button"></slot>
                <el-button type="warning" @click="resetForm">重置</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-item': () => import('./Item.vue')
        },
        props: {
            //form表单
            formData: {
                type: Object,
                default: () => {
                }
            },
            //form表单规则
            formRules: {
                type: Object,
                default: () => {
                }
            },
            //form表单组件
            formOptions: {
                type: Array,
                default: () => []
            },
            //表单左边的宽度
            labelWidth: {
                type: String,
                default: '7rem'
            }
        },
        methods: {
            itemsType(items) {
                let typeList = [
                    null,
                    'width:100%',
                    'width:49%;float:left',
                    'width:31%;float:left',
                    'width:23%;float:left',
                ];
                return typeList[items.length];
            },
            //下拉框值改变触发事件
            selectChange(value, prop) {
                this.$emit('selectChange', value, prop);
            },
            //监听表单提交
            submitForm() {
                let that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                    let form = {};
                    for (let key in that.formData) {
                        if (!that.formData.hasOwnProperty(key)) {
                            break;
                        }
                        if ('id' === key) {
                            form['id'] = that.formData['id'];
                            continue;
                        }
                        for (let i = 0; i < that.formOptions.length; i++) {
                            let {prop, hidden, items} = that.formOptions[i];
                            //一行有一项
                            if (null == items || 0 >= items.length) {
                                //如果当前项隐藏
                                if (hidden) {
                                    continue;
                                }
                                if (key === prop) {
                                    form[key] = that.formData[key];
                                    break;
                                }
                                continue;
                            }
                            //一行有多项
                            for (let j = 0; j < items.length; j++) {
                                let {prop, hidden} = items[j];
                                //如果当前项隐藏
                                if (hidden) {
                                    continue;
                                }
                                if (key === prop) {
                                    form[key] = that.formData[key];
                                    i = that.formOptions.length;
                                    break;
                                }
                            }
                        }
                    }
                    that.$emit('submit', form);
                });
            },
            //重置表单
            resetForm() {
                this.$refs['form'].resetFields();
            }
        }
    }
</script>