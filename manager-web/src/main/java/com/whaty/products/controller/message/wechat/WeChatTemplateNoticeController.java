package com.whaty.products.controller.message.wechat;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.message.WeChatTemplateNoticeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信模板消息controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("weChatTemplateNoticeController")
@RequestMapping("/entity/message/weChatTemplateNotice")
public class WeChatTemplateNoticeController {

    @Resource(name = "weChatTemplateNoticeService")
    private WeChatTemplateNoticeService weChatTemplateNoticeService;

    /**
     * 获取模板配置
     * @param templateCode
     * @return
     */
    @RequestMapping("/templateConfig/{code}")
    public ResultDataModel getTemplateConfig(@PathVariable("code") String templateCode) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.weChatTemplateNoticeService.getTemplateConfig(templateCode));
    }

}
