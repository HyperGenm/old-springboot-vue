<template>
    <div id="index">
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
                    <wei-dialog :show.sync="dialogDetail">
                        <detail :detailData="formData" :parentData="parentData"></detail>
                    </wei-dialog>
                </div>
                <div class="edit">
                    <wei-dialog :show.sync="dialogEditForm" :title="'add' === handleType ? '新增' : '编辑'">
                        <edit-form :parentData="parentData" :handleType="handleType"
                                   :formData="formData"
                                   @closeDialog="dialogEditForm = false"
                                   @renderTable="$refs.table.renderTable()"
                                   @renderTree="getAllFunctionTreeNotButton"></edit-form>
                    </wei-dialog>
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
            'wei-dialog': () => import('@/components/dialog/index/Index.vue'),
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
                    url: that.$global.URL.system.sysFunction.getList,
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
                    {label: '功能名', prop: 'name'},
                    {label: '路径', prop: 'path'},
                    {label: '路由标题', prop: 'title'},
                    {label: '对应api', prop: 'containApi'},
                    {
                        label: '路由图标', prop: 'icon', formatter(row) {
                            return `<i class="${row.icon}"></i>`;
                        }
                    },
                    {label: '排序', prop: 'sort'},
                    {label: '路由描述', prop: 'description'}
                ],
                tableOperates: {
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
            this.getAllFunctionTreeNotButton();
        },
        methods: {
            //获取所有功能列表树形结构
            getAllFunctionTreeNotButton() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysFunction.getAllFunctionTreeNotButton,
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
                            url: that.$global.URL.system.sysFunction.delete,
                            method: 'post',
                            data: {
                                ids
                            },
                            success() {
                                that.$globalFun.successMsg('删除成功');
                                let {tableData, total} = that.$refs['table'];
                                for (let i = 0; i < ids.length; i++) {
                                    tableData = tableData.filter(value => value['id'] !== ids[i]);
                                }
                                total -= ids.length;
                                that.$refs['table']['tableData'] = tableData;
                                that.$refs['table']['total'] = total;
                                that.getAllFunctionTreeNotButton();
                            }
                        })
                    }
                });
            }
        }
    }
</script>

<style lang="less" scoped>
    #index {
        display: flex;
        .tree {
            flex: 2.1;
            .title {
                font-weight: bold;
                margin-bottom: 30px;
            }
        }
        .wei-table {
            flex: 5.7;
            border-left: 1px solid #eee;
            padding-left: 2%;
        }
    }
</style>