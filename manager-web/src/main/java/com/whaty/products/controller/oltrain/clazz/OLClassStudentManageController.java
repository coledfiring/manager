package com.whaty.products.controller.oltrain.clazz;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlClassStudent;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.clazz.impl.OLClassStudentManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 学员管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olClassStudentManage")
@BasicOperateRecord("学员管理")
public class OLClassStudentManageController extends TycjGridBaseControllerAdapter<OlClassStudent> {

    @Resource(name = "olClassStudentManageService")
    private OLClassStudentManageServiceImpl olClassStudentManageService;

    @Override
    public GridService<OlClassStudent> getGridService() {
        return this.olClassStudentManageService;
    }
}
