package com.whaty.framework.aop.operatelog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 集成功能记录的类注解，只用于controller
 * 加入此注解的controller将记录集成功能的操作日志
 * @author weipengsen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BasicOperateRecord {

    /**
     * 功能名称，会被拼接集成功能名
     * @return
     */
    String value() default "";

    /**
     * 功能所在模块名称，用于统计用户习惯
     * @return
     */
    String moduleCode() default "";

    /**
     * 是否重要数据，重要数据会记录简单数据到数据库
     * @return
     */
    boolean isImportant() default false;

}
