<template>
    <div id="leftNav">
        <el-row class="tac" :style="'width: ' + (menuCollapse ? 70 : 220) + 'px'">
            <el-col>
                <el-menu background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
                         router unique-opened
                         :default-active="$route.path" :collapse="menuCollapse">
                    <div v-for="item in $store.state.routers.routersTree" :key="item.name">
                        <el-menu-item v-if="null == item.children || 0 >= item.children.length"
                                      :index="'/' + item.path">
                            <i :class="item['icon'] || 'el-icon-info'"></i>
                            <span slot="title">{{item.title}}</span>
                        </el-menu-item>
                        <el-submenu v-else :index="'/' + item.path">
                            <template slot="title">
                                <i :class="item['icon'] || 'el-icon-s-help'"></i>
                                <span>{{item.title}}</span>
                            </template>
                            <tree-menu :data="item" :parnetPath="item.path"></tree-menu>
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
        }
    }
</script>

<style lang="scss" scoped>
    #leftNav {
        .tac {
            position: absolute;
            top: 61px;
            bottom: 30px;
            left: 0;
            overflow-y: scroll;
            overflow-x: hidden;
        }
    }
</style>