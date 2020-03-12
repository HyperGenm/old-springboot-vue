<template>
    <div id="index">
        <el-form-item :label='item.label' :prop="item.prop" :required="item.required || false">
            <template v-if="'input' === item.type">
                <el-input v-model="formData[item.prop]" :size="item.size" :type="item.inputType || 'text'"
                          :placeholder="item.placeholder || item.label || '请输入'" clearable
                          :disabled="item.disabled || false"></el-input>
            </template>
            <template v-else-if="'select' === item.type">
                <el-select v-model="formData[item.prop]" :size="item.size"
                           clearable filterable :disabled="item.disabled || false"
                           :placeholder="item.placeholder || '请选择'"
                           @change="selectChange(formData[item.prop],item.prop)">
                    <el-option v-for="option in item.options" :key="option.value"
                               :label="option.label" :value="option.value"
                               :disabled="option.disabled || false"></el-option>
                </el-select>
            </template>
            <template v-else-if="'cascader' === item.type">
                <el-cascader v-model="formData[item.prop]" :size="item.size"
                             clearable filterable :disabled="item.disabled || false"
                             :placeholder="item.placeholder || '请选择'"
                             :options="item.options || []"
                             :props="item['props'] || {  checkStrictly: true }"
                             @change="selectChange(formData[item.prop],item.prop)"></el-cascader>
            </template>
            <template v-else-if="'radio' === item.type">
                <el-radio-group v-model="formData[item.prop]" :size="item.size"
                                :disabled="item.disabled || false">
                    <el-radio v-for="option in item.options" :key="option.value"
                              :label="option.value" :disabled="option.disabled || false">
                        {{option.label}}
                    </el-radio>
                </el-radio-group>
            </template>
            <template v-else-if="'textarea' === item.type">
                <el-input type="textarea" :size="item.size" v-model="formData[item.prop]"
                          :disabled="item.disabled || false" autosize></el-input>
            </template>
            <template v-else-if="'checkbox' === item.type">
                <el-checkbox-group v-model="formData[item.prop]" :size="item.size"
                                   :disabled="item.disabled || false">
                    <el-checkbox v-for="option in options" :key="option.value"
                                 :label="option.value" :disabled="option.disabled || false">{{option.label}}
                    </el-checkbox>
                </el-checkbox-group>
            </template>
            <template v-else-if="'datePicker' === item.type">
                <el-date-picker type="date" :placeholder="item.placeholder || '选择日期'"
                                v-model="formData[item.prop]" :size="item.size"
                                :value-format="item.valueFormat || 'yyyy-MM-dd'"
                                :disabled="item.disabled || false"></el-date-picker>
            </template>
            <template v-else-if="'timePicker' === item.type">
                <el-time-picker :placeholder="item.placeholder || '选择时间'"
                                v-model="formData[item.prop]" :size="item.size"
                                :value-format="item.valueFormat || 'HH:mm:ss'"
                                :disabled="item.disabled || false"></el-time-picker>
            </template>
            <template v-else-if="'dateTimePicker' === item.type">
                <el-date-picker type="datetime" :placeholder="item.placeholder || '选择时间'"
                                v-model="formData[item.prop]" :size="item.size"
                                :value-format="item.valueFormat || 'yyyy-MM-dd HH:mm:ss'"
                                :disabled="item.disabled || false"></el-date-picker>
            </template>
            <template v-else>
                {{item.label}}没有指定type
            </template>
        </el-form-item>
    </div>
</template>

<script>
    export default {
        name: "Item",
        props: {
            //每个表单项
            item: {
                type: Object,
                default: () => {

                }
            },
            //form表单
            formData: {
                type: Object,
                default: () => {
                }
            },
        },
        methods: {
            //下拉框值改变触发事件
            selectChange(value, prop) {
                this.$emit('selectChange', value, prop);
            },
        }
    }
</script>