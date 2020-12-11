package com.whaty.products.controller.clazz;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ClassHotelArrange;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.ClassHotelArrangeServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CLASS_HOTEL_ARRANGE_BASIC_SQL;

/**
 * 班级宾馆安排
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/classHotelArrange")
@BasicOperateRecord("班级宾馆安排")
@SqlRecord(namespace = "classHotelArrange", sql = CLASS_HOTEL_ARRANGE_BASIC_SQL)
public class ClassHotelArrangeController extends TycjGridBaseControllerAdapter<ClassHotelArrange> {

    @Resource(name = "classHotelArrangeService")
    private ClassHotelArrangeServiceImpl classHotelArrangeService;

    /**
     * 安排宾馆
     * @return
     */
    @RequestMapping("/arrangeHotel")
    @OperateRecord(value = "安排宾馆", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "classHotelArrange", sql = CLASS_HOTEL_ARRANGE_BASIC_SQL)
    public ResultDataModel arrangeHotel(@RequestBody ParamsDataModel paramsDataModel) {
        String detailId = paramsDataModel.getStringParameter(CommonConstant.PARAM_PARENT_ID);
        String hotelId = paramsDataModel.getStringParameter("hotelId");
        String malePrice = paramsDataModel.getStringParameter("malePrice");
        String maleRoomNumber = paramsDataModel.getStringParameter("maleRoomNumber");
        String femalePrice = paramsDataModel.getStringParameter("femalePrice");
        String femaleRoomNumber = paramsDataModel.getStringParameter("femaleRoomNumber");
        this.classHotelArrangeService.doArrangeHotel(detailId, hotelId, malePrice, maleRoomNumber,
                femalePrice, femaleRoomNumber);
        return ResultDataModel.handleSuccessResult("安排成功");
    }

    @Override
    public GridService<ClassHotelArrange> getGridService() {
        return this.classHotelArrangeService;
    }
}
