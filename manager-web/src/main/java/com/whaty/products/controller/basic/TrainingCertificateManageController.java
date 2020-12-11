package com.whaty.products.controller.basic;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingCertificate;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.TrainingCertificateManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.TRAINING_CERTIFICATE_BASIC_SQL;

/**
 * 培训证书管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/trainingCertificateManage")
@BasicOperateRecord("培训证书")
@SqlRecord(namespace = "trainingCertificate", sql = TRAINING_CERTIFICATE_BASIC_SQL)
public class TrainingCertificateManageController extends TycjGridBaseControllerAdapter<TrainingCertificate> {

    @Resource(name = "trainingCertificateManageService")
    private TrainingCertificateManageServiceImpl trainingCertificateManageService;

    @Override
    public GridService<TrainingCertificate> getGridService() {
        return this.trainingCertificateManageService;
    }
}
