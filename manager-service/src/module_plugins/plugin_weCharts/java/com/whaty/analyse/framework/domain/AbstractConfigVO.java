package com.whaty.analyse.framework.domain;

import com.whaty.analyse.framework.AnalyseType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 配置vo，统计展示数据与配置的元数据模型
 *
 * @author weipengsen
 */
@Data
public abstract class AbstractConfigVO implements Serializable {

    private static final long serialVersionUID = 7218520383582473467L;

    @NotNull
    protected String type;

    protected String title;

    protected Map<String, Object> extEChartConfig;

    public AbstractConfigVO() {
        this.type = this.getAnalyseType().getCode();
    }

    /**
     * 模板方法，获取对应枚举
     * @return
     */
    protected abstract AnalyseType getAnalyseType();
}
