package com.whaty.products.controller.requirement;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.RequirementInfo;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.requirement.RequirementFollowUpListServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.REQUIREMENT_FOLLOW_UP_LIST;

/**
 * 需求追踪信息管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/requirement/requirementFollowUpList")
@BasicOperateRecord(value = "需求跟进列表管理")
@SqlRecord(namespace = "requirementFollowUpList", sql = REQUIREMENT_FOLLOW_UP_LIST)
public class RequirementFollowUpListController extends TycjGridBaseControllerAdapter<RequirementInfo> {
    @Resource(name = "requirementFollowUpListService")
    private RequirementFollowUpListServiceImpl requirementFollowUpListService;

    /**
     * 确认接受需求
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/confirm")
    public ResultDataModel confirm(@RequestBody ParamsDataModel paramsDataModel) {
        int count = this.requirementFollowUpListService.doConfirmRequirement(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("确认成功，共成功接受" + count + "条数据");
    }

    @Override
    public GridService<RequirementInfo> getGridService() {
        return this.requirementFollowUpListService;
    }
}