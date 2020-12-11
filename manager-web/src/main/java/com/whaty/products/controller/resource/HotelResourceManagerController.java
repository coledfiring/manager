package com.whaty.products.controller.resource;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeHotel;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.resource.impl.HotelResourceManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.HOTEL_BASIC_INFO;

/**
 * 合作宾馆controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/resource/hotelResourceManage")
@BasicOperateRecord(value = "合作宾馆管理")
@SqlRecord(namespace = "peHotel", sql = HOTEL_BASIC_INFO)
public class HotelResourceManagerController extends TycjGridBaseControllerAdapter<PeHotel> {

    @Resource(name = "hotelResourceManageService")
    private HotelResourceManageServiceImpl hotelResourceManageService;

    @Override
    public GridService<PeHotel> getGridService() {
        return this.hotelResourceManageService;
    }
}
