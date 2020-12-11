package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.InfoSourceManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.INFO_SOURCE_BASIC_SQL;

/**
 * 信息来源管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/infoSourceManage")
@BasicOperateRecord("信息来源")
@SqlRecord(namespace = "infoSource", sql = INFO_SOURCE_BASIC_SQL)
public class InfoSourceManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "infoSourceManageService")
    private InfoSourceManageServiceImpl infoSourceManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.infoSourceManageService;
    }
}