package com.whaty.products.controller.flow;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.flow.CheckFlowDesignService;
import com.whaty.products.service.flow.CheckFlowService;
import com.whaty.products.service.flow.domain.CheckFlowPage;
import com.whaty.products.service.flow.domain.CustomCheckFlowParams;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 审核流程操作
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/flow/checkFlow")
public class CheckFlowController {

    @Resource(name = "checkFlowService")
    private CheckFlowService checkFlowService;

    @Resource(name = "checkFlowDesignService")
    private CheckFlowDesignService checkFlowDesignService;

    /**
     * 发起审核
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/apply")
    public ResultDataModel apply(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        String itemId = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String type = paramsDataModel.getStringParameter("type");
        this.checkFlowService.doApply(itemId, type);
        return ResultDataModel.handleSuccessResult("发起成功");
    }

    /**
     * 是否可以发起
     * @param itemId
     * @param type
     * @return
     */
    @GetMapping("/canApply")
    public ResultDataModel canApply(@RequestParam("itemId") String itemId, @RequestParam("type") String type) {
        this.checkFlowService.canApply(itemId, type);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 获取基于自定义的审批配置
     *
     * @param type
     * @return
     */
    @GetMapping("/checkFlowConfigWithCustom")
    public ResultDataModel getCheckFlowConfigWithCustom(@RequestParam("type") String type) {
        return ResultDataModel.handleSuccessResult(this.checkFlowService.getCheckFlowConfigWithCustom(type));
    }

    /**
     * 获取审核流程配置
     *
     * @param id
     * @param type
     * @param isCheckFlowId  1-当前id是checkFlowId 0-当前id不是checkFlowId
     * @return
     */
    @RequestMapping("/checkFlowConfig")
    public ResultDataModel getCheckFlowConfig(@RequestParam("id") String id, @RequestParam("type") String type,
                                   @RequestParam(name = "isCheckFlowId",defaultValue = "1") String isCheckFlowId) {
        if ("0".equals(isCheckFlowId)) {
            // 不是checkFlowId 默认为 fk_item_id
            return ResultDataModel.handleSuccessResult(this.checkFlowService.getCheckFlowConfigByItemId(id, type));
        }
        // 当前id是checkFlowId
        return ResultDataModel.handleSuccessResult(this.checkFlowService.getCheckFlowConfig(id, type));

    }

    /**
     * 审核通过
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/passCheck", method = RequestMethod.POST)
    public ResultDataModel passCheck(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        String id = paramsDataModel.getStringParameter("id");
        String currentNode = paramsDataModel.getStringParameter("currentNode");
        String note = paramsDataModel.getStringParameter("note");
        this.checkFlowService.doPassCheck(id, currentNode, note);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 驳回
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/noPassCheck", method = RequestMethod.POST)
    public ResultDataModel noPassCheck(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        String id = paramsDataModel.getStringParameter("id");
        String currentNode = paramsDataModel.getStringParameter("currentNode");
        String note = paramsDataModel.getStringParameter("note");
        this.checkFlowService.doNoPassCheck(id, currentNode, note);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 撤回申请
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/cancelCheck")
    public ResultDataModel cancelCheck(@RequestBody ParamsDataModel paramsDataModel) {
        String id = paramsDataModel.getStringParameter("id");
        this.checkFlowService.doCancelCheck(id);
        return ResultDataModel.handleSuccessResult("操作成功");
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
     * 基于自定义审批的发起审核
     * @return
     * @throws
     */
    @PostMapping("/applyWithCustom")
    public ResultDataModel applyWithCustom(@RequestBody CustomCheckFlowParams params) throws Exception {
        this.checkFlowService.doApplyWithCustom(params);
        return ResultDataModel.handleSuccessResult("发起成功");
    }

}
