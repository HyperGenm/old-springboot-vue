<template>
    <div id="weiTable" ref="weiTable">
        <div v-if="!notRequest" class="search" v-show="tableSearch && 0 < tableSearch.length" ref="search">
            <!--表格查询-->
            <el-form inline size="mini">
                <el-form-item v-for="item in tableSearch" :key="item.prop">
                    <template v-if="'input' === item.type">
                        <el-input v-model="tableDataRequest.data[item.prop]" :type="item.inputType || 'text'"
                                  :placeholder="item.placeholder" clearable
                                  :disabled="item.disabled || false"></el-input>
                    </template>
                    <template v-else-if="'select' === item.type">
                        <el-select v-model="tableDataRequest.data[item.prop]"
                                   clearable filterable :disabled="item.disabled || false"
                                   :placeholder="item.placeholder || '请选择'">
                            <el-option v-for="option in item.options" :key="option.value"
                                       :label="option.label" :value="option.value"
                                       :disabled="option.disabled || false"></el-option>
                        </el-select>
                    </template>
                    <template v-else-if="'radio' === item.type">
                        <el-radio-group v-model="tableDataRequest.data[item.prop]"
                                        :disabled="item.disabled || false">
                            <el-radio v-for="option in item.options" :key="option.value"
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
                            <el-checkbox v-for="option in options" :key="option.value"
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
                    <template v-else-if="'dateTimePicker' === item.type">
                        <el-date-picker type="datetime" :placeholder="item.placeholder || '选择时间'"
                                        v-model="tableDataRequest.data[item.prop]"
                                        :value-format="item.valueFormat || 'yyyy-MM-dd HH:mm:ss'"
                                        :disabled="item.disabled || false"></el-date-picker>
                    </template>
                    <template v-else>
                        {{item.label}}没有指定type
                    </template>
                </el-form-item>
                <slot name="search"></slot>
                <el-button type="primary" size="mini" icon="el-icon-refresh" style="margin-bottom: 10px;"
                           @click="renderTable">
                    查询
                </el-button>
            </el-form>
        </div>
        <div v-if="!notRequest" class="header" ref="header">
            <!--表格头部按钮组-->
            <el-row>
                <el-button type="primary" size="mini" icon="el-icon-refresh" @click="renderTable">刷新</el-button>
                <el-button type="primary" size="mini" icon="el-icon-s-tools"
                           @click="columnChangeDialog = true">字段
                </el-button>
                <el-button v-for="btn in tableHeaderButtons" :key="btn.name"
                           v-if="btn['show'] || false"
                           :type="btn['type'] || 'primary'" size="mini"
                           :icon="btn.icon" @click="btn.handleClick(JSON.parse(JSON.stringify(selection)))"
                >{{btn.name}}
                </el-button>
            </el-row>
        </div>
        <div class="content">
            <!--表格-->
            <el-table ref="table" :show-summary="tableShowSummary" :summary-method="summaryMethod"
                      :data="tableData" height="10000px"
                      :max-height="maxHeight || tableMaxHeight"
                      v-loading="loading" :empty-text="emptyText" @header-click="headerClick"
                      :stripe="null == selection || 0 >= selection.length"
                      @selection-change="selectionChange" :row-style="rowStyle"
                      @sort-change="sortChange"
                      :row-key="tableOther.rowKey || 'id'"
                      :default-expand-all="tableOther.defaultExpandAll || true"
                      border highlight-current-row size="small">
                <el-table-column v-if="!notRequest" type="selection" width="40"></el-table-column>
                <el-table-column type="index" fixed="left" width="50"></el-table-column>
                <slot name="startColumn"></slot>
                <el-table-column
                        v-for="column in tableShowColumns"
                        :key="column.prop"
                        :prop="column.prop"
                        :fixed="column.fixed || false"
                        :width="column.width"
                        min-width="80"
                        :sortable="column.sortable || false"
                        :show-overflow-tooltip="column.showOverflowTooltip || true">
                    <template slot="header" slot-scope="scope">
                        <!--自定义表头-->
                        <template v-if="column.labelFormatter">
                            <div v-html="column.labelFormatter()"></div>
                        </template>
                        <!--普通表头，只显示一行，超出部分隐藏-->
                        <template v-else>
                            <template v-if="null != column.label && 3 < column.label.length">
                                <el-tooltip effect="dark" :content="column.label" placement="top">
                                    <span style="white-space: nowrap;overflow: hidden;">{{column.label}}</span>
                                </el-tooltip>
                            </template>
                            <template v-else>
                                <span>{{column.label}}</span>
                            </template>
                        </template>
                    </template>
                    <template slot-scope="scope">
                        <!--自定义，单元格直接编辑-->
                        <template v-if="column.edit">
                            <template v-if="'input' === column.type">
                                <!--需要处理元素———:formatter=""-->
                                <template v-if="column.formatter">
                                    <div style="cursor:pointer;"
                                         v-html="column.formatter(scope.row)"
                                         @click="columnEditInputClick(column,scope)"></div>
                                </template>
                                <!--表格普通元素-->
                                <template v-else>
                                    <div style="cursor:pointer;"
                                         @click="columnEditInputClick(column,scope)">
                                        {{scope.row[column.prop]}}
                                    </div>
                                </template>
                            </template>
                            <template v-else><h1 style="color: #ff4949;">{{column.label}}没有指定type</h1></template>
                        </template>
                        <!--自定义显示element-ui组件，属性详情请看element-ui官网-->
                        <template v-else-if="column.element">
                            <template v-if="'tag' === column.type">
                                <el-tag :type="column.element(scope.row)['type'] || ''"
                                        :size="column.element(scope.row)['size'] || 'medium'"
                                        :effect="column.element(scope.row)['effect'] || 'light'">
                                    {{column.element(scope.row)['content'] || scope.row[column.prop]}}
                                </el-tag>
                            </template>
                            <template v-else-if="'link' === column.type">
                                <el-link :target="column.element(scope.row)['target'] || '_blank'"
                                         :href="column.element(scope.row)['href'] || null"
                                         :type="column.element(scope.row)['type'] || ''"
                                         :icon="column.element(scope.row)['icon'] || ''"
                                         :underline="column.element(scope.row)['underline'] || false">
                                    {{column.element(scope.row)['content'] || scope.row[column.prop]}}
                                </el-link>
                            </template>
                            <template v-else-if="'switch' === column.type">
                                <el-switch style="cursor:pointer;"
                                           @change="columnSwitchChange($event,scope)"
                                           :value="column.element(scope.row)['value'] || false"
                                           :disabled="column.element(scope.row)['disabled'] || false"
                                           :activeColor="column.element(scope.row)['activeColor'] || '#13ce66'"
                                           :inactiveColor="column.element(scope.row)['inactiveColor'] || '#ff4949'"
                                           :activeText="column.element(scope.row)['activeText'] || ''"
                                           :inactiveText="column.element(scope.row)['inactiveText'] || ''"></el-switch>
                            </template>
                            <template v-else-if="'icon' === column.type">
                                <i :class="column.element(scope.row)['leftIcon'] || ''"></i>
                                <span style="margin-left: 5px">{{column.element(scope.row)['content'] || scope.row[column.prop]}}</span>
                                <i :class="column.element(scope.row)['rightIcon'] || ''"></i>
                            </template>
                            <template v-else-if="'avatar' === column.type">
                                <div @click="avatarClick(column.element(scope.row)['src'])">
                                    <el-image :src="column.element(scope.row)['src']"
                                              :lazy="column.element(scope.row)['lazy'] || true"
                                              :alt="column.element(scope.row)['alt'] || ''"
                                              :fit="column.element(scope.row)['fit'] || 'cover'"
                                              :style="column.element(scope.row)['style'] || 'width:30px;height:30px'">
                                        <div slot="error">
                                            <i style="font-size: 21px;" class="el-icon-picture-outline"></i>
                                        </div>
                                    </el-image>
                                </div>
                            </template>
                            <template v-else><h1 style="color: #ff4949;">{{column.label}}没有指定type</h1></template>
                        </template>
                        <template v-else>
                            <!--需要处理元素———:formatter=""-->
                            <template v-if="column.formatter">
                                <div v-html="column.formatter(scope.row)"></div>
                            </template>
                            <!--表格普通元素-->
                            <template v-else>
                                <div>{{scope.row[column.prop]}}</div>
                            </template>
                        </template>
                    </template>
                </el-table-column>
                <slot name="endColumn"></slot>
                <!--表格中的操作按钮组-->
                <el-table-column label="操作" fixed="right"
                                 v-if="tableOperates && tableOperates.buttons && 0 < tableOperates.buttons.length"
                                 :width="tableOperates.width || 100">
                    <template slot-scope="scope">
                        <div v-if="isShowTableOperatesPopover(tableOperates['buttons'])">
                            <el-button v-for="btn in tableOperates.buttons" :key="btn.name"
                                       v-if="( btn['showFormatter'] && btn['showFormatter'](JSON.parse(JSON.stringify(scope.row))))
                                       || btn['show'] || false"
                                       @click="btn.handleClick(JSON.parse(JSON.stringify(scope.row)),scope['$index'])"
                                       size="mini" :type="btn.type">{{btn.name}}
                            </el-button>
                        </div>
                        <div v-else>
                            <el-popover placement="left"
                                        :width="tableOperates.width || 150"
                                        trigger="click">
                                <div style="margin: 10px auto;text-align: center;"
                                     v-for="(btn, index) in tableOperates.buttons" :key="index"
                                     v-if="( btn['showFormatter'] && btn['showFormatter'](JSON.parse(JSON.stringify(scope.row))))
                                       || btn['show'] || false">
                                    <el-button size="mini" :type="btn.type"
                                               @click="btn.handleClick(JSON.parse(JSON.stringify(scope.row)),scope['$index'])">
                                        {{btn.name}}
                                    </el-button>
                                </div>
                                <el-button slot="reference" style="padding: 7px 15px;">
                                    <span style="margin-right: 5px;">操作</span>
                                    <i class="el-icon-arrow-down"></i>
                                </el-button>
                            </el-popover>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div v-if="!notRequest && isPagination" class="pagination" ref="pagination">
            <!--表格分页-->
            <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                           :page-sizes="[10, 20, 50, 100, 200]" :page-size="pageSize" :total="total"
                           @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
            ></el-pagination>
        </div>
        <!--表格上面切换隐藏字段显示-->
        <div class="columnChoose">
            <wei-dialog :show.sync="columnChangeDialog">
                <el-checkbox-group v-model="columnCheckBox">
                    <el-checkbox v-for="item in tableColumns" :key="item.prop"
                                 style="margin-bottom: 10px;"
                                 :label="item.label" border></el-checkbox>
                </el-checkbox-group>
                <div style="overflow: hidden;margin-bottom: 20px;">
                    <el-button type="primary" style="float: right;margin-top: 20px;"
                               @click="changeColumn">保存
                    </el-button>
                </div>
            </wei-dialog>
        </div>
        <div class="show">
            <wei-dialog :show.sync="dialogShowImage">
                <img width="100%" :src="dialogImageUrl">
            </wei-dialog>
        </div>
    </div>
</template>

<script>
    /**引入axios*/
    import axios from "axios";
    /**引入参数处理*/
    import Qs from 'qs';

    export default {
        name: "Index",
        components: {
            'wei-dialog': () => import('@/components/dialog/index/Index.vue')
        },
        props: {
            // 表格数据请求
            tableDataRequest: {
                type: Object
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
            maxHeight: {
                type: Number
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
            },
            // 是否展示分页
            isPagination: {
                type: Boolean,
                default: true
            },
            // 是否展示合计行
            tableShowSummary: {
                type: Boolean,
                default: false
            },
            //其他属性自定义扩展
            tableOther: {
                type: Object,
                default: () => ({
                    rowKey: 'id',
                    defaultExpandAll: true
                })
            },
            //表格不需要请求
            notRequest: {
                type: Boolean,
                default: false
            },
            //不需要请求时默认的数据
            notRequestTableData: {
                type: Array,
                default: () => []
            }
        },
        data() {
            let that = this;
            return {
                tableData: [],
                pageSize: 20,
                pageNum: 1,
                total: 0,
                tableMaxHeight: 0,
                //表格加载中动画
                loading: false,
                //空数据时显示的内容
                emptyText: '',
                //当前选中行
                selection: [],
                //表格展示的表头
                tableShowColumns: that.tableColumns,
                //选择展示字段
                columnChangeDialog: false,
                //当前选中的表格字段
                columnCheckBox: that.tableColumns.map(value => value['label']),
                //弹窗展示图片
                dialogShowImage: false,
                //弹窗展示图片的路径
                dialogImageUrl: ''
            }
        },
        watch: {
            notRequestTableData(data) {
                this.tableData = data;
            }
        },
        mounted() {
            //如果不需要请求
            if (this.notRequest) {
                this.tableData = this.notRequestTableData;
            } else {
                //获取数据
                this.getTableList();
            }
            //初始化表格高度
            this.initTableMaxHeight();
        },
        methods: {
            initTableMaxHeight() {
                let that = this;
                this.$nextTick(() => {
                    let weiTableHeight = that.$refs['weiTable'].getBoundingClientRect().height;
                    let searchHeight = !that.notRequest ? that.$refs['search'].getBoundingClientRect().height : 0;
                    let headerHeight = !that.notRequest ? that.$refs['header'].getBoundingClientRect().height : 0;
                    let paginationHeight = (!that.notRequest && that.isPagination) ? that.$refs['pagination'].getBoundingClientRect().height : 0;
                    that.tableMaxHeight = weiTableHeight - searchHeight - headerHeight - paginationHeight - 20;
                });
            },
            // 获取数据
            getTableList() {
                /**开启加载中动画*/
                this.loading = true;
                /**重置表格数据*/
                this.tableData = [];
                let that = this;
                let _url = that.tableDataRequest.url;
                let _method = that.tableDataRequest.method || 'get';
                let _header = that.tableDataRequest.header || 'application/x-www-form-urlencoded; charset=UTF-8';
                let _timeout = that.tableDataRequest.timeout || parseInt(process.env.VUE_APP_AXIOS_TIMEOUT);
                let _axios = {
                    url: that.$global.GLOBAL.base_url + _url,
                    method: _method,
                    headers: {
                        'Content-Type': _header
                    },
                    timeout: _timeout
                };
                //每个请求加上请求头
                _axios['headers'][that.$global.GLOBAL.token] = that.$store.state.token || '';
                let _data = that.tableDataRequest.data || {};
                if (that.isPagination) {
                    _data['pageNum'] = that.pageNum;
                    _data['pageSize'] = that.pageSize;
                }
                _data['__t'] = (new Date()).getTime();
                _method = _method.toUpperCase();
                if (_method === 'GET') {
                    _axios['params'] = _data;
                } else {
                    _axios['data'] = Qs.stringify(_data, {indices: false});
                }
                axios(_axios).then((res) => {
                    /**关闭加载中动画*/
                    that.loading = false;
                    //获取响应状态码
                    let {axios_result_code} = that.$global.GLOBAL;
                    /**token过期处理*/
                    if (axios_result_code['errorToken'] === res.data.code) {
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
                    if (axios_result_code['success'] !== res.data.code) {
                        that.$globalFun.errorMsg(res.data['msg']);
                        that.emptyText = JSON.stringify(res['data']);
                        that.$globalFun.consoleWarnTable(`请求出错url:${_url}`, res['data']);
                        return;
                    }
                    //不是分页表格
                    if (!that.isPagination) {
                        /**展示数据*/
                        that.emptyText = '暂无数据';
                        that.tableData = res.data.data;
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
                    // 如果请求被取消则进入该方法
                    if (axios.isCancel(error)) {
                        return;
                    }
                    if (error.response) {
                        error = error['response']['data'];
                    }
                    //限制访问
                    if (403 === error.status || 403 === error.code) {
                        that.$globalFun.errorMsg('拒绝访问,请稍后重试,详情:' + JSON.stringify(error));
                        that.emptyText = '拒绝访问,详情' + JSON.stringify(error);
                    } else {
                        that.$globalFun.errorMsg('内部服务器错误，请稍后重试,详情:' + JSON.stringify(error));
                        that.emptyText = '内部服务器错误,详情' + JSON.stringify(error);
                    }
                    that.$globalFun.consoleWarnTable(`表格请求失败url:${_url}`, error);
                });
            },
            //pageSize改变触发
            handleSizeChange(pageSize) {
                this.pageNum = 1;
                this.pageSize = pageSize;
                this.getTableList();
            },
            //pageNum改变触发
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.getTableList();
            },
            //刷新表格数据
            renderTable() {
                this.getTableList();
            },
            //字段展示---选择部分字段展示
            changeColumn() {
                let columns = [];
                let {columnCheckBox, tableColumns} = this;
                for (let i = 0; i < tableColumns.length; i++) {
                    if (columnCheckBox.includes(tableColumns[i]['label'])) {
                        columns.push(tableColumns[i]);
                    }
                }
                this.tableShowColumns = columns;
                //初始化表格高度
                this.initTableMaxHeight();
                //重新渲染表格布局
                this.$nextTick(() => {
                    this.$refs['table'].doLayout()
                });
                this.columnChangeDialog = false;
            },
            //表格内部操作按钮是否折叠展示
            isShowTableOperatesPopover(buttons) {
                let num = 0;
                buttons.forEach(value => {
                    let {show, showFormatter} = value;
                    if (show) {
                        num++;
                    } else if (showFormatter && showFormatter()) {
                        num++;
                    }
                });
                return 2 > num;
            },
            //求和
            summaryMethod(param) {
                let sums = [];
                let that = this;
                this.$emit('summaryMethod', {
                    param,
                    total: that.total
                }, function (res) {
                    sums = res;
                });
                return sums;
            },
            //表头点击事件
            headerClick(column, event) {
                this.$emit('headerClick', column);
            },
            //当选择项发生变化时会触发该事件
            selectionChange(selection) {
                this.selection = selection;
            },
            //表格行样式
            rowStyle({row, rowIndex}) {
                if (this.selection.includes(row)) {
                    return {'background-color': 'rgba(185, 221, 249, 0.75) !important'};
                }
            },
            //当表格的排序条件发生变化的时候会触发该事件
            sortChange({column, prop, order}) {
                if ('descending' === order) {
                    order = 'desc';
                } else if ('ascending' === order) {
                    order = 'acs';
                }
                this.tableDataRequest.data[`${prop}Sort`] = order;
                this.getTableList();
            },
            //switch状态改变时触发
            columnSwitchChange(value, {$index, column, row}) {
                this.$emit('columnSwitchChange', {
                    index: $index,//在表格中的行数-1
                    prop: column['property'],//当前prop
                    row,//当前行的值
                    value//改变后得值
                });
            },
            columnEditInputClick({prop, label}, {$index, row}) {
                let that = this;
                this.$globalFun.messageBoxInput({
                    message: `请输入'${label}'列要更改的值`,
                    confirm(value, done) {
                        that.$emit('columnEditInputClick', {
                            value,//输入框中的值
                            done,//调用done()关闭对话框
                            index: $index,//在表格中的行数-1
                            prop,//当前prop
                            row//当前行的值
                        });
                    }
                });
            },
            //展示图片
            avatarClick(src) {
                this.dialogImageUrl = src;
                this.dialogShowImage = true;
            }
        }
    }
</script>

<style lang="scss">
    #weiTable {
        overflow: hidden;
        .search {
            .el-form-item {
                margin-bottom: 3px;
            }
        }
    }
</style>

<style lang="scss">
    @import "@/assets/sass/element-variables.scss";

    #weiTable {
        /*没有数据，或者出错时显示的文字*/
        span.el-table__empty-text {
            color: $--color-primary !important;
        }

        /*表头背景颜色*/
        .el-table thead th {
            background-color: #ddeeff;
        }

        /*表格树形结构，箭头错位*/
        .el-table table tbody tr td:nth-child(3) .cell.el-tooltip {
            display: flex;
        }

    }
</style>

<style lang="scss" scoped>
    #weiTable {
        height: 100%;
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