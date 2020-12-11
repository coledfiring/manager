package com.whaty.framework.asserts;

import com.whaty.util.ValidateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 成教参数校验断言类
 *
 * @author weipengsen
 */
public class TycjAssert {

    /**
     * 参数全都不为null和空串
     * @param target
     */
    public static void isAllNotBlank(String... target) {
        predicateOrigin(e -> StringUtils.isBlank(e) || "undefined".equals(e), target);
    }

    /**
     * 参数全都不为null和空
     * @param target
     */
    public static void isAllNotEmpty(Collection... target) {
        predicateOrigin(CollectionUtils::isEmpty, target);
    }

    /**
     * 参数全都不为null和空
     * @param target
     */
    public static void isAllNotEmpty(Object[]... target) {
        predicateOrigin(ArrayUtils::isEmpty, target);
    }

    /**
     * 参数全都不为null和空
     * @param target
     */
    public static void isAllNotEmpty(Map... target) {
        predicateOrigin(MapUtils::isEmpty, target);
    }

    /**
     * 参数全部不得为空
     * @param target
     */
    public static void isAllNotNull(Object... target) {
        predicateOrigin(Objects::isNull, target);
    }

    /**
     * 文件全部存在
     * @param upload
     */
    public static void fileAllExists(File... upload) {
        predicateOrigin(e -> e == null || !e.exists(), upload);
    }

    /**
     * 函数式断言
     * @param predicate
     * @param origin
     * @param <T>
     */
    private static <T> void predicateOrigin(Predicate<T> predicate, T... origin) {
        if (Arrays.stream(origin).anyMatch(predicate)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 校验必须通过
     * @param param
     */
    public static void validatePass(Object param) {
        predicateOrigin(e -> !ValidateUtils.checkPassConstraintValidate(e), param);
    }
}
