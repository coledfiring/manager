package com.whaty.schedule.util;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 获取aop中的被代理对象
 */
public class AopTargetUtils {

    private final static String DYNAMIC_INTERCEPTOR_FIELD_NAME = "CGLIB$CALLBACK_0";

    private final static String DYNAMIC_ADVISE_FIELD_NAME = "advised";

    private final static String DYNAMIC_AOP_PROXY_FIELD_NAME = "h";

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        return AopUtils.isJdkDynamicProxy(proxy) ? getJdkDynamicProxyTargetObject(proxy)
                : getCglibProxyTargetObject(proxy);
    }

    /**
     * 获取cglib代理对象中的原对象
     * @param proxy
     * @return
     * @throws Exception
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField(DYNAMIC_INTERCEPTOR_FIELD_NAME);
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField(DYNAMIC_ADVISE_FIELD_NAME);
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }

    /**
     * 获取jdk动态代理对象中的原对象
     * @param proxy
     * @return
     * @throws Exception
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField(DYNAMIC_AOP_PROXY_FIELD_NAME);
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField(DYNAMIC_ADVISE_FIELD_NAME);
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }
}
