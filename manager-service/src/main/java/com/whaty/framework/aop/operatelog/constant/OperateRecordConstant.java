package com.whaty.framework.aop.operatelog.constant;

/**
 * 操作日志常量类
 * @author weipengsen
 */
public interface OperateRecordConstant {

    /**
     * operateRecordProvider依赖名
     */
    String BEAN_NAME_OPERATE_RECORD_PROVIDER = "operateRecordProvider";
    /**
     * operateRecordConsumer依赖名
     */
    String BEAN_NAME_OPERATE_RECORD_CONSUMER = "operateRecordConsumer";
    /**
     * 消费者守护线程名
     */
    String CONSUMER_THREAD_NAME = "operateRecordConsumer";
    /**
     * 参数名，传参参数
     */
    String ARG_NAME_PARAMS = "params";
    /**
     * 参数名，传参中的自定义sql查询记录结果
     */
    String ARG_NAME_CUSTOM_RECORD_PARAMS = "customRecordParams";
    /**
     * 配置，生产者阻塞时间
     */
    String PROPERTY_KEY_PROVIDER_TIMEOUT = "operateRecord.providerTimeout";
    /**
     * 配置，基础功能可记录方法名列表
     */
    String PROPERTY_KEY_BASIC_CAN_RECORD_LIST = "operateRecord.basicCanRecordList";
    /**
     * 配置，集成功能操作方法名与功能名的对应关系的key
     */
    String PROPERTY_KEY_BASIC_OPERATE_NAME = "operateRecord.basicOperateName.%s";
    /**
     * 配置，阻塞队列的初始值
     */
    String PROPERTY_KEY_STORE_BLOCKING_QUEUE_CAPACITY = "operateRecord.storeBlockingQueueCapacity";
    /**
     * 配置，生产者的线程调度池核心线程数
     */
    String PROPERTY_KEY_PROVIDER_THREAD_POOL_CORE_POOL_SIZE = "operateRecord.providerThreadPool.corePoolSize";
    /**
     * 配置，生产者的线程调度池最大线程数
     */
    String PROPERTY_KEY_PROVIDER_THREAD_POOL_MAX_POOL_SIZE = "operateRecord.providerThreadPool.maximumPoolSize";
    /**
     * 配置，生产者的线程调度池空闲时间
     */
    String PROPERTY_KEY_PROVIDER_THREAD_POOL_KEEP_ALIVE_TIME = "operateRecord.providerThreadPool.keepAliveTime";
    /**
     * 配置，生产者的线程调度池阻塞队列的容量
     */
    String PROPERTY_KEY_PROVIDER_THREAD_POOL_BLOCKING_QUEUE_CAPACITY = "operateRecord.providerThreadPool.blockingQueueCapacity";
    /**
     * 生产者线程名
     */
    String PROVIDER_THREAD_NAME = "providerThread";
    /**
     * 参数列表中的key，文件存储路径
     */
    String PARAM_KEY_UPLOAD_FILE_PATH = "recordFilePath";
    /**
     * 操作日志参数中的文件存放位置
     */
    String PARAM_FILE_STORE_PATH = "/incoming/operateLogFile/paramFile/%s/%s/%s.tmp";
    /**
     * 模块名命名空间
     */
    String ENUM_CONST_NAME_SPACE_MODULE_CODE = "FlagModuleName";
    /**
     * 存储日志记录文件的目录
     */
    String CSV_FILE_PATH = "/incoming/operateRecordFile/recordFile/";
    /**
     * 日志记录文件名
     */
    String CSV_FILE_NAME = "operateRecordFile_%s_%s_%s.csv";
    /**
     * url请求参数分隔符
     */
    String URL_PARAM_SPLIT_SIGN = "?";
}
