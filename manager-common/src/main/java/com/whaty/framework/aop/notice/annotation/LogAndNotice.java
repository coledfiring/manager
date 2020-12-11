package com.whaty.framework.aop.notice.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录的注解。在方法前加@LogAndNotice("功能名称")
 * 与Log4jRecordAspect aop注解配合
 * @author weipengsen
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) 
@Documented 
public @interface LogAndNotice {

	/**
	 * 操作名
	 * @return
	 */
	String value() default "";

}
