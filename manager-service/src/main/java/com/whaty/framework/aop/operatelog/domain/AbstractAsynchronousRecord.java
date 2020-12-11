package com.whaty.framework.aop.operatelog.domain;

import com.whaty.designer.provider.AbstractProvider;
import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.aop.operatelog.util.OperateRecordConfigUtils;
import com.whaty.framework.common.spring.SpringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 抽象的异步的记录
 *
 * @author weipengsen
 */
public abstract class AbstractAsynchronousRecord implements ICanRecord {

    private final AbstractProvider provider;

    /**
     * 生产阻塞时间
     */
    private static long providerWaitTimeout = 30 * 60;

    /**
     * 生产者的线程池
     */
    private static ExecutorService providerThreadPool;

    private final static Logger logger = LoggerFactory.getLogger(AbstractAsynchronousRecord.class);

    static {
        // 创建线程池
        ThreadFactory namedThreadFactory = new ThreadFactory() {

            private final AtomicLong index = new AtomicLong(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(OperateRecordConstant.PROVIDER_THREAD_NAME + index.incrementAndGet());
                return thread;
            }
        };
        // 读取配置
        int corePoolSize = Integer.parseInt(OperateRecordConfigUtils
                .getProperty(OperateRecordConstant.PROPERTY_KEY_PROVIDER_THREAD_POOL_CORE_POOL_SIZE));
        int maximumPoolSize = Integer.parseInt(OperateRecordConfigUtils
                .getProperty(OperateRecordConstant.PROPERTY_KEY_PROVIDER_THREAD_POOL_MAX_POOL_SIZE));
        long keepAliveTime = Long.parseLong(OperateRecordConfigUtils
                .getProperty(OperateRecordConstant.PROPERTY_KEY_PROVIDER_THREAD_POOL_KEEP_ALIVE_TIME));
        int blockingQueueCapacity = Integer.parseInt(OperateRecordConfigUtils
                .getProperty(OperateRecordConstant.PROPERTY_KEY_PROVIDER_THREAD_POOL_BLOCKING_QUEUE_CAPACITY));
        providerThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(blockingQueueCapacity), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public AbstractAsynchronousRecord() {
        this.provider = (AbstractProvider) SpringUtil
                .getBean(OperateRecordConstant.BEAN_NAME_OPERATE_RECORD_PROVIDER);
    }

    @Override
    public void offerToProvider() {
        try {
            String waitTimeoutStr = OperateRecordConfigUtils
                    .getProperty(OperateRecordConstant.PROPERTY_KEY_PROVIDER_TIMEOUT);
            if (StringUtils.isNotBlank(waitTimeoutStr)) {
                providerWaitTimeout = Long.parseLong(waitTimeoutStr);
            }
            // 由于生产受仓库大小影响所以使用异步
            providerThreadPool.execute(() -> {
                try {
                    this.provider.provide(this, providerWaitTimeout, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("the thread of collect provider is interrupted", e);
                    }
                }
            });
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("collect error: %s", this), e);
            }
        }
    }
}
