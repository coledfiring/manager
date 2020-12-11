package com.whaty.products.controller.resource;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeCourse;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.resource.impl.CourseResourceManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.COURSE_RESOURCE_BASIC_INFO;

/**
 * 课程资源controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/resource/courseResourceManage")
@BasicOperateRecord(value = "课程资源管理")
@SqlRecord(namespace = "peCourse", sql = COURSE_RESOURCE_BASIC_INFO)
public class CourseResourceManageController extends TycjGridBaseControllerAdapter<PeCourse> {

    @Resource(name = "courseResourceManageService")
    private CourseResourceManageServiceImpl courseResourceManageService;

    /**
     * 查询课程照片
     * @param courseId 课程id
     * @return 课程图片地址、课程名称
     */
    @RequestMapping("/showPhoto/{courseId}")
    public ResultDataModel showPhoto(@PathVariable String courseId){
        Map<String, Object> photoMap = courseResourceManageService.getPhotoPreviewList(courseId);
        return ResultDataModel.handleSuccessResult(photoMap);
    }

    /**
     * 获取课程图片
     * @return
     */
    @RequestMapping(value = "/courseImage/{courseId}", method = RequestMethod.GET)
    public ResultDataModel getCourseImage(@PathVariable String courseId) {
        return ResultDataModel.handleSuccessResult(this.courseResourceManageService.getCourseImage(courseId));
    }

    /**
     * 上传课程图片
     * @return
     */
    @RequestMapping("/uploadCourseImage")
    public ResultDataModel uploadCourseImage(@RequestParam(CommonConstant.PARAM_IDS) String ids,
                                           @RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        this.courseResourceManageService
                .doUploadCourseImage(ids, CommonUtils.convertMultipartFileToFile(upload), upload.getOriginalFilename());
        return ResultDataModel.handleSuccessResult("上传成功");
    }

    @Override
    public GridService<PeCourse> getGridService() {
        return this.courseResourceManageService;
    }

}
