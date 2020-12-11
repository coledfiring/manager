package com.whaty.analyse.framework;

import com.whaty.analyse.framework.domain.AnalyseParam;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.state.AnalyseStateContext;

/**
 * 统计配置管理器,用于调度状态机上下文进行数据统计
 *
 * @author weipengsen
 */
public class AnalyseConfigManager {

    private final AnalyseStateContext context;

    public AnalyseConfigManager(AnalyseParam param) {
        this.context = new AnalyseStateContext(param);
    }

    /**
     * 获取统计数据
     *
     * @return
     */
    public AbstractConfigVO getAnalyseData() throws Exception {
        return this.context.handle();
    }

}
