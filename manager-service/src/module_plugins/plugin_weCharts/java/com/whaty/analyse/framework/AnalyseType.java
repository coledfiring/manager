package com.whaty.analyse.framework;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseConvertState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.analyse.framework.type.barline.BarAndLineConfigDO;
import com.whaty.analyse.framework.type.barline.BarAndLineSearchItemState;
import com.whaty.analyse.framework.type.block.BlockConfigDO;
import com.whaty.analyse.framework.type.block.BlockSearchDataState;
import com.whaty.analyse.framework.type.column.ColumnConfigDO;
import com.whaty.analyse.framework.type.column.ColumnSearchDataState;
import com.whaty.analyse.framework.type.compare.CompareConfigDO;
import com.whaty.analyse.framework.type.compare.CompareSearchDataState;
import com.whaty.analyse.framework.type.funnel.FunnelConfigDO;
import com.whaty.analyse.framework.type.funnel.FunnelSearchDataState;
import com.whaty.analyse.framework.type.grid.GridConfigDO;
import com.whaty.analyse.framework.type.grid.GridConfigVO;
import com.whaty.analyse.framework.type.map.MapConfigDO;
import com.whaty.analyse.framework.type.map.MapSearchDataState;
import com.whaty.analyse.framework.type.percent.PercentConfigDO;
import com.whaty.analyse.framework.type.percent.PercentSearchDataState;
import com.whaty.analyse.framework.type.pie.PieConfigDO;
import com.whaty.analyse.framework.type.pie.PieSearchDataState;
import com.whaty.analyse.framework.type.radar.RadarConfigDO;
import com.whaty.analyse.framework.type.radar.RadarSearchDataState;
import com.whaty.analyse.framework.type.scatter.ScatterConfigDO;
import com.whaty.analyse.framework.type.scatter.ScatterSearchDataState;
import com.whaty.analyse.framework.type.weather.WeatherConfigDO;
import com.whaty.analyse.framework.type.weather.WeatherDataState;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * 统计类型
 *
 * @author weipengsen
 */
public enum AnalyseType {

    /**
     * 横列统计
     */
    COLUMN_ANALYSE("1", ColumnConfigDO.class, ColumnSearchDataState::new),

    /**
     * 柱状和折线图统计
     */
    BAR_AND_LINE_ANALYSE("2", BarAndLineConfigDO.class, BarAndLineSearchItemState::new),

    /**
     * 饼图统计
     */
    PIE_ANALYSE("3", PieConfigDO.class, PieSearchDataState::new),

    /**
     * 漏斗图统计
     */
    FUNNEL_ANALYSE("4", FunnelConfigDO.class, FunnelSearchDataState::new),

    /**
     * 雷达图统计
     */
    RADAR_ANALYSE("5", RadarConfigDO.class, RadarSearchDataState::new),

    /**
     * 文本块统计
     */
    BLOCK_ANALYSE("6", BlockConfigDO.class, BlockSearchDataState::new),

    /**
     * grid统计
     */
    GRID_ANALYSE("7", GridConfigDO.class, (b, c) ->
            new AnalyseConvertState<>(c, (GridConfigDO) b.getIConfigDO(), GridConfigVO.class)),

    /**
     * 进度条统计
     */
    PERCENT_ANALYSE("8", PercentConfigDO.class, PercentSearchDataState::new),

    /**
     * 散点图统计
     */
    SCATTER_ANALYSE("9", ScatterConfigDO.class, ScatterSearchDataState::new),

    /**
     * 对比图统计
     */
    COMPARE_ANALYSE("10", CompareConfigDO.class, CompareSearchDataState::new),

    /**
     * 地区统计
     */
    MAP_ANALYSE("11", MapConfigDO.class, MapSearchDataState::new),

    /**
     * 天气统计
     */
    WEATHER_ANALYSE("12", WeatherConfigDO.class, WeatherDataState::new),
    ;

    private String code;

    private Class<? extends AbstractConfigDO> doClass;

    private BiFunction<AnalyseBasicConfig, AnalyseStateContext, AbstractState> nextStateFunction;

    AnalyseType(String code, Class<? extends AbstractConfigDO> doClass,
                BiFunction<AnalyseBasicConfig, AnalyseStateContext, AbstractState> nextStateFunction) {
        this.code = code;
        this.doClass = doClass;
        this.nextStateFunction = nextStateFunction;
    }

    /**
     * 通过类型获取DO配置类
     * @param code
     * @return
     */
    public static Class<? extends AbstractConfigDO> getConfigDO(String code) {
        return getTypeByCode(code).getDoClass();
    }

    /**
     * 获取下一个状态机
     * @param code
     * @param config
     * @param context
     * @return
     */
    public static AbstractState getNextState(String code, AnalyseBasicConfig config, AnalyseStateContext context) {
        return getTypeByCode(code).nextStateFunction.apply(config, context);
    }

    /**
     * 通过类型获取枚举对象
     * @param code
     * @return
     * @throws IllegalArgumentException
     */
    private static AnalyseType getTypeByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getCode() {
        return code;
    }

    public Class<? extends AbstractConfigDO> getDoClass() {
        return doClass;
    }

    public BiFunction<AnalyseBasicConfig, AnalyseStateContext, AbstractState> getNextStateFunction() {
        return nextStateFunction;
    }
}
