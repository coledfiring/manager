package com.whaty.products.controller.enroll;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ExamReserve;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.enroll.impl.ExamReserveServiceImpl;
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

/**
 * 考点管理controller
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/examReserve")
public class ExamReserveController extends TycjGridBaseControllerAdapter<ExamReserve> {

    @Resource(name = "examReserveService")
    private ExamReserveServiceImpl examReserveService;

    /**
     * 审核考生报名通过
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/checkEnrollPass")
    @OperateRecord(value = "审核考生报名通过", moduleCode = OperateRecordModuleConstant.ENROLL_EXAM_CODE)
    public ResultDataModel checkEnrollPass(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult(this.examReserveService.doCheckEnrollPass(this.getIds(paramsDataModel)));
    }

    /**
     * 审核考生报名不通过
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/checkEnrollNoPass")
    @OperateRecord(value = "审核考生报名不通过", moduleCode = OperateRecordModuleConstant.ENROLL_EXAM_CODE)
    public ResultDataModel checkEnrollNoPass(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult(this.examReserveService.doCheckEnrollNoPass(this.getIds(paramsDataModel)));
    }

    /**
     * 考场信息导入模版下载
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/downloadExamInfoNoTemplate")
    public void downloadFeeInfoTemplate(HttpServletResponse response)
            throws IOException, ServletException {
        this.downFile(response, examReserveService::downloadExamInfoNoTemplate, "examInfo.xls");
    }

    /**
     * 考场信息导入
     *
     * @param upload
     * @return
     * @throws IOException
     */
    @RequestMapping("/uploadExamInfo")
    public ResultDataModel uploadExamInfo(@RequestParam(CommonConstant.PARAM_FILE_UPLOAD) MultipartFile upload)
            throws Exception {
        Map<String, Object> resultMap = examReserveService.doUploadExamInfo(CommonUtils.convertMultipartFileToFile(upload));
        NoticeServerPollUtils.noticeUploadError(resultMap);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 一键生成准考证号
     * @return
     */
    @RequestMapping("/generateExamCardNo")
    @OperateRecord(value = "一键生成准考证号", moduleCode = OperateRecordModuleConstant.ENROLL_EXAM_CODE)
    public ResultDataModel generateExamCardNo() {
        return ResultDataModel.handleSuccessResult("生成准考证号成功，共操作" +
                this.examReserveService.doGenerateExamCardNo() + "条数据");
    }

    /**
     * 删除学生报名信息
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/deleteExamStudentInfo")
    @OperateRecord(value = "删除学生报名信息", moduleCode = OperateRecordModuleConstant.ENROLL_EXAM_CODE)
    public ResultDataModel deleteExamStudentInfo(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult("删除考生报名信息成功，共操作" + examReserveService.
                deleteExamStudentInfo(this.getIds(paramsDataModel)) + "条数据");
    }

    @Override
    public GridService<ExamReserve> getGridService() {
        return this.examReserveService;
    }
}
