package com.whaty.function;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 函数式库
 *
 * @author weipengsen
 */
public class Functions {

    public static BinaryOperator<String> stringAppend = (e1, e2) -> e1 + e2;

    /**
     * 返回传入的参数
     * @param <T>
     * @return
     */
    public static <T> Function<T, T> self() {
        return e -> e;
    }

    /**
     * map收集器
     * @param keyFunction
     * @param <T>
     * @param <A>
     * @return
     */
    public static <T, A> Collector<T, ?, Map<A, T>> map(Function<T, A> keyFunction) {
        return Collectors.toMap(keyFunction, self());
    }

    /**
     * double数值相加
     * @param doubleFunction
     * @return
     */
    public static BinaryOperator<Double> doubleSum(Function<Double, Double> doubleFunction) {
        return (e1, e2) -> new BigDecimal(doubleFunction.apply(e1))
                .add(new BigDecimal(doubleFunction.apply(e2))).doubleValue();
    }

}
