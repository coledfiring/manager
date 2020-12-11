package com.whaty.products.controller.completion;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.file.grid.service.PrintTemplateService;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.completion.constants.CompletionConstants;
import com.whaty.products.service.completion.impl.CompletionCertificatePrintServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.COMPLETION_CERTIFICATE_INFO;

/**
 * 结业证书打印
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/completion/completionCertificatePrint")
public class CompletionCertificatePrintController extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "completionCertificatePrintService")
    private CompletionCertificatePrintServiceImpl completionCertificatePrintService;

    @Resource(name = "printTemplateService")
    private PrintTemplateService printTemplateService;

    /**
     * 更新证书编号
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/updateCertificateNumber")
    @OperateRecord(value = "更新证书编号", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "completionCertificate", sql = COMPLETION_CERTIFICATE_INFO)
    public ResultDataModel updateCertificateNumber(@RequestBody ParamsDataModel paramsDataModel) {
        String certificateNumber = paramsDataModel.getStringParameter(CompletionConstants.PARAM_CERTIFICATE_NUMBER);
        this.completionCertificatePrintService.updateCertificateNumber(this.getIds(paramsDataModel), certificateNumber);
        return ResultDataModel.handleSuccessResult("更新成功");
    }

    /**
     * 打印并下载模板
     * @param response
     * @throws IOException
     */
    @RequestMapping("/printAndDown")
    @OperateRecord(value = "打印模板",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE, isImportant = true)
    public void printAndDown(HttpServletResponse response) throws IOException, ServletException {
        Map<String, String> params = this.getRequestMap();
        completionCertificatePrintService.updateCertificateDownLoadTime(this.getIds(params), this.getUser().getLoginId());
        try {
            this.printTemplateService.printAndDown(params, response);
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }

    @Override
    public GridService<PeStudent> getGridService() {
        return this.completionCertificatePrintService;
    }
}
