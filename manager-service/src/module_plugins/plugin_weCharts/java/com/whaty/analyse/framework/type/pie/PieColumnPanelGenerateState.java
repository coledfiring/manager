package com.whaty.analyse.framework.type.pie;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.analyse.framework.type.column.ColumnConfigVO;
import com.whaty.function.Tuple;

import java.util.Objects;

/**
 * 饼图的横列面板生成状态
 *
 * @author weipengsen
 */
public class PieColumnPanelGenerateState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final PieConfigVO pieConfigVO;

    private AnalyseBasicConfig columnBasicConfig;

    public PieColumnPanelGenerateState(AnalyseStateContext context,
                                       Tuple<AnalyseBasicConfig, PieConfigVO> configTuple) {
        super(context);
        this.basicConfig = configTuple.t0;
        this.pieConfigVO = configTuple.t1;
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        if (Objects.nonNull(((PieConfigDO) this.basicConfig.getIConfigDO()).getColumnPanel())) {
            this.generateColumnBasicConfig();
            this.pieConfigVO.setColumnPanel(this.generateColumnConfig());
        }
        return this.pieConfigVO;
    }

    /**
     * 生成横列基础配置
     */
    private void generateColumnBasicConfig() {
        this.columnBasicConfig = new AnalyseBasicConfig();
        this.columnBasicConfig.setAnalyseParam(this.basicConfig.getAnalyseParam());
        this.columnBasicConfig.setCode(AnalyseType.COLUMN_ANALYSE.getCode());
        this.columnBasicConfig.setIConfigDO(((PieConfigDO) this.basicConfig.getIConfigDO()).getColumnPanel());
    }

    /**
     * 生成横列vo配置
     * @return
     */
    private ColumnConfigVO generateColumnConfig() throws Exception {
        return (ColumnConfigVO) AnalyseType.COLUMN_ANALYSE.getNextStateFunction()
                .apply(this.columnBasicConfig, this.context).handle();
    }
}
