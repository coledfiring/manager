package com.whaty.products.controller.resource;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeCourse;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.resource.impl.CourseResourceManageBaseServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.COURSE_RESOURCE_BASIC_INFO;

/**
 * 面授课程资源controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/resource/faceTeachingCourseResourceManage")
@BasicOperateRecord(value = "面授课程资源管理")
@SqlRecord(namespace = "peCourse", sql = COURSE_RESOURCE_BASIC_INFO)
public class FaceTeachingCourseResourceManageController extends TycjGridBaseControllerAdapter<PeCourse> {

    @Resource(name = "courseResourceManageBaseService")
    private CourseResourceManageBaseServiceImpl courseResourceManageBaseService;

    /**
     * 查询课程照片
     *
     * @param courseId 课程id
     * @return 课程图片地址、课程名称
     */
    @RequestMapping("/showPhoto/{courseId}")
    public ResultDataModel showPhoto(@PathVariable String courseId) {
        Map<String, Object> photoMap = courseResourceManageBaseService.getPhotoPreviewList(courseId);
        return ResultDataModel.handleSuccessResult(photoMap);
    }

    /**
     * 获取课程图片
     *
     * @return
     */
    @RequestMapping(value = "/courseImage/{courseId}", method = RequestMethod.GET)
    public ResultDataModel getCourseImage(@PathVariable String courseId) {
        return ResultDataModel.handleSuccessResult(this.courseResourceManageBaseService.getCourseImage(courseId));
    }

    /**
     * 上传课程图片
     *
     * @return
     */
    @RequestMapping("/uploadCourseImage")
    public ResultDataModel uploadCourseImage(@RequestParam(CommonConstant.PARAM_IDS) String ids,
                                             @RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        this.courseResourceManageBaseService
                .doUploadCourseImage(ids, CommonUtils.convertMultipartFileToFile(upload), upload.getOriginalFilename());
        return ResultDataModel.handleSuccessResult("上传成功");
    }

    @Override
    public GridService<PeCourse> getGridService() {
        return this.courseResourceManageBaseService;
    }

}
