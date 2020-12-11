package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.mvc.service.SetAnalyseConfigDetailService;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设置统计配置详情
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/analyse/setAnalyseConfigDetail")
public class SetAnalyseConfigDetailController {

    @Resource(name = "setAnalyseConfigDetailService")
    private SetAnalyseConfigDetailService setAnalyseConfigDetailService;

    /**
     * 通过id获取配置
     * @param id
     * @return
     */
    @GetMapping("/config/{id}")
    public ResultDataModel getConfigById(@PathVariable String id) {
        return ResultDataModel.handleSuccessResult(this.setAnalyseConfigDetailService.getConfigById(id));
    }

    /**
     * 保存配置
     * @return
     */
    @PostMapping("/config")
    public ResultDataModel saveConfig(@RequestBody ParamsDataModel paramsDataModel) {
        String id = paramsDataModel.getStringParameter("id");
        String config = paramsDataModel.getStringParameter("config");
        String typeCode = paramsDataModel.getStringParameter("typeCode");
        this.setAnalyseConfigDetailService.saveConfig(id, typeCode, config);
        return ResultDataModel.handleSuccessResult();
    }

}
