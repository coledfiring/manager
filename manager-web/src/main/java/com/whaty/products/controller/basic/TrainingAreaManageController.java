package com.whaty.products.controller.basic;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeArea;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.TrainingAreaManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.TRAINING_AREA_BASIC_SQL;

/**
 * 培训地区管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/trainingAreaManage")
@BasicOperateRecord("培训地区")
@SqlRecord(namespace = "trainingArea", sql = TRAINING_AREA_BASIC_SQL)
public class TrainingAreaManageController extends TycjGridBaseControllerAdapter<PeArea> {

    @Resource(name = "trainingAreaManageService")
    private TrainingAreaManageServiceImpl trainingAreaManageService;

    @Override
    public GridService<PeArea> getGridService() {
        return this.trainingAreaManageService;
    }
}
