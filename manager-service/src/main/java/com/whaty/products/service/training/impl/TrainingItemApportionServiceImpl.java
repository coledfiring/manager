package com.whaty.products.service.training.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.domain.bean.PeUnit;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 项目分派服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("trainingItemApportionService")
public class TrainingItemApportionServiceImpl extends AbstractTrainingItemApplyService {


    /**
     * 项目分派
     *
     * @param ids
     */
    @LogAndNotice("项目安排-项目分派")
    public void doApportionItem(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        List idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                  ");
        sql.append("   1                                                                     ");
        sql.append(" from                                                                    ");
        sql.append("   training_item item                                                    ");
        sql.append(" LEFT JOIN pe_class cl on cl.fk_training_item_id=item.id                 ");
        sql.append(" LEFT JOIN enum_const apt on apt.id=item.flag_is_apportion               ");
        sql.append(" where                                                                   ");
        sql.append("   (item.fk_arrange_unit_id is not null                                  ");
        sql.append(" or cl.id is not null                                                    ");
        sql.append(" or apt.code = '1')                                                      ");
        sql.append(" and " + CommonUtils.madeSqlIn(idList, "item.id"));
        List checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new ServiceException("项目分派失败，只能分派未安排且未开班的项目");
        }
        sql.delete(0, sql.length());
        sql.append(" update training_item item                                                                 ");
        sql.append(" inner JOIN enum_const apt on apt.namespace='flagIsApportion'                              ");
        sql.append(" and apt.code = '1'                                                                        ");
        sql.append(" set item.flag_is_apportion = apt.id,                                                      ");
        sql.append(" item.fk_apportion_user_id = '" + UserUtils.getCurrentUserId() + "',                       ");
        sql.append(" item.apportion_date = now()                                                               ");
        sql.append(" where                                                                                     ");
        sql.append(CommonUtils.madeSqlIn(idList, "item.id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 取消分派
     *
     * @param ids
     */
    @LogAndNotice("项目安排-取消分派")
    public void doCancelApportionItem(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        PeUnit unit = UserUtils.getCurrentUnit();
        if (unit == null) {
            throw new ServiceException("当前用户没有关联单位");
        }
        List idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                     ");
        sql.append("   1                                                                        ");
        sql.append(" from                                                                       ");
        sql.append("   training_item item                                                       ");
        sql.append(" LEFT JOIN pe_class cl on cl.fk_training_item_id=item.id                    ");
        sql.append(" LEFT JOIN training_item_apply apply on apply.fk_training_item_id=item.id   ");
        sql.append(" inner JOIN enum_const apt on apt.id=item.flag_is_apportion                 ");
        sql.append(" where                                                                      ");
        sql.append("   item.fk_arrange_unit_id is null                                          ");
        sql.append(" and cl.id is null                                                          ");
        sql.append(" and apply.id is null                                                       ");
        sql.append(" and item.fk_unit_id = '" + unit.getId() + "'                               ");
        sql.append(" and apt.code = '1'                                                         ");
        sql.append(" and " + CommonUtils.madeSqlIn(idList, "item.id"));
        List checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(checkList) || checkList.size() < idList.size()) {
            throw new ServiceException("取消分派失败，只能取消分派已分派且未安排且未开班且未被申请的项目");
        }
        sql.delete(0, sql.length());
        sql.append(" update training_item item                                                                 ");
        sql.append(" inner JOIN enum_const apt on apt.namespace='flagIsApportion'                              ");
        sql.append(" and apt.code = '0'                                                                        ");
        sql.append(" set item.flag_is_apportion = apt.id,                                                      ");
        sql.append(" item.fk_apportion_user_id = null,                                                         ");
        sql.append(" item.apportion_date = null                                                                ");
        sql.append(" where                                                                                     ");
        sql.append(CommonUtils.madeSqlIn(idList, "item.id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 项目申请
     *
     * @param ids
     */
    @LogAndNotice("项目安排-项目申请")
    public void doApplyItem(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        PeUnit unit = UserUtils.getCurrentUnit();
        if (unit == null) {
            throw new ServiceException("当前用户没有关联单位");
        }
        List idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                    ");
        sql.append("   1                                                                       ");
        sql.append(" from                                                                      ");
        sql.append("   training_item item                                                      ");
        sql.append(" LEFT JOIN pe_class cl on cl.fk_training_item_id=item.id                   ");
        sql.append(" LEFT JOIN training_item_apply apply on apply.fk_training_item_id=item.id  ");
        sql.append(" AND apply.fk_unit_id = ?                                                  ");
        sql.append(" inner JOIN enum_const apt on apt.id=item.flag_is_apportion                ");
        sql.append(" where                                                                     ");
        sql.append("   item.fk_arrange_unit_id is null                                         ");
        sql.append(" and cl.id is null                                                         ");
        sql.append(" and apply.id is null                                                      ");
        sql.append(" and apt.code = '1'                                                        ");
        sql.append(" and " + CommonUtils.madeSqlIn(idList, "item.id"));
        List checkList = this.myGeneralDao.getBySQL(sql.toString(), unit.getId());
        if (CollectionUtils.isEmpty(checkList) || checkList.size() < idList.size()) {
            throw new ServiceException("项目申请失败，只能申请已经分派、未安排、未申请且没有分班的项目");
        }
        sql.delete(0, sql.length());
        sql.append(" insert into  training_item_apply (          ");
        sql.append("   id,                                       ");
        sql.append("   fk_training_item_id,                      ");
        sql.append("   fk_unit_id,                               ");
        sql.append("   fk_apply_user_id,                         ");
        sql.append("   apply_date,                               ");
        sql.append("   site_code                                 ");
        sql.append(" ) select                                    ");
        sql.append("     replace(uuid(),'-',''),                 ");
        sql.append("     id,                                     ");
        sql.append("     '" + unit.getId() + "',                 ");
        sql.append("     '" + UserUtils.getCurrentUserId() + "', ");
        sql.append("     now(),                                  ");
        sql.append("     '" + SiteUtil.getSiteCode() + "'        ");
        sql.append("   from training_item                        ");
        sql.append("   where                                     ");
        sql.append(CommonUtils.madeSqlIn(idList, "id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 取消申请
     *
     * @param ids
     */
    @LogAndNotice("项目安排-取消申请")
    public void doCancelApplyItem(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        PeUnit unit = UserUtils.getCurrentUnit();
        if (unit == null) {
            throw new ServiceException("当前用户没有关联单位");
        }
        List idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                                      ");
        sql.append("   1                                                                                         ");
        sql.append(" from                                                                                        ");
        sql.append("   training_item item                                                                        ");
        sql.append(" inner JOIN training_item_apply apply on apply.fk_training_item_id=item.id                   ");
        sql.append(" inner JOIN enum_const apt on apt.id=item.flag_is_apportion                                  ");
        sql.append(" where                                                                                       ");
        sql.append("   (item.fk_arrange_unit_id is null or item.fk_arrange_unit_id <> '" + unit.getId() + "')    ");
        sql.append(" and apply.fk_unit_id = '" + unit.getId() + "'                                               ");
        sql.append(" and apt.code = '1'                                                                          ");
        sql.append(" and " + CommonUtils.madeSqlIn(idList, "item.id"));
        List checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(checkList) || checkList.size() < idList.size()) {
            throw new ServiceException("取消申请失败，只能取消已申请且未安排的项目");
        }
        sql.delete(0, sql.length());
        sql.append(" DELETE                                                                  ");
        sql.append(" FROM                                                                    ");
        sql.append("   training_item_apply                                                   ");
        sql.append(" WHERE                                                                   ");
        sql.append(CommonUtils.madeSqlIn(idList, "fk_training_item_id"));
        sql.append(" and fk_unit_id = '" + unit.getId() + "'                                 ");
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 删除申请
     *
     * @param ids
     */
    @LogAndNotice("项目安排-删除申请")
    public void doDeleteApplyItem(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        PeUnit unit = UserUtils.getCurrentUnit();
        if (unit == null) {
            throw new ServiceException("当前用户没有关联单位");
        }
        List idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                                      ");
        sql.append("   1                                                                                         ");
        sql.append(" from                                                                                        ");
        sql.append("   training_item item                                                                        ");
        sql.append(" inner JOIN training_item_apply apply on apply.fk_training_item_id=item.id                   ");
        sql.append(" inner JOIN enum_const apt on apt.id=item.flag_is_apportion                                  ");
        sql.append(" where                                                                                       ");
        sql.append("   item.fk_arrange_unit_id is null                                                           ");
        sql.append(" and item.fk_unit_id = '" + unit.getId() + "'                                                ");
        sql.append(" and apt.code = '1'                                                                          ");
        sql.append(" and " + CommonUtils.madeSqlIn(idList, "apply.id"));
        List checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(checkList) || checkList.size() < idList.size()) {
            throw new ServiceException("删除申请失败，只能删除权限内已经分派且未安排的项目");
        }
        sql.delete(0, sql.length());
        sql.append(" DELETE                                                                  ");
        sql.append(" FROM                                                                    ");
        sql.append("   training_item_apply                                                   ");
        sql.append(" WHERE                                                                   ");
        sql.append(CommonUtils.madeSqlIn(idList, "id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 获取可申请单位
     *
     * @param ids
     * @return
     */
    public List getArrangeUnit(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                             ");
        sql.append("   unit.id as id,                                                   ");
        sql.append("   unit.name as name                                                ");
        sql.append(" from                                                               ");
        sql.append("   training_item_apply apply                                        ");
        sql.append(" inner JOIN training_item item on apply.fk_training_item_id=item.id ");
        sql.append(" inner JOIN pe_unit unit on unit.id=apply.fk_unit_id                ");
        sql.append(" where                                                              ");
        sql.append(CommonUtils.madeSqlIn(ids, "item.id"));
        sql.append(" group by unit.id                                                   ");
        return this.myGeneralDao.getBySQL(sql.toString());
    }

    /**
     * 项目安排
     *
     * @param ids
     * @param unitId
     */
    @LogAndNotice("项目安排-项目安排")
    public void doArrangeItem(String ids, String unitId) {
        TycjParameterAssert.isAllNotBlank(ids);
        List idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                                   ");
        sql.append("   1                                                                                      ");
        sql.append(" from                                                                                     ");
        sql.append("   training_item_apply apply                                                              ");
        sql.append(" inner JOIN training_item item on apply.fk_training_item_id=item.id                       ");
        sql.append(" inner JOIN pe_unit unit on unit.id=apply.fk_unit_id                                      ");
        sql.append(" inner JOIN enum_const apt on apt.id=item.flag_is_apportion                               ");
        sql.append(" LEFT JOIN pe_class cl on cl.fk_training_item_id=item.id                                  ");
        sql.append(" where                                                                                    ");
        sql.append("    cl.id is null                                                                         ");
        sql.append(" and apt.code = '1'                                                                       ");
        if (StringUtils.isNotBlank(unitId)) {
            sql.append(" and unit.id = '" + unitId + "'                                                       ");
            unitId = "'" + unitId + "'";
        } else {
            unitId = "null";
        }
        sql.append(" and" + CommonUtils.madeSqlIn(ids, "item.id"));
        List checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(checkList) || checkList.size() < idList.size()) {
            throw new ServiceException("项目安排失败，只能安排已经分派、已申请且未开班的项目");
        }
        sql.delete(0, sql.length());
        sql.append(" update training_item item                                                                 ");
        sql.append(" set item.fk_arrange_unit_id = " + unitId + ",                                             ");
        sql.append(" item.fk_arrange_user_id = '" + UserUtils.getCurrentUserId() + "',                         ");
        sql.append(" item.arrange_date = now()                                                                 ");
        sql.append(" where                                                                                     ");
        sql.append(CommonUtils.madeSqlIn(idList, "item.id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 取消安排
     * @param ids
     */
    public void doCancelArrangeItem(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        List<Object> list = this.myGeneralDao.getBySQL(ScopeHandleUtils
                .handleScopeSignOfSql("select 1 from training_item where [peUnit|fk_unit_id] and " +
                        CommonUtils.madeSqlIn(ids, "id"), UserUtils.getCurrentUserId()));
        if (CollectionUtils.isEmpty(list) || list.size() != ids.split(CommonConstant.SPLIT_ID_SIGN).length) {
            throw new ServiceException("存在无权限操作的项目");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_class where " +
                CommonUtils.madeSqlIn(ids, "fk_training_item_id"))) {
            throw new ServiceException("存在关联了班级的项目，无法取消");
        }
        this.myGeneralDao.executeBySQL("update training_item set fk_arrange_unit_id = null, " +
                "fk_arrange_user_id = null, arrange_date = null where " + CommonUtils.madeSqlIn(ids, "id"));
    }
}
