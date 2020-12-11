package com.whaty.products.controller.sitemanager;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeManager;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.siteManager.impl.SiteManagerManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.MANAGER_BASIC_SQL;

/**
 * 管理员用户管理controller
 * @author suoqiangqiang
 */
@Lazy
@RequestMapping("/entity/siteManager/siteManagerManage")
@RestController("oldSiteManagerManageController")
@BasicOperateRecord(value = "管理员用户管理",
        moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
@SqlRecord(namespace = "peManager", sql = MANAGER_BASIC_SQL)
public class SiteManagerManageController extends TycjGridBaseControllerAdapter<PeManager> {

    @Resource(name = "siteManagerManageService")
    private SiteManagerManageServiceImpl siteManagerManageService;

    /**
     * 重置密码
     * @param paramsData
     * @return
     */
    @RequestMapping({"/resetUserPwd"})
    @OperateRecord(value = "重置密码",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel resetUserPwd(@RequestBody ParamsDataModel paramsData) {
        int count = this.siteManagerManageService.resetUserPwd(this.getIds(paramsData), this.getSite());
        return ResultDataModel.handleSuccessResult("成功为" + count + "位管理员重置密码！");
    }

    /**
     * 同步管理员数据
     * @param paramsData
     * @return
     */
    @RequestMapping({"/syncData"})
    @ResponseBody
    public ResultDataModel syncData(@RequestBody ParamsDataModel paramsData) {
        String ids = this.getIds(paramsData.getParams());
        this.siteManagerManageService.doSyncData(ids);
        return ResultDataModel.handleSuccessResult("同步操作成功！");
    }

    /**
     * 设为有效
     * @param paramsData
     * @return
     */
    @RequestMapping({"/setActive"})
    @OperateRecord(value = "设为有效",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel setActive(@RequestBody ParamsDataModel paramsData) {
        int count = this.siteManagerManageService.doSetActive(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功为" + count + "位管理员设为有效！");
    }

    /**
     * 设为无效
     * @param paramsData
     * @return
     */
    @RequestMapping({"/setNotActive"})
    @OperateRecord(value = "设为无效",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel setNotActive(@RequestBody ParamsDataModel paramsData) {
        int count = this.siteManagerManageService.doSetNotActive(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功为将" + count + "位管理员设为无效！");
    }

    /**
     * 生成头像
     * @return
     */
    @RequestMapping("/generateProfilePicture")
    public ResultDataModel generateProfilePicture(@RequestBody ParamsDataModel paramsDataModel) {
        this.siteManagerManageService.doGenerateProfilePicture(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("生成成功");
    }

    /**
     * 同步管理员到im
     *
     * @param paramsDataModel
     * @return
     * @throws Exception
     */
    @RequestMapping("/syncUserToIm")
    public ResultDataModel syncUserToIm(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        int count = this.siteManagerManageService.syncUserToIm(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("同步成功，共为" + count + "名管理员同步信息");
    }
    /**
     * 解绑微信用户
     * @param paramsData
     * @return
     */
    @RequestMapping({"/unbindWeChatUser"})
    @OperateRecord(value = "解绑微信用户",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel unbindWeChatUser(@RequestBody ParamsDataModel paramsData) {
        int count = this.siteManagerManageService.doUnbindWeChatUser(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功解绑" + (count > 0 ? count / 2 : 0) + "位管理员用户");
    }

    /**
     * 解绑微信小程序用户
     * @param paramsData
     * @return
     */
    @RequestMapping("/unbindWeChatAppUser")
    @OperateRecord(value = "解绑微信用户",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel unbindWeChatAppUser(@RequestBody ParamsDataModel paramsData) {
        int count = this.siteManagerManageService.doUnbindWeChatAppUser(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功解绑" + count + "位管理员用户");
    }


    @Override
    public GridService<PeManager> getGridService() {
        return this.siteManagerManageService;
    }
}
