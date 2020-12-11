package com.whaty.products.controller.resource;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PePlace;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.resource.impl.PlaceResourceManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.PLACE_BASIC_INFO;

/**
 * 培训场地controller
 *
 * @author weipengsen
 *
 */
@Lazy
@RestController
@RequestMapping("/entity/resource/placeResourceManage")
@BasicOperateRecord(value = "培训场地管理")
@SqlRecord(namespace = "pePlace", sql = PLACE_BASIC_INFO)
public class PlaceResourceManageController extends TycjGridBaseControllerAdapter<PePlace> {

    @Resource(name = "placeResourceManageService")
    private PlaceResourceManageServiceImpl placeResourceManageService;

    @Override
    public GridService<PePlace> getGridService() {
        return this.placeResourceManageService;
    }
}
