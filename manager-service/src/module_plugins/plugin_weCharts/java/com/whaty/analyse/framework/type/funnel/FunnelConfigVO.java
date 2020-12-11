package com.whaty.analyse.framework.type.funnel;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 漏斗图统计VO
 *
 * @author weipengsen
 */
@Data
public class FunnelConfigVO extends AbstractConvertConfigVO<FunnelConfigDO> {

    private static final long serialVersionUID = 4138592221041803942L;

    private String unit;

    private List<FunnelDataVO> data;

    public FunnelConfigVO(FunnelConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(FunnelConfigDO config) {
        this.setData(config.getItems().stream()
                .map(e -> new FunnelDataVO(e.getName(), config.getData().get(e.getAlias())))
                .collect(Collectors.toList()));
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.FUNNEL_ANALYSE;
    }

    /**
     * 漏斗数据展示vo
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static class FunnelDataVO implements Serializable {

        private static final long serialVersionUID = 1081691670275487652L;

        private String name;

        private Number value;
    }
}
