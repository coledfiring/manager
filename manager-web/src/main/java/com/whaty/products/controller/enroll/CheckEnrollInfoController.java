package com.whaty.products.controller.enroll;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.enroll.CheckEnrollInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 审核注册信息
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/checkEnrollInfo")
public class CheckEnrollInfoController {

    @Resource(name = "checkEnrollInfoService")
    private CheckEnrollInfoService checkEnrollInfoService;

    /**
     * 获取报名信息
     * @return
     */
    @GetMapping("/enrollInfo")
    public ResultDataModel getEnrollInfo(@RequestParam("studentId") String studentId) {
        return ResultDataModel.handleSuccessResult(this.checkEnrollInfoService.getEnrollInfo(studentId));
    }

    /**
     * 审核通过
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/pass")
    public ResultDataModel pass(@RequestBody ParamsDataModel paramsDataModel) {
        this.checkEnrollInfoService.doPass(paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS));
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 审核不通过
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/noPass")
    public ResultDataModel noPass(@RequestBody ParamsDataModel paramsDataModel) {
        String reason = paramsDataModel.getStringParameter("reason");
        this.checkEnrollInfoService
                .doNoPass(paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS), reason);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

}
