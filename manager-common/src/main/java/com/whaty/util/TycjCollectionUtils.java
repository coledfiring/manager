package com.whaty.util;

import com.whaty.framework.asserts.TycjAssert;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 产品容器工具类
 *
 * @author weipengsen
 */
public class TycjCollectionUtils {

    private static final int MAP_GENERATE_KEY_VALUE_SPLIT_NUM = 2;

    /**
     * map生成方法
     * @param keyAndValue
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Map<T, R> map(Object... keyAndValue) {
        TycjAssert.isAllNotEmpty(keyAndValue);
        if (keyAndValue.length % MAP_GENERATE_KEY_VALUE_SPLIT_NUM != 0) {
            throw new IllegalArgumentException();
        }
        Map<T, R> target = new HashMap<>(16);
        for (int i = 0; i < keyAndValue.length; i = i + MAP_GENERATE_KEY_VALUE_SPLIT_NUM) {
            target.put((T) keyAndValue[i], (R) keyAndValue[i + 1]);
        }
        return target;
    }

    /**
     * 求集B在集A中的绝对补集
     * @param cA
     * @param cB
     * @param <T>
     * @return
     */
    public static <T> List<T> complement(Collection<T> cA, Collection<T> cB) {
        return cA.stream().filter(e -> !cB.contains(e)).collect(Collectors.toList());
    }

    /**
     * 求集A与集B的并集
     * 泛型T必须是基础类型或Comparable的实现
     * @param cA
     * @param cB
     * @param <T>
     * @return
     */
    public static <T> Set<T> union(Collection<T> cA, Collection<T> cB) {
        Set<T> target = new HashSet<>(cA);
        target.addAll(cB);
        return target;
    }

    /**
     * 求集A与集B的交集
     * @param cA
     * @param cB
     * @param <T>
     * @return
     */
    public static <T> List<T> intersect(Collection<T> cA, Collection<T> cB) {
        // 将最大的集作为基准集，最小的集作为查找集修改为hash结构
        Collection<T> maxC = cA.size() > cB.size() ? cA : cB;
        Collection<T> minC = cA.size() > cB.size() ? cB : cA;
        Set<T> searchHash = new HashSet<>(minC);
        return maxC.stream().filter(searchHash::contains).collect(Collectors.toList());
    }
}
