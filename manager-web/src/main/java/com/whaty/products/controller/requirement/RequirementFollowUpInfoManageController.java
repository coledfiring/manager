package com.whaty.products.controller.requirement;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.RequirementFollowUpInfo;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.requirement.RequirementFollowUpInfoManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.REQUIREMENT_FOLLOW_UP_INFO;

/**
 * 需求信息跟踪管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/requirement/requirementFollowUpInfoManage")
@BasicOperateRecord(value = "需求信息跟踪管理")
@SqlRecord(namespace = "requirementFollowUpInfo", sql = REQUIREMENT_FOLLOW_UP_INFO)
public class RequirementFollowUpInfoManageController extends TycjGridBaseControllerAdapter<RequirementFollowUpInfo> {

    @Resource(name = "requirementFollowUpInfoManageService")
    private RequirementFollowUpInfoManageServiceImpl requirementFollowUpInfoManageService;

    @Override
    public GridService<RequirementFollowUpInfo> getGridService() {
        return this.requirementFollowUpInfoManageService;
    }
}
