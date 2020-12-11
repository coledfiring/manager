package com.whaty.products.controller.flow;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.flow.CheckFlowDesignService;
import com.whaty.products.service.flow.domain.CheckFlowPage;
import com.whaty.products.service.flow.domain.config.CheckFlowConfig;
import com.whaty.products.service.flow.domain.config.CheckFlowConfigNode;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审核流程设计
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/flow/checkFlowDesign")
public class CheckFlowDesignController {

    @Resource(name = "checkFlowDesignService")
    private CheckFlowDesignService checkFlowDesignService;

    /**
     * 通过id获取组信息
     * @param groupId
     * @return
     */
    @RequestMapping("/checkFlowGroup/{groupId}")
    public ResultDataModel getCheckFlowGroup(@PathVariable String groupId) {
        return ResultDataModel.handleSuccessResult(this.checkFlowDesignService.getCheckFlowGroup(groupId));
    }

    /**
     * 列出所有有效的角色
     * @param page
     * @return
     */
    @PostMapping("/role")
    public ResultDataModel listRoles(@RequestBody CheckFlowPage page) {
        return ResultDataModel.handleSuccessResult(this.checkFlowDesignService
                .listRoles(page));
    }

    /**
     * 列出所有有效的管理员
     * @param page
     * @return
     */
    @PostMapping("/manager")
    public ResultDataModel listManagers(@RequestBody CheckFlowPage page) {
        return ResultDataModel.handleSuccessResult(this.checkFlowDesignService.listManagers(page));
    }

    /**
     * 保存审核人节点
     * @param node
     * @return
     */
    @PostMapping("/saveNode")
    public ResultDataModel saveNode(@RequestBody CheckFlowConfigNode node) {
        this.checkFlowDesignService.saveNode(node);
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 添加节点
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/addNode", method = RequestMethod.POST)
    public ResultDataModel addNode(@RequestBody ParamsDataModel paramsDataModel) {
        String groupId = paramsDataModel.getStringParameter("groupId");
        String previousId = paramsDataModel.getStringParameter("previousId");
        this.checkFlowDesignService.addNodeAfter(groupId, previousId);
        return ResultDataModel.handleSuccessResult("添加成功");
    }

    /**
     * 添加第一个节点
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/addFirstNode", method = RequestMethod.POST)
    public ResultDataModel addFirstNode(@RequestBody ParamsDataModel paramsDataModel) {
        String groupId = paramsDataModel.getStringParameter("groupId");
        this.checkFlowDesignService.addFirstNode(groupId);
        return ResultDataModel.handleSuccessResult("添加成功");
    }

    /**
     * 删除节点
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/deleteNode", method = RequestMethod.POST)
    public ResultDataModel deleteNode(@RequestBody ParamsDataModel paramsDataModel) {
        String groupId = paramsDataModel.getStringParameter("groupId");
        String nodeId = paramsDataModel.getStringParameter("nodeId");
        this.checkFlowDesignService.deleteNode(groupId, nodeId);
        return ResultDataModel.handleSuccessResult("删除审批人节点成功");
    }

    /**
     * 保存抄送人
     * @param config
     * @return
     */
    @PostMapping("/saveCopyPerson")
    public ResultDataModel saveCopyPerson(@RequestBody CheckFlowConfig config) {
        this.checkFlowDesignService.saveCopyPerson(config);
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 是否有效
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/isActive", method = RequestMethod.GET)
    public ResultDataModel isActive(@RequestParam("groupId") String groupId) {
        return ResultDataModel.handleSuccessResult(this.checkFlowDesignService.isActive(groupId));
    }

}
