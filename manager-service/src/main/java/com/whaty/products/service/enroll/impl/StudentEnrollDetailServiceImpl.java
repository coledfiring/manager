package com.whaty.products.service.enroll.impl;

import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 学员注册信息查看
 *
 * @author 索强强
 */
@Lazy
@Service("studentEnrollDetailService")
public class StudentEnrollDetailServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeStudent> {

    @Override
    protected String getParentIdSearchParamName() {
        return "cardNo";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
