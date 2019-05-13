<template>
    <div id="sysLog">
        <div class="table">
            <!--表格查询-->
            <div class="header">
                <el-row>
                    <el-col :span="4">
                        <el-input clearable size="mini" v-model="tableDataRequest.data.username"
                                  placeholder="用户名"></el-input>
                    </el-col>
                    <el-col :span="4" :offset="1">
                        <el-select size="mini" v-model="tableDataRequest.data.roleId" placeholder="是否允许登录">
                            <el-option label="请选择" value=""></el-option>
                            <el-option v-for="item in roleList" :key="item.id" :label="item.name"
                                       :value="item.id"></el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="5" :offset="1">
                        <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd" size="mini"
                                        v-model="tableDataRequest.data.createTime"></el-date-picker>
                    </el-col>
                    <el-col :span="4" :offset="1">
                        <el-input clearable size="mini" v-model="tableDataRequest.data.description"
                                  placeholder="操作"></el-input>
                    </el-col>
                    <el-button type="primary" size="mini" icon="el-icon-refresh" @click="$refs.table.renderTable()">查询
                    </el-button>
                </el-row>
            </div>
            <wei-table ref="table" :tableDataRequest="tableDataRequest" :tableColumns="tableColumns"></wei-table>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Index.vue')
        },
        data() {
            let that = this;
            return {
                tableDataRequest: {
                    url: that.$global.URL.getLogList,
                    data: {
                        username: '',
                        roleId: '',
                        createTime: ''
                    }
                },
                tableColumns: [
                    {prop: 'username', label: '用户名'},
                    {prop: 'realName', label: '真实姓名'},
                    {prop: 'roleName', label: '角色'},
                    {prop: 'description', label: '操作'},
                    {prop: 'create_time', label: '创建时间'}
                ],
                roleList: []
            }
        },
        mounted() {
            let that = this;
            this.$axios({
                url: that.$global.URL.getRoleList,
                success(data) {
                    that.roleList = data;
                }
            });
        }
    }
</script>

<style scoped>
    .header {
        margin-bottom: 10px;
    }
</style>