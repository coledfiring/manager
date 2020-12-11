package com.whaty.products.controller.siteconfig;

import com.alibaba.fastjson.JSON;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.siteconfig.SiteConfigConstant;
import com.whaty.products.service.siteconfig.SiteConfigInfoManageService;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * siteConfigPage页面管理controller
 * @author suoqiangqiang
 */
@Lazy
@RequestMapping("/superAdmin/siteConfig/siteConfigInfo")
@RestController("siteConfigInfoController")
public class SiteConfigInfoController {

    @Resource(name = "siteConfigInfoManageService")
    private SiteConfigInfoManageService siteConfigInfoManageService;

    /**
     * 获取站点名称
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/getSiteName", method = RequestMethod.POST)
    public ResultDataModel getSiteName(@RequestBody ParamsDataModel paramsDataModel) {
        String siteCode = paramsDataModel.getStringParameter(CommonConstant.PARAM_WEB_SITE_CODE);
        if (StringUtils.isBlank(siteCode)) {
            throw new ParameterIllegalException();
        }
        Map<String, Object> siteMap = new HashMap<String, Object>() {{
            put("site", SiteUtil.getSite(siteCode));
        }};
        return ResultDataModel.handleSuccessResult(siteMap);
    }

    /**
     * 获取规则正则
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/getRegularJson", method = RequestMethod.POST)
    public ResultDataModel getRegularJson(@RequestBody ParamsDataModel paramsDataModel) {
        String siteCode = paramsDataModel.getStringParameter(CommonConstant.PARAM_WEB_SITE_CODE);
        String name = paramsDataModel.getStringParameter(SiteConfigConstant.PARAMS_REGULAR_JSON_NAME);
        if (StringUtils.isBlank(siteCode) || StringUtils.isBlank(name)) {
            throw new ParameterIllegalException();
        }
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        return ResultDataModel.handleSuccessResult(siteConfigInfoManageService.getRegularJson(name));
    }

    /**
     * 保存规则正则
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/saveRegularJson", method = RequestMethod.POST)
    public ResultDataModel saveRegularJson(@RequestBody ParamsDataModel paramsDataModel) {
        JSONObject regularJson = JSONObject
                .fromObject(paramsDataModel.getParameter(SiteConfigConstant.PARAMS_REGULAR_JSON_VALUE));
        String siteCode = paramsDataModel.getStringParameter(CommonConstant.PARAM_WEB_SITE_CODE);
        String name = paramsDataModel.getStringParameter(SiteConfigConstant.PARAMS_REGULAR_JSON_NAME);
        if (StringUtils.isBlank(siteCode) || StringUtils.isBlank(name) || JSONUtils.isNull(regularJson)) {
            throw new ParameterIllegalException();
        }
        String regularJsonStr = JSON.toJSONString(regularJson);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        siteConfigInfoManageService.saveRegularJson(name, regularJsonStr);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 保存规则正则
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/getEnrollInfoConfig", method = RequestMethod.POST)
    public ResultDataModel getEnrollInfoConfig(@RequestBody ParamsDataModel paramsDataModel) {
        String siteCode = paramsDataModel.getStringParameter(CommonConstant.PARAM_WEB_SITE_CODE);
        if (StringUtils.isBlank(siteCode)) {
            throw new ParameterIllegalException();
        }
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        return ResultDataModel.handleSuccessResult(siteConfigInfoManageService.getEnrollInfoConfig());
    }

    /**
     * 成教定制，获取站点名称
     * @return
     */
    @RequestMapping(value = "/getSite", method = RequestMethod.POST)
    public ResultDataModel getSite() {
        return ResultDataModel.handleSuccessResult(SiteUtil.getSite().getName());
    }
}
