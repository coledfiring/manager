package com.whaty.products.controller.enroll;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.enroll.impl.StudentEnrollInfoManageServiceImpl;
import com.whaty.products.service.enroll.constant.StudentEnrollConstant;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
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

import static com.whaty.constant.SqlRecordConstants.ENROLL_INFO_MANAGE_INFO;

/**
 * 学生报名信息管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/studentEnrollInfoManage")
public class StudentEnrollInfoManageController extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "studentEnrollInfoManageService")
    private StudentEnrollInfoManageServiceImpl studentEnrollInfoManageService;

    /**
     * 交费
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/payFee")
    @OperateRecord(value = "报名管理员交费", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "studentEnrollInfo", sql = ENROLL_INFO_MANAGE_INFO)
    public ResultDataModel payFee(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        this.studentEnrollInfoManageService.doPayFee(ids);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 取消交费
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/cancelPay")
    @OperateRecord(value = "报名管理员取消交费", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "studentEnrollInfo", sql = ENROLL_INFO_MANAGE_INFO)
    public ResultDataModel cancelPay(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        this.studentEnrollInfoManageService.doCancelPay(ids);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 退学
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/quitSchool")
    @OperateRecord(value = "报名管理退学", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "studentEnrollInfo", sql = ENROLL_INFO_MANAGE_INFO)
    public ResultDataModel quitSchool(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        this.studentEnrollInfoManageService.doQuitSchool(ids);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 获取勾选学生分班数据
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/getSelectedArrangeClazz")
    public ResultDataModel getSelectedArrangeClazz(@RequestParam(StudentEnrollConstant.PARAMS_ITEM_ID) String itemId) {
        return ResultDataModel.handleSuccessResult(this.studentEnrollInfoManageService.getSelectedArrangeClazz(itemId));
    }


    /**
     * 学生分班
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/arrangeStudentClass")
    @OperateRecord(value = "报名管理分班", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "studentEnrollInfo", sql = ENROLL_INFO_MANAGE_INFO)
    public ResultDataModel arrangeStudentClass(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String clazzId = paramsDataModel.getStringParameter(StudentEnrollConstant.PARAMS_CLAZZ_ID);
        this.studentEnrollInfoManageService.doArrangeStudentClass(ids, clazzId);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 取消学生分班
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/cancelArrangeStudentClass")
    @OperateRecord(value = "报名管理取消分班", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "studentEnrollInfo", sql = ENROLL_INFO_MANAGE_INFO)
    public ResultDataModel cancelArrangeStudentClass(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        this.studentEnrollInfoManageService.doCancelArrangeStudentClass(ids);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 下载导入票据号模版
     *
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/downloadInvoiceNoTemplate")
    public void downloadFeeInfoTemplate(HttpServletResponse response)
            throws IOException, ServletException {
        this.downFile(response, studentEnrollInfoManageService::downloadInvoiceNoTemplate, "invoiceNo.xls");
    }

    /**
     * 导入票据号
     *
     * @param upload
     * @return
     * @throws IOException
     */
    @RequestMapping("/uploadStudentInvoiceNo")
    public ResultDataModel uploadStudentInvoiceNo(@RequestParam(CommonConstant.PARAM_FILE_UPLOAD) MultipartFile upload)
            throws Exception {
        Map<String, Object> resultMap = this.studentEnrollInfoManageService
                .doUploadStudentInvoiceNo(CommonUtils.convertMultipartFileToFile(upload));
        NoticeServerPollUtils.noticeUploadError(resultMap);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 汇款单审核
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/checkMoneyOrder")
    public ResultDataModel checkMoneyOrder(@RequestBody ParamsDataModel paramsDataModel) {
        int count = this.studentEnrollInfoManageService.doCheckMoneyOrder(this.getIds(paramsDataModel) ,
                paramsDataModel.getStringParameter("checkId"),
                paramsDataModel.getStringParameter("checkReason"));
        NoticeServerPollUtils.selfNotice("汇款单审批成功，共审批" + count + "条数据",
                NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
        return ResultDataModel.handleSuccessResult("汇款单审批成功，共审批" + count + "条数据");
    }

    /**
     * 调整期次
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/changeClass")
    public ResultDataModel changeClass(@RequestBody ParamsDataModel paramsDataModel) {
        int count = this.studentEnrollInfoManageService.doChangeClass(this.getIds(paramsDataModel) ,
                paramsDataModel.getStringParameter("checkId"));
        NoticeServerPollUtils.selfNotice("调整期次成功，共调整" + count + "条数据",
                NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
        return ResultDataModel.handleSuccessResult("调整期次成功，共调整" + count + "条数据");
    }

    /**
     * 线下缴费
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/underlinePayFee")
    public ResultDataModel underlinePayFee(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        this.studentEnrollInfoManageService.doUnderlinePayFee(ids);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 设置报名费
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setEnrollFee")
    @OperateRecord(value = "报名管理设置报名费", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "studentEnrollInfo", sql = ENROLL_INFO_MANAGE_INFO)
    public ResultDataModel setEnrollFee(@RequestBody ParamsDataModel paramsDataModel) {
        int count = this.studentEnrollInfoManageService.doSetEnrollFee(this.getIds(paramsDataModel),
                paramsDataModel.getStringParameter(StudentEnrollConstant.PARAMS_ENROLL_FEE));
        NoticeServerPollUtils.selfNotice("设置报名费成功，共成功为" + count + "位学生重新设置报名费",
                NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
        return ResultDataModel.handleSuccessResult("设置报名费成功，共成功为" + count + "位学生重新设置报名费");
    }

    @Override
    public GridService<PeStudent> getGridService() {
        return this.studentEnrollInfoManageService;
    }
}
