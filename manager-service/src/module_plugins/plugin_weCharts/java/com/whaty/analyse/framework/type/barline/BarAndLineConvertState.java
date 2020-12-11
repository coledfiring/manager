package com.whaty.analyse.framework.type.barline;

import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AnalyseConvertState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.function.Tuple;

/**
 * 柱状折线图转换状态
 *
 * @author weipengsen
 */
public class BarAndLineConvertState extends AnalyseConvertState<BarAndLineConfigDO, BarAndLineConfigVO> {

    private final AnalyseBasicConfig basicConfig;

    public BarAndLineConvertState(AnalyseStateContext context, AnalyseBasicConfig basicConfig) {
        super(context, (BarAndLineConfigDO) basicConfig.getIConfigDO(), BarAndLineConfigVO.class);
        this.basicConfig = basicConfig;
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        return this.handleNextState(new BarAndLinePanelGenerateState(this.context,
                new Tuple<>(this.basicConfig, (BarAndLineConfigVO) super.handle())));
    }
}
