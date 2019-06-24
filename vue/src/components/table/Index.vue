<template>
    <div id="weiTable">
        <div class="search" v-show="tableSearch && 0 < tableSearch.length">
            <!--表格查询-->
            <el-form inline size="mini">
                <el-form-item>
                    <el-form-item v-for="(item,index) in tableSearch" :key="index">
                        <template v-if="'input' === item.type">
                            <el-input v-model="tableDataRequest.data[item.prop]" :type="item.inputType || 'text'"
                                      :placeholder="item.placeholder" clearable
                                      :disabled="item.disabled || false"></el-input>
                        </template>
                        <template v-else-if="'select' === item.type">
                            <el-select v-model="tableDataRequest.data[item.prop]"
                                       clearable :disabled="item.disabled || false"
                                       :placeholder="item.placeholder || '请选择'">
                                <el-option v-for="(option,index) in item.options" :key="index"
                                           :label="option.label" :value="option.value"
                                           :disabled="option.disabled || false"></el-option>
                            </el-select>
                        </template>
                        <template v-else-if="'radio' === item.type">
                            <el-radio-group v-model="tableDataRequest.data[item.prop]"
                                            :disabled="item.disabled || false">
                                <el-radio v-for="(option,index) in item.options" :key="index"
                                          :label="option.value" :disabled="option.disabled || false">
                                    {{option.label}}
                                </el-radio>
                            </el-radio-group>
                        </template>
                        <template v-else-if="'textarea' === item.type">
                            <el-input type="textarea" v-model="tableDataRequest.data[item.prop]"
                                      :disabled="item.disabled || false"></el-input>
                        </template>
                        <template v-else-if="'checkbox' === item.type">
                            <el-checkbox-group v-model="tableDataRequest.data[item.prop]"
                                               :disabled="item.disabled || false">
                                <el-checkbox v-for="(option,index) in options" :key="index"
                                             :label="option.value" :disabled="option.disabled || false">{{option.label}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </template>
                        <template v-else-if="'switch' === item.type">
                            <el-switch v-model="tableDataRequest.data[item.prop]"
                                       :disabled="item.disabled || false"></el-switch>
                        </template>
                        <template v-else-if="'datePicker' === item.type">
                            <el-date-picker :type="item['dateType'] || 'date'" :placeholder="item.placeholder || '选择日期'"
                                            v-model="tableDataRequest.data[item.prop]"
                                            :value-format="item.valueFormat || 'yyyy-MM-dd'"
                                            :disabled="item.disabled || false"></el-date-picker>
                        </template>
                        <template v-else-if="'timePicker' === item.type">
                            <el-date-picker :placeholder="item.placeholder || '选择日期'"
                                            v-model="tableDataRequest.data[item.prop]"
                                            :disabled="item.disabled || false"></el-date-picker>
                        </template>
                        <template v-else>
                            {{item.label}}没有指定type
                        </template>
                    </el-form-item>
                    <slot name="search"></slot>
                    <el-button type="primary" size="mini" icon="el-icon-refresh"
                               @click="renderTable">
                        查询
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="header">
            <!--表格头部按钮组-->
            <el-row>
                <el-button type="primary" size="mini" icon="el-icon-refresh" @click="renderTable">刷新</el-button>
                <el-button v-for="(btn,index) in tableHeaderButtons" :key="index"
                           :type="btn.primary || 'primary'" size="mini"
                           :icon="btn.icon" @click="btn.handleClick(JSON.parse(JSON.stringify(selection)))"
                >{{btn.name}}
                </el-button>
            </el-row>
        </div>
        <div class="content">
            <!--表格-->
            <el-table ref="table"
                      :data="tableData" :max-height="tableStyle.maxHeight" height="10000px"
                      v-loading="loading" :empty-text="emptyText"
                      border stripe highlight-current-row size="small">
                <el-table-column type="selection" width="40"></el-table-column>
                <el-table-column type="index" width="50"></el-table-column>
                <el-table-column
                        v-for="(column, index) in tableColumns"
                        :key="index"
                        :prop="column.prop"
                        :label="column.label"
                        :width="column.width"
                        min-width="80"
                        :sortable="column.sortable"
                        :show-overflow-tooltip="column.showOverflowTooltip || true">
                    <template slot-scope="scope">
                        <!--需要处理元素———:formatter=""-->
                        <template v-if="column.formatter">
                            <div v-html="column.formatter(scope.row)"></div>
                        </template>
                        <!--表格普通元素-->
                        <template v-else>
                            <div>{{scope.row[column.prop]}}</div>
                        </template>
                    </template>
                </el-table-column>
                <!--表格中的操作按钮组-->
                <el-table-column label="操作"
                                 v-if="tableOperates && tableOperates.buttons && 0 < tableOperates.buttons.length"
                                 :width="tableOperates.width || 170" fixed="right">
                    <template slot-scope="scope">
                        <el-button v-for="(btn, index) in tableOperates.buttons" :key="index"
                                   @click="btn.handleClick(JSON.parse(JSON.stringify(scope.row)))"
                                   size="mini"
                                   :type="btn.type">{{btn.name}}
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="pagination">
            <!--表格分页-->
            <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                           :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="total"
                           @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
            ></el-pagination>
        </div>
    </div>
</template>

<script>
    /**引入axios*/
    import axios from "axios";
    /**引入参数处理*/
    import Qs from 'qs';

    /**axios默认配置*/
    axios.defaults.timeout = 20000;//设置超时时间，规定时间内没有响应则执行失败回调

    export default {
        name: "Index",
        props: {
            // 表格数据请求
            tableDataRequest: {
                type: Object,
                default: () => {
                }
            },
            //表格搜索
            tableSearch: {
                type: Array,
                default: () => []
            },
            //表格顶部按钮
            tableHeaderButtons: {
                type: Array,
                default: () => []
            },
            //表格样式
            tableStyle: {
                type: Object,
                default: () => {
                    return {
                        maxHeight: '670'
                    }
                }
            },
            // 表格的字段展示
            tableColumns: {
                type: Array,
                default: () => []
            },
            // 表格的操作按钮
            tableOperates: {
                type: Object,
                default: () => {
                }
            }
        },
        data() {
            return {
                tableData: [],
                pageSize: 20,
                pageNum: 1,
                total: 0,
                //表格加载中动画
                loading: false,
                //空数据时显示的内容
                emptyText: ''
            }
        },
        computed: {
            // 所有选中的行
            selection() {
                return this.$refs['table'].selection;
            }
        },
        mounted() {
            this.getCourseList();
        },
        methods: {
            // 获取数据
            getCourseList() {
                /**开启加载中动画*/
                this.loading = true;
                /**重置表格数据*/
                this.tableData = [];
                let that = this;
                let _url = that.tableDataRequest.url;
                let _method = that.tableDataRequest.method || 'get';
                let _header = that.tableDataRequest.header || 'application/x-www-form-urlencoded; charset=UTF-8';
                let _axios = {
                    url: that.$global.GLOBAL.base_url + _url,
                    method: _method,
                    headers: {
                        'Content-Type': _header,
                        'token': that.$store.state.token || ''
                    }
                };
                let _data = that.tableDataRequest.data || {};
                _data['pageNum'] = that.pageNum;
                _data['pageSize'] = that.pageSize;
                _data['__t'] = (new Date()).getTime();
                if (_method === 'get' || _method === 'GET') {
                    _axios['params'] = _data;
                } else {
                    _axios['data'] = Qs.stringify(_data, {indices: false});
                }
                axios(_axios).then((res) => {
                    /**关闭加载中动画*/
                    that.loading = false;
                    /**处理status不为200的出错请求*/
                    if (200 !== res.status) {
                        that.$globalFun.errorMsg('请求出错:' + res.status);
                        that.emptyText = res;
                        console.warn(_url, '-----status:', res.status, '------请求出错-----res:', res);
                        return;
                    }
                    /**token过期处理*/
                    if (401 === res.data.code) {
                        that.$globalFun.errorMsg('登陆过期，即将跳转到登录页面');
                        that.$store.dispatch('resetState');
                        sessionStorage.setItem('loginStatus', 'logout');
                        let timer = setTimeout(() => {
                            clearTimeout(timer);
                            that.$router.replace('login');
                        }, 3000);
                        return;
                    }
                    /**处理code不为200的出错请求*/
                    if (200 !== res.data.code) {
                        that.$globalFun.errorMsg(res.data.msg);
                        that.emptyText = res;
                        console.warn(_url, '-----code:', res.data.code, '------请求出错-----res:', res);
                        return;
                    }
                    /**判断返回格式是否正确*/
                    if (null == res.data.data.list) {
                        that.emptyText = '返回格式出错。示例:{"list":[],"pageNum":1,"pageSize":10}';
                        return;
                    }
                    /**展示数据*/
                    that.emptyText = '暂无数据';
                    that.tableData = res.data.data.list;
                    that.total = res.data.data.total;
                }).catch((error) => {
                    /**关闭加载中动画*/
                    that.loading = false;
                    that.$globalFun.errorMsg('请求失败');
                    that.emptyText = error.response.data.message;
                    if (error.response) {
                        console.warn(_url, '------请求失败-----error:', error, '---失败详情:', error.response);
                    } else {
                        console.warn(_url, '------请求失败-----error:', error);
                    }
                });
            },
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                this.getCourseList();
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.getCourseList();
            },
            renderTable() {
                this.getCourseList();
            }
        }
    }
</script>

<style lang="less">
    #weiTable {
        overflow: hidden;
        .search {
            .el-form-item {
                margin-bottom: 3px;
            }
        }
    }
</style>

<style lang="less" scoped>
    #weiTable {
        overflow: hidden;
        .header {
            margin-bottom: 10px;
        }
        .pagination {
            float: right;
            margin-top: 7px;
            margin-right: 20px;
        }
    }
</style>