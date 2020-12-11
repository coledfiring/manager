package com.whaty.wecharts.chart;

import com.whaty.wecharts.exception.WeChartsServiceException;
import com.whaty.wecharts.jsonbean.BaseOption;
import com.whaty.wecharts.jsonbean.Grid;
import com.whaty.wecharts.jsonbean.Option;
import com.whaty.wecharts.jsonbean.Timeline;
import com.whaty.wecharts.jsonbean.axis.Axis;
import com.whaty.wecharts.jsonbean.axis.CategoryAxis;
import com.whaty.wecharts.jsonbean.axis.ValueAxis;
import com.whaty.wecharts.jsonbean.code.Orient;
import com.whaty.wecharts.jsonbean.code.Tool;
import com.whaty.wecharts.jsonbean.code.Trigger;
import com.whaty.wecharts.jsonbean.data.Data;
import com.whaty.wecharts.jsonbean.series.Bar;
import com.whaty.wecharts.jsonbean.series.Line;
import com.whaty.wecharts.jsonbean.series.Pie;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchartsUtil {

    private static Log logger = LogFactory.getLog("EchartsUtil");

    /**
     * 表格处理的图表数据内容
     */
    private ChartData chartData;

    /**
     * echart图表对象
     */
    private Option option;

    /**
     * 存放timeline中个Option
     */
    private Map<String, Option> optionMap;

    public EchartsUtil(ChartData chartData) {
        this.chartData = chartData;
        this.option = new Option();
        this.optionMap = new HashMap<>();
    }

    public Option chartDataToEcharts() throws WeChartsServiceException {
        this.option.title(this.chartData.getName());
        if (this.chartData.isHasTimeLine()) {

            Grid grid = new Grid();
            grid.setBottom(100);
            grid.setTop(60);
            BaseOption baseOption = new BaseOption(grid);
            Timeline timeline = new Timeline();
            timeline.setAxisType("category");
            timeline.label().interval(0);
            timeline.autoPlay(true);
            timeline.notMerge(true);
            option.timeline(timeline);
            option.baseOption(baseOption);

        } else {
            this.initOption(option, null, this.chartData);
            this.setOptionAxis(this.option, null, chartData.axisData());
        }
        option.grid().left("15%");
        option.toolbox().left("90%");
        option.toolbox().top("10%");
        SeriesData[] seriesDatas = chartData.seriesData();
        for (SeriesData seriesData : seriesDatas) {
            String group = seriesData.getGroup();
            String optionName;
            if (group == null || "default".equals(group)) {
                optionName = seriesData.getName();
            } else {
                optionName = group;
            }
            Option groupOption = this.getOption(optionName);

            if (chartData.isHasTimeLine()) {
                this.setOptionAxis(groupOption, optionName, chartData.axisData());
            }

            this.setOptionSeries(groupOption, optionName, seriesData);
        }
        return option;
    }

    /**
     * 设置option的系列
     * 1. 无timeline的图表中option为图标的主option
     * 2. 有timeline的图表中option为各子option，optionName为当前操作的option的时间轴坐标名称
     * <p>
     * action中可重写此方法，当acion中包含相同方法时将调用action中方法
     *
     * @param option
     * @param optionName
     * @param seriesData
     * @return
     */
    private Option setOptionSeries(Option option, String optionName, SeriesData seriesData) {
        Map<ChartColumnType, List<Object>> data = seriesData.data();

        switch (seriesData.getType()) {
            case line:
                Line line = new Line();
                if (seriesData.getAxisIndex() != null) {
                    line.xAxisIndex(seriesData.getAxisIndex());
                }
                option.legend().data(seriesData.getName());
                line.name(seriesData.getName()).data(data.get(ChartColumnType.lineValue).toArray());
                option.series(line);
                break;
            case bar:
                Bar bar = createBar(seriesData, data, ChartColumnType.barValue);
                bar.setBarWidth(25);
                option.legend().data(seriesData.getName());
                option.series(bar);
                break;
            case stack:
                Bar stack = createBar(seriesData, data, ChartColumnType.stackValue);
                option.legend().data(seriesData.getName());
                option.series(stack);
                break;
            case pie:
                //饼图：图例右侧剧中垂直，图例改为右侧顶部横向，避免页面缩放时遮挡
                option.toolbox().show(true).x("right").y("top").orient(Orient.horizontal);
                option.legend().y("center").orient(Orient.vertical);
                option.tooltip().formatter("{b}<br/>{c} ({d}%)");

                Pie pie = new Pie();
                pie.setName(seriesData.getName());
                List<Object> pieDatas = pie.clockWise(false).center("50%", "50%").radius("70%").data();

                List<Object> pieNames = data.get(ChartColumnType.pieName);
                List<Object> pieValues = data.get(ChartColumnType.pieValue);
                double max = 0;
                for (int i = 0; i < pieNames.size(); i++) {
                    option.legend().data(pieNames.get(i));

                    double value = Double.parseDouble(StringUtils.isBlank((String) pieValues.get(i)) ? "0" : pieValues.get(i).toString());
                    if (max < value) {
                        max = value;
                    }

                    Data pieData = new Data(pieNames.get(i).toString(), value);
                    pieDatas.add(pieData);
                }

                option.series(pie);
                break;
            default:
                break;
        }
        return option;
    }

    /**
     * 处理条形图
     *
     * @param seriesData
     * @param data
     * @return
     */
    private final Bar createBar(SeriesData seriesData, Map<ChartColumnType, List<Object>> data, ChartColumnType type) {
        Bar bar = new Bar();
        if (seriesData.getAxisIndex() != null) {
            bar.xAxisIndex(seriesData.getAxisIndex());
        }
        if (ChartColumnType.stackValue.name().equals(type.name())) {
            bar.stack(ChartColumnType.stackValue.name());
        }

        bar.name(seriesData.getName()).data(data.get(type).toArray())
                .itemStyle().normal().label().show(true);

        return bar;
    }

    /**
     * 获取对应的Option
     * 当图表没有timeline时返回this.option
     * 当图表有timeline时，timeline的分类默认为每个系列对应一个分类，分类名称为系列名称
     * 当分类中需要包含多个系列时，可以为多个系列设置相同的group，相同group的系列将在同一个分类中显示，分类名称为group名
     *
     * @param optionName
     * @return
     * @throws WeChartsServiceException
     */
    private Option getOption(String optionName) throws WeChartsServiceException {
        if (this.chartData.isHasTimeLine()) {
            Option option = this.optionMap.get(optionName);
            if (option == null) {
                Timeline timeline = this.option.getTimeline();
                if (timeline != null) {
                    timeline.data(optionName);
                }
                option = new Option();
                this.initOption(option, optionName, this.chartData);
                this.optionMap.put(optionName, option);
                this.option.options(option);
            }
            return option;
        } else {
            return this.option;
        }
    }

    /**
     * 设置坐标轴
     * 1. 无timeline的图表中option为图标的主option
     * 2. 有timeline的图表中option为各子option，optionName为当前操作的option的时间轴坐标名称
     * <p>
     * action中可重写此方法，当acion中包含相同方法时将调用action中方法
     *
     * @param option
     * @param optionName
     * @param axisDatas
     * @return
     */
    private Option setOptionAxis(Option option, String optionName, List<AxisData> axisDatas) {
        if (CollectionUtils.isNotEmpty(axisDatas)) {
            //设置数据缩放区域
            if (this.chartData.isHasDataZoom()) {
                option.dataZoom().show(true)
                        .start(this.chartData.dataZoomStart)
                        .end(this.chartData.dataZoomEnd)
                        .zoomLock(this.chartData.zoomLock);
                //图表中存在时间轴则将数据缩放区域放至图表顶部
                if (this.chartData.isHasTimeLine()) {
                    option.dataZoom().y(30);
                    option.grid().y(80);
                }
            }

            for (AxisData axisData : axisDatas) {
                List<Axis> xAxises = option.xAxis();
                List<Axis> yAxises = option.yAxis();
                CategoryAxis xAxis = new CategoryAxis().data(axisData.getData().toArray());
                ValueAxis yAxis = new ValueAxis();
                yAxis.axisLabel().interval(0);
                if (axisData.getIndex() == 0) {
                    if (xAxises.size() == 0 || xAxises.get(0) == null) {
                        xAxises.add(0, xAxis);
                        yAxises.add(0, yAxis);
                    }
                } else {
                    if (yAxises.size() == 0 || yAxises.get(0) == null) {
                        xAxises.add(0, yAxis);
                        yAxises.add(0, xAxis);
                    }
                }
            }
        }
        return option;
    }

    /**
     * 初始化option
     * 1. 无timeline的图表中option为图标的主option
     * 2. 有timeline的图表中option为各子option，optionName为当前操作的option的时间轴坐标名称
     * <p>
     * action中可重写此方法，当acion中包含相同方法时将调用action中方法
     *
     * @param option
     * @param optionName
     * @return
     */
    private Option initOption(Option option, String optionName, ChartData chartData) {

        option.title().text(chartData.getName()).padding(0, 0, 0, 20);
        option.legend().x("right").padding(5).itemGap(10);
        option.toolbox().show(true).x("right").y("center").orient(Orient.vertical).feature(Tool.saveAsImage);
        if (ChartColumnType.pieValue.name().equals(chartData.getType().name())) {
            option.tooltip().trigger(Trigger.item);
        } else {
            option.tooltip().trigger(Trigger.axis);
            option.dataZoom().show(true);
        }
        return option;
    }

}
