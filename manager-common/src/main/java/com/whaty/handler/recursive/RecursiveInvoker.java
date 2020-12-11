package com.whaty.handler.recursive;

/**
 * 尾递归执行器
 *
 * @author weipengsen
 */
public class RecursiveInvoker {


    public static <T> TailRecursion<T> call(final TailRecursion<T> next) {
        return next;
    }

    /**
     * 结束当前递归
     * @param value
     * @param <T>
     * @return
     */
    public static <T> TailRecursion<T> done(final T value) {
        return new TailRecursion<T>() {

            @Override
            public TailRecursion<T> apply() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public T getResult() {
                return value;
            }
        };
    }

}
