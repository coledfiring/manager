package com.whaty.products.controller.oltrain.clazz;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlPeStudent;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.clazz.impl.OLClassStudentSearchServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 学员查询controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olClassStudentSearch")
@BasicOperateRecord("学员查询")
public class OLClassStudentSearchController extends TycjGridBaseControllerAdapter<OlPeStudent> {

    @Resource(name = "olClassStudentManageService")
    private OLClassStudentSearchServiceImpl olClassStudentSearchService;

    @Override
    public GridService<OlPeStudent> getGridService() {
        return this.olClassStudentSearchService;
    }
}
