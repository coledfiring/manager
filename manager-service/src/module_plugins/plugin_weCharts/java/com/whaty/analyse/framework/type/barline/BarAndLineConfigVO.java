package com.whaty.analyse.framework.type.barline;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import com.whaty.analyse.framework.type.column.ColumnConfigVO;
import com.whaty.util.TycjCollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 柱状与折线图配置vo
 *
 * {
 *     xAxis: {// x轴
 *         unit: '年月',// x轴单位
 *         xItems: ["x1", "x2"]// x轴项
 *     },
 *     series: [// 统计维度
 *         {
 *              name: "x1",// 维度名称
 *              type: "bar",// 维度类型
 *              unit: "m/s",// 维度单位
 *              values: [1, 2, 3, 4, 5, 6]// 维度的y轴值
 *         }, {
 *              name: "x2",
 *              type: "line",
 *              unit: "m/s",
 *              values: [1, 2, 3, 4, 5, 6]
 *         }
 *     ]
 * }
 *
 * @author weipengsen
 */
@Data
public class BarAndLineConfigVO extends AbstractConvertConfigVO<BarAndLineConfigDO> {

    private static final long serialVersionUID = -4618633707842459599L;

    private BarAndLineItemVO item;

    private Boolean isReverse;

    private Boolean isCustomize;

    private List<ValueAxisVO> valueAxis;

    private List<ValueAxisVO> xAxis;

    private List<ValueAxisVO> yAxis;

    private List<BarAndLineGrid> grid;

    private List<SeriesVO> series;

    private List<BarAndLineDataZoom> dataZoom;

    private ColumnConfigVO columnPanel;

    public BarAndLineConfigVO(BarAndLineConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(BarAndLineConfigDO configDO) {
        this.setIsReverse(configDO.getIsReverse());
        this.setIsCustomize(configDO.getIsCustomize() == null ? false: configDO.getIsCustomize());
        this.setItem(new BarAndLineItemVO(configDO.getItem()));
        this.setValueAxis(ValueAxisVO.convert(configDO.getAxis()));
        this.setGrid(configDO.getGrid());
        this.setDataZoom(configDO.getDataZoom());
        if (this.getIsCustomize()) {
            this.setXAxis(ValueAxisVO.convert(configDO.getXAxis()));
            this.setValueAxis(ValueAxisVO.convert(configDO.getYAxis()));
        }
        this.setSeries(this.getIsCustomize() ? SeriesVO.customConvert(configDO.getSeries().getItems(),
                this.filterData(configDO.getItem().getAlias(), configDO.getData()))
                : SeriesVO.convert(configDO.getSeries().getItems(), configDO.getAxisIndexList(),
                this.filterData(configDO.getItem().getAlias(), configDO.getData())));
    }

    /**
     * 过滤数据
     * 只有项中出现的数据才可以显示
     *
     * @param alias
     * @param origin
     * @return
     */
    private List<Map<String, Object>> filterData(String alias, List<Map<String, Object>> origin) {
        List<Map<String, Object>> target = origin.stream()
                .filter(e -> this.item.items.contains(e.get(alias))).collect(Collectors.toList());
        List<String> complementSet = TycjCollectionUtils.complement(this.item.items,
                origin.stream().map(e -> (String) e.get(alias)).collect(Collectors.toSet()));
        target.addAll(complementSet.stream().map(e -> TycjCollectionUtils.<String, Object>map(alias, e))
                .collect(Collectors.toList()));
        return target.stream().sorted(Comparator.comparingInt(e -> this.item.items.indexOf(e.get(alias))))
                .collect(Collectors.toList());
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.BAR_AND_LINE_ANALYSE;
    }

    /**
     * 值域轴
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static class ValueAxisVO implements Serializable {

        private static final long serialVersionUID = 4585982552619195352L;

        private String name;

        private String unit;

        private String position;

        private String type;

        private Integer gridIndex;

        private Boolean inverse;

        public static List<ValueAxisVO> convert(List<BarAndLineConfigDO.ValueAxis> axis) {
            return Optional.ofNullable(axis).map(e -> e.stream().sorted()
                    .map(a -> new ValueAxisVO(a.getName(), a.getUnit(), a.getPosition(), a.getType(), a.getGridIndex(),
                            a.getInverse())).collect(Collectors.toList())).orElse(null);
        }
    }

    /**
     * 项配置
     *
     * @author weipengsen
     */
    @Data
    public static class BarAndLineItemVO implements Serializable {

        private static final long serialVersionUID = 4748540337375146338L;

        private String name;

        private List<String> items;

        public BarAndLineItemVO(BarAndLineConfigDO.BarAndLineItemDO xAxisDO) {
            this.setName(xAxisDO.getName());
            this.setItems(new ArrayList<>(xAxisDO.getItems()));
        }
    }

    /**
     * 统计维度配置与值
     *
     * @author weipengsen
     */
    @Data
    public static class SeriesVO implements Serializable {

        private static final long serialVersionUID = -9101553053118638974L;

        private String name;

        private String type;

        private String group;

        /**
         * 是否展示水平线
         */
        private Boolean hasAverageLine;

        private Integer axisIndex;

        private Integer xAxisIndex;

        private Integer yAxisIndex;

        private List<? extends Number> data;

        public SeriesVO(BarAndLineConfigDO.SeriesDO.SeriesItemDO seriesDO, Integer axisIndex,
                        List<? extends Number> value) {
            this.name = seriesDO.getName();
            this.type = seriesDO.getType();
            this.group = seriesDO.getGroup();
            this.hasAverageLine = seriesDO.getHasAverageLine();
            this.data = value;
            this.axisIndex = axisIndex;
        }

        public SeriesVO(BarAndLineConfigDO.SeriesDO.SeriesItemDO seriesDO,
                        List<? extends Number> value) {
            this.name = seriesDO.getName();
            this.type = seriesDO.getType();
            this.group = seriesDO.getGroup();
            this.hasAverageLine = seriesDO.getHasAverageLine();
            this.data = value;
            this.xAxisIndex = seriesDO.getXAxisIndex();
            this.yAxisIndex = seriesDO.getYAxisIndex();
        }

        /**
         * 转换数据类型
         * @param series
         * @param axisIndexList
         *@param data  @return
         */
        public static List<SeriesVO> convert(List<BarAndLineConfigDO.SeriesDO.SeriesItemDO> series,
                                             List<String> axisIndexList, List<Map<String, Object>> data) {
            return series.stream().map(e -> new SeriesVO(e, e.getValueAxisKey() != null
                    ? (axisIndexList.indexOf(e.getValueAxisKey()) < 0 ? 0 : axisIndexList.indexOf(e.getValueAxisKey()))
                    : 0, extractSeriesValue(data, e.getAlias()))).collect(Collectors.toList());
        }

        /**
         * 自定义转换
         * @param series
         *@param data  @return
         */
        public static List<SeriesVO> customConvert(List<BarAndLineConfigDO.SeriesDO.SeriesItemDO> series,
                                                   List<Map<String, Object>> data) {
            return series.stream().map(e -> new SeriesVO(e, extractSeriesValue(data, e.getAlias())))
                    .collect(Collectors.toList());
        }

        /**
         * 提取series的值域
         * @param data
         * @param alias
         * @return
         */
        private static List<? extends Number> extractSeriesValue(List<Map<String, Object>> data, String alias) {
            return data.stream()
                    .map(e -> (Number) (e.get(alias) == null ? 0 : e.get(alias)))
                    .collect(Collectors.toList());
        }
    }

}
