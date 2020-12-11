package com.whaty.products.controller.hbgr.device;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeDeviceExperience;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeDeviceExperienceServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * author weipengsen  Date 2020/6/20
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peDeviceExperienceManage")
public class PeDeviceExperienceManageController extends TycjGridBaseControllerAdapter<PeDeviceExperience> {

    @Resource(name = "peDeviceExperienceService")
    private PeDeviceExperienceServiceImpl peDeviceExperienceService;

    @Override
    public GridService<PeDeviceExperience> getGridService() {
        return this.peDeviceExperienceService;
    }

    /**
     * 添加维修经验
     *
     * @param paramsDataModel
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/addExperience")
    public ResultDataModel addExperience(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String experience = paramsDataModel.getStringParameter("experience");
        String reason = paramsDataModel.getStringParameter("reason");
        this.peDeviceExperienceService.addExperience(ids, experience, reason);
        return ResultDataModel.handleSuccessResult("添加成功");
    }
}
