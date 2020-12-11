package com.whaty.products.controller.message.send;

import com.whaty.domain.bean.message.SendMessageTimeConfig;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.SendMessageTimeConfigManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送消息配置管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/sendMessageTimeConfigManage")
public class SendMessageTimeConfigManageController extends TycjGridBaseControllerAdapter<SendMessageTimeConfig> {

    @Resource(name = "sendMessageTimeConfigManageService")
    private SendMessageTimeConfigManageServiceImpl sendMessageTimeConfigManageService;

    @Override
    public GridService<SendMessageTimeConfig> getGridService() {
        return this.sendMessageTimeConfigManageService;
    }
}
