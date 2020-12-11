package com.whaty.framework.aop.operatelog.resource;

import com.whaty.framework.aop.operatelog.domain.IMethodAspectHandle;

import java.util.Stack;

/**
 * 记录收集上下文
 * 使用stack结构实现可重入特性
 * @author weipengsen
 */
public class RecordContext {

    private final ThreadLocal<Stack<IMethodAspectHandle>> CONTEXT = ThreadLocal.withInitial(Stack::new);

    /**
     * 获取当前线程绑定上下文
     * @return
     */
    public IMethodAspectHandle get() {
        return CONTEXT.get().peek();
    }

    /**
     * 设置当前线程绑定上下文
     * @param record
     */
    public void set(IMethodAspectHandle record) {
        CONTEXT.get().push(record);
    }

    /**
     * 移除当前线程绑定上下文
     */
    public void remove() {
        if (!CONTEXT.get().isEmpty()) {
            CONTEXT.get().pop();
        }
    }

}
