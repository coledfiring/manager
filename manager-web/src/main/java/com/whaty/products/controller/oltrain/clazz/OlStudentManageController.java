package com.whaty.products.controller.oltrain.clazz;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlPeStudent;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.clazz.impl.OLStudentManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 学员基本信息管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olStudentManage")
@BasicOperateRecord("学员基本信息管理")
public class OlStudentManageController extends TycjGridBaseControllerAdapter<OlPeStudent> {

    @Resource(name = "olStudentManageService")
    private OLStudentManageServiceImpl olStudentManageService;

    @Override
    public GridService<OlPeStudent> getGridService() {
        return this.olStudentManageService;
    }
}
