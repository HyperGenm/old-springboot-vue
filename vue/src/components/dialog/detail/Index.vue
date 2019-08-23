<template>
    <div id="wei-dialog-detail">
        <slot name="rowHead"></slot>
        <el-row v-for="(row,index) in rows" :key="index">
            <el-col :span="5">
                <el-tooltip class="item" effect="dark" :content="row.title || '标题'" placement="top">
                    <div class="title">
                        {{row.title || '标题'}}
                    </div>
                </el-tooltip>
            </el-col>
            <el-col :span="19">
                <div class="content">
                    <template v-if="row.formatter">
                        <div v-html="row.formatter()"></div>
                    </template>
                    <!--表格普通元素-->
                    <template v-else>
                        <div>{{row.content}}</div>
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
            }
        }
    }
</script>

<style lang="less">
    #wei-dialog-detail {
        .el-row {
            color: #666;
            margin-bottom: 5px;
        }
        .title {
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