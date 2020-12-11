package com.whaty.framework.api.wx;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.core.flow.service.FlowConfigService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.wechat.ApiWeChatPublicService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 微信公众号API
 *
 * @author weipengsen
 */
@Lazy
@RestController("apiWeChatPublicController")
@RequestMapping("/open/wxPublic")
public class ApiWeChatPublicController extends TycjGridBaseControllerAdapter {

    @Resource(name = "apiWeChatPublicServiceImpl")
    private ApiWeChatPublicService apiWeChatPublicService;

    @RequestMapping("/sendTemplateMsg")
    public ResultDataModel sendTemplateMsg(HttpServletRequest request) {
        apiWeChatPublicService.doSendTemplateMsg(RequestUtils.getRequestMap(request), request.getHeader("referer"), "");
        return ResultDataModel.handleSuccessResult("发送成功");
    }

}
