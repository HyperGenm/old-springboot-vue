<template>
    <div id="leftNav">
        <el-row class="tac" :style="menuCollapse?'width: 70px;':'width: 220px;'">
            <el-col>
                <el-menu
                        class="el-menu-vertical-demo"
                        :default-active="defaultActive"
                        router :collapse="menuCollapse"
                        background-color="#545c64"
                        text-color="#fff"
                        active-text-color="#ffd04b">
                    <div v-for="(item,index) in $store.state.routers.routersTree" :key="index" :index="item.name">
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
                </el-menu>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'tree-menu': () => import('./treeMenu.vue')
        },
        props: {
            menuCollapse: {
                type: Boolean,
                default: false
            }
        },
        watch: {
            //监听路由变化
            $route(to, from) {
                this.defaultActive = to.name;
            }
        },
        data() {
            return {
                defaultActive: ''
            }
        }
    }
</script>

<style lang="less" scoped>
    #leftNav {
        .tac {
            position: absolute;
            top: 61px;
            bottom: 0;
            left: 0;
            height: 100%;
            overflow-y: scroll;
            overflow-x: hidden;
        }
    }
</style>