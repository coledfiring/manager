package com.whaty.framework.aop.operatelog.resource;

import java.util.Stack;

/**
 * 操作时间计数器 v2.0
 *
 * 使用委托，增加基于Stack结构的可重入特性
 * @author weipengsen
 */
public class OperateTimeCounter {

    private static final ThreadLocal<Stack<Long>> TIME_STACK_CONTEXT = ThreadLocal.withInitial(Stack::new);

    /**
     * 开始计数
     */
    public void startCount() {
        TIME_STACK_CONTEXT.get().push(System.currentTimeMillis());
    }

    /**
     * 结束计数并返回时长
     * @return
     */
    public Long countOperateTime() {
        return System.currentTimeMillis() - TIME_STACK_CONTEXT.get().peek();
    }

    public void remove() {
        if (!TIME_STACK_CONTEXT.get().isEmpty()) {
            TIME_STACK_CONTEXT.get().pop();
        }
    }
}
