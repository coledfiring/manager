package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.TrainingTargetManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.TRAINING_TARGET_BASIC_SQL;

/**
 * 培训对象管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/trainingTargetManage")
@BasicOperateRecord("培训对象")
@SqlRecord(namespace = "trainingTarget", sql = TRAINING_TARGET_BASIC_SQL)
public class TrainingTargetManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "trainingTargetManageService")
    private TrainingTargetManageServiceImpl trainingTargetManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.trainingTargetManageService;
    }
}
