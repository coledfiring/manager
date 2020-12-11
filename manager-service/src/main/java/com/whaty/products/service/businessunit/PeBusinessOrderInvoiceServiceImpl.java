package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeBusinessOrder;
import com.whaty.domain.bean.PeBusinessOrderInvoice;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单发票管理service
 *
 * @author shanshuai
 */
@Lazy
@Service("peBusinessOrderInvoiceService")
public class PeBusinessOrderInvoiceServiceImpl extends TycjGridServiceAdapter<PeBusinessOrderInvoice> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void checkBeforeAdd(PeBusinessOrderInvoice bean, Map<String, Object> params) throws EntityException {
        Map<String, Object> invoiceInfoMap = myGeneralDao.
                getOneMapBySQL("SELECT name, type_name typeName, identify_number identifyNumber, " +
                        " bank_name bankName, invoice_unit invoiceUnit, invoice_phone phone, flag_invoice_type invoiceType " +
                        " FROM pe_business_invoice WHERE id = ?", bean.getName());
        bean.setName((String) invoiceInfoMap.get("name"));
        bean.setTypeName((String) invoiceInfoMap.get("typeName"));
        bean.setInvoiceUnit((String) invoiceInfoMap.get("invoiceUnit"));
        bean.setIdentifyNumber((String) invoiceInfoMap.get("identifyNumber"));
        bean.setBankName((String) invoiceInfoMap.get("bankName"));
        bean.setInvoicePhone((String) invoiceInfoMap.get("phone"));
        bean.setEnumConstByFlagInvoiceType(myGeneralDao.getById(EnumConst.class,
                (String) invoiceInfoMap.get("invoiceType")));

        Map<String, Object> businessUnitInfoInfoMap = myGeneralDao.
                getOneMapBySQL("SELECT name, unit_address unitAddress FROM pe_business_unit WHERE fk_sso_user_id = ?",
                        userService.getCurrentUser().getId());
        bean.setUnitName((String) businessUnitInfoInfoMap.get("name"));
        bean.setBusinessUnitAddress((String) businessUnitInfoInfoMap.get("unitAddress"));

        String orderId = params.get("parentId").toString();
        this.checkBeforeAndOrUpdate(bean, orderId);
        bean.setPeBusinessOrder((PeBusinessOrder) myGeneralDao.
                getOneByHQL("FROM PeBusinessOrder where id = ?", orderId));
        bean.setCreateUser(UserUtils.getCurrentUser());
        bean.setCreateTime(new Date());
        bean.setSiteCode(SiteUtil.getSiteCode());
    }

    @Override
    public void checkBeforeUpdate(PeBusinessOrderInvoice bean) throws EntityException {
        // 修改发票名称的逻辑
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL("SELECT 1 FROM pe_business_order_invoice WHERE NAME <> ? and id = ?",
                        bean.getName(), bean.getId()))) {
            bean.setName(myGeneralDao.
                    getOneBySQL("SELECT NAME FROM pe_business_invoice WHERE id = ?", bean.getName()));
        }
        this.checkBeforeAndOrUpdate(bean, bean.getPeBusinessOrder().getId());
        boolean isBlankBusinessInfo = StringUtils.isBlank(bean.getInvoicePhone()) ||
                StringUtils.isBlank(bean.getBusinessUnitAddress()) || StringUtils.isBlank(bean.getBankName());
        if (isBlankBusinessInfo && CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL("SELECT 1 FROM enum_const WHERE id = ? AND CODE = '2'",
                        bean.getEnumConstByFlagInvoiceType().getId()))) {
            throw new EntityException("发票类型为专票时，单位地址、电话号码、开户银行不能为空，请先完善信息");
        }
        bean.setUpdateUser(UserUtils.getCurrentUser());
        bean.setUpdateTime(new Date());
    }

    /**
     * 添加更新前的校验
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAndOrUpdate(PeBusinessOrderInvoice bean, String orderId) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                             ");
        sql.append("    	1                                                                              ");
        sql.append("    FROM                                                                               ");
        sql.append("    	pe_business_order ord                                                          ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.id = ord.flag_business_fee_status     ");
        sql.append("    WHERE                                                                              ");
        sql.append("        ord.id = ?                                                                     ");
        sql.append("    AND feeStatus. CODE IN ('1', '2', '4')                                             ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), orderId))) {
            throw new EntityException("订单状态不是未支付状态不能添加或修改发票信息");
        }
        boolean isBlankReceiveInfo = StringUtils.isBlank(bean.getRecipientAddress()) ||
                StringUtils.isBlank(bean.getRecipientPhone()) || StringUtils.isBlank(bean.getRecipientAddress());
        if (isBlankReceiveInfo && CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL("SELECT 1 FROM enum_const WHERE id = ? AND CODE = '0'",
                        bean.getEnumConstByFlagReceiceStatus().getId()))) {
            throw new EntityException("领取方式为邮寄时，收件人姓名、收件人电话、收件人地址，请先完善信息");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                                ");
        sql.append("    	1                                                                                 ");
        sql.append("    FROM                                                                                  ");
        sql.append("    	pe_business_order_invoice invoice                                                 ");
        sql.append("    INNER JOIN pe_business_order ord ON ord.id = invoice.fk_business_order_id             ");
        sql.append("    INNER JOIN pe_business_unit unit ON unit.id = ord.fk_business_unit_id                 ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.id = ord.flag_business_fee_status        ");
        sql.append("    WHERE                                                                                 ");
        sql.append("        unit.fk_sso_user_id = ?                                                           ");
        sql.append("    AND      " + CommonUtils.madeSqlIn(idList, "invoice.id"));
        sql.append("    AND feeStatus. CODE IN ('1', '2', '3', '4')                                           ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), userService.getCurrentUser().getId()))) {
            throw new EntityException("订单状态不是未支付状态，不能删除发票信息");
        }
    }

}
