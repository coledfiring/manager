package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.TrainingTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.TRAINING_TYPE_BASIC_SQL;

/**
 * 培训类型管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/trainingTypeManage")
@BasicOperateRecord("培训类型")
@SqlRecord(namespace = "trainingType", sql = TRAINING_TYPE_BASIC_SQL)
public class TrainingTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "trainingTypeManageService")
    private TrainingTypeManageServiceImpl trainingTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.trainingTypeManageService;
    }
}
