package com.whaty.analyse.framework.type.barline;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import com.whaty.analyse.framework.type.column.ColumnConfigDO;
import com.whaty.common.string.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 柱状与折线图配置do
 *
 * @author weipengsen
 */
@Data
public class BarAndLineConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = -6944911160201689135L;

    private String sql;

    private BarAndLineItemDO item;

    private Boolean isReverse;

    private Boolean isCustomize;

    private List<ValueAxis> axis;

    private List<ValueAxis> xAxis;

    private List<ValueAxis> yAxis;

    private List<BarAndLineGrid> grid;

    private List<BarAndLineDataZoom> dataZoom;

    private SeriesDO series;

    private transient List<Map<String, Object>> data;

    private ColumnConfigDO columnPanel;

    /**
     * 获取值域轴的顺序表
     * @return
     */
    public List<String> getAxisIndexList() {
        return Optional.ofNullable(this.getAxis()).map(a -> a.stream().sorted()
                .map(BarAndLineConfigDO.ValueAxis::getKey).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    /**
     * 值域轴
     *
     * @author weipengsen
     */
    @Data
    public class ValueAxis implements Serializable, Comparable<ValueAxis> {

        private static final long serialVersionUID = -5940014985874765988L;

        private String name;

        private String type;

        private Integer gridIndex;

        private Boolean inverse;

        private String position;

        private String key;

        private Number serial;

        private String unit;

        @Override
        public int compareTo(ValueAxis o) {
            if (o.serial == null && this.serial == null) {
                return 0;
            }
            if (o.serial == null) {
                return 1;
            }
            if (this.serial == null) {
                return -1;
            }
            return Integer.compare(this.serial.intValue(), o.serial.intValue());
        }
    }

    /**
     * 统计维度配置do
     *
     * @author weipengsen
     */
    @Data
    @NoArgsConstructor
    public static class SeriesDO implements Serializable {

        private static final long serialVersionUID = 898732713663575129L;

        private String sql;

        private List<SeriesItemDO> items;

        /**
         * 列举sql查询别名
         * @return
         */
        public List<String> listSqlAlias() {
            return Optional.ofNullable(this.items).map(e -> e.stream()
                            .map(BarAndLineConfigDO.SeriesDO.SeriesItemDO::getSqlAlias)
                            .filter(StringUtils::isNotBlank).collect(Collectors.toList())).orElse(new ArrayList<>());
        }

        /**
         * 维度项
         *
         * @author weipengsen
         */
        @Data
        public static class SeriesItemDO implements Serializable {

            private static final long serialVersionUID = -8491693236673583225L;

            private String alias;

            private String name;

            private String type;

            /**
             * 是否展示水平线
             */
            private Boolean hasAverageLine;

            private String valueAxisKey;

            private transient String sqlAlias;

            private Integer xAxisIndex;

            private Integer yAxisIndex;

            /**
             * 堆积柱状图的组
             */
            private String group;

        }

    }

    /**
     * 项配置do
     *
     * 查用两种策略去处理x轴数据的获取，填写xItemSql则使用sql查询出数据
     * 使用className与staticMethodName则使用反射调用获取数据
     *
     * @author weipengsen
     */
    @Data
    public class BarAndLineItemDO implements Serializable {

        private static final long serialVersionUID = 2087479176606068193L;

        private String name;

        private String alias;

        private String xItemSql;

        private String className;

        private String staticMethodName;

        private List<String> items;

    }
}
