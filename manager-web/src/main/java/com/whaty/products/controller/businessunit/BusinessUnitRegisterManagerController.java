package com.whaty.products.controller.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.businessunit.BusinessUnitRegisterManagerServiceImpl;
import com.whaty.products.service.businessunit.domain.EnrollStudentInfParameter;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 单位报名信息管理
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/businessUnitRegisterManager")
public class BusinessUnitRegisterManagerController extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "businessUnitRegisterManagerService")
    private BusinessUnitRegisterManagerServiceImpl businessUnitRegisterManagerService;

    /**
     * 单个学生报名时需要的信息
     *
     * @return
     */
    @GetMapping(value = "/enrollInfo")
    @OperateRecord(value = "单个学生报名时需要的信息",
            moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel enrollInfo() {
        return ResultDataModel.handleSuccessResult(businessUnitRegisterManagerService.getEnrollInfo());
    }

    /**
     * 列出可以用来报名的培训班级信息
     *
     * @return
     */
    @RequestMapping(value = "/listCanEnrollClassInfo")
    @OperateRecord(value = "列出可以用来报名的培训班级信息",
            moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel listCanEnrollClassInfo() {
        return ResultDataModel.handleSuccessResult(businessUnitRegisterManagerService.listCanEnrollClassInfo());
    }

    /**
     * 单个学生单位报名
     *
     * @return
     */
    @PostMapping(value = "/singleBusinessEnrollInfo")
    @OperateRecord(value = "单个学生单位报名", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel saveSingleBusinessEnrollInfo(@RequestBody EnrollStudentInfParameter enrollStudentInfParameter) {
        businessUnitRegisterManagerService.saveSingleBusinessEnrollInfo(enrollStudentInfParameter);
        return ResultDataModel.handleSuccessResult("报名成功");
    }

    /**
     * 取消报名
     *
     * @return
     */
    @RequestMapping(value = "/cancelEnroll")
    @OperateRecord(value = "取消报名", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel cancelEnroll(@RequestBody ParamsDataModel paramsDataModel) {
        businessUnitRegisterManagerService.doCancelEnroll(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("取消报名成功");
    }

    /**
     * 下载导入报名信息模板
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/downloadEnrollTemplate")
    public void downloadEnrollTemplate(HttpServletResponse response)
            throws IOException, ServletException {
        this.downFile(response, businessUnitRegisterManagerService::downloadEnrollTemplate, "studentEnroll.xls");
    }

    /**
     * 批量导入报名
     * @param upload
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadStudentEnrollInfo")
    public ResultDataModel uploadStudentEnrollInfo(@RequestParam(CommonConstant.PARAM_FILE_UPLOAD) MultipartFile upload)
            throws Exception {
        Map<String, Object> resultMap = this.businessUnitRegisterManagerService
                .doUploadStudentEnrollInfo(CommonUtils.convertMultipartFileToFile(upload));
        NoticeServerPollUtils.noticeUploadError(resultMap);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 获取期次费用信息
     * @param classId
     * @return
     */
    @GetMapping(value = "/classFeeInfo")
    @OperateRecord(value = "获取期次费用信息", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel classFeeInfo(@RequestParam("classId") String classId) {
        return ResultDataModel.handleSuccessResult(businessUnitRegisterManagerService.getClassFeeInfo(classId));
    }

    @Override
    public GridService<PeStudent> getGridService() {
        return this.businessUnitRegisterManagerService;
    }
}
