package com.whaty.products.service.onlineexam;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.StudentOnlineExamScore;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 在线考试成绩管理
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("onlineExamScoreManageService")
public class OnlineExamScoreManageServiceImpl extends TycjGridServiceAdapter<StudentOnlineExamScore> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 发布考试成绩
     *
     * @param ids
     */
    public int doPublishStudentScore(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        this.checkSelectData(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" update student_online_exam_score sc                                   ");
        sql.append(" inner join enum_const em on em.namespace = 'flagScorePublish'         ");
        sql.append(" and em.code = '1'                                                     ");
        sql.append(" set sc.flag_score_publish = em.id                                     ");
        sql.append(" where                                                                 ");
        sql.append(CommonUtils.madeSqlIn(ids, "sc.id"));
        return this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 取消发布成绩
     *
     * @param ids
     */
    public int doCancelPublishStudentScore(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        this.checkSelectData(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" update student_online_exam_score sc                                   ");
        sql.append(" inner join enum_const em on em.namespace = 'flagScorePublish'         ");
        sql.append(" and em.code = '0'                                                     ");
        sql.append(" set sc.flag_score_publish = em.id                                     ");
        sql.append(" where                                                                 ");
        sql.append(CommonUtils.madeSqlIn(ids, "sc.id"));
        return this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 检查学生数据
     *
     * @param ids
     */
    private void checkSelectData(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                             ");
        sql.append("   1                                                ");
        sql.append(" from                                               ");
        sql.append("   student_online_exam_score                        ");
        sql.append(" where                                              ");
        sql.append(CommonUtils.madeSqlIn(ids, "id"));
        List checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(checkList) || checkList.size() < ids.split(CommonConstant.SPLIT_ID_SIGN).length) {
            throw new ServiceException("勾选条目中含有未参加考试的学生！");
        }
    }
}
