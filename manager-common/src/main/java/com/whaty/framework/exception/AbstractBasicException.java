package com.whaty.framework.exception;

import com.whaty.core.framework.api.advice.CustomException;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常基础类
 * 平台中的所有异常类继承此异常，并定义自己的level级别和需要的构造器，如：
 * level = Level.DEBUG
 * 继承message构造器并初始化时传递message为xxx
 * 则日志记录时此异常抛出后统一异常日志管理会进行logger.debug(operateName + ",xxx")的调用
 *
 * 继承throwable构造器并初始化时传递throwable
 * 则日志记录时此异常抛出后统一异常日志管理会进行logger.debug(operateName, throwable)的调用
 *
 * @author weipengsen
 */
public abstract class AbstractBasicException extends RuntimeException implements CustomException {

    private static final long serialVersionUID = 2121518612486147564L;

    /**
     * 无参构造器，日志将不记录任何除了操作说明外的信息
     */
    public AbstractBasicException() {
        super();
    }

    /**
     * message单参构造器需要在日志记录时增加额外的信息时继承
     * @param msg
     */
    public AbstractBasicException(String msg) {
        super(msg);
    }

    /**
     * throwable构造器需要在日志记录时打印异常堆栈信息时继承
     * @param t
     */
    public AbstractBasicException(Throwable t) {
        super(t);
    }

    /**
     * 双参构造器需要在日志记录时增加额外信息并打印异常堆栈时使用
     * @param msg
     * @param t
     */
    public AbstractBasicException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * 获取对应的日志级别
     * @return
     */
    protected abstract Level getLevel();

    /**
     * 获取提示信息
     * @return
     */
    public abstract String getInfo();

    /**
     * 获取日志信息
     * @return
     */
    public String getLogInfo() {
        return this.getInfo();
    }

    /**
     * 异常对应的日志记录方法
     * @param logger 记录日志的操作类
     * @param operateName 操作名称描述
     */
    public void log(Logger logger, String operateName) {
        String logInfo = operateName + (this.getLogInfo() == null ? "" : "," + this.getLogInfo());
        ThConsumer<Throwable, Logger, String> consumer = LogLevel.getConsumerByLevel(this.getLevel().toInt());
        if (consumer == null) {
            return;
        }
        consumer.accept(this.getCause(), logger, logInfo);
    }

    private enum LogLevel {

        /**
         * debug日志级别
         */
        DEBUG(Level.DEBUG.toInt(), (t, l, i) -> {
            if (l.isDebugEnabled()) {
                if (t == null) {
                    l.debug(i);
                } else {
                    l.debug(i, t);
                }
            }
        }),

        /**
         * info日志级别
         */
        INFO(Level.INFO.toInt(), (t, l, i) -> {
            if (l.isInfoEnabled()) {
                if (t == null) {
                    l.info(i);
                } else {
                    l.info(i, t);
                }
            }
        }),

        /**
         * warn日志级别
         */
        WARN(Level.WARN.toInt(), (t, l, i) -> {
            if (l.isWarnEnabled()) {
                if (t == null) {
                    l.warn(i);
                } else {
                    l.warn(i, t);
                }
            }
        }),

        /**
         * error日志级别
         */
        ERROR(Level.ERROR.toInt(), (t, l, i) -> {
            if (l.isErrorEnabled()) {
                if (t == null) {
                    l.error(i);
                } else {
                    l.error(i, t);
                }
            }
        }),

        ;

        private int logLevel;

        private ThConsumer<Throwable, Logger, String> loggerConsumer;

        private final static Map<Integer, ThConsumer<Throwable, Logger, String>> LOG_CACHE = new HashMap<>(8);

        LogLevel(int logLevel, ThConsumer<Throwable, Logger, String> loggerConsumer) {
            this.logLevel = logLevel;
            this.loggerConsumer = loggerConsumer;
        }

        public int getLogLevel() {
            return logLevel;
        }

        public ThConsumer<Throwable, Logger, String> getLoggerConsumer() {
            return loggerConsumer;
        }

        public static ThConsumer<Throwable, Logger, String> getConsumerByLevel(int logLevel) {
            if (!LOG_CACHE.containsKey(logLevel)) {
                LogLevel target = Arrays.stream(values()).filter(e -> e.getLogLevel() == logLevel)
                        .findFirst().orElse(null);
                if (target == null) {
                    return null;
                }
                LOG_CACHE.put(logLevel, target.getLoggerConsumer());
            }
            return LOG_CACHE.get(logLevel);
        }
    }

    /**
     * 两个参的消费接口
     * @param <T>
     * @param <R>
     * @param <S>
     */
    @FunctionalInterface
    private interface ThConsumer<T, R, S> {

        /**
         * 消费
         * @param t
         * @param r
         * @param s
         */
        void accept(T t, R r, S s);

    }

}
