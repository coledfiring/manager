package com.whaty.products.controller.message.wechat;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.WeChatTemplateTime;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.WeChatTemplateTimeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 微信模板定时推送
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/weChatTemplateTimeManage")
public class WeChatTemplateTimeManageController extends TycjGridBaseControllerAdapter<WeChatTemplateTime> {

    @Resource(name = "weChatTemplateTimeManageService")
    private WeChatTemplateTimeManageServiceImpl weChatTemplateTimeManageService;

    /**
     * 列举时间配置
     * @return
     */
    @GetMapping("/timeConfig")
    public ResultDataModel listTimeConfig() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.weChatTemplateTimeManageService.listTimeConfig());
    }

    /**
     * 保存配置
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/timeConfig")
    public ResultDataModel saveTimeConfig(@RequestBody ParamsDataModel paramsDataModel) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Map<String, Object>> timeConfig = (List<Map<String, Object>>) paramsDataModel.getParameter("timeConfig");
        this.weChatTemplateTimeManageService.saveTimeConfig(timeConfig);
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    @Override
    public GridService<WeChatTemplateTime> getGridService() {
        return this.weChatTemplateTimeManageService;
    }
}
