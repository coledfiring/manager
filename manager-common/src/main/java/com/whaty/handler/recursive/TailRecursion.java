package com.whaty.handler.recursive;

import java.util.stream.Stream;

/**
 * 尾递归函数
 *
 * @param <T>
 * @author weipengsen
 */
@FunctionalInterface
public interface TailRecursion<T> {

    /**
     * 递归动作
     * @return
     */
    TailRecursion<T> apply();

    /**
     * 是否结束
     * @return
     */
    default boolean isFinished() {
        return false;
    }

    /**
     * 获取结果
     * @return
     */
    default T getResult() {
        throw new UnsupportedOperationException();
    }

    /**
     * 执行递归
     * @return
     */
    default T invoke() {
        return Stream.iterate(this, TailRecursion::apply)
                .filter(TailRecursion::isFinished)
                .findFirst()
                .get().getResult();
    }

}
