package com.whaty.analyse.framework.state;

import com.whaty.analyse.framework.domain.AnalyseParam;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.framework.asserts.TycjAssert;

/**
 * 统计状态机上下文
 *
 * 功能：
 *
 * 用于作为控制机传递的中控对象，第一个状态机恒定为basicConfig查询状态机，
 * 之后basicConfig状态机根据config中的类型做判断，决定下一个处理状态，
 * 状态机组首尾相连，成为一个状态机链，每种类型的统计图表对应一组状态机
 *
 * 原理：
 *
 * 状态机组中状态机遵循链式结构，数量不定，功能不定，都由一个初始状态机接受basicConfig，一个终止状态机返回IConfigVo，
 * 完成处理过程的绝对抽象
 *
 * 实例：
 *
 * 横列统计column类型定义初始状态机为查询状态ColumnSearchDataState，他在查询完后指定下一个状态机为
 * 数据转换状态机ColumnConvertState，此状态机为终止状态，返回最终的vo对象。
 *
 * @author weipengsen
 */
public class AnalyseStateContext {

    private AbstractState abstractState;

    public AnalyseStateContext(AnalyseParam param) {
        this.abstractState = new BasicConfigState(param, this);
    }

    /**
     * 处理
     *
     * @return
     */
    public AbstractConfigVO handle() throws Exception {
        AbstractConfigVO configVO = this.abstractState.handle();
        TycjAssert.validatePass(configVO);
        return configVO;
    }

    public void setState(AbstractState state) {
        this.abstractState = state;
    }
}
