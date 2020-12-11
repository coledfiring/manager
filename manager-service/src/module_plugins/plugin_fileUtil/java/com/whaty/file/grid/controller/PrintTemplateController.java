package com.whaty.file.grid.controller;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.grid.service.PrintTemplateService;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 模板打印controller
 * @author weipengsen
 */
@Lazy
@RequestMapping("/entity/printTemplate")
@RestController("printTemplateController")
public class PrintTemplateController extends TycjGridBaseControllerAdapter {

    @Resource(name = "printTemplateService")
    private PrintTemplateService printTemplateService;

    /**
     * 打印并下载模板
     * @param response
     * @throws IOException
     */
    @RequestMapping("/printAndDown")
    @OperateRecord(value = "打印模板",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE, isImportant = true)
    public void printAndDown(HttpServletResponse response) throws IOException, ServletException {
        try {
            this.printTemplateService.printAndDown(this.getRequestMap(), response);
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }

    /**
     * 获得所有的可自定义的模板
     * @return
     */
    @GetMapping("/template")
    @OperateRecord(value = "获得所有的可自定义的模板",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE)
    public ResultDataModel listTemplate() {
        return ResultDataModel.handleSuccessResult(this.printTemplateService.listTemplate());
    }

    /**
     * 上传自定义模板
     * @param upload
     * @param printId
     * @return
     * @throws IOException
     */
    @RequestMapping("/uploadTemplateFile")
    @OperateRecord(value = "上传自定义模板文件",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE, isImportant = true)
    public ResultDataModel uploadTemplate(@RequestParam(CommonConstant.PARAM_FILE_UPLOAD) MultipartFile upload,
                                          @RequestParam(PrintConstant.PARAM_PRINT_ID) String printId) throws Exception {
        this.printTemplateService.doUploadTemplate(CommonUtils.convertMultipartFileToFile(upload), printId);
        return ResultDataModel.handleSuccessResult("模板上传成功");
    }

}
