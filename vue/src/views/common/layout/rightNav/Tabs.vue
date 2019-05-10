<template>
    <div id="tabs">
        <el-tabs v-if="null != tabs || 0 < tabs.length" type="card" closable stretch
                 v-model="tabValue"
                 @tab-remove="removeTab"
                 @tab-click="clickTab">
            <el-tab-pane v-for="item in tabs" :key="item.name" :label="item.title"
                         :name="item.name">
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
                this.tabValue = to.name;
                let tabs = this.tabs;
                for (let i = 0; i < tabs.length; i++) {
                    let value = tabs[i];
                    if (value.name === to.name) {
                        return;
                    }
                }
                this.tabs = tabs.concat({
                    title: to.meta.title,
                    name: to.name,
                    icon: to.meta.icon || 'el-icon-info'
                });
            }
        },
        methods: {
            removeTab(targetName) {
                let tabs = this.tabs;
                let activeName = this.tabValue;
                if (activeName === targetName) {
                    tabs.forEach((tab, index) => {
                        if (tab.name === targetName) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                activeName = nextTab.name;
                            }
                        }
                    });
                }
                this.tabValue = activeName;
                this.tabs = tabs.filter(tab => tab.name !== targetName);
            },
            clickTab(tab) {
                this.$router.push(tab.name);
            }
        }
    }
</script>