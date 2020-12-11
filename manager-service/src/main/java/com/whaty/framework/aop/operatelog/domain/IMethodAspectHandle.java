package com.whaty.framework.aop.operatelog.domain;

import org.aspectj.lang.JoinPoint;

/**
 * 方法切面处理行为
 *
 * @author weipengsen
 */
public interface IMethodAspectHandle {

    /**
     * 操作前记录
     *
     * @param join
     */
    void operateBefore(JoinPoint join);

    /**
     * 操作成功后记录
     * @param join
     * @param result
     * @param operateTimeMillis
     */
    void operateReturning(JoinPoint join, Object result, Long operateTimeMillis);

    /**
     * 操作失败后记录
     * @param join
     * @param t
     * @param operateTimeMillis
     */
    void operateThrowing(JoinPoint join, Throwable t, Long operateTimeMillis);

}
