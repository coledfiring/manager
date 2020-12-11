package com.whaty.framework.reference.annotation;

import com.whaty.framework.reference.ValidatePolicy;
import com.whaty.framework.reference.ValidateSite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类上注解，用于在使用hibernate工具获取实体类时进行站点的排除和包含操作
 * 比如bxqk的部分实体类之后百校千课有就增加类上注解让删除检查只检查百校千课
 *
 * @author weipengsen
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ValidateReference {

    /**
     * 指定的站点编号
     *
     * 不填写默认全部站点
     *
     * @return
     */
    String[] siteCodes() default ValidateSite.ALL_SITE;

    /**
     * 检查的策略
     * @return
     */
    ValidatePolicy validatePolicy() default ValidatePolicy.EXCLUDE_POLICY;

}
