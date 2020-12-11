package com.whaty.products.controller.databoard;

import com.whaty.domain.bean.BoardDataModel;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.databoard.DataBoardShowService;
import com.whaty.products.service.databoard.constant.DataBoardConstant;
import com.whaty.wecharts.constant.WeChartsConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 数据看板controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("dataBoardShowController")
@RequestMapping("/analyse/dataBoard/dataBordShow")
public class DataBoardShowController {

    @Resource(name = "dataBoardShowService")
    private DataBoardShowService dataBoardShowService;

    /**
     * 获取数据看板的节点
     *
     * @return
     */
    @RequestMapping(value = "/dataBoardCategory", method = RequestMethod.GET)
    @OperateRecord(value = "获取数据看板的节点",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE)
    public ResultDataModel getDataBoardCategory() {
        Map<String, Object> result = this.dataBoardShowService.getDataBoardCategory();
        return ResultDataModel.handleSuccessResultAndSetData(result);
    }

    /**
     * 获取数据看板的所有图表
     *
     * @return
     */
    @RequestMapping(value = "/getChartInfo", method = RequestMethod.GET)
    @OperateRecord(value = "获取数据看板的所有图表",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE)
    public ResultDataModel getChartInfo() {
        List<Map<String, Object>> result = this.dataBoardShowService.getChartInfo();
        return ResultDataModel.handleSuccessResultAndSetData(result);
    }

    /**
     * 获取数据看板页面数据信息
     *
     * @return
     */
    @RequestMapping(value = "/getChartListData", method = RequestMethod.GET)
    @OperateRecord(value = "获取数据看板页面数据信息",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE)
    public ResultDataModel getChartListData(@RequestParam(value = DataBoardConstant.IS_UPDATE, required = false) String isUpdate,
                                            @RequestParam(value = DataBoardConstant.CONFIG_NAME) String configName) {
        List<BoardDataModel> result = this.dataBoardShowService.getDataBoardListData(isUpdate, configName);
        return ResultDataModel.handleSuccessResultAndSetData(result);
    }

    /**
     * 获取图表数据
     *
     * @return
     */
    @RequestMapping(value = "/getOneChartDataInfo", method = RequestMethod.GET)
    @OperateRecord(value = "获取图表数据",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE)
    public ResultDataModel getOneChartDataInfo(@RequestParam(WeChartsConstants.PARAM_CODE) String code) {
        List<Map<String, Object>> result = this.dataBoardShowService.getOneChartDataInfo(code);
        return ResultDataModel.handleSuccessResultAndSetData(result);
    }

}
