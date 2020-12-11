package com.whaty.products.controller.requirement;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.RequirementInfo;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.requirement.RequirementInfoManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.REQUIREMENT_INFO_BASIC_INFO;

/**
 * 需求信息管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/requirement/requirementInfoManage")
@BasicOperateRecord(value = "需求信息管理")
@SqlRecord(namespace = "requirementInfo", sql = REQUIREMENT_INFO_BASIC_INFO)
public class RequirementInfoManageController extends TycjGridBaseControllerAdapter<RequirementInfo> {
    @Resource(name = "requirementInfoManageService")
    private RequirementInfoManageServiceImpl requirementInfoManageService;

    /**
     * 需求分派
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/arrange")
    @OperateRecord(value = "项目分派", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "requirementInfo", sql = REQUIREMENT_INFO_BASIC_INFO)
    public ResultDataModel confirm(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String followUpUserId = paramsDataModel.getStringParameter("followUpUserId");
        int count = this.requirementInfoManageService.doArrangeManager(ids, followUpUserId);
        return ResultDataModel.handleSuccessResult("分派成功，共成功分派" + count + "个需求");
    }

    /**
     * 获取管理员
     *
     * @return
     */
    @RequestMapping("/getManager")
    public ResultDataModel getManager() {
        return ResultDataModel.handleSuccessResult(this.requirementInfoManageService.getManager());
    }

    @Override
    public GridService<RequirementInfo> getGridService() {
        return this.requirementInfoManageService;
    }
}
