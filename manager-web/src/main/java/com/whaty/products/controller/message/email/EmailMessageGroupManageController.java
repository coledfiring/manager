package com.whaty.products.controller.message.email;

import com.whaty.domain.bean.message.EmailMessageGroup;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.EmailMessageGroupManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 邮件消息组管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/emailMessageGroupManage")
public class EmailMessageGroupManageController extends TycjGridBaseControllerAdapter<EmailMessageGroup> {

    @Resource(name = "emailMessageGroupManageService")
    private EmailMessageGroupManageServiceImpl emailMessageGroupManageService;

    @Override
    public GridService<EmailMessageGroup> getGridService() {
        return this.emailMessageGroupManageService;
    }
}
