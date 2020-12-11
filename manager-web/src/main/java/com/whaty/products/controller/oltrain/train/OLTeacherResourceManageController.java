package com.whaty.products.controller.oltrain.train;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlPeTeacher;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.train.impl.OLTeacherResourceManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 课程资源管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olTeacherResourceManage")
@BasicOperateRecord("课程资源管理")
public class OLTeacherResourceManageController extends TycjGridBaseControllerAdapter<OlPeTeacher> {

    @Resource(name = "olTeacherResourceManageService")
    private OLTeacherResourceManageServiceImpl olTeacherResourceManageService;

    @Override
    public GridService<OlPeTeacher> getGridService() {
        return this.olTeacherResourceManageService;
    }
}
