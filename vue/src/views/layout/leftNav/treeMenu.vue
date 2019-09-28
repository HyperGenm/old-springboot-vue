<template>
    <div class="treeMenu">
        <div v-for="(item,index) in data" :key="index" :index="item.name">
            <el-menu-item v-if="null == item.children || 0 >= item.children.length" :index="item.name">
                <el-tooltip :content="item.title" placement="right">
                    <div>
                        <i :class="item['icon'] || 'el-icon-s-help'"></i>
                        <span>{{item.title}}</span>
                    </div>
                </el-tooltip>
            </el-menu-item>
            <el-submenu v-else :index="item.name">
                <template slot="title">
                    <el-tooltip :content="item.title" placement="right">
                        <div>
                            <i :class="item['icon'] || 'el-icon-s-help'"></i>
                            <span>{{item.title}}</span>
                        </div>
                    </el-tooltip>
                </template>
                <tree-menu :data="item.children"></tree-menu>
            </el-submenu>
        </div>
    </div>
</template>

<script>
    export default {
        name: "treeMenu",
        props: {
            data: {
                type: Array,
                default: () => []
            }
        }
    }
</script>