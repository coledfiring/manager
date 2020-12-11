package com.whaty.analyse.framework.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计传递参数
 *
 * @author weipengsen
 */
@Data
public class AnalyseParam implements Serializable {

    private static final long serialVersionUID = 6386411822834121474L;

    @NotNull
    private String configCode;

    private Map<String, Object> search = new HashMap<>(16);

    private Map<String, Object> extParams;

    /**
     * 向搜索参中写入值
     * @param key
     * @param value
     */
    public void putSearch(String key, Object value) {
        if (this.search == null) {
            this.search = new HashMap<>(16);
        }
        this.search.put(key, value);
    }
}
