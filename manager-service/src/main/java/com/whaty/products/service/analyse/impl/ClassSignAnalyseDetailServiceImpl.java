package com.whaty.products.service.analyse.impl;

import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 教师评价统计详情
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("classSignAnalyseDetailService")
public class ClassSignAnalyseDetailServiceImpl extends AbstractTwoLevelListGridServiceImpl {

    @Override
    protected String getParentIdSearchParamName() {
        return "clId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
