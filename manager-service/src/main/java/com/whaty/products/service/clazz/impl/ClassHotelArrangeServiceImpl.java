package com.whaty.products.service.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ClassHotelArrange;
import com.whaty.domain.bean.ClassHotelDetail;
import com.whaty.domain.bean.PeHotel;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班级宾馆安排服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("classHotelArrangeService")
public class ClassHotelArrangeServiceImpl extends AbstractTwoLevelListGridServiceImpl<ClassHotelArrange> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 安排宾馆
     * @param detailId
     * @param hotelId
     * @param malePrice
     * @param maleRoomNumber
     * @param femalePrice
     * @param femaleRoomNumber
     */
    public void doArrangeHotel(String detailId, String hotelId, String malePrice, String maleRoomNumber,
                               String femalePrice, String femaleRoomNumber) {
        TycjParameterAssert.isAllNotBlank(detailId, hotelId, malePrice, maleRoomNumber);
        ClassHotelDetail detail = this.myGeneralDao.getById(ClassHotelDetail.class, detailId);
        PeHotel hotel = this.myGeneralDao.getById(PeHotel.class, hotelId);
        if (detail == null || hotel == null) {
            throw new ParameterIllegalException();
        }
        ClassHotelArrange arrange = new ClassHotelArrange();
        arrange.setArrangeTime(new Date());
        arrange.setArrangeUser(UserUtils.getCurrentUser());
        arrange.setClassHotelDetail(detail);
        arrange.setMalePrice(new BigDecimal(malePrice));
        arrange.setMaleRoomNumber(Integer.parseInt(maleRoomNumber));
        arrange.setPeHotel(hotel);
        this.myGeneralDao.save(arrange);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "detailId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
