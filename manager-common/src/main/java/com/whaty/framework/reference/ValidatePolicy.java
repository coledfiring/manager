package com.whaty.framework.reference;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * 校验策略
 *
 * @author weipengsen
 */
public enum ValidatePolicy {

    /**
     * 排除
     * 此策略指定 指定站点不检查
     */
    EXCLUDE_POLICY(1, (siteCodes, siteCode) -> {
        Arrays.sort(siteCodes);
        return Arrays.binarySearch(siteCodes, ValidateSite.ALL_SITE) < 0
                && Arrays.binarySearch(siteCodes, siteCode) < 0;
    }),

    /**
     * 包含
     * 此策略指定 只有指定站点可以检查
     */
    ONLY_POLICY(2, (siteCodes, siteCode) -> {
        Arrays.sort(siteCodes);
        return Arrays.binarySearch(siteCodes, ValidateSite.ALL_SITE) >= 0
                || Arrays.binarySearch(siteCodes, siteCode) >= 0;
    }),
    ;

    private int policy;

    private BiFunction<String[], String, Boolean> checkFunction;

    ValidatePolicy(int policy, BiFunction<String[], String, Boolean> checkFunction) {
        this.policy = policy;
        this.checkFunction = checkFunction;
    }

    /**
     * 检查站点集合是否可以校验
     *
     * @param siteCodes
     * @return
     */
    public boolean checkCanValidate(String[] siteCodes, String siteCode) {
        return this.checkFunction.apply(siteCodes, siteCode);
    }

    public int getPolicy() {
        return policy;
    }

    public BiFunction<String[], String, Boolean> getCheckFunction() {
        return checkFunction;
    }

}
