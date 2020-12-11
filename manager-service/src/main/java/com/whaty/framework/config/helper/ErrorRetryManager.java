package com.whaty.framework.config.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 错误重试器
 *
 * @author weipengsen
 */
public interface ErrorRetryManager {

    int DEFAULT_RETRY_NUM = 5;

    int WAIT_TIME = 3;

    Logger logger = LoggerFactory.getLogger(ErrorRetryManager.class);

    /**
     * 处理任务
     */
    default void workWithRetry() {
        try {
            this.doWork();
        } catch (Exception e) {
            try {
                TimeUnit.SECONDS.sleep(WAIT_TIME);
            } catch (InterruptedException e1) {
                logger.warn("do work error wait retry is interrupted", e1);
            }
            if (this.retry()) {
                logger.warn("do work error but retry success", e);
                return;
            }
            throw e;
        }
    }

    /**
     * 重试
     * @return
     */
    default boolean retry() {
        int retryNum = 1;
        while (retryNum <= DEFAULT_RETRY_NUM) {
            logger.warn("retry do work for " + retryNum);
            try {
                this.doWork();
                logger.warn("retry success for " + retryNum);
                return true;
            } catch (Exception e) {
                logger.error("retry do work for " + retryNum + " error", e);
                retryNum ++;
                try {
                    TimeUnit.SECONDS.sleep(WAIT_TIME);
                } catch (InterruptedException e1) {
                    logger.warn("retry do work error wait retry is interrupted", e1);
                }
            }
        }
        return false;
    }

    /**
     * 原始任务处理
     */
    void doWork();

}
