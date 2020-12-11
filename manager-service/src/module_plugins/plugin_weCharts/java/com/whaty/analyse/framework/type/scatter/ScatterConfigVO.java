package com.whaty.analyse.framework.type.scatter;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 散点图配置VO
 *
 * @author weipengsen
 */
@Data
public class ScatterConfigVO extends AbstractConvertConfigVO<ScatterConfigDO> {

    private static final long serialVersionUID = -6226556778994398419L;

    private List<ScatterDataVO> data;

    public ScatterConfigVO(ScatterConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(ScatterConfigDO configDO) {
        this.data = configDO.getData().stream()
                .map(e -> new ScatterDataVO((Number) e.get("x"), (Number) e.get("y"), (String) e.get("label")))
                .collect(Collectors.toList());
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.SCATTER_ANALYSE;
    }

    /**
     * 散点图值模型DO
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public class ScatterDataVO implements Serializable {

        private static final long serialVersionUID = -530331876094728089L;

        private Number x;

        private Number y;

        private String label;

    }

}
