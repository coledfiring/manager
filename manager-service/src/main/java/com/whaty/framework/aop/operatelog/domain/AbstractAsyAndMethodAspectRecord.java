package com.whaty.framework.aop.operatelog.domain;

import com.whaty.constant.CommonConstant;
import com.whaty.framework.aop.MethodAspectParseUtils;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.aop.operatelog.param.ParamExtractStateManager;
import com.whaty.framework.aop.operatelog.util.OperateRecordConfigUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 抽象异步的方法切面记录
 * 重写operate处理方法作为hook函数
 *
 * @author weipengsen
 */
public abstract class AbstractAsyAndMethodAspectRecord extends AbstractAsynchronousRecord
        implements IMethodAspectHandle {

    /**
     * 基础功能可记录方法列表
     */
    private final static List<String> BASIC_CAN_RECORD_LIST = new ArrayList<>();

    static {
        // 加载可记录的方法名
        String canRecordBasicListStr = OperateRecordConfigUtils
                .getProperty(OperateRecordConstant.PROPERTY_KEY_BASIC_CAN_RECORD_LIST);
        if (StringUtils.isNotBlank(canRecordBasicListStr)) {
            BASIC_CAN_RECORD_LIST.addAll(Arrays.asList(canRecordBasicListStr.split(CommonConstant.SPLIT_ID_SIGN)));
        }
    }

    /**
     * 记录日志的函数式编程方法
     *
     * @param join
     * @return
     */
    protected boolean recordFunctional(JoinPoint join, Consumer<OperateRecord> operateRecordConsumer,
                                     BiConsumer<String, BasicOperateRecord> basicOperateRecordBiConsumer) {
        // 判断方法是否有OperateRecord注解
        OperateRecord operateRecord = MethodAspectParseUtils.getMethodAnnotation(join, OperateRecord.class);
        Method method;
        if (operateRecord != null) {
            operateRecordConsumer.accept(operateRecord);
        } else if ((method = MethodAspectParseUtils.findPointcutMethod(join)) != null
                && BASIC_CAN_RECORD_LIST.contains(method.getName())) {
            // 没有operateRecord注解，判断是否有BasicOperateRecord注解
            BasicOperateRecord basicOperateRecord = MethodAspectParseUtils.getClassAnnotation(join, BasicOperateRecord.class);
            if (basicOperateRecord == null) {
                return false;
            }
            basicOperateRecordBiConsumer.accept(method.getName(), basicOperateRecord);
        } else {
            return false;
        }
        return true;
    }

    /**
     * 获取参数
     *
     * @return
     */
    protected Map<String, Object> getParams(JoinPoint join) {
        return new ParamExtractStateManager(join.getArgs()).extractParam();
    }

    @Override
    public void operateBefore(JoinPoint join) {}

    @Override
    public void operateReturning(JoinPoint join, Object result, Long operateTimeMillis) {}

    @Override
    public void operateThrowing(JoinPoint join, Throwable t, Long operateTimeMillis) {}
}
