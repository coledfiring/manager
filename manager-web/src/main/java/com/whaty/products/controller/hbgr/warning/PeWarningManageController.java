package com.whaty.products.controller.hbgr.warning;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeWarning;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.warning.PeWarningManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * author weipengsen  Date 2020/6/20
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peWarningManage")
public class PeWarningManageController extends TycjGridBaseControllerAdapter<PeWarning> {

    @Resource(name = "peWarningManageService")
    private PeWarningManageServiceImpl peWarningManageService;

    @Override
    public GridService<PeWarning> getGridService() {
        return this.peWarningManageService;
    }

    /**
     * 访问获取报警预警规则设置信息显示在页面
     *
     * @param restrictionId
     * @return
     */
    @RequestMapping(value = "/warningRestrictions", method = RequestMethod.GET)
    public ResultDataModel getGraduateRestrictions(@RequestParam(CommonConstant.PARAM_IDS) String restrictionId) {
        Map<String, Object> map = this.peWarningManageService.getRestriction(restrictionId);
        return ResultDataModel.handleSuccessResult(map);
    }

    /**
     * 设置报警规则
     *
     * @param paramsDataModel
     * @throws Exception
     * @return
     */
    @RequestMapping(value = "/setWarningRestrictions", method = RequestMethod.POST)
    public ResultDataModel setGraduateRestrictions(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        String restrictionId = (String) paramsDataModel.getParameter(CommonConstant.PARAM_IDS);
        Map<String, Object> map = (Map) paramsDataModel.getParameter(CommonConstant.PARAM_RESULT);
        this.peWarningManageService.saveRestriction(restrictionId, map);
        return ResultDataModel.handleSuccessResult("设置成功");
    }
}
