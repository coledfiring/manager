package com.whaty.products.controller.suda;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.suda.PlatformConnectService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 苏大培训平台对接controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController("platformConnectController")
@RequestMapping("/entity/suda/platformConnect")
public class PlatformConnectController {

    @Resource(name = "platformConnectService")
    private PlatformConnectService platformConnectService;

    /**
     * 获取成教平台URL映射
     *
     * @return
     */
    @RequestMapping(value = "/getCjPlatformUrlMap", method = RequestMethod.GET)
    public ResultDataModel getCjPlatformUrlMap() {
        return ResultDataModel.handleSuccessResult(platformConnectService.getCjPlatformUrlMap());
    }

    /**
     * 获取自考平台URL映射
     *
     * @return
     */
    @RequestMapping(value = "/getZkPlatformUrlMap", method = RequestMethod.GET)
    public ResultDataModel getZkPlatformUrlMap() {
        return ResultDataModel.handleSuccessResult(platformConnectService.getZkPlatformUrlMap());
    }

    /**
     * 获取培训官网地址
     *
     * @return
     */
    @RequestMapping(value = "/getTrainOfficialWebsite", method = RequestMethod.GET)
    public ResultDataModel getTrainOfficialWebsite() {
        return ResultDataModel.handleSuccessResult(platformConnectService.getTrainOfficialWebsite());
    }

    /**
     * 获取成教官网地址
     *
     * @return
     */
    @RequestMapping(value = "/getCjOfficialWebsite", method = RequestMethod.GET)
    public ResultDataModel getCjOfficialWebsite() {
        return ResultDataModel.handleSuccessResult(platformConnectService.getCjOfficialWebsite());
    }
}
