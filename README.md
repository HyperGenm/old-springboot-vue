# springboot2-vue3

##演示地址
[在线演示地址](http://39.96.52.201/)    
用户名:admin  
密码:111111  
*多个用户同时登陆可能会被顶掉*

##结构简介
*tip:前后端分离项目*

###后端:
####springboot、mybatis、redis
1. 简介
    * 基于springboot模板创建的项目
    * 项目需要安装、启动redis环境
2. 基本配置
    * .yml文件可以配置相关信息

###前端:
####vue:
1. 简介
    * 基于vue cli3创建的项目
    * 界面UI:element-ui
    * 网络请求:axios
        * 全局调用方式 `this.$axios({
                          url: '',
                          data: {},
                          success(data) {}
                     });`;
            * url:只需要域名之后的地址
            * success:回调只需要处理code为200的情况

2. 基本配置
    * 初次运行需要进入vue文件运行npm install命令安装依赖
    * 配合后端实现动态路由:功能管理表单填写path路径，默认根路径为/src/views/index/
    * .env.production/development文件为配置文件
                     
3. 封装的常用组件
    * dialog:弹出框  
        * detail: 标题+内容的方式展示数据
        * form: 表单提交,`@submit`只需要处理表单验证之后的情况
        * index: 普通弹出框
    * table:表格
        * 表格接受的返回示例: `{"list":[],"pageNum":1,"pageSize":10}`
        * 表格数据请求 `tableDataRequest: {
                                      url: '',
                                      data: {}
                        }`
            * url:请求地址
            * data:额外参数，配合顶部搜索使用
        * 表格展示 `tableColumns: [
                         {prop: 'username', label: '用户名',formatter(){
                               return '';
                         }}
                    ]` 接受一个数组---参考layui表格
            * formatter: 复杂展示，可以返回一个dom