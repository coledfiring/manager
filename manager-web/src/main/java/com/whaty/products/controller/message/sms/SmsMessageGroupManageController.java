package com.whaty.products.controller.message.sms;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.message.SmsMessageGroup;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.SmsMessageGroupManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 短信消息组管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/smsMessageGroupManage")
public class SmsMessageGroupManageController extends TycjGridBaseControllerAdapter<SmsMessageGroup> {

    @Resource(name = "smsMessageGroupManageService")
    private SmsMessageGroupManageServiceImpl smsMessageGroupManageService;

    @Override
    public GridService<SmsMessageGroup> getGridService() {
        return this.smsMessageGroupManageService;
    }
}
