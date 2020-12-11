package com.whaty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 与grid配合的联合唯一索引校验
 *
 * @author weipengsen
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnionUnique {

    /**
     * 类成员名
     * @return
     */
    String[] fieldNames();

    /**
     * 提示信息
     * @return
     */
    String info();

}
