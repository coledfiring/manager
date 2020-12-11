package com.whaty.analyse.framework.state;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AnalyseParam;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;

/**
 * 基础配置获取状态机
 *
 * @author weipengsen
 */
public class BasicConfigState extends AbstractState {

    private final AnalyseParam param;

    private AnalyseBasicConfig config;

    public BasicConfigState(AnalyseParam param, AnalyseStateContext context) {
        super(context);
        this.param = param;
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config = this.buildBasicConfig();
        return this.handleNextState(this.getNextState());
    }

    /**
     * 建造基础配置
     * @return
     */
    protected AnalyseBasicConfig buildBasicConfig() {
        return new AnalyseBasicConfig.BasicConfigBuilder()
                .withCode(this.param.getConfigCode())
                .withParam(this.param).build();
    }

    /**
     * 获取下一个状态机
     *
     * @return
     */
    protected AbstractState getNextState() {
        return AnalyseType.getNextState(this.config.getEnumConstByFlagAnalyseType().getCode(),
                this.config, this.context);
    }
}
