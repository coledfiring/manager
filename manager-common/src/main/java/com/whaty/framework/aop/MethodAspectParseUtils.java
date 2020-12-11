package com.whaty.framework.aop;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 切面方法分析工具类
 * @author weipengsen
 */
public class MethodAspectParseUtils {

    /**
     * 寻找被切的方法
     *
     * @param join
     * @return
     */
    public static Method findPointcutMethod(JoinPoint join) {
        Method target = null;
        //aop截取的类的方法数组
        Method[] methods = join.getTarget().getClass().getMethods();
        //截取的方法名
        String methodName = join.getSignature().getName();
        //截取的方法的参数
        Object[] args = join.getArgs();
        for (Method method : methods) {
            //方法名与参数列表与aop截取的方法相同
            if (method.getName().equals(methodName)
                    && args.length == method.getParameterTypes().length) {
                target = method;
                break;
            }
        }
        return target;
    }

    /**
     * 获取方法上注解
     * @param join
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getMethodAnnotation(JoinPoint join, Class<T> annotationClass) {
        Method method = findPointcutMethod(join);
        if (method == null) {
            return null;
        }
        return method.getAnnotation(annotationClass);
    }

    /**
     * 获取类上注解
     * @param join
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getClassAnnotation(JoinPoint join, Class<T> annotationClass) {
        return join.getTarget().getClass().getAnnotation(annotationClass);
    }

    /**
     * 获取方法上注解
     * @param join
     * @return
     */
    public static Annotation[] getMethodAnnotation(JoinPoint join) {
        Method method = findPointcutMethod(join);
        if (method == null) {
            return null;
        }
        return method.getAnnotations();
    }

    /**
     * 获取类上注解
     * @param join
     * @return
     */
    public static Annotation[] getClassAnnotation(JoinPoint join) {
        return join.getTarget().getClass().getAnnotations();
    }

}
