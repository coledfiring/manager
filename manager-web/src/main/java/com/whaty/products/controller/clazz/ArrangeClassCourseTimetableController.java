package com.whaty.products.controller.clazz;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ClassCourseTimetable;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.clazz.constants.ClazzConstants;
import com.whaty.products.service.clazz.impl.ArrangeClassCourseTimetableServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.ARRANGE_CLASS_COURSE_TIMETABL_BASIC_SQL;

/**
 * 安排班级课程
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/arrangeClassCourseTimetable")
@BasicOperateRecord("安排班级课程")
@SqlRecord(namespace = "arrangeClassCourseTimetable", sql = ARRANGE_CLASS_COURSE_TIMETABL_BASIC_SQL)
public class ArrangeClassCourseTimetableController extends TycjGridBaseControllerAdapter<ClassCourseTimetable> {

    @Resource(name = "arrangeClassCourseTimetableService")
    private ArrangeClassCourseTimetableServiceImpl arrangeClassCourseTimetableService;

    /**
     * 列举所有关联附件
     * @return
     */
    @RequestMapping("/listAttachFiles")
    public ResultDataModel listAttachFiles(@RequestBody ParamsDataModel paramsDataModel) {
        String courseId = this.getIds(paramsDataModel);
        return ResultDataModel.handleSuccessResult(this.arrangeClassCourseTimetableService.listAttachFiles(courseId));
    }

    /**
     * 设置课件
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setCourseware")
    public ResultDataModel setCourseware(@RequestBody ParamsDataModel paramsDataModel) {
        String attachId = paramsDataModel.getStringParameter("attachId");
        this.arrangeClassCourseTimetableService.doSetCourseware(this.getIds(paramsDataModel), attachId);
        return ResultDataModel.handleSuccessResult("设置课件成功");
    }

    /**
     * 初始化课程表
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/initCourseTimeTable")
    public ResultDataModel initCourseTimeTable(@RequestBody ParamsDataModel paramsDataModel) {
        int count = this.arrangeClassCourseTimetableService
                .doInitCourseTimeTable(paramsDataModel.getStringParameter(CommonConstant.PARAM_PARENT_ID));
        return ResultDataModel.handleSuccessResult("初始化课程表成功，共新增" + count + "门课程");
    }

    /**
     * 下载导入课程表模板
     *
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/downloadCourseTimeTableTemplate")
    public void downloadCourseTimeTableTemplate(HttpServletResponse response,
                                                @RequestParam(CommonConstant.PARAM_PARENT_ID) String parentId)
            throws IOException, ServletException {
        this.downFile(response, e -> arrangeClassCourseTimetableService
                .downloadCourseTimeTableTemplate(parentId, e), "courseTimetable.xls");
    }

    /**
     * 导入课程表
     *
     * @param upload
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadCourseTimeTableTemplate")
    public ResultDataModel uploadCourseTimeTableTemplate(@RequestParam(CommonConstant.PARAM_FILE_UPLOAD) MultipartFile upload,
                                                         @RequestParam(CommonConstant.PARAM_PARENT_ID) String parentId)
            throws Exception {
        Map<String, Object> resultMap = this.arrangeClassCourseTimetableService
                .doUploadCourseTimeTableTemplate(parentId, CommonUtils.convertMultipartFileToFile(upload));
        NoticeServerPollUtils.noticeUploadError(resultMap);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 获取课程教师信息
     *
     * @return
     */
    @RequestMapping(value = "/listCourseTeacher", method = RequestMethod.GET)
    public ResultDataModel listCourseTeacher(@RequestParam(ClazzConstants.PARAM_QUERY) String query)
            throws UnsupportedEncodingException {
        query = URLDecoder.decode(query,"UTF-8");
        return ResultDataModel.handleSuccessResult(this.arrangeClassCourseTimetableService.listCourseTeacher(query));
    }

    /**
     * 添加课程表
     *
     * @param classCourseTimetable
     * @return
     */
    @RequestMapping("/addCourseTimetable")
    public ResultDataModel addCourseTimetable(@RequestBody ClassCourseTimetable classCourseTimetable) {
        this.arrangeClassCourseTimetableService
                .doAddClassTimetable(classCourseTimetable);
        return ResultDataModel.handleSuccessResult("添加课程表成功！");
    }

    /**
     * 修改课程表
     *
     * @param classCourseTimetable
     * @return
     */
    @RequestMapping("/updateCourseTimetable")
    public ResultDataModel updateCourseTimetable(@RequestBody ClassCourseTimetable classCourseTimetable) {
        this.arrangeClassCourseTimetableService
                .doAddClassTimetable(classCourseTimetable);
        return ResultDataModel.handleSuccessResult("修改课程表成功！");
    }

    @Override
    public GridService<ClassCourseTimetable> getGridService() {
        return this.arrangeClassCourseTimetableService;
    }
}
