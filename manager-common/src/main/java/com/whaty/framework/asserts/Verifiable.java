package com.whaty.framework.asserts;

/**
 * 可被校验接口，用于模型接校验时复杂校验的调用
 *
 * @author weipengsen
 */
public interface Verifiable {

    /**
     * 校验
     * @return
     */
    boolean verify();

}
