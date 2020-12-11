package com.whaty.framework.im;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * im的响应数据
 *
 * @author weipengsen
 */
@Data
@AllArgsConstructor
public class ImResponseResult implements Serializable {

    private static final long serialVersionUID = -3375160456048375857L;

    private String ActionStatus;

    private String ErrorInfo;

    private Integer ErrorCode;

    private String GroupId;

    private Map<String, Object> data;

    private static final String ACTION_STATUS_SUCCESS = "OK";

    private static final String ACTION_STATUS_FAILURE = "OK";

    /**
     * 将json字符串转换成对象
     * @param jsonString
     * @return
     */
    public static ImResponseResult convert(String jsonString) {
        JSONObject json = JSON.parseObject(jsonString);
        String[] fields = Arrays.stream(ImResponseResult.class.getDeclaredFields())
                .map(Field::getName).sorted().toArray(String[]::new);
        Map<String, Object> data = json.entrySet().stream().filter(e -> Arrays.binarySearch(fields, e.getKey()) < 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ImResponseResult(json.getString("ActionStatus"), json.getString("ErrorInfo"),
                json.getInteger("ErrorCode"), json.getString("GroupId"), data);
    }

    /**
     * 返回结果是否正确
     * @return
     */
    public boolean isSuccess() {
        return ACTION_STATUS_SUCCESS.equals(this.getActionStatus()) && this.getErrorCode() == 0;
    }

}
