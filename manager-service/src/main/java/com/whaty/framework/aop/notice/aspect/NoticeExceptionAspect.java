package com.whaty.framework.aop.notice.aspect;

import com.whaty.framework.aop.MethodAspectParseUtils;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.constant.CommonConstant;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.util.NoticeServerPollUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * LogAnnotation注解配合的异常时通知推送切面
 *
 * @author weipengsen
 */
@Aspect
@Component
public class NoticeExceptionAspect {

    /**
     * 失败操作日志字符串
     */
    private final static String FAILURE_LOG_STR = "%s操作失败";

    private final static String INCLUDE_PACKAGE = "com.whaty.products.service";

    @Pointcut("@annotation(com.whaty.framework.aop.notice.annotation.LogAndNotice)")
    public void cutMethod() {
    }

    @AfterThrowing(value = "cutMethod()", throwing = "e")
    public void doAfterThrowing(JoinPoint join, Exception e) {
        try {
            if (this.getCutObjectClass(join).getPackage().getName().contains(INCLUDE_PACKAGE)) {
                String info = e instanceof AbstractBasicException ? ((AbstractBasicException) e).getInfo()
                        : CommonConstant.ERROR_STR;
                NoticeServerPollUtils.selfNotice(String.format(FAILURE_LOG_STR, getLogAnnotation(join).value())
                        + "," + info, NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 从方法中拿到方法LogAnnotation注解
     *
     * @param join
     * @return
     */
    private LogAndNotice getLogAnnotation(JoinPoint join) {
        return MethodAspectParseUtils.findPointcutMethod(join).getAnnotation(LogAndNotice.class);
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
