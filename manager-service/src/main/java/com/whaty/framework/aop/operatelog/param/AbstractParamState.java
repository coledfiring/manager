package com.whaty.framework.aop.operatelog.param;

import java.util.Map;

/**
 * 参数状态机接口
 *
 * @author weipengsen
 */
public interface AbstractParamState<T> {

    /**
     * 提取参数
     * @param param
     * @return
     */
    Map<String, Object> extract(T param);

}
