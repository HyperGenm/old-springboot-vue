# springboot2-vue3

## 演示地址
[在线演示地址](http://39.96.52.201/)    
用户名:superadmin  
密码:111111  
*tip:多个用户同时登陆可能会被顶掉*

## 结构简介
tip:前后端分离项目，代码通过90%*阿里巴巴编码规约扫描*、*findbugs*

##项目环境
   * JDK1.8、mysql 8、redis、rabbitmq、lombok
   * node、vue-cli3
   
##常见错误
   * `/login ------请求失败-----error: Error: Cannot find module './q/Index'`
       * 出错：后台创建新菜单(该菜单没有下级)并且权限添加该菜单后，重新登录出现该错误
       * 解决：vue项目中/src/views/index/路径下添加该菜单目录，以及Index.vue文件 

### 后端:
#### springboot、mybatis、redis
1. 简介
    * 基于 *springboot* 模板创建的项目
    * 项目需要安装、启动 *redis* 环境
2. 基本配置
    * *.yml*文件可以配置相关信息
3. 权限管理
    * 带有`@AdminAuthToken`注解的接口，请求头必须有 *token* 才能访问
    * 配合vue前端页面动态渲染路由，以及隐藏显示按钮:按钮保存在`$store.state.role['buttons']`中
    * 精确到接口级别权限，必须完善 *功能管理* 中菜单或者按钮对应的 *对应api* ,否则默认放行
4. 封装厂用CURD,继承 *BaseService* 即可
5. 根据数据库自动生成实体类 
    * 运行 *org.mybatis.generator.plugin.MyBatisTest.main()* 方法
    * 具体配置 *resources/config/generator-config.xml* 

### 前端:
#### vue:
1. 简介
    * 基于*vue cli3*创建的项目
    * 界面UI:*element-ui*
    * 网络请求:*axios*
        * 全局调用方式 `this.$axios({
                          url: '',
                          data: {},
                          success(data) {}
                     });`
            * url:只需要域名之后的地址
            * success:回调只需要处理 *code为200* 的情况

2. 基本配置
    * 初次运行需要进入vue文件运行 *npm install* 命令安装依赖
    * 配合后端实现动态路由:功能管理表单填写path路径，默认根路径为*/src/views/index/*
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