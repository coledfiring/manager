package com.whaty.framework.aop.notice.aspect;

import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.function.Tuple;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 自动的log4j日志记录切面
 *
 * @author weipengsen
 */
@Aspect
@Component
public class Log4jRecordAspect {

    /**
     * 本地对使用过的logger对象的缓存池
     */
    private static SoftReference<Map<String, Logger>> LOGGER_MAP = new
            SoftReference<>(new HashMap<String, Logger>());

    /**
     * 成功操作日志字符串
     */
    private final static String SUCCESS_LOG_STR = "%s execute success, args: [%s], result: [%s]";
    /**
     * 失败操作日志字符串
     */
    private final static String FAILURE_LOG_STR = "%s execute error";
    /**
     * 换行符
     */
    private final static String NEW_LINE_SIGN = System.getProperty("line.separator");

    @Pointcut("execution(* com.whaty..*.*(..))")
    public void cutMethod() {
    }

    @AfterReturning(value = "cutMethod()", returning = "result")
    public void doAfterReturning(JoinPoint join, Object result) {
        Optional.of(this.getCutObjectClass(join))
                .map(e -> new Tuple<>(e, this.getLogger(e)))
                .filter(e -> e.t1.isInfoEnabled())
                .ifPresent(e -> e.t1.info(String.format(SUCCESS_LOG_STR, e.t0.getName(),
                        Arrays.toString(join.getArgs()), result)));
    }

    @AfterThrowing(value = "cutMethod()", throwing = "e")
    public void doAfterThrowing(JoinPoint join, Exception e) {
        Class clazz = this.getCutObjectClass(join);
        Logger logger = this.getLogger(clazz);
        if (e instanceof AbstractBasicException) {
            //系统内部定义的异常类
            ((AbstractBasicException) e).log(logger, String.format(FAILURE_LOG_STR, clazz.getName()));
        } else {
            //系统异常或错误，使用error打印堆栈
            if (logger.isErrorEnabled()) {
                String errorDescription = this.appendArgsToDescription(String.format(FAILURE_LOG_STR, clazz.getName()),
                        join.getArgs());
                logger.error(errorDescription, e);
            }
        }
    }

    /**
     * 将参数列表和请参拼接到日志说明上
     *
     * @param description
     * @param args
     * @return
     */
    private String appendArgsToDescription(String description, Object[] args) {
        return description + NEW_LINE_SIGN + "method args:" + Arrays.toString(args);
    }

    /**
     * 获得日志对象
     *
     * @param clazz
     * @return
     */
    private Logger getLogger(Class clazz) {
        String className = clazz.getName();
        if (LOGGER_MAP.get() == null) {
            LOGGER_MAP = new SoftReference<>(new HashMap<String, Logger>(16));
        }
        Logger logger = LOGGER_MAP.get().get(className);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            LOGGER_MAP.get().put(className, logger);
        }
        return logger;
    }

    /**
     * 获得被切的对象的class对象
     *
     * @param join
     * @return
     */
    private Class getCutObjectClass(JoinPoint join) {
        return join.getTarget().getClass();
    }

}
