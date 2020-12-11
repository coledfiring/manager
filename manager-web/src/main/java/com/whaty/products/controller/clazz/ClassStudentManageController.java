package com.whaty.products.controller.clazz;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.constants.ClazzConstants;
import com.whaty.products.service.clazz.impl.ClassStudentManageServiceImpl;
import com.whaty.products.service.common.template.AbstractZipReadTemplate;
import com.whaty.schedule.util.CommonUtils;
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

import static com.whaty.constant.SqlRecordConstants.CLASS_STUDENT_BASIC_SQL;

/**
 * 班级学员管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/classStudentManage")
@BasicOperateRecord("学员管理")
@SqlRecord(namespace = "classStudent", sql = CLASS_STUDENT_BASIC_SQL)
public class ClassStudentManageController extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "classStudentManageService")
    private ClassStudentManageServiceImpl classStudentManageService;

    /**
     * 上传学员图片
     * @param ids
     * @param upload
     * @param uploadType
     * @return
     */
    @RequestMapping("/uploadPicture")
    public ResultDataModel uploadPicture(@RequestParam(CommonConstant.PARAM_IDS) String ids,
                                         @RequestParam(ClazzConstants.PARAM_UPLOAD_TYPE) String uploadType,
                                         @RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload) {
        try {
            Map<String, Integer> numMap = this.classStudentManageService
                    .doUploadPicture(ids, uploadType, CommonUtils.convertMultipartFileToFile(upload));
            int total = numMap.get(AbstractZipReadTemplate.RESULT_TOTAL_NUM);
            int success = numMap.get(AbstractZipReadTemplate.RESULT_SUCCESS_NUM);
            return ResultDataModel.handleSuccessResult("上传成功，共" + total + "张图片，成功" + success + "张"
                    + (total <= success ? "" : "，有部分图片上传失败，详情请查看通知推送"));
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("文件上传失败");
        }
    }

    /**
     * 生成证书编号
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/generateCertificateNumber")
    public ResultDataModel generateCertificateNumber(@RequestBody ParamsDataModel paramsDataModel) {
        int count = this.classStudentManageService.doGenerateCertificateNumber(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("生成证书编号成功，共为" + count + "名学生生成证书编号");
    }

    /**

     * 导出学员照片
     *
     * @param ids
     * @param response
     * @throws IOException
     */
    @RequestMapping("/exportStudentPicture")
    public void exportStudentPicture(@RequestParam(CommonConstant.PARAM_IDS) String ids, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            this.classStudentManageService.doExportStudentPicture(ids, response);
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }
    /*
     * 导出学员信息
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/exportStuCertificateInfo")
    public void exportStuCertificateInfo(HttpServletResponse response)
            throws IOException, ServletException {
        this.downFile(response, out -> this.classStudentManageService.
                        doExportStuCertificateInfo(this.getRequestMap().get(CommonConstant.PARAM_IDS), out),
                "studentCertificateInfo.xls");
    }

    @Override
    public GridService<PeStudent> getGridService() {
        return this.classStudentManageService;
    }
}
