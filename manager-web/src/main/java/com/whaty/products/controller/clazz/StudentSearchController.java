package com.whaty.products.controller.clazz;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.StudentSearchServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 学生查询
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/studentSearch")
public class StudentSearchController extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "studentSearchService")
    private StudentSearchServiceImpl studentSearchService;

    /**
     * 生成头像
     * @return
     */
    @RequestMapping("/generateProfilePicture")
    public ResultDataModel generateProfilePicture(@RequestBody ParamsDataModel paramsDataModel) {
        this.studentSearchService.doGenerateProfilePicture(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("生成成功");
    }

    /**
     * 同步学生到im
     *
     * @param paramsDataModel
     * @return
     * @throws Exception
     */
    @RequestMapping("/syncUserToIm")
    public ResultDataModel syncUserToIm(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        int count = this.studentSearchService.syncUserToIm(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("同步成功，共为" + count + "名学生同步信息");
    }

    /**
     * 解绑微信用户
     *
     * @param paramsData
     * @return
     */
    @RequestMapping({"/unbindWeChatUser"})
    public ResultDataModel unbindWeChatUser(@RequestBody ParamsDataModel paramsData) {
        int count = this.studentSearchService.doUnbindWeChatUser(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功解绑" + count + "位微信学生用户");
    }

    /**
     * 解绑微信小程序用户
     * @param paramsData
     * @return
     */
    @RequestMapping("/unbindWeChatAppUser")
    public ResultDataModel unbindWeChatAppUser(@RequestBody ParamsDataModel paramsData) {
        int count = this.studentSearchService.doUnbindWeChatAppUser(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("成功解绑" + count + "位学员");
    }

    @Override
    public GridService<PeStudent> getGridService() {
        return this.studentSearchService;
    }
}
