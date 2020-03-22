<template>
    <div id="tabs">
        <el-tabs v-if="null != tabs || 0 < tabs.length"
                 type="border-card" closable
                 v-model="tabValue"
                 @tab-remove="removeTab"
                 @tab-click="clickTab">
            <el-tab-pane v-for="item in tabs" :key="item.path" :name="item.path">
                <span slot="label" @contextmenu.prevent="closeAll(item)">
                    <i :class="item.icon || 'el-icon-s-help'"></i>
                    <span style="margin-left: 5px;">{{item.title}}</span>
                </span>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "Tabs",
        data() {
            return {
                tabs: [],
                tabValue: ''
            }
        },
        watch: {
            //监听路由变化
            $route(to, from) {
                //根路径不添加到tabs里面
                if ('/' === to.path) {
                    return;
                }
                this.tabValue = to.path;
                let tabs = this.tabs;
                for (let i = 0; i < tabs.length; i++) {
                    let value = tabs[i];
                    if (value.path === to.path) {
                        return;
                    }
                }
                this.tabs = tabs.concat({
                    title: to.meta.title,
                    path: to.path,
                    icon: to.meta.icon || 'el-icon-s-help'
                });
            }
        },
        mounted() {
            let {path, meta} = this.$router.history.current;
            this.tabs = [{
                title: meta.title,
                path,
                icon: meta.icon || 'el-icon-s-help'
            }];
            this.tabValue = path;
        },
        methods: {
            removeTab(targetName) {
                let {tabs, tabValue} = this;
                //关闭的页面不是当前活跃页面
                if (targetName !== tabValue) {
                    this.tabs = tabs.filter(tab => tab.path !== targetName);
                    return;
                }
                //当前只有一个页面
                if (1 >= tabs.length) {
                    return;
                }
                tabs.forEach((tab, index) => {
                    if (tab.path === targetName) {
                        let nextTab = tabs[index + 1] || tabs[index - 1];
                        if (nextTab) {
                            tabValue = nextTab.path;
                        }
                    }
                });
                this.tabValue = tabValue;
                this.tabs = tabs.filter(tab => tab.path !== targetName);
                this.$router.push(tabValue);
            },
            clickTab(tab) {
                this.$router.push(tab.name);
            },
            closeAll(item) {
                let {path, title, icon} = item;
                let that = this;
                this.$globalFun.messageBox({
                    message: `保留'${title}'页面,关闭其他页面`,
                    confirm() {
                        that.tabs = [{
                            title,
                            path,
                            icon
                        }];
                        that.tabValue = path;
                        that.$router.push(path);
                    }
                });
            }
        }
    }
</script>

<style>
    .el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
        height: 44px;
    }
</style>