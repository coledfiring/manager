package com.whaty.framework.aop.operatelog;

import com.whaty.framework.aop.operatelog.domain.DataHistoryRecord;
import com.whaty.framework.aop.operatelog.domain.IMethodAspectHandle;
import com.whaty.framework.aop.operatelog.domain.OperateRecord;
import com.whaty.framework.aop.operatelog.resource.OperateTimeCounter;
import com.whaty.framework.aop.operatelog.resource.RecordContext;
import com.whaty.framework.exception.UncheckException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务操作日志记录aop
 * aop切面切使用了OperateLog注解的方法，struts2.5以下尽量不要使用controller层的aop切面，
 * 会引起部分struts标签不能使用的情况
 *
 * @author weipengsen
 */
@Aspect
@Component
public class OperateRecordAspect {

    private final OperateTimeCounter counter = new OperateTimeCounter();

    private static final Logger logger = LoggerFactory.getLogger(OperateRecordAspect.class);

    private final static Map<Class<? extends IMethodAspectHandle>, RecordContext>
            RECORD_CONTEXT_MAP = new HashMap<>();

    static {
        RECORD_CONTEXT_MAP.put(OperateRecord.class, new RecordContext());
        RECORD_CONTEXT_MAP.put(DataHistoryRecord.class, new RecordContext());
    }

    @Pointcut("execution(* com.whaty..*.controller..*.*(..))")
    public void cutMethod() {
    }

    @Before(value = "cutMethod()")
    public void before(JoinPoint join) {
        try {
            this.counter.startCount();
            RECORD_CONTEXT_MAP.keySet().stream().map(this::generateRecordDomain)
                    .peek(e -> RECORD_CONTEXT_MAP.get(e.getClass()).set(e))
                    .forEach(e -> e.operateBefore(join));
        } catch (Exception e) {
            logger.error("collect before error", e);
        }
    }

    /**
     * 正常返回时记录操作的日志
     *
     * @param join 切面对象
     */
    @AfterReturning(value = "cutMethod()", returning = "result")
    public void doAfterReturning(JoinPoint join, Object result) {
        try {
            RECORD_CONTEXT_MAP.values().stream().map(RecordContext::get)
                    .forEach(e -> e.operateReturning(join, result, this.counter.countOperateTime()));
        } catch (Exception e) {
            logger.error("collect returning error", e);
        } finally {
            this.counter.remove();
            RECORD_CONTEXT_MAP.values().forEach(RecordContext::remove);
        }
    }

    /**
     * 异常时记录
     *
     * @param join
     * @param t
     */
    @AfterThrowing(value = "cutMethod()", throwing = "t")
    public void doAfterThrowing(JoinPoint join, Exception t) {
        try {
            RECORD_CONTEXT_MAP.values().stream().map(RecordContext::get)
                    .forEach(e -> e.operateThrowing(join, t, this.counter.countOperateTime()));
        } catch (Exception e) {
            logger.error("collect throwing error", e);
        } finally {
            this.counter.remove();
            RECORD_CONTEXT_MAP.values().forEach(RecordContext::remove);
        }
    }

    /**
     * 生成记录领域对象
     * @param originClass
     * @param <T>
     * @return
     */
    private <T> T generateRecordDomain(Class<T> originClass) {
        try {
            return originClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

}
