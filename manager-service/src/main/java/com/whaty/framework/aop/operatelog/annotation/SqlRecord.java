package com.whaty.framework.aop.operatelog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * sql记录注解
 * sql已传入参数作为可变参数，使用[paramKey|column]的格式替换
 * sql查询的结果会解析成List<Map>，其中map的key为为用户展示的字段名，且map中必须存在key`id`
 *
 * @author weipengsen
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SqlRecord {

    /**
     * 命名空间
     * @return
     */
    String namespace();

    /**
     * 执行的sql
     * @return
     */
    String sql() default "";

}
