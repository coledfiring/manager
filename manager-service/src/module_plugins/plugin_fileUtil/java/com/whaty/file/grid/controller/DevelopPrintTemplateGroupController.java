package com.whaty.file.grid.controller;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.file.domain.bean.PePrintTemplateGroup;
import com.whaty.framework.grid.supergrid.controller.SuperAdminGridController;
import com.whaty.file.grid.service.impl.DevelopPrintTemplateGroupServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 超管打印模板分组管理controller
 * @author weipengsen
 */
@Lazy
@RestController("developPrintTemplateGroupController")
@RequestMapping("/print/printTemplateGroup")
public class DevelopPrintTemplateGroupController extends SuperAdminGridController<PePrintTemplateGroup> {

    @Resource(name = "developPrintTemplateGroupService")
    private DevelopPrintTemplateGroupServiceImpl developPrintTemplateGroupService;

    @Override
    public GridService<PePrintTemplateGroup> getGridService() {
        return this.developPrintTemplateGroupService;
    }

}
