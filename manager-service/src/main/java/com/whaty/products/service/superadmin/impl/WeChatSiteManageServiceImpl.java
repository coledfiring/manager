package com.whaty.products.service.superadmin.impl;

import com.whaty.domain.bean.wechat.PeWeChatSite;
import com.whaty.framework.grid.supergrid.service.impl.SuperAdminGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 微信站点配置管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("weChatSiteManageService")
public class WeChatSiteManageServiceImpl extends SuperAdminGridServiceImpl<PeWeChatSite> {
}
