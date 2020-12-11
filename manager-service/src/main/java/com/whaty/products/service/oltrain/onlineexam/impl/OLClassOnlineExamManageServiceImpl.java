package com.whaty.products.service.oltrain.onlineexam.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlClassOnlineExam;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.clazz.utils.ClazzUtils;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 班级课程管理
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olClassOnlineExamManageService")
public class OLClassOnlineExamManageServiceImpl extends TycjGridServiceAdapter<OlClassOnlineExam> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(OlClassOnlineExam bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateUser(UserUtils.getCurrentUser());
        bean.setCreateTime(new Date());
    }

    @Override
    public void checkBeforeUpdate(OlClassOnlineExam bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    private void checkBeforeAddOrUpdate(OlClassOnlineExam bean) throws EntityException {
        if (bean.getStartTime().compareTo(bean.getEndTime()) >= 0) {
            throw new EntityException("开始时间必须在结束时间之前");
        }
        String additionalSql = StringUtils.isBlank(bean.getId()) ? "" : " and id <> '" + bean.getId() + "'";
        List<Map<String, Object>> existsTime = this.myGeneralDao
                .getMapBySQL("SELECT start_time AS startTime,end_time AS endTime " +
                                "FROM ol_class_online_exam WHERE fk_class_id = ? " + additionalSql,
                        bean.getOlPeClass().getId());
        if (ClazzUtils.verificationPeriodConflict(existsTime, CommonUtils.changeDateToString(bean.getStartTime()),
                CommonUtils.changeDateToString(bean.getEndTime()))) {
            throw new EntityException("考试时间存在冲突");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from ol_class_online_exam e " +
                "inner join enum_const io on io.id = e.flag_is_open where io.code = '1' and "
                + CommonUtils.madeSqlIn(idList, "e.id"))) {
            throw new EntityException("已开启的班级考试不能删除");
        }
    }
}
