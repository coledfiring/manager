package com.whaty.products.controller.hbgr.energy.control;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.energyControl.PeEnergyControlServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.CHECK_SCENE_BASIC_SQL;

/**
 * author weipengsen  Date 2020/6/20
 *
 * 控制管理
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/peEnergyControlManage")
@BasicOperateRecord("现场")
@SqlRecord(namespace = "flagScene", sql = CHECK_SCENE_BASIC_SQL)
public class PeEnergyControlController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "peEnergyControlService")
    private PeEnergyControlServiceImpl peEnergyControlService;

    /**
     * 访问获取控制规则设置信息显示在页面
     *
     * @param restrictionId
     * @return
     */
    @RequestMapping(value = "/getControlRestrictions", method = RequestMethod.GET)
    public ResultDataModel getControlRestrictions(@RequestParam(CommonConstant.PARAM_IDS) String restrictionId) throws Exception {
        return ResultDataModel.handleSuccessResult(this.peEnergyControlService.getRestriction(restrictionId));
    }

    /**
     * l联动控制设置信息
     *
     * @param restrictionId
     * @return
     */
    @RequestMapping(value = "/getParameterInfo", method = RequestMethod.GET)
    public ResultDataModel getLinkControlRestrictions(@RequestParam(CommonConstant.PARAM_IDS) String restrictionId) throws Exception {
        return ResultDataModel.handleSuccessResult(this.peEnergyControlService.getParameterInfo(restrictionId));
    }

    /**
     *  获取管井控制
     *
     * @param restrictionId
     * @return
     */
    @RequestMapping(value = "/getTubeWellControl", method = RequestMethod.GET)
    public ResultDataModel getTubeWellControl(@RequestParam(CommonConstant.PARAM_IDS) String restrictionId) throws Exception {
        return ResultDataModel.handleSuccessResult(this.peEnergyControlService.getTubeWellControl(restrictionId));
    }

    /**
     * 设置控制规则
     *
     * @param paramsDataModel
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setControlRestrictions", method = RequestMethod.POST)
    public ResultDataModel setControlRestrictions(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        String restrictionId = (String) paramsDataModel.getParameter(CommonConstant.PARAM_IDS);
        Map<String, Object> map = (Map) paramsDataModel.getParameter(CommonConstant.PARAM_RESULT);
        this.peEnergyControlService.setControlRestrictions(restrictionId, map);
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    /**
     * 设置管井开度
     *
     * @param paramsDataModel
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setTubeWellControl", method = RequestMethod.POST)
    public ResultDataModel setTubeWellControl(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        String restrictionId = (String) paramsDataModel.getParameter(CommonConstant.PARAM_IDS);
        Map<String, Object> map = (Map) paramsDataModel.getParameter(CommonConstant.PARAM_RESULT);
        this.peEnergyControlService.setTubeWellControl(restrictionId, map);
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    /**
     * 设置参数
     *
     * @param paramsDataModel
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setParameterInfo", method = RequestMethod.POST)
    public ResultDataModel setParameterInfo(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        String restrictionId = (String) paramsDataModel.getParameter(CommonConstant.PARAM_IDS);
        Map<String, Object> map = (Map) paramsDataModel.getParameter(CommonConstant.PARAM_RESULT);
        this.peEnergyControlService.setParameterInfo(restrictionId, map);
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    @Override
    public GridService<EnumConst> getGridService() {
        return this.peEnergyControlService;
    }
}
