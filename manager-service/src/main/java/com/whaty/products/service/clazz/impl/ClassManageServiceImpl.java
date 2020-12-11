package com.whaty.products.service.clazz.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeClass;
import com.whaty.domain.bean.PeUnit;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 班级管理
 *
 * @author weipengsen
 */
@Lazy
@Service("classManageService")
public class ClassManageServiceImpl extends TycjGridServiceAdapter<PeClass> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeClass bean, Map<String, Object> params) throws EntityException {
        if (UserUtils.getCurrentUnit() == null) {
            throw new ServiceException("当前用户没有关联单位");
        }
        this.checkBeforeAddOrUpdate(bean);
        bean.setEndTime(CommonUtils.convertDateToDayEnd(bean.getEndTime()));
        bean.setPeUnit(UserUtils.getCurrentUnit());
        bean.setEnumConstByFlagApplyCertificateStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_APPLY_CERTIFICATE_STATUS, "0"));
        bean.setEnumConstByFlagSettleAccountStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_SETTLE_ACCOUNT_STATUS, "0"));
        bean.setName(Optional.of(bean).map(PeClass::getName).filter(StringUtils::isNotBlank)
                .orElse(this.generateName(bean.getTrainingItem().getId())));
        bean.setCode(this.generateCode(bean));
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    protected void afterAdd(PeClass bean) throws EntityException {
        this.myGeneralDao.flush();
        // 继承项目的课程关联
        this.myGeneralDao
                .executeBySQL("INSERT INTO class_course(id, fk_class_id, fk_course_id, create_by, create_date) " +
                        "SELECT REPLACE (uuid(), '-', ''), '" + bean.getId() + "', fk_course_id, '" +
                        UserUtils.getCurrentUserId() + "', now() FROM training_item_course " +
                        "WHERE fk_training_item_id = ?", bean.getTrainingItem().getId());
        // 生成空课表
        this.myGeneralDao
                .executeBySQL("insert into class_course_timetable(fk_class_course_id, create_by, create_date)" +
                        " select id, ?, now() from class_course where fk_class_id = ?",
                        UserUtils.getCurrentUserId(), bean.getId());
    }

    @Override
    public Map update(PeClass bean, GridConfig gridConfig) {
        bean.getTrainingItem().setEnumConstByFlagTrainingItemType(null);
        return super.update(bean, gridConfig);
    }

    @Override
    public void checkBeforeUpdate(PeClass bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setEndTime(CommonUtils.convertDateToDayEnd(bean.getEndTime()));
        String oldTrainingItem = this.myGeneralDao
                .getOneBySQL("select fk_training_item_id from pe_class where id = ?", bean.getId());
        if (!bean.getTrainingItem().getId().equals(oldTrainingItem)) {
            bean.setName(this.generateName(bean.getTrainingItem().getId()));
            bean.setCode(this.generateCode(bean));
        }
    }

    private void checkBeforeAddOrUpdate(PeClass bean) throws EntityException {
        String arrangeUnitId = this.myGeneralDao
                .getOneBySQL(" select fk_arrange_unit_id from training_item where id = '"
                        + bean.getTrainingItem().getId() + "'");
        PeUnit unit = UserUtils.getCurrentUnit();
        if (unit == null && bean.getId() == null) {
            throw new EntityException("当前用户没有关联单位");
        }
        if (StringUtils.isNotBlank(arrangeUnitId) && unit != null && !unit.getId().equals(arrangeUnitId)) {
            throw new EntityException("您没有当前项目的开班资格");
        }
        if (StringUtils.isNotBlank(bean.getScoreBased()) && bean.getScore() == null) {
            throw new EntityException("只填写了评分依据，没有填写评分");
        }
        if (bean.getScore() != null) {
            bean.setScoreUser(UserUtils.getCurrentUser());
            bean.setScoreDate(new Date());
        }
        if (bean.getStartTime().after(bean.getEndTime())) {
            throw new EntityException("开始时间必须在结束时间之前");
        }
        if (StringUtils.isNotBlank(bean.getEntrustUnitLinkman()) && bean.getEntrustUnitLinkman().contains("/")) {
            String[] entrustUnitLinkman = bean.getEntrustUnitLinkman().split("/");
            bean.setEntrustUnitLinkman(entrustUnitLinkman[0]);
            bean.setEntrustUnitLinkPhone(entrustUnitLinkman[1]);
        }
        if (StringUtils.isNotBlank(bean.getName())) {
            String sql = "select 1 from pe_class where name = ? " +
                    (StringUtils.isNotBlank(bean.getId()) ? "AND id <> '" + bean.getId() + "'" : "");
            if (this.myGeneralDao.checkNotEmpty(sql, bean.getName())) {
                throw new EntityException("班级名称已存在");
            }
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_class where fk_class_hotel_detail_id is not null and "
                + CommonUtils.madeSqlIn(idList, "id"))) {
            throw new EntityException("存在安排了住宿的班级，无法删除");
        }
        if (this.myGeneralDao.checkNotEmpty("SELECT 1 FROM pe_class cl " +
                "INNER JOIN enum_const st ON st.id = cl.flag_apply_certificate_status " +
                "INNER JOIN enum_const ss ON ss.id = cl.flag_settle_account_status " +
                "WHERE (st. CODE in ('1', '2') or ss. CODE in ('1', '2')) and " +
                CommonUtils.madeSqlIn(idList, "cl.id"))) {
            throw new EntityException("存在待审核或审核通过的证书申请或结算申请数据，不可删除");
        }
    }

    /**
     * 生成名称
     * @param itemId
     * @return
     */
    private String generateName(String itemId) {
        String prefix = this.myGeneralDao.getOneBySQL("select name from training_item where id = ?", itemId);
        String maxSerialNumber = this.myGeneralDao
                .getOneBySQL("select cast(max(SUBSTR(name, CHAR_LENGTH(name)-2, 3))+1 as char) from pe_class " +
                        "where SUBSTR(name, 1, CHAR_LENGTH(?)) = ?", prefix, prefix);
        if (StringUtils.isBlank(maxSerialNumber)) {
            maxSerialNumber = "001";
        }
        return prefix + CommonUtils.leftAddZero(maxSerialNumber, 3);
    }

    /**
     * 生成编号
     * @param bean
     * @return
     */
    private String generateCode(PeClass bean) {
        String prefix = this.myGeneralDao.getOneBySQL("select code from training_item where id = ?",
                bean.getTrainingItem().getId());
        String maxSerialNumber = this.myGeneralDao
                .getOneBySQL("select cast(max(SUBSTR(code, CHAR_LENGTH(code)-2, 3))+1 as char) from pe_class " +
                        "where SUBSTR(code, 1, CHAR_LENGTH(?)) = ?", prefix, prefix);
        if (StringUtils.isBlank(maxSerialNumber)) {
            maxSerialNumber = "001";
        }
        return prefix + CommonUtils.leftAddZero(maxSerialNumber, 3);
    }

    /**
     * 申请培训地点
     * @param ids
     * @param placeType
     * @param placeNote
     * @return
     */
    public int doApplyTrainingPlace(String ids, String placeType, String placeNote) {
        TycjParameterAssert.isAllNotBlank(ids, placeType);
        if (!this.myGeneralDao
                .checkNotEmpty("select 1 from enum_const where namespace = 'flagPlaceType' and id = ?", placeType)) {
            throw new ParameterIllegalException();
        }
        return this.myGeneralDao.executeBySQL("update pe_class set flag_place_type = ?, apply_time = now()," +
                " place_note = ? ,apply_user = ? WHERE flag_place_type is null and " +
                CommonUtils.madeSqlIn(ids, "id"), placeType, placeNote, UserUtils.getCurrentUserId());
    }

    /**
     * 列举详情配置
     * @return
     */
    public Map<String, Object> listDetailConfig() {
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("title", "班级详情管理");
        List<Map<String, Object>> tabs = new LinkedList<>();
        Map<String, Object> attachTab = new HashMap<>(4);
        attachTab.put("label", "附件管理");
        attachTab.put("gridUrl", "/entity/common/attachFile/abstractGrid?actionId=attachFileManage");
        attachTab.put("gridId", "attachFileManage");
        attachTab.put("namespace", "peClass");
        tabs.add(attachTab);
        Map<String, Object> courseTab = new HashMap<>(4);
        courseTab.put("label", "课程管理");
        courseTab.put("gridUrl", "/entity/clazz/classCourseManage/abstractGrid?actionId=classCourseManage");
        courseTab.put("gridId", "classCourseManage");
        tabs.add(courseTab);
        Map<String, Object> studentTab = new HashMap<>(4);
        studentTab.put("label", "班级学员管理");
        studentTab.put("gridUrl", "/entity/clazz/classStudentManage/abstractGrid?actionId=classStudentManage");
        studentTab.put("gridId", "classStudentManage");
        tabs.add(studentTab);
        Map<String, Object> courseTimeTab = new HashMap<>(4);
        courseTimeTab.put("label", "课程表安排");
        courseTimeTab.put("gridUrl",
                "/entity/clazz/arrangeClassCourseTimetable/abstractGrid?actionId=arrangeClassCourseTimetable");
        courseTimeTab.put("gridId", "arrangeClassCourseTimetable");
        tabs.add(courseTimeTab);
        resultMap.put("tabs", tabs);
        return resultMap;
    }

    /**
     * 列举班级扩展信息类型
     * @return
     */
    public List<Map<String, Object>> listItemExtendType() {
        return this.myGeneralDao.getMapBySQL("select id as id,name as name,'' as editorInstance,'' as content, " +
                        "code as code from enum_const where namespace=? and code > '4' order by code",
                EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_ITEM_EXTEND_TYPE);
    }

    /**
     * 获取项目需要创建的新班级预填信息
     * @param itemId
     * @return
     */
    public Map<String, Object> getNewClassInfo(String itemId) {
        TycjParameterAssert.isAllNotBlank(itemId);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                              ");
        sql.append("   concat(eul.name,'/',eul.mobile_number) as entrustUnitLinkman,                     ");
        sql.append("   ti.training_start_time as startTime,                                              ");
        sql.append("   ti.training_end_time as endTime,                                                  ");
        sql.append("   ti.training_person_number as personNum,                                           ");
        sql.append("   if(tt.code = '1',ti.training_person_number*ti.total_fee,ti.total_fee) as totalFee ");
        sql.append(" from                                                                                ");
        sql.append("   training_item ti                                                                  ");
        sql.append(" INNER JOIN enum_const tt on tt.id = ti.flag_training_item_type                      ");
        sql.append(" left join entrusted_unit eu on ti.fk_entrusted_unit_id = eu.id                      ");
        sql.append(" left join entrusted_unit_linkman eul ON eul.fk_unit_id = eu.id                      ");
        sql.append(" where                                                                               ");
        sql.append("   ti.id = ?                                                                         ");
        Map<String, Object> classInfo = this.myGeneralDao.getOneMapBySQL(sql.toString(), itemId);
        TycjParameterAssert.isAllNotEmpty(classInfo);
        classInfo.put("className", this.generateName(itemId));
        return classInfo;
    }
}
