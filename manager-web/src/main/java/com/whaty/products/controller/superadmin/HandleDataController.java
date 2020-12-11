package com.whaty.products.controller.superadmin;

import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.superadmin.HandleDataService;
import com.whaty.products.service.superadmin.constant.SuperAdminConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据处理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("handleDataController")
@RequestMapping("/entity/superAdmin/handleData")
public class HandleDataController {

    @Resource(name = "handleDataServiceImpl")
    private HandleDataService handleDataService;

    /**
     * 保存openApi的登录用户
     * @return
     */
    @RequestMapping(value = "/openApiLoginUser", method = RequestMethod.POST)
    public ResultDataModel saveOpenApiLoginUser(@RequestBody ParamsDataModel paramsDataModel) {
        String loginId = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_LOGIN_ID);
        String password = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_PASSWORD);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        this.handleDataService.saveOpenApiLoginUser(loginId, password, siteCode);
        return ResultDataModel.handleSuccessResult("添加成功");
    }

}
