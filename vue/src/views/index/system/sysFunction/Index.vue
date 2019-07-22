<template>
    <div id="sysFunc">
        <div class="tree">
            <div class="title">功能菜单</div>
            <el-tree ref="tree" node-key="id"
                     highlight-current default-expand-all
                     :expand-on-click-node="false" :data="[{title:'最高级',id:0,children:data}]" :props="treeProps"
                     @node-click="handleNodeClick"></el-tree>
        </div>
        <div class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest" :tableHeaderButtons="tableHeaderButtons"
                       :tableColumns="tableColumns" :tableOperates="tableOperates"></wei-table>
            <div class="handle">
                <div class="show">
                    <detail :show.sync="dialogDetail" :detailData="formData" :parentData="parentData"></detail>
                </div>
                <div class="edit">
                    <edit-form :show.sync="dialogEditForm" :parentData="parentData" :handleType="handleType"
                               :formData="formData"
                               @renderTable="$refs.table.renderTable()" @renderTree="getAllFunctionTree"></edit-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue'),
            'edit-form': () => import('./EditForm.vue'),
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            let {
                sysFunc_add, sysFunc_update, sysFunc_delete
            } = that.$store.state.role['buttons'];
            return {
                //功能菜单树数据
                data: [],
                //功能菜单树label字段替换为title
                treeProps: {
                    label: 'title'
                },
                tableDataRequest: {
                    url: that.$global.URL.sysFunctionGetList,
                    data: {
                        parentId: 0
                    }
                },
                tableHeaderButtons: [
                    {
                        name: '新增', icon: 'el-icon-plus', show: sysFunc_add, handleClick() {
                            that.handleType = 'add';
                            that.formData = {
                                name: '',
                                path: '',
                                title: '',
                                icon: '',
                                sort: 0,
                                type: 0,
                                description: '',
                                parentId: that.parentData.id
                            };
                            that.dialogEditForm = true;
                        }
                    },
                    {
                        name: '删除', icon: 'el-icon-delete', show: sysFunc_delete, handleClick(rows) {
                            let ids = [];
                            rows.forEach((value) => {
                                ids.push(value.id);
                            });
                            if (0 >= ids.length) {
                                that.$globalFun.errorMsg('请选择行');
                                return;
                            }
                            that.deleteFunction(ids);
                        }
                    }
                ],
                tableColumns: [
                    {prop: 'name', label: '功能名'},
                    {prop: 'path', label: '路径'},
                    {prop: 'title', label: '路由标题'},
                    {
                        prop: 'icon', label: '路由图标', formatter(row) {
                            return `<i class="${row.icon}"></i>`;
                        }
                    },
                    {prop: 'sort', label: '排序', sortable: true},
                    {prop: 'description', label: '路由描述'}
                ],
                tableOperates: {
                    width: 250,
                    buttons: [
                        {
                            name: '查看', type: 'primary', show: true, handleClick(row) {
                                that.formData = row;
                                that.dialogDetail = true;
                            }
                        },
                        {
                            name: '编辑', type: 'success', show: sysFunc_update, handleClick(row) {
                                that.handleType = 'update';
                                that.formData = row;
                                that.dialogEditForm = true;
                            }
                        },
                        {
                            name: '删除', type: 'danger', show: sysFunc_delete, handleClick(row) {
                                that.handleDeleteClick(row);
                            }
                        }
                    ]
                },
                //表单弹窗
                dialogEditForm: false,
                //是新增还是修改
                handleType: 'add',
                //表单数据
                formData: {},
                //父级功能信息
                parentData: {
                    id: 0,
                    title: '最高级'
                },
                //详情弹窗
                dialogDetail: false
            };
        },
        mounted() {
            this.getAllFunctionTree();
        },
        methods: {
            //获取所有功能列表树形结构
            getAllFunctionTree() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.sysFunctionGetAllFunctionTreeNotButton,
                    success(data) {
                        that.data = data;
                        that.$nextTick(() => {
                            that.$refs['tree'].setCurrentKey(that.parentData.id);
                        });
                    }
                });
            },
            //处理树点击事件
            handleNodeClick(data) {
                this.tableDataRequest.data.parentId = data.id;
                this.parentData = data;
                this.$refs.table.renderTable();
            },
            handleDeleteClick(row) {
                this.deleteFunction([row.id]);
            },
            //删除方法
            deleteFunction(ids) {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定删除，该操作无法撤销',
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.sysFunctionDelete,
                            method: 'post',
                            data: {
                                ids
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                that.$refs.table.renderTable();
                                that.getAllFunctionTree();
                            }
                        })
                    }
                });
            }
        }
    }
</script>

<style lang="less" scoped>
    #sysFunc {
        display: flex;
        .tree {
            flex: 1;
            .title {
                font-weight: bold;
                margin-bottom: 30px;
            }
        }
        .table {
            flex: 2.7;
            border-left: 1px solid #eee;
            padding-left: 2%;
        }
    }
</style>