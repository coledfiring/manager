package com.whaty.analyse.framework.type.radar;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 雷达图配置DO
 *
 * @author weipengsen
 */
@Data
public class RadarConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = 3980863044030988619L;

    private List<RadarIndicatorConfigDO> indicators;

    private List<RadarSeriesConfigDO> series;

    /**
     * 雷达图最小范围
     */
    private Number minScope;

    /**
     * 获取雷达图边界值
     * {@link RadarConfigDO#minScope} == NAN 返回1.5倍的数据平均值
     * != NAN 返回 {@link RadarConfigDO#minScope}与数据平均值中的最大值
     *
     * @return
     */
    public Number getScope() {
        BigDecimal calcResult = series.stream().map(RadarSeriesConfigDO::getMaxValue)
                .max(Comparator.comparingDouble(Number::doubleValue))
                .map(e -> BigDecimal.valueOf(e.doubleValue()))
                .orElse(BigDecimal.valueOf(0));
        return Objects.isNull(minScope) ? calcResult : Math.max(this.minScope.doubleValue(), calcResult.doubleValue());
    }

    /**
     * 雷达图统计项配置DO
     *
     * @author weipengsen
     */
    @Data
    public class RadarIndicatorConfigDO implements Serializable {

        private static final long serialVersionUID = -5085299130739932548L;

        private String alias;

        private String name;

        /**
         * 雷达图单个项最小范围
         */
        private Number minScope;

        /**
         * 获取雷达图单个项的范围
         *
         * @return
         */
        public Number getScope() {
            return Objects.isNull(this.minScope)
                    ? RadarConfigDO.this.getScope()
                    : Math.max(this.minScope.doubleValue(), RadarConfigDO.this.getScope().doubleValue());
        }

    }

    /**
     * 雷达图统计维度配置DO
     *
     * @author weipengsen
     */
    @Data
    public class RadarSeriesConfigDO implements Serializable {

        private static final long serialVersionUID = -5428857260927010529L;

        private String name;

        private List<String> sql;

        private transient Map<String, ? extends Number> data;

        /**
         * 获取数据中的最大值
         *
         * @return
         */
        Number getMaxValue() {
            return RadarConfigDO.this.indicators.stream()
                    .map(e -> this.getData().get(e.getAlias()))
                    .filter(Objects::nonNull).map(Number::doubleValue)
                    .max(Comparator.comparingDouble(Number::doubleValue)).orElse(0D);
        }
    }
}
