package com.whaty.products.controller.message.sms;

import com.whaty.domain.bean.message.SmsMessageSite;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信信息与站点关联管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/smsMessageSiteManage")
public class SmsMessageSiteManageController extends TycjGridBaseControllerAdapter<SmsMessageSite> {
}
