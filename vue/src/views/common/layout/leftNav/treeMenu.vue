<template>
    <div class="treeMenu">
        <div v-for="(item,index) in data" :key="index" :index="item.name">
            <el-menu-item v-if="null == item.children || 0 >= item.children.length" :index="item.name">
                <i :class="item['icon']"></i>
                <span slot="title">{{item.title}}</span>
            </el-menu-item>
            <el-submenu v-else :index="item.name">
                <template slot="title">
                    <i :class="item['icon']"></i>
                    <span>{{item.title}}</span>
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