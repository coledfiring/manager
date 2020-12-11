package com.whaty.products.controller.enroll;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.enroll.SetEnrollColumnRegulationService;
import com.whaty.products.service.enroll.domain.SetRegulationParams;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设置报名规则
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/setEnrollColumnRegulation")
public class SetEnrollColumnRegulationController {

    @Resource(name = "setEnrollColumnRegulationService")
    private SetEnrollColumnRegulationService setEnrollColumnRegulationService;

    /**
     * 获取规则
     * @param id
     * @return
     */
    @GetMapping("/regulation/{id}")
    public ResultDataModel getRegulation(@PathVariable("id") String id) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.setEnrollColumnRegulationService.getRegulation(id));
    }

    /**
     * 保存规则
     * @param param
     * @return
     */
    @PostMapping("/regulation")
    public ResultDataModel saveRegulation(@RequestBody SetRegulationParams param) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.setEnrollColumnRegulationService.saveRegulation(param);
        return ResultDataModel.handleSuccessResult();
    }

}
