package com.whaty.analyse.framework.type.pie;

import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AnalyseConvertState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.function.Tuple;

/**
 * 饼图配置转换状态
 * 把饼图的do转换成vo
 *
 * @author weipengsen
 */
public class PieConvertState extends AnalyseConvertState<PieConfigDO, PieConfigVO> {

    private final AnalyseBasicConfig basicConfig;

    public PieConvertState(AnalyseStateContext context, AnalyseBasicConfig basicConfig) {
        super(context, (PieConfigDO) basicConfig.getIConfigDO(), PieConfigVO.class);
        this.basicConfig = basicConfig;
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        return this.handleNextState(new PieColumnPanelGenerateState(this.context,
                new Tuple<>(this.basicConfig, (PieConfigVO) super.handle())));
    }
}
