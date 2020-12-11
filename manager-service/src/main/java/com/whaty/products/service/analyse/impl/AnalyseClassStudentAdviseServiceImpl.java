package com.whaty.products.service.analyse.impl;

import com.whaty.domain.bean.evaluate.OverallEvaluateOption;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 学员反馈详情
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("analyseClassStudentAdviseService")
public class AnalyseClassStudentAdviseServiceImpl extends AbstractTwoLevelListGridServiceImpl<OverallEvaluateOption> {

    @Override
    protected String getParentIdSearchParamName() {
        return "clId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "clId";
    }
}
