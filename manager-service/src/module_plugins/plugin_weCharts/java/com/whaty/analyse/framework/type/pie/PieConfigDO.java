package com.whaty.analyse.framework.type.pie;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import com.whaty.analyse.framework.type.column.ColumnConfigDO;
import com.whaty.function.Functions;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 饼图配置do
 *
 * @author weipengsen
 */
@Data
public class PieConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = -7377097825698171175L;

    private String unit;

    /**
     * 查询出的元数据是map的格式还是list的格式，即是单行数据转换维度还是直接查询出多行数据
     * 为true时使用OneMap查询，且需要items，否则使用Map查询且不需要items
     */
    private Boolean isMapMetaData;

    private String sql;

    private Boolean showLegend;

    private Boolean showLabel;

    private List<PieItemConfigDO> items;

    private List<InnerPieItemConfigDO> innerPieItems;

    private ColumnConfigDO columnPanel;

    private String innerContentSql;

    private transient String innerContent;

    private transient Map<String, ? extends Number> data;

    /**
     * 转换复合函数
     */
    private final static Function<Map<String, PieItemConfigDO>, Consumer<InnerPieItemConfigDO>>
            CONVERT_MAP_FUN = r -> e -> e.convertItem(r);

    /**
     * 转换复合函数
     */
    private final static Function<PieConfigDO, Consumer<InnerPieItemConfigDO>> CONVERT_FUN = c ->
            CONVERT_MAP_FUN.apply(c.getItems().stream()
                    .collect(Functions.map(PieConfigDO.PieItemConfigDO::getAlias)));

    /**
     * 内部饼图do
     *
     * @author weipengsen
     */
    @Data
    public class InnerPieItemConfigDO implements Serializable {

        private static final long serialVersionUID = -144200219553950667L;

        private String name;

        private List<String> mappingOutItems;

        private transient List<PieItemConfigDO> items;

        /**
         * 转换
         *
         * @return
         */
        public void convertItem() {
            PieConfigDO.CONVERT_FUN.apply(PieConfigDO.this).accept(this);
        }

        /**
         * 转换
         *
         * @return
         */
        public void convertItem(Map<String, PieItemConfigDO> itemMap) {
            this.setItems(this.getMappingOutItems().stream().map(itemMap::get)
                    .filter(Objects::nonNull).collect(Collectors.toList()));
        }

        /**
         * 拿到总和
         *
         * @return
         */
        public Number getValue() {
            return items.stream().map(PieItemConfigDO::getValue)
                    .map(e -> BigDecimal.valueOf(Objects.nonNull(e) ? e.doubleValue() : 0))
                    .reduce(new BigDecimal(0), BigDecimal::add);
        }
    }

    /**
     * 饼图统计项do
     *
     * @author weipengsen
     */
    @Data
    @NoArgsConstructor
    public static class PieItemConfigDO implements Serializable, Comparable<PieItemConfigDO> {

        private static final long serialVersionUID = -6334027918567096995L;

        private String alias;

        private String name;

        private transient Number value;

        public PieItemConfigDO(String name, Number value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int compareTo(PieItemConfigDO o) {
            if (o.value == null && this.value == null) {
                return 0;
            }
            if (o.value == null) {
                return 1;
            }
            if (this.value == null) {
                return -1;
            }
            return Double.compare(this.value.doubleValue(), o.value.doubleValue());
        }
    }

}
