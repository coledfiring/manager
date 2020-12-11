package com.whaty.framework.core.controller.flow;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.core.flow.domain.Flow;
import com.whaty.framework.core.flow.service.FlowConfigService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 流程图配置controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/flow/flowConfig")
public class FlowConfigController {

    @Resource(name = "flowConfigService")
    private FlowConfigService flowConfigService;

    /**
     * 列举流程图配置
     * @return
     */
    @GetMapping("/flowConfig")
    public ResultDataModel listFlowConfig() {
        return ResultDataModel.handleSuccessResult(this.flowConfigService.listFlowConfig());
    }

    @GetMapping("/flowConfig/{flowId}")
    public ResultDataModel getFlowConfig(@PathVariable("flowId") String id) {
        return ResultDataModel.handleSuccessResult(this.flowConfigService.getFlowConfig(id));
    }

    /**
     * 获取菜单和按钮
     * @return
     */
    @GetMapping("/categoryAndMenu")
    public ResultDataModel listCategoryAndMenu() {
        Map<String, Map> categoryAndMenu = this.flowConfigService.listCategoryAndMenu();
        return ResultDataModel.handleSuccessResult(categoryAndMenu);
    }

    /**
     * 保存流程图
     * @return
     */
    @PostMapping("/flowConfig")
    public ResultDataModel saveFlowConfig(@RequestBody Flow flow) {
        Flow target = this.flowConfigService.saveFlowConfig(flow);
        return ResultDataModel.handleSuccessResult(target);
    }

    /**
     * 删除流程图
     * @param id
     * @return
     */
    @PostMapping("/flowConfig/{flowId}")
    public ResultDataModel deleteFlowConfig(@PathVariable("flowId") String id) {
        this.flowConfigService.deleteFlowConfig(id);
        return ResultDataModel.handleSuccessResult();
    }

}
