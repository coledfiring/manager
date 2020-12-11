package com.whaty.framework.api.workspace.controller;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.file.grid.service.PrintTemplateService;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.wechat.ApiWeChatPublicService;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 工作室开放操作controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController("workSpaceCommonController")
@RequestMapping("/open/workSpaceCommon")
public class WorkSpaceCommonController extends TycjGridBaseControllerAdapter {

    @Resource(name = "printTemplateService")
    private PrintTemplateService printTemplateService;

    /**
     * 获取文件地址
     * @return
     */
    @RequestMapping("/getFileRealPath")
    public ResultDataModel uploadAttachFile(@RequestParam("url") String url) {
        return ResultDataModel.handleSuccessResult(CommonUtils.getRealPath(url));
    }

    @Resource(name = "apiWeChatPublicServiceImpl")
    private ApiWeChatPublicService apiWeChatPublicService;

    @RequestMapping("/sendTemplateMsg")
    public ResultDataModel sendTemplateMsg(HttpServletRequest request) {
        String ip = StringUtils.isNotBlank(request.getHeader("X-Real-IP"))?
                request.getHeader("X-Real-IP") : request.getRemoteAddr();
        apiWeChatPublicService.doSendTemplateMsg(RequestUtils.getRequestMap(request), request.getHeader("referer"), ip);
        return ResultDataModel.handleSuccessResult("发送成功");
    }

    /**
     * 打印并下载模板
     * @param response
     * @throws IOException
     */
    @RequestMapping("/printAndDown")
    public void printAndDown(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String siteCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteByDomain(request.getServerName()).getCode());
        try {
            this.printTemplateService.printAndDown(RequestUtils.getRequestMap(request), response);
            MasterSlaveRoutingDataSource.setDbType(siteCode);
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }
}
