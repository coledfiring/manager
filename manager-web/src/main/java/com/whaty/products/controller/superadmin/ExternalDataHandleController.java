package com.whaty.products.controller.superadmin;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.superadmin.ExternalDataHandleService;
import com.whaty.products.service.superadmin.constant.SuperAdminConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 超管端外部数据处理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController("externalDataHandleController")
@RequestMapping("/entity/superAdmin/externalDataHandle")
public class ExternalDataHandleController {

    @Resource(name = "externalDataHandleService")
    private ExternalDataHandleService externalDataHandleService;

    /**
     * 删除课程空间课程
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/removeLearnSpaceCourse", method = RequestMethod.POST)
    public ResultDataModel removeLearnSpaceCourse(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.removeLearnSpaceCourse(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 同步课程到课程空间
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/syncLearnSpaceCourse", method = RequestMethod.POST)
    public ResultDataModel syncLearnSpaceCourse(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.syncLearnSpaceCourse(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 删除选课
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/removeTchElective", method = RequestMethod.POST)
    public ResultDataModel removeTchElective(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.removeTchElective(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 删除教师-课程关联关系
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/removeTeacherCourseRel", method = RequestMethod.POST)
    public ResultDataModel removeTeacherCourseRel(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.removeLearnSpaceCourseTeacherRel(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 同步教师-课程关联关系
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/syncTeacherCourseRel", method = RequestMethod.POST)
    public ResultDataModel syncTeacherCourseRel(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.syncLearnSpaceCourseTeacherRel(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 删除课程空间选课
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/removeLearnSpaceElective", method = RequestMethod.POST)
    public ResultDataModel removeLearnSpaceElective(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.removeLearnSpaceElective(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 同步课程空间选课
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/syncLearnSpaceElective", method = RequestMethod.POST)
    public ResultDataModel syncLearnSpaceElective(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.syncLearnSpaceElective(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 删除课程空间用户
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/removeLearnSpaceUser", method = RequestMethod.POST)
    public ResultDataModel removeLearnSpaceUser(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        String userType = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_USER_TYPE);
        this.externalDataHandleService.removeLearnSpaceUser(searchSql, ids, userType, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 同步用户到课程空间
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/syncLearnSpaceUser", method = RequestMethod.POST)
    public ResultDataModel syncLearnSpaceUser(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        String userType = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_USER_TYPE);
        this.externalDataHandleService.syncLearnSpaceUser(searchSql, ids, userType, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 删除用户到中心用户
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/removeCenterUser", method = RequestMethod.POST)
    public ResultDataModel removeCenterUser(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        String userType = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_USER_TYPE);
        this.externalDataHandleService.removeUCenterUser(searchSql, ids, userType, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 同步用户到中心用户
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/syncCenterUser", method = RequestMethod.POST)
    public ResultDataModel syncCenterUser(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        String userType = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_USER_TYPE);
        this.externalDataHandleService.syncUCenterUser(searchSql, ids, userType, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 同步文件到idocv
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/syncToIdocv", method = RequestMethod.POST)
    public ResultDataModel syncToIdocv(@RequestBody ParamsDataModel paramsDataModel) {
        String searchSql = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SEARCH_SQL);
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        String siteCode = paramsDataModel.getStringParameter(SuperAdminConstant.PARAM_SITE_CODE);
        this.externalDataHandleService.doSyncToIdocv(searchSql, ids, siteCode);
        return ResultDataModel.handleSuccessResult();
    }

}
