package com.whaty.products.controller.basic;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.EntrustedUnitLinkman;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.EntrustedUnitLinkmanManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.ENTRUSTED_UNIT_LINKMAN_BASIC_SQL;

/**
 * 委托单位联系人管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/entrustedUnitLinkmanManage")
@BasicOperateRecord("委托单位联系人")
@SqlRecord(namespace = "entrustedUnitLinkman", sql = ENTRUSTED_UNIT_LINKMAN_BASIC_SQL)
public class EntrustedUnitLinkmanManageController extends TycjGridBaseControllerAdapter<EntrustedUnitLinkman> {

    @Resource(name = "entrustedUnitLinkmanManageService")
    private EntrustedUnitLinkmanManageServiceImpl entrustedUnitLinkmanManageService;

    @Override
    public GridService<EntrustedUnitLinkman> getGridService() {
        return this.entrustedUnitLinkmanManageService;
    }
}
