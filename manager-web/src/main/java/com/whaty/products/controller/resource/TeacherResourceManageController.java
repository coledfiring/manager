package com.whaty.products.controller.resource;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeTeacher;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.common.template.AbstractZipReadTemplate;
import com.whaty.products.service.resource.impl.TeacherResourceManageServiceImpl;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.PRINTING_BASIC_INFO;
import static com.whaty.constant.SqlRecordConstants.TEACHER_RESOURCE_BASIC_INFO;

/**
 * 师资管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/resource/teacherResourceManage")
@BasicOperateRecord(value = "师资管理")
@SqlRecord(namespace = "peTeacher", sql = TEACHER_RESOURCE_BASIC_INFO)
public class TeacherResourceManageController extends TycjGridBaseControllerAdapter<PeTeacher> {

    @Resource(name = "teacherResourceManageService")
    private TeacherResourceManageServiceImpl teacherResourceManageService;

    /**
     * 重置密码
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/resetPassword")
    public ResultDataModel resetPassword(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        int count = this.teacherResourceManageService.doResetPassword(ids, this.getSite());
        int total = ids.split(CommonConstant.SPLIT_ID_SIGN).length;
        NoticeServerPollUtils.selfNotice("重置教师密码成功，共" + total + "条记录，成功" + count + "条",
                NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
        return ResultDataModel.handleSuccessResult("重置成功，共" + total + "条记录，成功" + count + "条");
    }

    /**
     * 上传教师图片
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/uploadPicture")
    public ResultDataModel uploadPicture(@RequestBody ParamsDataModel paramsDataModel) {
        String upload = paramsDataModel.getStringParameter(CommonConstant.PARAM_UPLOAD);
        TycjParameterAssert.isAllNotBlank(upload);
        Map<String, Integer> numMap = this.teacherResourceManageService
                .doUploadPicture(new File(CommonUtils.getRealPath(upload)));
        int total = numMap.get(AbstractZipReadTemplate.RESULT_TOTAL_NUM);
        int success = numMap.get(AbstractZipReadTemplate.RESULT_SUCCESS_NUM);
        return ResultDataModel.handleSuccessResult("上传成功，共" + total + "张图片，成功" + success + "张"
                + (total <= success ? "" : "，有部分图片上传失败，详情请查看通知推送"));
    }

    /**
     * 同步教师数据
     * @param paramsData
     * @return
     */
    @RequestMapping("/syncData")
    public ResultDataModel syncData(@RequestBody ParamsDataModel paramsData) {
        String ids = this.getIds(paramsData.getParams());
        this.teacherResourceManageService.doSyncData(ids);
        return ResultDataModel.handleSuccessResult("同步操作成功！");
    }

    /**
     * 生成头像
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/generateProfilePicture")
    public ResultDataModel generateProfilePicture(@RequestBody ParamsDataModel paramsDataModel) {
        this.teacherResourceManageService.doGenerateProfilePicture(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("生成成功");
    }

    /**
     * 解绑微信用户
     *
     * @param paramsData
     * @return
     */
    @RequestMapping({"/unbindWeChatUser"})
    @OperateRecord(value = "解绑微信用户", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "peTeacher", sql = TEACHER_RESOURCE_BASIC_INFO)
    public ResultDataModel unbindWeChatUser(@RequestBody ParamsDataModel paramsData) {
        int count = this.teacherResourceManageService.doUnbindWeChatUser(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功解绑" + count + "位微信教师用户");
    }

    /**
     * 解绑微信小程序用户
     * @param paramsData
     * @return
     */
    @RequestMapping("/unbindWeChatAppUser")
    @OperateRecord(value = "解绑微信小程序用户", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "peTeacher", sql = TEACHER_RESOURCE_BASIC_INFO)
    public ResultDataModel unbindWeChatAppUser(@RequestBody ParamsDataModel paramsData) {
        int count = this.teacherResourceManageService.doUnbindWeChatAppUser(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功解绑" + count + "位教师");
    }

    @Override
    public GridService<PeTeacher> getGridService() {
        return this.teacherResourceManageService;
    }
}
