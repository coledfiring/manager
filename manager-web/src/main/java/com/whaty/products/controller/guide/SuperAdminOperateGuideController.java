package com.whaty.products.controller.guide;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.service.guide.OperateGuideService;
import com.whaty.core.framework.service.guide.constant.GuideConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 操作引导超管管理controller
 * @author weipengsen
 */
@RestController("superAdminOperateGuideController")
@RequestMapping("/superAdmin/guide/superAdminOperateGuideManage")
public class SuperAdminOperateGuideController extends TycjGridBaseControllerAdapter {

    @Resource(name = "operateGuideService")
    private OperateGuideService operateGuideService;

    /**
     * 修改描述图标
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/operateGuideIcon", method = RequestMethod.POST)
    public ResultDataModel setOperateGuideIcon(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String icon = paramsDataModel.getStringParameter(GuideConstant.PARAM_ICON);
        this.operateGuideService.doSetOperateGuideIcon(ids, icon);
        return ResultDataModel.handleSuccessResult("修改图标成功");
    }

}
