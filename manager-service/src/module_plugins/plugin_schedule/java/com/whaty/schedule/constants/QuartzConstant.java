package com.whaty.schedule.constants;

/**
 * 常量池
 *
 * @author weipengsen
 */
public interface QuartzConstant {
    /**
     * 任务状态
     */
    String ENUM_CONST_NAMESPACE_FLAG_JOB_STATUS = "flagJobStatus";
    /**
     * 数据key，任务
     */
    String DATA_JOB = "job";
    /**
     * 数据key，调度器
     */
    String DATA_TRIGGER = "trigger";

    /**
     * 加版本号的格式
     */
    String VERSION_STRING_FORMAT = "%s.v%s";
    /**
     * 线程名称
     */
    String INVOKE_THREAD_NAME = "schedule-invoke";
    /**
     * 任务状态，进行中
     */
    String JOB_STATUS_DOING = "1";
    /**
     * 任务状态，成功
     */
    String JOB_STATUS_SUCCESS = "2";
    /**
     * 任务状态，失败
     */
    String JOB_STATUS_FAILURE = "3";
    /**
     * 本机标识的格式
     */
    String LOCAL_KEY_FORMAT = "%s:%s";
    /**
     * 检查是否有新任务的后台线程名
     */
    String CHECK_NEW_JOB_THREAD_NAME = "schedule_checkNewJob";

    String GENERAL_DAO_BEAN_NAME = "scheduleGeneralDao";

    String OPEN_GENERAL_DAO_BEAN_NAME = "scheduleOpenGeneralDao";
}
