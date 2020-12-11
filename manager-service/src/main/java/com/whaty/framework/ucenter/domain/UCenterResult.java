package com.whaty.framework.ucenter.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 用户中心返回值的dto对象
 *
 * @author weipengsen
 */
public class UCenterResult implements Serializable {

    private static final long serialVersionUID = 5018833384184070155L;

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data;

    /**
     * 转换dto对象
     * @param jsonObject
     * @return
     */
    public static UCenterResult convert(JSONObject jsonObject) {
        return JSON.toJavaObject(jsonObject, UCenterResult.class);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UCenterResult that = (UCenterResult) o;
        return Objects.equals(success, that.success) &&
                Objects.equals(code, that.code) &&
                Objects.equals(message, that.message) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, code, message, data);
    }

    @Override
    public String toString() {
        return "UCenterResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
