#分布式定时任务插件

## 说明

用于在主项目均衡负载环境中增加定时任务功能时使用，解决多机环境下的**执行冲突问题**，且具备**热更新能力**，插件依赖**新框架**，使用了新框架中的部分依赖，且自身包含新框架grid配置类。

## 依赖

Hibernate，mysql，管理端新框架，jdk1.8，redis，quartz2.x

## 配置方式

spring IOC中需要给插件的dao对象注入sessionFactory对象，对象的名称与GeneralDao的两个实现类中的sessionFactory一致。

在新框架的中央配置库中导入document文件夹下的init.sql文件，sql文件中包含表结构、grid配置，导入后在超管中配置角色权限开启调度管理的菜单。

插件代码中所有使用MasterSlaveRoutingDataSource的地方为切换到存在调度表结构的数据库的代码，需要对其进行更改。

代码中使用redisService的地方需要换成自己的项目的redis服务实现

## 目录结构

----+
    |-- document  
        |-- init.sql // 初始化sql  
        |-- README.md // readme文档  
    |-- java  
        |-- com  
            |-- whaty  
                |-- schedule  
                    |-- bean // 调度使用的domain  
                    |-- constants // 常量池  
                    |-- dao // 持久层操作类  
                    |-- designer // 机制超类，守护线程处理  
                    |-- grid // 新框架grid接口  
                    |-- job // 调度器的功能类  
                    |-- util // 工具类  
    |-- resources // 调度任务配置   

## 设计

详细的设计说明与使用方式在公司46网段访问 http://192.168.46.80/docs/developTools/schedule 可以查看