# springboot2-vue3
*一款基于springboot2和vue3的后台通用模板，没有多余的功能，只有 *权限管理* 和常用功能* 。前后端分离项目，代码通过90% *阿里巴巴编码规约扫描* 、 *findbugs*

## 上手指南
以下指南将帮助你在本地机器上安装和运行该项目，进行开发和测试。关于如何将该项目部署到在线环境，请参考部署小节。
### 安装要求、步骤
   * 安装配置Java环境， *JDK1.8*
   * 安装 *mysql 8* ，创建数据库(utf8mb4)导入sql(doc目录下)
   * 安装 *redis* ,下载安装即可
   * 开发工具需要安装 *lombok* 插件(开发工具推荐IDEA)
   * ---后端运行(如果出问题一般是yml配置文件中数据源之类的配置出错)
   * 安装配置 *node* 环境
   * 安装 *vue-cli3* ,进入vue目录执行`npm install`
   * ---前端运行`npm run serve`( *WebStorm* 或者 *IDEA* 可以直接点击 *package.json* 文件中第6行左边绿三角)

### 演示地址
   * [在线演示地址](http://39.96.52.201/)    
        * 用户名:superadmin  
        * 密码:111111  
        * *tip:多个用户同时登陆可能会被顶掉,数据库10分钟重置一次，如果登录或者操作异常，请每个整10分钟之后重试*
   * [接口文档](http://39.96.52.201:8080/swagger-ui.html)
        * *swagger*

## 部署
   * springboot目录下运行`mvn clean package`命令打包,打包后生成文件在/target/build目录下
        * config目录为存放的配置文件
        * lib目录为maven依赖的jar包
        * static目录存放静态文件
        * jar文件为生成的jar包(以后pom依赖不变的话可以只替换该jar包)
   * vue目录下运行`npm run build`命令打包，打包后生成文件在/dist目录下
        * 打包配置在 *.env* 文件和 *vue.config.js* 文件中
   * 部署服务器上面需要配置 *JDK1.8* 、 *mysql 8* 、 *redis* 环境
   * jar包运行`nohup java -jar springboot.jar &`可以在后台运行并且将日志输出到当前目录下 *nohup.out* 文件
   * 部署服务器建议配置 *nginx* ,vue打包后放在nginx下,不配置可以放在 *jar* 包同一目录 *static* 下面
   
## 常见错误
   * `java.lang.RuntimeException: Cannot resolve classpath entry: E:\maven-repository\mysql\mysql-connector-java\8.0.15\mysql-connector-java-8.0.15.jar`
       * 出错: 根据数据库生成实体类
       * 解决: *resources/config/generator-config.xml* 第6行jar包路径改成自己的jar包路径
   * `npm install`
       * 出错: vue安装依赖出错，一般是 *node-sass*
       * 解决: 更换淘宝镜像或者`npm install --save node-sass --registry=https://registry.npm.taobao.org --disturl=https://npm.taobao.org/dist --sass-binary-site=http://npm.taobao.org/mirrors/node-sass`   
   * `/login ------请求失败-----error: Error: Cannot find module './q/Index'`
       * 出错：后台创建新菜单(该菜单没有下级)并且权限添加该菜单后，重新登录出现该错误
       * 解决：vue项目中/src/views/index/路径下添加该菜单目录，以及Index.vue文件 

### 后端:
#### springboot、mybatis、redis
1. 简介
    * 基于 *springboot* 模板创建的项目
2. 基本配置
    * *.yml* 文件可以配置相关信息
    * *config* 目录下为常用模块配置
    * *filter* 配置了 *跨域* 、 *ip过滤* 、 *参数过滤*等
        * *参数过滤* 请求 *自动去除前后空格* ,使用了 *Jsoup* 过滤 *html标签* (可以自定义配置过滤级别)
3. 权限管理
    * 带有`@AdminAuthToken`注解的接口，请求头必须有 *token* 才能访问
    * 配合vue前端页面动态渲染路由，以及隐藏显示按钮:按钮保存在`$store.state.role['buttons']`中(前端)
    * 精确到接口级别权限，必须完善 *功能管理* 中菜单或者按钮对应的 *对应api* ,否则存在 *token* 后默认放行
4. 封装厂用CURD,继承 *BaseService* 即可
    * `baseInsert()`和`baseUpdate()`会自动过滤值为null的字段
    * `baseUpdate()`请做好参数过滤，或者 *new* 一个新的实体类进行赋值操作
5. 根据数据库自动生成实体类 
    * 运行 *org.mybatis.generator.plugin.MyBatisTest.main()* 方法
    * 具体配置 *resources/config/generator-config.xml* 
6. 日志按天存放，具体配置在 *resources/config/logback-spring.xml* 
7. 自动记录 *admin* 用户日志， `@SystemLog`注解放在 *Controller* 上即可

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
    * 全局变量和方法在 /src/utils目录下
    * 覆盖element-ui样式在 /src/assets/sass/element-variables.scss 文件
    * 引入iconfont字体库
        * 覆盖 /src/assets/font/iconfont 目录下文件即可
        * 引用`<i class="iconfont iconfont-address"></i>`
    * 项目中大部分都有注释

2. 基本配置
    * 配合后端实现动态路由:功能管理表单填写path路径，默认根路径为 */src/views/index/*
    * *.env.production/development* 文件和 *vue.config.js* 为配置文件
                     
3. 封装的常用组件( **具体可以参考 /src/views/index/system/sysUser/ 详细注释**)
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

## 鸣谢
   首先感谢 *springboot* 、 *vue* 、*element-ui* 等优秀的开源项目  
   其次该项目参考了很多网上的示例，如果看到相似的代码，**那么，答案只有一个了**