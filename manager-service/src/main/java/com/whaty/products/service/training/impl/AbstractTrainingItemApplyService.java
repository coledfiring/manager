package com.whaty.products.service.training.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.bean.ParameterCondition;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeUnit;
import com.whaty.domain.bean.SsoUser;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.function.Tuple;
import com.whaty.util.CommonUtils;
import com.whaty.util.TycjCollectionUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 抽象培训项目申报服务类
 *
 * @author weipengsen
 */
public abstract class AbstractTrainingItemApplyService extends TycjGridServiceAdapter<TrainingItem> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    protected GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    protected UserService userService;

    @Override
    public void checkBeforeAdd(TrainingItem bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCode(this.generateCode(bean));
        bean.setPeUnit(UserUtils.getCurrentUnit());
        bean.setEnumConstByFlagItemStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_ITEM_STATUS, "0"));
        bean.setTrainingEndTime(CommonUtils.convertDateToDayEnd(bean.getTrainingEndTime()));
        bean.setCreateBy(this.myGeneralDao.getById(SsoUser.class, this.userService.getCurrentUser().getId()));
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(TrainingItem bean) throws EntityException {
        bean.setTrainingEndTime(CommonUtils.convertDateToDayEnd(bean.getTrainingEndTime()));
        this.checkBeforeAddOrUpdate(bean);
        if (this.myGeneralDao.checkNotEmpty("select 1 from training_item ti " +
                "inner join enum_const st on st.id = ti.flag_item_status " +
                "where st.code in (1, 3) and ti.id = ?", bean.getId())) {
            throw new EntityException("只有未申请或审核不通过的项目才可以编辑");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("SELECT 1 FROM training_item ite " +
                "INNER JOIN enum_const st ON st.id = ite.flag_item_status WHERE st. CODE in ('1', '3') and " +
                CommonUtils.madeSqlIn(idList, "ite.id"))) {
            throw new EntityException("只能删除未申请或审核不通过的数据");
        }
    }

    /**
     * 生成编号
     *
     * @return
     */
    protected String generateCode(TrainingItem bean) throws EntityException {
        PeUnit unit = UserUtils.getCurrentUnit();
        if (unit == null) {
            throw new EntityException("当前用户没有关联单位");
        }
        String prefixStr = generatePrefix(bean.getEnumConstByFlagTrainingItemType().getCode());
        String prefix = prefixStr + unit.getCode() + CommonUtils.changeDateToString(new Date(), "YYYY");
        String maxCode = this.myGeneralDao
                .getOneBySQL("SELECT cast(floor(max(SUBSTR(CODE, CHAR_LENGTH(CODE) - 2, 3)) + 1) as char) " +
                                "FROM training_item WHERE fk_unit_id = ? AND flag_training_item_type = ? " +
                                "AND SUBSTR(CODE, 1, CHAR_LENGTH(?)) = ?", unit.getId(),
                        bean.getEnumConstByFlagTrainingItemType().getId(), prefix, prefix);
        if (StringUtils.isBlank(maxCode)) {
            maxCode = "001";
        }
        return prefix + CommonUtils.leftAddZero(maxCode, 3);
    }

    /**
     * 根据项目类型生成编号前缀
     * 社会培训 前缀为 'S'
     * 委托培训 前缀为 'W'
     */
    private String generatePrefix(String trainingItemTypeCode) {
        switch (trainingItemTypeCode) {
            case "1":
                return "S";
            case "2":
                return "W";
            default:
                throw new ParameterIllegalException();
        }
    }

    protected void checkBeforeAddOrUpdate(TrainingItem bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from training_item where name = ? and site_code = ? " +
                        "and flag_training_item_type = ?" + additionalSql, bean.getName(),
                MasterSlaveRoutingDataSource.getDbType(),
                bean.getEnumConstByFlagTrainingItemType().getId())) {
            throw new EntityException("此名称已存在");
        }
        if (bean.getTrainingStartTime().after(bean.getTrainingEndTime())) {
            throw new EntityException("培训开始时间必须在培训结束时间之前");
        }
    }

    /**
     * 获取培训内容
     *
     * @param extendId
     * @return
     */
    public Map<String, Object> getTrainingContent(String extendId) {
        String trainingContent = this.myGeneralDao
                .getOneBySQL("select training_content from training_item_extend where id = ?", extendId);
        return Collections.singletonMap("trainingContent", trainingContent);
    }

    /**
     * 保存培训内容
     *
     * @param id
     * @param content
     */
    public void saveTrainingContent(String id, String content, String code, String namespace) {
        TycjParameterAssert.isAllNotBlank(id, code, namespace);
        TycjParameterAssert.isAllNotNull(content);
        EnumConst type = this.myGeneralDao.getEnumConstByNamespaceCode("flagItemExtendType", code);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into training_item_extend (                ");
        sql.append("  id,                                             ");
        sql.append("  training_content,                               ");
        sql.append("  flag_item_extend_type,                          ");
        sql.append("  namespace,                                      ");
        sql.append("  fk_item_id                                      ");
        sql.append(") values (                                        ");
        sql.append("  replace(uuid(),'-',''),                         ");
        sql.append("  ?,                                              ");
        sql.append("  ?,                                              ");
        sql.append("  ?,                                              ");
        sql.append("  ?                                               ");
        sql.append(") on duplicate key update training_content = ?    ");
        this.myGeneralDao.executeBySQL(sql.toString(), content, type.getId(), namespace, id, content);
    }

    /**
     * 获取培训内容
     *
     * @param itemId
     * @param code
     * @return
     */
    public Map<String, Object> getTrainingContentByItemId(String itemId, String code) {
        TycjParameterAssert.isAllNotBlank(itemId, code);
        EnumConst type = this.myGeneralDao.getEnumConstByNamespaceCode("flagItemExtendType", code);
        if (type == null) {
            throw new ServiceException("项目信息类型不存在");
        }
        String trainingContent = this.myGeneralDao
                .getOneBySQL("select training_content from training_item_extend where fk_item_id = ? " +
                        "and flag_item_extend_type = ?", itemId, type.getId());
        return TycjCollectionUtils.map("trainingContent", trainingContent, "name", type.getName());
    }

    /**
     * 设置报名截止时间
     *
     * @param ids
     * @param enrollEndTime
     */
    public void doSetEnrollEndTime(String ids, Date enrollEndTime) {
        TycjParameterAssert.isAllNotBlank(ids);
        TycjParameterAssert.isAllNotNull(enrollEndTime);
        this.myGeneralDao.executeBySQL("update training_item set enroll_end_time=? where id=?", enrollEndTime, ids);
    }

    /**
     * 列举项目扩展信息类型
     *
     * @return
     */
    public List<Map<String, Object>> listItemExtendType() {
        return this.myGeneralDao.getMapBySQL("select id as id,name as name,'' as editorInstance,'' as content, " +
                        "code as code from enum_const where namespace=? and code = '1' order by code",
                EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_ITEM_EXTEND_TYPE);
    }

    /**
     * 获取项目基本信息
     * @param ids
     * @return
     */
    public Map<String, Object> getTrainingItemBaseInfo(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                                 ");
        sql.append("   concat(eul.name,'/',eul.mobile_number) as entrustUnitLinkman,                        ");
        sql.append("   ti.training_start_time as startTime,                                                 ");
        sql.append("   ti.training_end_time as endTime,                                                     ");
        sql.append("   ti.training_person_number as personNum,                                              ");
        sql.append("   if(tt.code = '1',ti.training_person_number*ti.total_fee,ti.total_fee) as totalFee    ");
        sql.append(" from                                                                                   ");
        sql.append("   training_item ti                                                                     ");
        sql.append(" INNER JOIN enum_const tt on tt.id = ti.flag_training_item_type                         ");
        sql.append(" left join entrusted_unit eu on ti.fk_entrusted_unit_id = eu.id                         ");
        sql.append(" left join entrusted_unit_linkman eul ON eul.fk_unit_id = eu.id                         ");
        sql.append(" where                                                                                  ");
        sql.append("   ti.id = ?                                                                            ");
        return this.myGeneralDao.getOneMapBySQL(sql.toString(), ids);
    }

    /**
     * 通过引导创建数据
     *
     * @param bean
     * @param params
     * @param gridConfig
     * @return
     */
    public String createByFlow(TrainingItem bean, Map<String, Object> params, GridConfig gridConfig)
            throws ServiceException {
        try {
            this.setParameterProperty(bean, params);
            this.mergeBeanByGridConfig(bean, bean, gridConfig, false);
            this.checkBeforeAdd(bean);
            this.checkBeforeAdd(bean, params);
            this.myGeneralDao.save(bean);
            this.afterAdd(bean);
            return bean.getId();
        } catch (EntityException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private void setParameterProperty(TrainingItem bean, Map<String, Object> mapParams) {
        Optional.ofNullable(this.getParameterConditions()).filter(CollectionUtils::isNotEmpty)
                .ifPresent(l -> this.setPropertyToBean(bean, mapParams, l));
    }

    private void setPropertyToBean(TrainingItem bean, Map<String, Object> mapParams,
                                   List<ParameterCondition> conditions) {
        conditions.stream().filter(e -> e.getPropertyKey() != null && e.getParameterKey() != null
                        && mapParams.containsKey(e.getParameterKey()))
                .forEach(e -> this.setPropertyToBean(bean, mapParams, e));
    }

    private void setPropertyToBean(TrainingItem bean, Map<String, Object> mapParams, ParameterCondition condition) {
        try {
            CommonUtils.setPropertyToBean(bean, condition.getPropertyKey(), mapParams.get(condition.getParameterKey()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 更改费用
     * @param bean
     */
    public void updateFee(TrainingItem bean) {
        String setSql = Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(e -> e.getName().endsWith("Fee"))
                .filter(e -> !"totalFee".equals(e.getName()))
                .map(e -> new Tuple<>(this.getFieldValue(e, bean), e.getAnnotation(Column.class)))
                .filter(e -> Objects.nonNull(e.t1))
                .map(e -> e.t1.name() + " = " + e.t0)
                .reduce("", (e1, e2) -> e1 + e2 + ", ");
        setSql = setSql.substring(0, setSql.lastIndexOf(","));
        this.myGeneralDao.executeBySQL("update training_item set " + setSql + " where id = ?", bean.getId());
    }

    /**
     * 获取字段的值
     * @param f
     * @param item
     * @return
     */
    private Number getFieldValue(Field f, TrainingItem item) {
        try {
            f.setAccessible(true);
            return (Number) f.get(item);
        } catch (IllegalAccessException e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 设置字段的值
     * @param f
     * @param item
     * @param value
     */
    private void setFieldValue(Field f, TrainingItem item, Object value) {
        f.setAccessible(true);
        try {
            f.set(item, value);
        } catch (IllegalAccessException e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 更新基础信息
     * @param bean
     * @param gridConfig
     */
    public Map updateBasicInfo(TrainingItem bean, GridConfig gridConfig) {
        TrainingItem item = this.myGeneralDao.getById(TrainingItem.class, bean.getId());
        Arrays.stream(item.getClass().getDeclaredFields())
                .filter(e -> e.getName().endsWith("Fee"))
                .filter(e -> !"totalFee".equals(e.getName()))
                .map(e -> new Tuple<>(e, this.getFieldValue(e, item)))
                .forEach(e -> this.setFieldValue(e.t0, bean, e.t1));
        return this.update(bean, gridConfig);
    }
}
