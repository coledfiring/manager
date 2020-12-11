package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeBusinessInvoice;
import com.whaty.domain.bean.PeBusinessUnit;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * 单位发票管理service
 *
 * @author shanshuai
 */
@Lazy
@Service("peBusinessInvoiceService")
public class PeBusinessInvoiceServiceImpl extends TycjGridServiceAdapter<PeBusinessInvoice> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void checkBeforeAdd(PeBusinessInvoice bean) throws EntityException {
        this.checkBeforeAddAndUpdate(bean);
        bean.setSiteCode(SiteUtil.getSiteCode());
        bean.setPeBusinessUnit((PeBusinessUnit) myGeneralDao.
                getOneByHQL("FROM PeBusinessUnit where ssoUser.id = ? and siteCode = ?",
                        userService.getCurrentUser().getId(), SiteUtil.getSiteCode()));
    }

    @Override
    public void checkBeforeUpdate(PeBusinessInvoice bean) throws EntityException {
        this.checkBeforeAddAndUpdate(bean);
    }

    /**
     * 添加更新前校验 开票名称、纳税人识别号是否重复
     *
     * @param bean
     */
    private void checkBeforeAddAndUpdate(PeBusinessInvoice bean) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                                ");
        sql.append("    	1                                                                                 ");
        sql.append("    FROM                                                                                  ");
        sql.append("    	pe_business_invoice invoice                                                       ");
        sql.append("    INNER JOIN pe_business_unit unit ON unit.id = invoice.fk_business_unit_id             ");
        sql.append("    WHERE                                                                                 ");
        sql.append("    	invoice.site_code = '" + SiteUtil.getSiteCode() + "'                              ");
        sql.append("    AND unit.fk_sso_user_id = '" + userService.getCurrentUser().getId() + "'              ");
        sql.append("    AND (                                                                                ");
        sql.append("     	invoice. NAME = '" + bean.getName() + "'                                          ");
        sql.append("     	OR invoice.identify_number = '" + bean.getIdentifyNumber() + "'                   ");
        sql.append("     )                                                                                    ");
        if (StringUtils.isNotBlank(bean.getId())) {
            sql.append("    AND invoice.id <> '" + bean.getId() + "'                                          ");
        }
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new EntityException("本单位下开票名称或纳税人识别号已存在");
        }
        boolean isBlankBusinessInfo = StringUtils.isBlank(bean.getInvoiceUnit()) ||
                StringUtils.isBlank(bean.getBankName()) || StringUtils.isBlank(bean.getInvoicePhone());
        if (isBlankBusinessInfo && CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL("SELECT 1 FROM enum_const WHERE id = ? AND CODE = '2'",
                        bean.getEnumConstByFlagInvoiceType().getId()))) {
            throw new EntityException("发票类型为专票时，单位、电话号码、开户银行不能为空，请先完善信息");
        }
    }

}
