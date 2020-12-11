package com.whaty.analyse.framework.state;

import com.whaty.analyse.framework.domain.AbstractConfigVO;

/**
 * 状态机超类
 *
 * @author weipengsen
 */
public abstract class AbstractState {

    protected final AnalyseStateContext context;

    public AbstractState(AnalyseStateContext context) {
        this.context = context;
    }

    /**
     * 切换下一个状态并处理
     * @param state
     * @return
     */
    protected AbstractConfigVO handleNextState(AbstractState state) throws Exception {
        this.context.setState(state);
        return this.context.handle();
    }

    /**
     * 处理
     * @return
     * @throws Exception
     */
    public abstract AbstractConfigVO handle() throws Exception;

}
