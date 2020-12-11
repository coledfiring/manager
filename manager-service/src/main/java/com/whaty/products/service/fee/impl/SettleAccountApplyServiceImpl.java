package com.whaty.products.service.fee.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.flow.CheckFlowService;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 申请结算
 *
 * @author weipengsen
 */
@Lazy
@Service("settleAccountApplyService")
public class SettleAccountApplyServiceImpl extends TycjGridServiceAdapter<PeClass> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = "checkFlowService")
    private CheckFlowService checkFlowService;

    /**
     * 申请结算
     * @param ids
     * @param feeMap
     */
    public void doApplySettleAccount(String ids, Map<String, Object> feeMap) throws Exception {
        TycjParameterAssert.isAllNotBlank(ids);
        TycjParameterAssert.isAllNotEmpty(feeMap);
        PeClass clazz = this.myGeneralDao.getById(PeClass.class, ids);
        TycjParameterAssert.isAllNotNull(clazz);
        if ("1".equals(clazz.getEnumConstByFlagSettleAccountStatus().getCode())
                || "2".equals(clazz.getEnumConstByFlagSettleAccountStatus().getCode())) {
            throw new ServiceException("未申请、审核不通过或驳回审核的数据才能申请结算");
        }
        feeMap.forEach((k, v) -> {
            BigDecimal value = StringUtils.isBlank((String) v) ? null : new BigDecimal(Double.parseDouble((String) v));
            Field field = CommonUtils.getField(clazz.getClass(), k);
            field.setAccessible(true);
            try {
                field.set(clazz, value);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        });
        this.myGeneralDao.save(clazz);
        this.myGeneralDao.flush();
        this.checkFlowService.doApply(ids, "4");
    }

}
