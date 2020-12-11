package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.TrainingModeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.TRAINING_MODE_BASIC_SQL;

/**
 * 培训形式管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/trainingModeManage")
@BasicOperateRecord("培训形式")
@SqlRecord(namespace = "trainingMode", sql = TRAINING_MODE_BASIC_SQL)
public class TrainingModeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "trainingModeManageService")
    private TrainingModeManageServiceImpl trainingModeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.trainingModeManageService;
    }
}
