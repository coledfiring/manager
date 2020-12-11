package com.whaty.file.grid.controller;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.file.domain.bean.PePrintTemplateSign;
import com.whaty.framework.grid.supergrid.controller.SuperAdminGridController;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.grid.service.impl.DevelopPrintTemplateSignServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超管打印模板占位符管理controller
 * @author weipengsen
 */
@Lazy
@RestController("developPrintTemplateSignController")
@RequestMapping("/print/printTemplateSign")
public class DevelopPrintTemplateSignController extends SuperAdminGridController<PePrintTemplateSign> {

    @Resource(name = "developPrintTemplateSignService")
    private DevelopPrintTemplateSignServiceImpl developPrintTemplateSignService;

    /**
     * 获取所有的模板
     * @return
     */
    @RequestMapping("/listTemplate")
    public ResultDataModel listTemplate(@RequestBody ParamsDataModel paramsData) {
        this.changeDataSource(paramsData);
        List<Object[]> templates = this.developPrintTemplateSignService.listTemplate();
        return ResultDataModel.handleSuccessResult(templates);
    }

    /**
     * 复制分组到指定的模板
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/copyGroupToTemplate")
    public ResultDataModel copyGroupToTemplate(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String printId = paramsDataModel.getStringParameter(PrintConstant.PARAM_PRINT_ID);
        this.changeDataSource(paramsDataModel);
        this.developPrintTemplateSignService.doCopyGroupToTemplate(ids, printId);
        return ResultDataModel.handleSuccessResult("复制成功");
    }

    @Override
    public GridService<PePrintTemplateSign> getGridService() {
        return this.developPrintTemplateSignService;
    }

}
