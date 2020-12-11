package com.whaty.products.controller.clazz;

import com.whaty.common.string.StringUtils;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ClassHotelDetail;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.ClassHotelDetailServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

import static com.whaty.constant.SqlRecordConstants.CLASS_HOTEL_BASIC_SQL;

/**
 * 班级宾馆详情
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/classHotelDetail")
@BasicOperateRecord("班级宾馆")
@SqlRecord(namespace = "classHotel", sql = CLASS_HOTEL_BASIC_SQL)
public class ClassHotelDetailController extends TycjGridBaseControllerAdapter<ClassHotelDetail> {

    @Resource(name = "classHotelDetailService")
    private ClassHotelDetailServiceImpl classHotelDetailService;

    /**
     * 申请宾馆
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/applyHotel")
    public ResultDataModel applyHotel(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String classId = StringUtils.isNotBlank(ids) ? ids : paramsDataModel.getStringParameter("classId");
        Date startTime = CommonUtils.convertUTCDate(paramsDataModel.getStringParameter("startTime"));
        Date endTime = CommonUtils.convertUTCDate(paramsDataModel.getStringParameter("endTime"));
        String note = paramsDataModel.getStringParameter("note");
        this.classHotelDetailService.doApplyHotel(classId, startTime, endTime, note);
        return ResultDataModel.handleSuccessResult("申请成功");
    }

    @Override
    public GridService<ClassHotelDetail> getGridService() {
        return this.classHotelDetailService;
    }
}
