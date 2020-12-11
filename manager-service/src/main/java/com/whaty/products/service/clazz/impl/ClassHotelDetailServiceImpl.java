package com.whaty.products.service.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ClassHotelDetail;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import com.whaty.utils.HibernatePluginsUtil;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 班级宾馆详情
 *
 * @author weipengsen
 */
@Lazy
@Service("classHotelDetailService")
public class ClassHotelDetailServiceImpl extends TycjGridServiceAdapter<ClassHotelDetail> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        this.myGeneralDao.executeBySQL("update pe_class set fk_class_hotel_detail_id = null where "
                + CommonUtils.madeSqlIn(ids, "fk_class_hotel_detail_id"));
        if (HibernatePluginsUtil.validateReferencingCurrentSession(ClassHotelDetail.class, ids)) {
            throw new ServiceException("数据已被引用");
        }

        this.myGeneralDao.deleteByIds(ClassHotelDetail.class, Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN)));
        return com.whaty.core.commons.util.CommonUtils.createSuccessInfoMap("删除成功");
    }

    /**
     * 申请宾馆
     * @param classId
     * @param startTime
     * @param endTime
     * @param note
     */
    public void doApplyHotel(String classId, Date startTime, Date endTime, String note) {
        TycjParameterAssert.isAllNotBlank(classId);
        TycjParameterAssert.isAllNotNull(startTime, endTime);
        if (startTime.after(endTime)) {
            throw new ServiceException("开始时间必须在结束时间之前");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_class where fk_class_hotel_detail_id is not null" +
                " AND id = ?", classId)) {
            throw new ServiceException("此班级已经安排了住宿");
        }
        ClassHotelDetail detail = new ClassHotelDetail();
        detail.setApplyTime(new Date());
        detail.setApplyUser(UserUtils.getCurrentUser());
        detail.setStartTime(startTime);
        detail.setEndTime(endTime);
        detail.setNote(note);
        this.myGeneralDao.save(detail);
        this.myGeneralDao.flush();
        this.myGeneralDao.executeBySQL("update pe_class set fk_class_hotel_detail_id = ? where id = ?",
                detail.getId(), classId);
    }
}
