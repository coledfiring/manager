package com.whaty.analyse.framework.type.pie;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import com.whaty.analyse.framework.type.column.ColumnConfigVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 饼图配置vo
 *
 * title: string,
 * data: [
 *     {
 *         value: number,
 *         name: string
 *     }
 * ],
 * innerContent: string,
 * innerPie: [
 *     {
 *         value: number,
 *         name: string
 *     }
 * ]
 * columnPanel: {
 *     items: [
 *         {
 *             label: string,
 *             value: object,
 *             serial: number,
 *             highlight: boolean
 *         }
 *     ]
 * }
 *
 * @author weipengsen
 */
@Data
public class PieConfigVO extends AbstractConvertConfigVO<PieConfigDO> {

    private static final long serialVersionUID = -7952875345029920665L;

    private String unit;

    private List<PieDataVO> data;

    private List<PieDataVO> innerPie;

    private Boolean showLegend;

    private Boolean showLabel;

    private String innerContent;

    private ColumnConfigVO columnPanel;

    public PieConfigVO(PieConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(PieConfigDO config) {
        this.setUnit(config.getUnit());
        this.setShowLegend(config.getShowLegend());
        this.setShowLabel(config.getShowLabel());
        if (CollectionUtils.isNotEmpty(config.getInnerPieItems())) {
            this.handleInnerPie(config);
        } else {
            this.setInnerContent(config.getInnerContent());
            this.setData(config.getItems().stream()
                    .map(e -> new PieDataVO(e.getName(), Objects.nonNull(e.getValue()) ? e.getValue() : 0))
                    .sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
        }
    }

    /**
     * 处理内部饼图数据，同时会改变外部饼图的排序
     *
     * @param configDO
     */
    private void handleInnerPie(PieConfigDO configDO) {
        this.setInnerPie(configDO.getInnerPieItems().stream()
                .peek(PieConfigDO.InnerPieItemConfigDO::convertItem)
                .map(e -> new PieDataVO(e.getName(), e.getValue()))
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
        this.setData(configDO.getInnerPieItems().stream()
                .sorted(Comparator.comparingDouble(e -> - e.getValue().doubleValue()))
                .map(PieConfigDO.InnerPieItemConfigDO::getItems)
                .peek(Collections::sort)
                .flatMap(Collection::stream)
                .map(e -> new PieDataVO(e.getName(), Objects.nonNull(e.getValue()) ? e.getValue() : 0))
                .collect(Collectors.toList()));
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.PIE_ANALYSE;
    }

    /**
     * 饼图数据展示vo
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static class PieDataVO implements Serializable, Comparable<PieDataVO> {

        private static final long serialVersionUID = 1081691670275487652L;

        private String name;

        private Number value;

        @Override
        public int compareTo(PieDataVO o) {
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
