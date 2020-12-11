package com.whaty.products.service.oltrain.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 职称系列管理服务类
 *
 * @author suoqioangqiang
 */
@Lazy
@Service("olProfessionalTitleSeriesManageService")
public class OLProfessionalTitleSeriesManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagOnlineProfessionalTitleSeries";
    }

}