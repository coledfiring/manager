package com.whaty.products.controller.hbgr.open.device;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.hbgr.yysj.PeDeviceInfoManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author weipengsen  Date 2020/7/2
 */
@Lazy
@RestController
@RequestMapping("/open/device/deviceInfo")
public class PeDeviceInfoOpenController {

    @Resource(name = "peDeviceInfoManageService")
    private PeDeviceInfoManageServiceImpl peDeviceInfoManageService;

    @RequestMapping("/getDeviceInfo")
    public ResultDataModel getDeviceInfo(@RequestParam String code) {
        return ResultDataModel.handleSuccessResult(peDeviceInfoManageService.getDeviceInfoByCode(code));
    }

    @RequestMapping("/getDeviceRepair")
    public ResultDataModel getDeviceRepair(@RequestParam String code) {
        return ResultDataModel.handleSuccessResult(peDeviceInfoManageService.getDeviceRepair(code));
    }

    @RequestMapping("/toAddDeviceRepair")
    public ResultDataModel toAddDeviceRepair(@RequestBody ParamsDataModel paramsData) {
        peDeviceInfoManageService.toAddDeviceRepair(paramsData.getStringParameter("code"),
                paramsData.getStringParameter("reason"), paramsData.getStringParameter("experience"));
        return ResultDataModel.handleSuccessResult("添加成功");
    }
}
