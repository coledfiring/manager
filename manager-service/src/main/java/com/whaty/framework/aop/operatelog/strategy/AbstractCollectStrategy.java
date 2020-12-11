package com.whaty.framework.aop.operatelog.strategy;

import com.whaty.constant.CommonConstant;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象的数据收集策略
 *
 * @author weipengsen
 */
public abstract class AbstractCollectStrategy<T extends Annotation> {

    protected final Map<String, Object> params;

    protected final T annotation;

    protected final JoinPoint join;

    protected AbstractCollectStrategy(Map<String, Object> params, T annotation, JoinPoint join) {
        this.params = params;
        this.annotation = annotation;
        this.join = join;
    }

    /**
     * 记录操作数据状态
     *
     * @return
     */
    public Map<String, Map<String, Object>> collect() {
        return this.convertListToMap(this.convertDataFormat(this.collectData()));
    }

    /**
     * 转换数据格式
     * @param origin
     * @return
     */
    protected List<Map<String, Object>> convertDataFormat(List<Map<String, Object>> origin) {
        return Optional.ofNullable(origin).map(e -> e.stream().map(this::convertDataFormat)
                .collect(Collectors.toList())).orElse(null);
    }

    private Map<String, Object> convertDataFormat(Map<String, Object> origin) {
        return origin.entrySet().stream()
                .filter(e -> Objects.nonNull(e.getValue()))
                .peek(e -> e.setValue(this.convertDataFormat(e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Object convertDataFormat(Object origin) {
        if (origin instanceof Date) {
            return CommonUtils.changeDateToString((Date) origin, CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR);
        }
        return origin;
    }

    /**
     * 收集数据
     * @return
     */
    protected abstract List<Map<String, Object>> collectData();

    /**
     * 获取当次数据命名空间
     * @return
     */
    public abstract String namespace();

    /**
     * 将list转化为map
     * @param data
     * @return
     */
    private Map<String, Map<String, Object>> convertListToMap(List<Map<String, Object>> data) {
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        if (data.stream().anyMatch(e -> !e.containsKey("id"))) {
            throw new IllegalArgumentException("collect data not found alias 'id'");
        }
        Map<String, Map<String, Object>> target = new HashMap<>(16);
        for (Map<String, Object> datum : data) {
            if (target.containsKey(datum.get("id"))) {
                throw new IllegalArgumentException("duplicate id");
            }
            target.put(getNullOrString(datum.get("id")), datum);
        }
        return target;
    }

    /**
     * 获取对象的字符串表示形式
     *
     * @param obj
     * @return
     */
    private String getNullOrString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    /**
     * 简单工厂方法
     * @param params
     * @param annotations
     * @param join
     * @return
     */
    public static AbstractCollectStrategy<? extends Annotation> newInstance(
            Map<String, Object> params, Annotation[] annotations, JoinPoint join)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Annotation annotation : annotations) {
            CollectStrategy recordStrategy = CollectStrategy.getStrategyClass(annotation);
            if (recordStrategy == null) {
                continue;
            }
            Constructor[] constructors = recordStrategy.getRecordStrategyClass().getConstructors();
            Constructor<AbstractCollectStrategy> constructor = Arrays.stream(constructors)
                    .filter(e -> e.getParameterCount() == 3)
                    .findFirst().orElse(null);
            if (constructor == null) {
                return null;
            }
            return constructor.newInstance(params, annotation, join);
        }
        return null;
    }

    private enum CollectStrategy {

        /**
         * sql记录策略
         */
        SQL_RECORD(SqlRecord.class, SqlCollectStrategy.class),
        ;

        private Class<? extends Annotation> annotationClass;

        private Class<? extends AbstractCollectStrategy> recordStrategyClass;

        CollectStrategy(Class<? extends Annotation> annotationClass,
                        Class<? extends AbstractCollectStrategy> recordStrategyClass) {
            this.annotationClass = annotationClass;
            this.recordStrategyClass = recordStrategyClass;
        }

        /**
         * 获取策略类
         * @param annotation
         * @return
         */
        static CollectStrategy getStrategyClass(Annotation annotation) {
            return Arrays.stream(values())
                    .filter(e -> e.getAnnotationClass().isAssignableFrom(annotation.getClass()))
                    .findFirst().orElse(null);
        }

        public Class<? extends Annotation> getAnnotationClass() {
            return annotationClass;
        }

        public Class<? extends AbstractCollectStrategy> getRecordStrategyClass() {
            return recordStrategyClass;
        }
    }

}
