<template>
    <div id="wei-dialog-detail">
        <slot name="rowHead"></slot>
        <el-row v-for="(row,index) in rows" :key="index"
                v-if="!row['hidden']">
            <el-col :span="colNum">
                <el-tooltip class="item" effect="dark" :content="row.label || '标题'" placement="top">
                    <div class="label">
                        {{row.label || '标题'}}
                    </div>
                </el-tooltip>
            </el-col>
            <el-col :span="24 - colNum">
                <div class="content">
                    <!--自定义显示element-ui组件，属性详情请看element-ui官网-->
                    <template v-if="row.element">
                        <template v-if="'tag' === row.type">
                            <el-tag :type="row.element(row)['type'] || ''"
                                    :size="row.element(row)['size'] || 'mini'"
                                    :effect="row.element(row)['effect'] || 'dark'">
                                {{row.element(row)['content'] || row.prop}}
                            </el-tag>
                        </template>
                        <template v-else-if="'link' === row.type">
                            <el-link :target="row.element(row)['target'] || '_blank'"
                                     :href="row.element(row)['href'] || ''"
                                     :type="row.element(row)['type'] || ''"
                                     :icon="row.element(row)['icon'] || ''"
                                     :underline="row.element(row)['underline'] || false">
                                {{row.element(row)['content'] || row.prop}}
                            </el-link>
                        </template>
                        <template v-else-if="'switch' === row.type">
                            <el-switch :value="row.element(row)['value'] || ''"
                                       :disabled="row.element(row)['disabled'] || false"
                                       :activeColor="row.element(row)['activeColor'] || '#13ce66'"
                                       :inactiveColor="row.element(row)['inactiveColor'] || '#ff4949'"
                                       :activeText="row.element(row)['activeText'] || ''"
                                       :inactiveText="row.element(row)['inactiveText'] || ''"></el-switch>
                        </template>
                        <template v-else><h1 style="color: #ff4949;">{{row.label}}没有指定type</h1></template>
                    </template>
                    <template v-else>
                        <!--需要处理元素———:formatter=""-->
                        <template v-if="row.formatter">
                            <div v-html="row.formatter(row)"></div>
                        </template>
                        <!--表格普通元素-->
                        <template v-else>
                            <div>{{row.prop}}</div>
                        </template>
                    </template>
                </div>
            </el-col>
        </el-row>
        <slot name="rowTail"></slot>
    </div>
</template>

<script>
    export default {
        name: "Index",
        props: {
            rows: {
                type: Array,
                default: () => []
            },
            //左边站的格数
            colNum: {
                type: Number,
                default: 5
            }
        }
    }
</script>

<style lang="scss">
    #wei-dialog-detail {
        .el-row {
            color: #666;
            margin-bottom: 5px;
        }
        .label {
            font-size: 0.9rem;
            background-color: #eee;
            text-align: center;
            height: 35px;
            line-height: 35px;
            overflow: hidden;
        }
        .content {
            font-size: 0.8rem;
            border: 1px solid #e2e2e2;
            padding: 7px;
            box-sizing: border-box;
            min-height: 35px;
        }
    }
</style>