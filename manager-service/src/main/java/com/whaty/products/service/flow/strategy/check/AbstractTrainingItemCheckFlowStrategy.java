package com.whaty.products.service.flow.strategy.check;

import com.whaty.dao.GeneralDao;
import com.whaty.products.service.flow.domain.tab.AbstractBaseTab;
import com.whaty.products.service.flow.domain.tab.BaseInfoTab;
import com.whaty.products.service.flow.domain.tab.CardInfoTab;
import com.whaty.products.service.flow.domain.tab.RichTextTab;
import com.whaty.products.service.flow.helper.TabHelper;
import com.whaty.utils.UserUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 抽象的项目审批流程策略
 *
 * @author weipengsen
 */
public abstract class AbstractTrainingItemCheckFlowStrategy extends AbstractCheckFlowStrategy {

    @Override
    public Boolean checkCanApply(String itemId) {
        return this.getGeneralDao().checkNotEmpty("select 1 from training_item i" +
                " inner join enum_const st on st.id = i.flag_item_status where st.code in ('0', '4', '5') " +
                "and i.id = ?", itemId);
    }

    @Override
    public void applyCheck(String itemId) {
        this.getGeneralDao().executeBySQL("UPDATE training_item i INNER JOIN enum_const st " +
                "ON st.namespace = 'flagItemStatus' AND st. CODE = '1' SET flag_item_status = st.id" +
                " WHERE i.id = ?", itemId);
    }

    @Override
    public void passCheck(String itemId) {
        this.getGeneralDao().executeBySQL("UPDATE training_item i INNER JOIN enum_const st " +
                "ON st.namespace = 'flagItemStatus' AND st. CODE = '3' SET flag_item_status = st.id," +
                " fk_check_user_id = ?, check_date = now() WHERE i.id = ?", UserUtils.getCurrentUserId(), itemId);
    }

    @Override
    public void noPassCheck(String itemId) {
        this.getGeneralDao().executeBySQL("UPDATE training_item i INNER JOIN enum_const st " +
                "ON st.namespace = 'flagItemStatus' AND st. CODE = '4' SET flag_item_status = st.id," +
                " fk_check_user_id = ?, check_date = now() WHERE i.id = ?", UserUtils.getCurrentUserId(), itemId);
    }

    @Override
    public void cancelCheck(String itemId) {
        this.getGeneralDao().executeBySQL("UPDATE training_item i INNER JOIN enum_const st " +
                "ON st.namespace = 'flagItemStatus' AND st. CODE = '0' SET flag_item_status = st.id" +
                " WHERE i.id = ?", itemId);
    }

    @Override
    public String getItemName(String itemId) {
        return this.getGeneralDao().getOneBySQL("select name from training_item where id = ?", itemId);
    }

    @Override
    public List<AbstractBaseTab> getBusiness(String itemId) {
        String enrollInst = this.getGeneralDao().getOneBySQL("SELECT tie.training_content " +
                "FROM training_item_extend tie INNER JOIN enum_const iet ON iet.id = tie.flag_item_extend_type" +
                " WHERE tie.fk_item_id = ? and iet.code = ?", itemId, "1");

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                   ");
        sql.append(" 	cou.name as title,                                    ");
        sql.append(" 	tea.true_name as `teacher_icon-user-stroke`,          ");
        sql.append(" 	ct.name as `courseType_icon-type-stroke`              ");
        sql.append(" FROM                                                     ");
        sql.append(" 	training_item_course tic                              ");
        sql.append(" INNER JOIN pe_course cou on cou.id = tic.fk_course_id    ");
        sql.append(" INNER JOIN pe_teacher tea on tea.id = cou.fk_teacher_id  ");
        sql.append(" INNER JOIN enum_const ct on ct.id = cou.flag_course_type ");
        sql.append(" WHERE                                                    ");
        sql.append(" 	tic.fk_training_item_id = ?                           ");
        List<Map<String, Object>> courseInfo = this.getGeneralDao().getMapBySQL(sql.toString(), itemId);
        return Arrays.asList(this.getBaseInfo(itemId), new RichTextTab("招生简章", enrollInst),
                new CardInfoTab("课程列表", courseInfo),
                TabHelper.buildFileListTab("附件列表", itemId, "trainingItem"));
    }

    /**
     * 获取基础数据
     * @return
     * @param itemId
     */
    protected abstract BaseInfoTab getBaseInfo(String itemId);

    @Override
    public String getScopeId(String itemId) {
        return this.getGeneralDao().getOneBySQL("select fk_unit_id from training_item where id = ?", itemId);
    }

    protected abstract GeneralDao getGeneralDao();
    
}
