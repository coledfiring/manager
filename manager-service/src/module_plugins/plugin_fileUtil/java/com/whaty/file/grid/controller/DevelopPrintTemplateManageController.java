package com.whaty.file.grid.controller;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.file.domain.bean.PePrintTemplate;
import com.whaty.framework.grid.supergrid.controller.SuperAdminGridController;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.grid.service.impl.DevelopPrintTemplateManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * 超管打印模板controller
 * @author weipengsen
 */
@Lazy
@RestController("developPrintTemplateManageController")
@RequestMapping("/print/printTemplate")
public class DevelopPrintTemplateManageController extends SuperAdminGridController<PePrintTemplate> {

    @Resource(name = "developPrintTemplateManageService")
    private DevelopPrintTemplateManageServiceImpl developPrintTemplateManageService;

    /**
     * 上传系统默认模板
     * @param paramsData
     * @return
     */
    @RequestMapping("/uploadDefaultTemplate")
    public ResultDataModel uploadDefaultTemplate(@RequestBody ParamsDataModel paramsData) throws IOException {
        this.changeDataSource(paramsData);
        String ids = this.getIds(paramsData);
        File upload = new File(CommonUtils.getRealPath(paramsData.getStringParameter(CommonConstant.PARAM_UPLOAD)));
        String namespace = paramsData.getStringParameter(PrintConstant.PARAM_NAMESPACE);
        this.developPrintTemplateManageService.doUploadDefaultTemplate(ids, upload, namespace);
        return ResultDataModel.handleSuccessResult("更换默认模板成功");
    }

    @Override
    public GridService<PePrintTemplate> getGridService() {
        return this.developPrintTemplateManageService;
    }

}
