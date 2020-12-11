package com.whaty.custom.controller;

import com.whaty.custom.constant.AnalyseConstant;
import com.whaty.custom.service.CustomAnalyseService;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.wecharts.exception.WeChartsServiceException;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 自定义统计图表controller
 * @author weipengsen
 */
@Lazy
@RestController("customAnalyseController")
@RequestMapping("/entity/customAnalyse")
public class CustomAnalyseController extends GridBaseController {

    @Resource(name = "customAnalyseService")
    private CustomAnalyseService customAnalyseServiceImpl;

    /**
     * 根据配置获取预览charts自定义统计
     * @return
     */
    @RequestMapping(value = "/customAnalysePreviewChartsConfig", method = RequestMethod.POST)
    public ResultDataModel getCustomAnalyseToPreviewChartsConfig(@RequestBody ParamsDataModel paramsDataModel) {
        try {
            String analyseGroupId = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_ANALYSE_GROUP_ID);
            String name = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_NAME);
            String chartsType = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_CHARTS_TYPE);
            Map<String, Object> charts = (Map<String, Object>) paramsDataModel
                    .getParameter(AnalyseConstant.PARAM_CHARTS);
            List<Map<String, Object>> conditions = (List<Map<String, Object>>) paramsDataModel
                    .getParameter(AnalyseConstant.PARAM_CONDITIONS);
            Map<String, Object> analyse = this.customAnalyseServiceImpl
                    .getCustomAnalyseToPreviewChartsConfig(analyseGroupId, name, chartsType, charts, conditions);
            return ResultDataModel.handleSuccessResult(analyse);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new UncheckException(e);
        } catch (WeChartsServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 根据配置获取grid预览自定义统计
     * @return
     */
    @RequestMapping(value = "/customAnalysePreviewGridConfig", method = RequestMethod.POST)
    public ResultDataModel getCustomAnalyseToPreviewGridConfig(@RequestBody ParamsDataModel paramsDataModel) {
        try {
            String analyseGroupId = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_ANALYSE_GROUP_ID);
            String name = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_NAME);
            String chartsType = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_CHARTS_TYPE);
            Map<String, Object> charts = (Map<String, Object>) paramsDataModel
                    .getParameter(AnalyseConstant.PARAM_CHARTS);
            List<Map<String, Object>> conditions = (List<Map<String, Object>>) paramsDataModel
                    .getParameter(AnalyseConstant.PARAM_CONDITIONS);
            Map<String, Object> analyse = this.customAnalyseServiceImpl
                    .getCustomAnalyseToPreviewGridConfig(analyseGroupId, name, chartsType, charts, conditions);
            return ResultDataModel.handleSuccessResult(analyse);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new UncheckException(e);
        } catch (WeChartsServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 保存自定义图表
     * @return
     */
    @RequestMapping(value = "/customAnalyse", method = RequestMethod.POST)
    public ResultDataModel saveCustomAnalyse(@RequestBody ParamsDataModel paramsDataModel) {
        try {
            String analyseId = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_ANALYSE_ID);
            String analyseGroupId = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_ANALYSE_GROUP_ID);
            String name = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_NAME);
            String chartsType = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_CHARTS_TYPE);
            Map<String, Object> viewLevel = (Map<String, Object>) paramsDataModel.getParameter(AnalyseConstant.PARAM_VIEW_LEVEL);
            Map<String, Object> charts = (Map<String, Object>) paramsDataModel
                    .getParameter(AnalyseConstant.PARAM_CHARTS);
            List<Map<String, Object>> conditions = (List<Map<String, Object>>) paramsDataModel
                    .getParameter(AnalyseConstant.PARAM_CONDITIONS);
            analyseId = this.customAnalyseServiceImpl
                    .saveCustomAnalyse(analyseId, name, analyseGroupId, chartsType, viewLevel, charts, conditions);
            return ResultDataModel.handleSuccessResult(analyseId);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new UncheckException(e);
        } catch (WeChartsServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 获取自定义统计配置
     * @param analyseGroupId
     * @param analyseId
     * @return
     */
    @RequestMapping(value = "/customAnalyseConfig", method = RequestMethod.GET)
    public ResultDataModel getCustomAnalyseConfig(
            @RequestParam(value = AnalyseConstant.PARAM_ANALYSE_GROUP_ID, required = false) String analyseGroupId,
            @RequestParam(value = AnalyseConstant.PARAM_ANALYSE_ID, required = false) String analyseId) {
        Map<String, Object> resultMap = this.customAnalyseServiceImpl.getCustomAnalyseConfig(analyseGroupId, analyseId);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 获取自定义统计配置的actionId和chartId
     * @param analyseId
     * @return
     */
    @RequestMapping(value = "/customAnalyse", method = RequestMethod.GET)
    public ResultDataModel getCustomAnalyse(@RequestParam(AnalyseConstant.PARAM_ANALYSE_ID) String analyseId) {
        Map<String, Object> resultMap = this.customAnalyseServiceImpl.getCustomAnalyse(analyseId);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 获取自定义统计组
     * @param analyseGroupCode
     * @return
     */
    @RequestMapping(value = "/customAnalyseGroup", method = RequestMethod.GET)
    public ResultDataModel getCustomAnalyseGroup(
            @RequestParam(AnalyseConstant.PARAM_ANALYSE_GROUP_CODE) String analyseGroupCode) {
        Map<String, Object> analyseGroup = this.customAnalyseServiceImpl.getCustomAnalyseGroup(analyseGroupCode);
        return ResultDataModel.handleSuccessResult(analyseGroup);
    }

    /**
     * 删除自定义统计
     * @return
     */
    @RequestMapping(value = "/deleteCustomAnalyse", method = RequestMethod.POST)
    public ResultDataModel deleteCustomAnalyse(@RequestBody ParamsDataModel paramsDataModel) {
        String analyseId = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_ANALYSE_ID);
        this.customAnalyseServiceImpl.deleteCustomAnalyse(analyseId);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 收藏自定义统计
     * @return
     */
    @RequestMapping(value = "/collectCustomAnalyse", method = RequestMethod.POST)
    public ResultDataModel collectCustomAnalyse(@RequestBody ParamsDataModel paramsDataModel) {
        String analyseId = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_ANALYSE_ID);
        this.customAnalyseServiceImpl.doCollectCustomAnalyse(analyseId);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 取消收藏自定义统计
     * @return
     */
    @RequestMapping(value = "/cancelCollectCustomAnalyse", method = RequestMethod.POST)
    public ResultDataModel cancelCollectCustomAnalyse(@RequestBody ParamsDataModel paramsDataModel) {
        String analyseId = paramsDataModel.getStringParameter(AnalyseConstant.PARAM_ANALYSE_ID);
        this.customAnalyseServiceImpl.doCancelCollectCustomAnalyse(analyseId);
        return ResultDataModel.handleSuccessResult("删除成功");
    }
}
