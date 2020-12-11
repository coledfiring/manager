package com.whaty.notice.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 通知推送路由参数配置
 *
 * @author weipengsen
 */
public class RouterConfig implements Serializable {

    private static final long serialVersionUID = -6577443805675878881L;

    private String routerName;

    private Map<String, Object> params;

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouterConfig that = (RouterConfig) o;
        return Objects.equals(routerName, that.routerName) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routerName, params);
    }

    @Override
    public String toString() {
        return "RouterConfig{" +
                "routerName='" + routerName + '\'' +
                ", params=" + params +
                '}';
    }
}
