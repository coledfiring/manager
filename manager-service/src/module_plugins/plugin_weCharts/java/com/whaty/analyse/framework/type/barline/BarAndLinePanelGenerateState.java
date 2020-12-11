package com.whaty.analyse.framework.type.barline;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.analyse.framework.type.column.ColumnConfigVO;
import com.whaty.function.Tuple;

import java.util.Objects;

/**
 * 柱状折线图生成状态机
 *
 * @author weipengsen
 */
public class BarAndLinePanelGenerateState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final BarAndLineConfigVO configVO;

    private AnalyseBasicConfig columnBasicConfig;

    public BarAndLinePanelGenerateState(AnalyseStateContext context,
                                       Tuple<AnalyseBasicConfig, BarAndLineConfigVO> configTuple) {
        super(context);
        this.basicConfig = configTuple.t0;
        this.configVO = configTuple.t1;
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        if (Objects.nonNull(((BarAndLineConfigDO) this.basicConfig.getIConfigDO()).getColumnPanel())) {
            this.generateColumnBasicConfig();
            this.configVO.setColumnPanel(this.generateColumnConfig());
        }
        return this.configVO;
    }

    /**
     * 生成横列基础配置
     */
    private void generateColumnBasicConfig() {
        this.columnBasicConfig = new AnalyseBasicConfig();
        this.columnBasicConfig.setAnalyseParam(this.basicConfig.getAnalyseParam());
        this.columnBasicConfig.setCode(AnalyseType.COLUMN_ANALYSE.getCode());
        this.columnBasicConfig.setIConfigDO(((BarAndLineConfigDO) this.basicConfig.getIConfigDO()).getColumnPanel());
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
