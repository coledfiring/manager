# 首佳节能

# 安装

### 安装数据库
    site_manage_single.sql      单站点部署使用的数据库
    site_manage_multi.sql       多站点部署使用的数据库
    
### 单站点项目部署需要修改的配置
    jdbc.properties         去掉control数据库配置
    spring-dataSource.xml   去掉control数据库配置
    spring-bean.xml         去掉数据源切面dataSourceCutAop
    web.xml                 修改MyOpenSessionInViewFilter为默认的过滤器,不需要再根据域名切换数据源了

### 配置数据源
    修改jdbc.properties中的数据源配置为本地开发的配置
    
### 配置redis
    修改redis.properties中的redis服务器地址和db

### 本地配置host
    127.0.0.1 admin.webtrn.cn control.admin.webtrn.cn
 
### 端口
    8080

### 访问

#### 启动前端项目

#### 登录普通用户
    http://admin.webtrn.cn

#### 登录控制端
    http://control.admin.webtrn.cn
    
# 模块划分

### manager-common
    通用性代码存放项目，不应该依赖任何业务逻辑，独立于业务之外
### manager-dao
    持久层操作代码存放项目，包含所有的持久成操作
### manager-domain
    领域模型代码存放项目， 包含所有项目相关领域模型，模型中包含dot，po等领域对象，包含数据与行为
### manager-service
    业务代码存放项目，应该是所有模型进行持久化操作的事务管理类和所有业务相关辅助类的存放项目
### manager-web
    web项目，存放数据交互controller层代码和网络资源

#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
