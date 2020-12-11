package com.whaty.products.controller.sitemanager;

import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.bean.CoreSsoUser;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.core.framework.service.SiteService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.siteManager.impl.SuperAdminSiteManagerManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 站点超管用户管理controller
 * @author suoqiangqiang
 */
@Lazy
@RestController("siteSuperManagerManageController")
@RequestMapping("/superAdmin/siteSuperManager/siteSuperManagerManage")
public class SuperAdminSiteManagerManageController extends TycjGridBaseControllerAdapter<CoreSsoUser> {

    @Resource(name = "superAdminSiteManagerManageService")
    private SuperAdminSiteManagerManageServiceImpl superAdminSiteManagerManageService;

    @Resource(name = "core_siteService")
    private SiteService siteService;

    @Override
    @RequestMapping({"/abstractList"})
    public ResultDataModel abstractList(@RequestBody ParamsDataModel<CoreSsoUser> paramsData) {
        this.changeDatasourceForSite(paramsData);
        return super.abstractList(paramsData);
    }

    @Override
    @RequestMapping({"/abstractAdd"})
    public ResultDataModel abstractAdd(@RequestBody ParamsDataModel<CoreSsoUser> paramsData) {
        this.changeDatasourceForSite(paramsData);
        return super.abstractAdd(paramsData);
    }

    @Override
    @RequestMapping({"/abstractUpdate"})
    public ResultDataModel abstractUpdate(@RequestBody ParamsDataModel<CoreSsoUser> paramsData) {
        this.changeDatasourceForSite(paramsData);
        return super.abstractUpdate(paramsData);
    }

    @Override
    @RequestMapping({"/abstractDelete"})
    public ResultDataModel abstractDelete(@RequestBody ParamsDataModel<CoreSsoUser> paramsData) throws ApiException {
        this.changeDatasourceForSite(paramsData);
        return super.abstractDelete(paramsData);
    }

    @Override
    @RequestMapping({"/abstractDetail"})
    public ResultDataModel abstractDetail(@RequestBody ParamsDataModel<CoreSsoUser> paramsData) throws ApiException {
        this.changeDatasourceForSite(paramsData);
        return super.abstractDetail(paramsData);
    }

    /**
     * 重置密码
     * @param paramsData
     * @return
     */
    @RequestMapping({"/resetUserPwd"})
    @ResponseBody
    public ResultDataModel resetUserPwd(@RequestBody ParamsDataModel paramsData) {
        this.changeDatasourceForSite(paramsData);
        int count = this.superAdminSiteManagerManageService.resetUserPwd(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功为" + count + "位管理员重置密码！");
    }

    /**
     * 切换数据源
     * @param paramsData
     */
    private void changeDatasourceForSite(ParamsDataModel<CoreSsoUser> paramsData) {
        String siteId = paramsData.getStringParameter("siteId");
        Site site = this.siteService.getSiteById(siteId);
        this.changeDatasourceForSite(site);
    }

    /**
     * 切换数据源
     * @param site
     */
    private void changeDatasourceForSite(Site site) {
        MasterSlaveRoutingDataSource.setDbType(site.getDatasourceCode());
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
        int count = this.superAdminSiteManagerManageService.syncUserToIm(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("同步成功，共为" + count + "名管理员同步信息");
    }

    /**
     * 生成头像
     * @return
     */
    @RequestMapping("/generateProfilePicture")
    public ResultDataModel generateProfilePicture(@RequestBody ParamsDataModel paramsDataModel) {
        this.changeDatasourceForSite(paramsDataModel);
        this.superAdminSiteManagerManageService.doGenerateProfilePicture(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("生成成功");
    }

    @Override
    public GridService<CoreSsoUser> getGridService() {
        return this.superAdminSiteManagerManageService;
    }
}
