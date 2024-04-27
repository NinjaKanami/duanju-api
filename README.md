**项目说明** 
- sqx-fast是一个轻量级的，前后端分离的Java快速开发平台，能快速开发项目并交付
- 支持MySQL、PostgreSQL等主流数据库
<br>
 

**具有如下特点** 
- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过token进行数据交互，前端再也不用关注后端技术
- 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
- 页面交互使用Vue2.x，极大的提高了开发效率
- 引入API模板，根据token作为登录令牌，极大的方便了APP接口开发
- 引入swagger文档支持，方便编写API接口文档
<br> 


**技术选型：** 
- 核心框架：Spring Boot 2.6
- 安全框架：Apache Shiro 1.4
- 视图框架：Spring MVC 5.0
- 持久层框架：MyBatis 3.3
- 数据库连接池：Druid 1.0
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x 
<br> 


 **后端部署**
- 通过git下载源码
- idea、eclipse需安装lombok插件，不然会提示找不到entity的get set方法
- 创建数据库sqx_fast，数据库编码为UTF-8
- 执行db/mysql.sql文件，初始化数据
- 修改application-dev.yml，更新MySQL账号和密码
- Eclipse、IDEA运行sqxApplication.java，则可启动项目
- Swagger文档路径：http://localhost:8080/sqx_fast/swagger/index.html
- Swagger注解路径：http://localhost:8080/sqx_fast/swagger-ui.html


<br>
