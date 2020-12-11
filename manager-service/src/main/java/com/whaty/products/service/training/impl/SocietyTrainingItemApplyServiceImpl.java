package com.whaty.products.service.training.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.OrderItem;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 社会培训项目申报
 *
 * @author weipengsen
 */
@Lazy
@Service("societyTrainingItemApplyService")
public class SocietyTrainingItemApplyServiceImpl extends AbstractTrainingItemApplyService {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map<String, Object> mapParam) {
        if (ArrayUtils.isNotEmpty(pageParam.getOrderItem())) {
            pageParam.setOrderItem(Arrays.stream(pageParam.getOrderItem())
                    .filter(e -> !e.getDataIndex().equals("regulation")).toArray(OrderItem[]::new));
        }
        Page page = super.list(pageParam, gridConfig, mapParam);
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Object[]> regulations = this.openGeneralDao.getBySQL("select re.id, re.name " +
                "from enroll_column_regulation re inner join enum_const ac on ac.id = re.flag_active " +
                "WHERE ac.code = '1' AND re.site_code = ?", SiteUtil.getSiteCode());
        if (CollectionUtils.isNotEmpty(regulations)) {
            Map<Integer, String> regulationMap = regulations.stream()
                    .collect(Collectors.toMap(e -> (Integer) e[0], e -> (String) e[1]));
            ((List<Map<String, Object>>) page.getItems())
                    .stream().filter(e -> Objects.nonNull(e.get("regulation")))
                    .forEach(e -> e.put("regulation", regulationMap.get(e.get("regulation"))));
        }
        return page;
    }

    @Override
    public void checkBeforeAdd(TrainingItem bean) throws EntityException {
        bean.setEnumConstByFlagTrainingItemType(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_TRAINING_ITEM_TYPE, "1"));
        if (bean.getEnrollEndTime().after(bean.getTrainingStartTime())) {
            throw new EntityException("报名截止时间必须在培训开始时间之前");
        }
        super.checkBeforeAdd(bean);
    }

    @Override
    public void checkBeforeUpdate(TrainingItem bean) throws EntityException {
        if (bean.getEnrollEndTime().after(bean.getTrainingStartTime())) {
            throw new EntityException("报名截止时间必须在培训开始时间之前");
        }
        super.checkBeforeUpdate(bean);
    }

    /**
     * 获取委托项目联系人
     *
     * @param ids
     * @return
     */
    public List<Object[]> getItemLinkman(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        if (ids.split(CommonConstant.SPLIT_ID_SIGN).length > 1) {
            throw new ParameterIllegalException();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                           ");
        sql.append("   eul.id as id,                                                  ");
        sql.append("   concat(eul.name,'/',eul.mobile_number) as name                 ");
        sql.append(" from                                                             ");
        sql.append("   training_item item                                             ");
        sql.append(" inner join entrusted_unit eu on eu.id=item.fk_entrusted_unit_id  ");
        sql.append(" inner join entrusted_unit_linkman eul on eu.id=eul.fk_unit_id    ");
        sql.append(" where                                                            ");
        sql.append("   item.id = ?                                                    ");
        return this.myGeneralDao.getBySQL(sql.toString(), ids);
    }

    /**
     * 设置委托项目联系人
     *
     * @param ids
     * @param linkmanId
     */
    public void doSetItemLinkman(String ids, String linkmanId) {
        TycjParameterAssert.isAllNotBlank(ids, linkmanId);
        if (ids.split(CommonConstant.SPLIT_ID_SIGN).length > 1) {
            throw new ParameterIllegalException();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" update                                                           ");
        sql.append("   training_item item                                             ");
        sql.append(" inner join entrusted_unit_linkman eul on eul.id = ?              ");
        sql.append(" set item.linkman=eul.name,                                       ");
        sql.append(" item.link_phone=eul.mobile_number                                ");
        sql.append(" where                                                            ");
        sql.append("   item.id = ?                                                    ");
        this.myGeneralDao.executeBySQL(sql.toString(), linkmanId, ids);
    }


    /**
     * 获取报名二维码配置
     *
     * @param ids
     * @return
     */
    public String getEnrollQrCodeData(String ids) {
        return CommonUtils.generateQRCode(String.format(TrainingConstant.STUDENT_ENROLL_URL_FORMAT_STR,
                CommonUtils.getBasicUrl(), CommonUtils.md5(ids)), 300, 300);
    }

    /**
     * 列举报名规则
     * @return
     */
    public List<String[]> listEnrollRegulation() {
        return this.myGeneralDao.getBySQL("select cast(re.id as char), re.name " +
                "from enroll_column_regulation re inner join enum_const ac on ac.id = re.flag_active " +
                "WHERE ac.code = '1' AND re.site_code = ?", SiteUtil.getSiteCode());
    }

    /**
     * 设置报名规则
     * @param ids
     * @param regulationId
     * @return
     */
    public int doSetEnrollRegulation(String ids, String regulationId) {
        TycjParameterAssert.isAllNotBlank(ids, regulationId);
        return this.myGeneralDao.executeBySQL("update training_item set fk_enroll_regulation_id = ? where " +
                CommonUtils.madeSqlIn(ids, "id"), regulationId);
    }

    /**
     * 设置是否需要报名审核
     * @param ids
     * @param isNeedCheckId
     * @return
     */
    public int doSetEnrollNeedCheck(String ids, String isNeedCheckId) {
        TycjParameterAssert.isAllNotBlank(ids, isNeedCheckId);
        return this.myGeneralDao.executeBySQL("update training_item set flag_enroll_need_check = ? where " +
                CommonUtils.madeSqlIn(ids, "id"), isNeedCheckId);
    }
}
