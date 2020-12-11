package com.whaty.products.controller.message.email;

import com.whaty.domain.bean.message.EmailMessageSite;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件信息组与站点管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/emailMessageSiteManage")
public class EmailMessageSiteManageController extends TycjGridBaseControllerAdapter<EmailMessageSite> {
}
