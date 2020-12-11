package com.whaty.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段加载顺序
 *
 * @author weipengsen
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FieldOrder {

    /**
     * 加载顺序
     * @return
     */
    int value() default 0;

}
