package com.whaty.analyse.framework.state;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import com.whaty.framework.exception.UncheckException;

/**
 * 雷达图数据转换状态
 *
 * @author weipengsen
 */
public class AnalyseConvertState<T extends AbstractConfigDO, R extends AbstractConvertConfigVO<T>>
        extends AbstractState {

    protected final T config;

    private final Class<R> clazzVO;

    public AnalyseConvertState(AnalyseStateContext context, T config, Class<R> clazzVO) {
        super(context);
        this.config = config;
        this.clazzVO = clazzVO;
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        try {
            return clazzVO.getConstructor(this.config.getClass()).newInstance(this.config);
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

}
