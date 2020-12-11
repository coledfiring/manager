package com.whaty.schedule.util;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.schedule.dao.QuartzDao;
import com.whaty.utils.StaticBeanUtils;
import org.quartz.Scheduler;

import javax.management.MalformedObjectNameException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import static com.whaty.schedule.constants.QuartzConstant.LOCAL_KEY_FORMAT;

/**
 * quartz调度工具类
 *
 * @author weipengsen
 */
public class QuartzUtils {

    private static QuartzDao quartzDao;

    private static RedisCacheService redisCacheService;

    private static Scheduler scheduler;

    /**
     * 使用springId获取目标对象
     *
     * @param springId
     * @return
     */
    public static Object getSpringTarget(String springId) throws Exception {
        return AopTargetUtils.getTarget(SpringUtil.getBean(springId));
    }

    /**
     * 使用反射获取目标对象
     *
     * @param beanClass
     * @return
     */
    public static Object getTargetByReflect(String beanClass)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return Class.forName(beanClass).newInstance();
    }

    /**
     * 复制成员属性
     * <p>
     * 此方法只复制非final的和非static的成员属性，且为浅层复制
     *
     * @param origin
     * @param target
     * @param <T>
     * @throws IllegalArgumentException
     */
    public static <T> void copyMember(T origin, T target) throws IllegalAccessException {
        if (!origin.getClass().equals(target.getClass())
                && !origin.getClass().isAssignableFrom(target.getClass())) {
            throw new IllegalArgumentException(String.format("class for %s is not assignable from %s",
                    target.getClass().getName(), origin.getClass().getName()));
        }
        Class clazz = origin.getClass();
        while (!Object.class.equals(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isFinal(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(origin);
                if (value != null) {
                    field.set(target, value);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 获取符合要求的方法
     * 规则：
     * 1、与参数中的方法名相同
     * 2、无参数列表
     * 3、参数列表为1，且为Map的子类
     *
     * @return
     */
    public static Method getValidMethod(Class clazz, String methodName) {
        Method targetMethod = null;
        for (Method method : clazz.getMethods()) {
            boolean methodParamInvalid = method.getParameterCount() == 0
                    || method.getParameterCount() == 1;
            if (method.getName().equals(methodName) && methodParamInvalid) {
                targetMethod = method;
                break;
            }
        }
        return targetMethod;
    }

    /**
     * 获取本机的标识
     *
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     * @throws MalformedObjectNameException
     */
    public static String getLocalKey() throws SocketException, UnknownHostException, MalformedObjectNameException {
        return String.format(LOCAL_KEY_FORMAT, CommonUtils.getServerIp(), CommonUtils.getServerPort());
    }

    /**
     * 获取指定触发器最后一次执行的时间
     * @param triggerGroup
     * @param triggerName
     * @return
     */
    public static Date getLastInvokeTime(String triggerGroup, String triggerName) {
        String sql = "select max(plan_time) from pe_schedule_job_record where trigger_group = ? AND trigger_name = ?";
        return StaticBeanUtils.getOpenGeneralDao().getOneBySQL(sql, triggerGroup, triggerName);
    }

    public static GeneralDao getOpenGeneralDao() {
        return getQuartzDao().getOpenGeneralDao();
    }

    public static Scheduler getScheduler() {
        if (scheduler == null) {
            scheduler = (Scheduler) SpringUtil.getBean("scheduler");
        }
        return scheduler;
    }

    public static QuartzDao getQuartzDao() {
        if (quartzDao == null) {
            quartzDao = (QuartzDao) SpringUtil.getBean("quartzDao");
        }
        return quartzDao;
    }

    public static GeneralDao getGeneralDao() {
        return getQuartzDao().getGeneralDao();
    }

    public static RedisCacheService getRedisService() {
        if (redisCacheService == null) {
            redisCacheService = (RedisCacheService) SpringUtil.getBean(CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME);
        }
        return redisCacheService;
    }
}
