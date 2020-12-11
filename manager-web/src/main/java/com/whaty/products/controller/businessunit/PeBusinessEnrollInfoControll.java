package com.whaty.products.controller.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.file.grid.service.PrintTemplateService;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.businessunit.PeBusinessEnrollInfoServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 报名信息查询
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/peBusinessEnrollInfo")
public class PeBusinessEnrollInfoControll extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "peBusinessEnrollInfoService")
    private PeBusinessEnrollInfoServiceImpl peBusinessEnrollInfoService;

    @Resource(name = "printTemplateService")
    private PrintTemplateService printTemplateService;

    /**
     * 总站管理员调整期次：调整期次不限制开班时间，需要校验人数
     *
     * @return
     */
    @RequestMapping(value = "/listCanEnrollClassInfo")
    @OperateRecord(value = "可以报名的培训班级信息",
            moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel listCanEnrollClassInfo() {
        return ResultDataModel.handleSuccessResult(peBusinessEnrollInfoService.listCanEnrollClassInfo());
    }

    /**
     * 勾选调整期次
     *
     * @return
     */
    @RequestMapping(value = "/updateStudentClassInfo")
    @OperateRecord(value = "勾选调整期次", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel updateStudentClassInfo(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String classId = paramsDataModel.getStringParameter("classId");
        return ResultDataModel.handleSuccessResult("调整期次成功，共操作" +
                peBusinessEnrollInfoService.updateStudentClassInfo(ids, classId) + "条数据");
    }

    /**
     * 打印听课证
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/printAndDown")
    @OperateRecord(value = "打印听课证",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE, isImportant = true)
    public void printAndDown(HttpServletResponse response) throws IOException, ServletException {
        Map<String, String> params = this.getRequestMap();
        try {
            peBusinessEnrollInfoService.checkIsCanPrintCertificateStudentInfo(this.getIds(params));
            this.printTemplateService.printAndDown(params, response);
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }

    @Override
    public GridService<PeStudent> getGridService() {
        return this.peBusinessEnrollInfoService;
    }
}
