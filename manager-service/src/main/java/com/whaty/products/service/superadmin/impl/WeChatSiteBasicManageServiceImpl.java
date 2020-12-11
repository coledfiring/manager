package com.whaty.products.service.superadmin.impl;

import com.whaty.domain.bean.wechat.PeWeChatBasic;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 微信站点配置管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("weChatSiteBasicManageService")
public class WeChatSiteBasicManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeWeChatBasic> {

    @Override
    protected String getParentIdSearchParamName() {
        return "peWeChatSite.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "peWeChatSite.id";
    }
}
