package com.whaty.products.controller.superadmin;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.superadmin.SuperAdminCreateSiteService;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 超管端开设站点controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("superAdminCreateSiteController")
@RequestMapping("/entity/superAdmin/superAdminCreateSite")
public class SuperAdminCreateSiteController {

    @Resource(name = "superAdminCreateSiteServiceImpl")
    private SuperAdminCreateSiteService superAdminCreateSiteService;

    /**
     * 一键开设站点
     * @return
     */
    @RequestMapping("/createSites")
    public ResultDataModel createSites(@RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        Map<String, Object> resultMap = new HashMap<>(4);
        try {
            this.superAdminCreateSiteService.createSites(CommonUtils.convertMultipartFileToFile(upload));
            resultMap.put("msg", "开设成功");
        } catch (ServiceException e) {
            resultMap.put("msg", e.getMessage());
        }
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 上传站点logo
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/uploadSiteLogo")
    public ResultDataModel uploadSiteLogo(@RequestBody ParamsDataModel paramsDataModel) throws IOException {
        File upload = new File(CommonUtils.getRealPath(paramsDataModel
                .getStringParameter(CommonConstant.PARAM_UPLOAD)));
        this.superAdminCreateSiteService.doUploadSiteLogo(upload);
        return ResultDataModel.handleSuccessResult("上传成功");
    }

    /**
     * 更新站点配置
     *
     * @return
     */
    @RequestMapping("/updateSiteConfig")
    public ResultDataModel updateSiteConfig() {
        this.superAdminCreateSiteService.updateSiteConfig();
        return ResultDataModel.handleSuccessResult("更新成功");
    }

}
