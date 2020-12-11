package com.whaty.framework.aop.operatelog.resource;

import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.aop.operatelog.domain.AbstractAsynchronousRecord;
import com.whaty.framework.aop.operatelog.util.OperateRecordConfigUtils;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 操作日志存储仓库
 * @author weipengsen
 */
public class OperateRecordStore {

    /**
     * 阻塞队列的默认值
     */
    private static final int DEFAULT_CAPACITY = 500;

    /**
     * 操作日志记录队列
     */
    public static final LinkedBlockingQueue<AbstractAsynchronousRecord> OPERATE_RECORD_QUEUE;

    static {
        /*
         * 读取配置创建阻塞仓库
         */
        String capacityConfig = OperateRecordConfigUtils
                .getProperty(OperateRecordConstant.PROPERTY_KEY_STORE_BLOCKING_QUEUE_CAPACITY);
        int capacity = DEFAULT_CAPACITY;
        if (StringUtils.isNotBlank(capacityConfig)) {
            capacity = Integer.parseInt(capacityConfig);
        }
        OPERATE_RECORD_QUEUE = new LinkedBlockingQueue<>(capacity);
    }

}
