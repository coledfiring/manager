package com.whaty.products.controller.oltrain.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.basic.impl.OLCourseTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 课程类型管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/basic/onlineCourseTypeManage")
@BasicOperateRecord("课程类型")
public class OnlineCourseTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "olCourseTypeManageService")
    private OLCourseTypeManageServiceImpl olCourseTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.olCourseTypeManageService;
    }
}
