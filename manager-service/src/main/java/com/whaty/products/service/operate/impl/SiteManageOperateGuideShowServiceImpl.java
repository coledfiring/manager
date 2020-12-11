package com.whaty.products.service.operate.impl;

import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 站点管理端，查看操作日志记录服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("siteManageOperateGuideShowService")
public class SiteManageOperateGuideShowServiceImpl extends TycjGridServiceAdapter {

    @Override
    protected String getOrderColumnIndex() {
        return "operateTime";
    }

    @Override
    protected String getOrderWay() {
        return "desc";
    }
}
