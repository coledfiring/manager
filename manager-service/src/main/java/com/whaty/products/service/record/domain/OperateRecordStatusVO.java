package com.whaty.products.service.record.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whaty.framework.aop.operatelog.constant.DataDiffType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 操作日志状态vo
 *
 * @author weipengsen
 */
@Data
@AllArgsConstructor
public class OperateRecordStatusVO implements Serializable {

    private static final long serialVersionUID = -5474559419995676114L;

    private String operateName;

    private String operateTime;

    private String operateUser;

    private DataDiffType diffType;

    private JSONArray diffs;

    /**
     * 将map转换为domain
     * @param originMap
     * @return
     */
    public static OperateRecordStatusVO convert(Map<String, Object> originMap) {
        JSONObject statusData = JSON.parseObject((String) originMap.get("statusData"));
        DataDiffType type = DataDiffType.getTypeByType(statusData.getString("type"));
        return new OperateRecordStatusVO((String) originMap.get("operateName"),
                (String) originMap.get("operateTime"), (String) originMap.get("operateUser"), type,
                statusData.getJSONArray("data"));
    }

}
