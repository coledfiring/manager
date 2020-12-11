package com.whaty.framework.aop.operatelog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志方法注解，只在controller使用
 * @author weipengsen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperateRecord {

    /**
     * 功能名称
     * @return
     */
    String value() default "";

    /**
     * 功能所在模块名，用于用户习惯收集
     * @return
     */
    String moduleCode() default "";

    /**
     * 记录一键操作操作对象的查询sql
     * @return
     */
    String recordSql() default "";

    /**
     * 是否为重要操作，重要操作会将简单数据写入数据库用于用户查询
     * @return
     */
    boolean isImportant() default false;

}
