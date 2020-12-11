package com.whaty.framework.aop.operatelog.param;

import com.whaty.core.framework.util.RequestUtils;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数提取的状态机管理器
 *
 * @author weipengsen
 */
public class ParamExtractStateManager {

    private AbstractParamState state;

    private final Object[] params;

    public ParamExtractStateManager(Object[] params) {
        this.params = params;
    }

    /**
     * 提取参数，根据参数的类型切换到不同的状态机，状态机提取参数
     * request的参数不管什么情况下都要放置
     *
     * @return
     */
    public Map<String, Object> extractParam() {
        Map<String, Object> resultMap = Optional.ofNullable(CommonUtils.getRequest())
                .map(RequestUtils::getRequestMap).map(this::convertMap).orElseGet(HashMap::new);
        // 使用状态机转换参数
        resultMap.putAll(Arrays.stream(this.params).filter(Objects::nonNull)
                .filter(e -> !(e instanceof HttpServletRequest) && !(e instanceof HttpServletResponse))
                // 切换状态机且收集参数
                .peek(this::switchState).map(this::extractParam).filter(MapUtils::isNotEmpty)
                //将参数转换为entry
                .map(Map::entrySet).flatMap(Set::stream)
                // 归并，将所有key相同的entry组成一个list
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList()))));
        return resultMap;
    }

    /**
     * 转换map泛型
     * @param originMap
     * @return
     */
    private Map<String, Object> convertMap(Map<String, String> originMap) {
        return originMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (Object) e.getValue()));
    }

    /**
     * 根据类型的不同切换不同的状态机
     *
     * @param param
     */
    private void switchState(Object param) {
        this.setState(StateEnum.getParamState(param.getClass()));
    }

    /**
     * 提取参数
     *
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> extractParam(Object param) {
        return this.state.extract(param);
    }

    private void setState(AbstractParamState state) {
        this.state = state;
    }
}
