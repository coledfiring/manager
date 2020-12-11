package com.whaty.analyse.framework.type.radar;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 雷达图配置vo
 *
 * @author weipengsen
 */
@Data
public class RadarConfigVO extends AbstractConvertConfigVO<RadarConfigDO> {

    private static final long serialVersionUID = -6958192676671551666L;

    private List<RadarIndicatorConfigVO> indicators;

    private List<RadarSeriesConfigVO> series;

    public RadarConfigVO(RadarConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.RADAR_ANALYSE;
    }

    @Override
    protected void convert(RadarConfigDO configDO) {
        this.setTitle(configDO.getTitle());
        this.setSeries(configDO.getSeries().stream()
                .map(e -> RadarSeriesConfigVO.convert(e, configDO.getIndicators())).collect(Collectors.toList()));
        this.setIndicators(configDO.getIndicators().stream()
                .map(e -> new RadarIndicatorConfigVO(e.getName(), e.getScope()))
                .collect(Collectors.toList()));
    }

    /**
     * 雷达图统计项配置vo
     *
     * @author weipengsen
     */
    @Data
    @NoArgsConstructor
    public static class RadarIndicatorConfigVO implements Serializable {

        private static final long serialVersionUID = -1359799272750233527L;

        private String name;

        private Number max;

        public RadarIndicatorConfigVO(String name, Number max) {
            this.name = name;
            this.max = max;
        }
    }

    /**
     * 雷达图统计维度配置vo
     *
     * @author weipengsen
     */
    @Data
    public static class RadarSeriesConfigVO implements Serializable {

        private static final long serialVersionUID = -1117959477451847392L;

        private String name;

        private List<Number> value;

        public static RadarSeriesConfigVO convert(RadarConfigDO.RadarSeriesConfigDO configDO,
                                                  List<RadarConfigDO.RadarIndicatorConfigDO> indicators) {
            RadarSeriesConfigVO configVO = new RadarSeriesConfigVO();
            configVO.setName(configDO.getName());
            configVO.setValue(indicators.stream()
                    .map(e -> configDO.getData().get(e.getAlias())).collect(Collectors.toList()));
            return configVO;
        }
    }

}
