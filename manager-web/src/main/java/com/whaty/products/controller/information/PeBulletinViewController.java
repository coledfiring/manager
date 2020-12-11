package com.whaty.products.controller.information;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.products.service.information.constant.InformationConstant;
import com.whaty.products.service.information.impl.PeBulletinViewServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 查看公告信息controller
 *
 * @author weipengsen
 */
@Lazy
@Controller("peBulletinViewController")
@RequestMapping("/entity/information/peBulletinView")
public class PeBulletinViewController extends GridBaseController {

    @Resource(name = "peBulletinViewService")
    private PeBulletinViewServiceImpl peBulletinViewService;

    /**
     * 查看公告详情
     *
     * @return
     */
    @RequestMapping(value = "/viewDetail", method = RequestMethod.GET)
    public ResultDataModel viewDetail(@RequestParam(CommonConstant.PARAM_IDS) String ids) {
        Map<String, Object> items = this.peBulletinViewService.viewDetail(ids);
        return ResultDataModel.handleSuccessResult(items);
    }

    /**
     * 设置公告为已读
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/setBulletIsRead")
    public ResultDataModel setBulletinIsRead(@RequestParam(CommonConstant.PARAM_IDS) String ids) {
        this.peBulletinViewService.setIsRead(ids);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 获取下拉选项
     *
     * @return
     */
    @RequestMapping(value = "/getAllSelectData", method = RequestMethod.GET)
    public ResultDataModel getAllSelectData() {
        Map<String, Object> items = this.peBulletinViewService.getAllSelectData();
        return ResultDataModel.handleSuccessResult(items);
    }

    /**
     * 保存公告
     *
     * @return
     */
    @RequestMapping(value = "/saveBulletin", method = RequestMethod.POST)
    public ResultDataModel saveBulletin(@RequestBody ParamsDataModel paramsDataModel) {
        Map<String, Object> formMap = paramsDataModel.getParams();
        this.peBulletinViewService.saveBulletin(formMap);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 获取公告信息
     *
     * @return
     */
    @RequestMapping(value = "/getBulletinInfo", method = RequestMethod.GET)
    public ResultDataModel getBulletinInfo(@RequestParam(InformationConstant.PARAM_BULLETIN_ID) String id) {
        return ResultDataModel.handleSuccessResult(this.peBulletinViewService.getBulletinInfo(id));
    }

    /**
     * 列出可选班级
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/class", method = RequestMethod.POST)
    public ResultDataModel listClasses(@RequestBody ParamsDataModel paramsDataModel) {
        String search = paramsDataModel.getStringParameter("search");
        Integer currentPage = (Integer) paramsDataModel.getParameter("currentPage");
        Integer pageSize = (Integer) paramsDataModel.getParameter("pageSize");
        return ResultDataModel.handleSuccessResult(this.peBulletinViewService
                .listClasses(search, currentPage, pageSize));
    }

    @Override
    public GridService getGridService() {
        return this.peBulletinViewService;
    }

}
