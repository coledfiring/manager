package com.whaty.products.service.seal.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePrinting;
import com.whaty.domain.bean.PeUnit;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 印刷管理
 *
 * @author weipengsen
 */
@Lazy
@Service("pePrintingManageService")
public class PePrintingManageServiceImpl extends TycjGridServiceAdapter<PePrinting> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(PePrinting bean) throws EntityException {
        PeUnit currentUnit = UserUtils.getCurrentUnit();
        if (Objects.isNull(currentUnit) || StringUtils.isBlank(currentUnit.getId())) {
            throw new EntityException("当前用户不可申请印刷");
        }
        bean.setPeUnit(currentUnit);
        bean.setApplicantUser(UserUtils.getCurrentUser());
        bean.setApplicantTime(new Date());
        bean.setEnumConstByFlagAcceptStatus(this.generalDao
                .getEnumConstByNamespaceCode(TrainingConstant.FLAG_ACCEPT_STATUS, "1"));
        bean.setEnumConstByFlagCheckStatus(this.generalDao
                .getEnumConstByNamespaceCode(TrainingConstant.FLAG_IS_CHECK, "1"));
    }


    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from pe_printing pp " +
                "INNER JOIN enum_const ec ON ec.id = pp.flag_accept_status " +
                "where ec.`CODE` = '0' AND " + CommonUtils.madeSqlIn(idList, "pp.id"))) {
            throw new EntityException("存在已受理的数据，不可删除");
        }
    }

    @Override
    public void checkBeforeUpdate(PePrinting bean) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from pe_printing pp " +
                "INNER JOIN enum_const ec ON ec.id = pp.flag_accept_status " +
                "where ec.`CODE` = '0' AND pp.id = ?", bean.getId())) {
            throw new EntityException("已受理的数据，不可更新");
        }
    }

    /**
     * 受理
     *
     * @param id
     */
    public void doAccept(String id) {
        TycjParameterAssert.isAllNotNull(id);
        String accepted = this.generalDao.getEnumConstByNamespaceCode(TrainingConstant.FLAG_ACCEPT_STATUS, "0").getId();
        this.generalDao.executeBySQL("UPDATE pe_printing SET flag_accept_status = ?, accept_time = ?, " +
                "fk_accept_user_id = ? where id = ? ", accepted, new Date(), UserUtils.getCurrentUser().getId(), id);
    }

    /**
     * 校验
     *
     * @param id
     */
    public void doCheck(String id) {
        TycjParameterAssert.isAllNotNull(id);
        if (this.generalDao.checkNotEmpty("select 1 from pe_printing pp " +
                "INNER JOIN enum_const ec ON ec.id = pp.flag_accept_status " +
                "where ec.`CODE` = '1' AND pp.id = ?", id)) {
            throw new ServiceException("存在未已受理的数据，不可验收");
        }
        String checked = this.generalDao.getEnumConstByNamespaceCode(TrainingConstant.FLAG_IS_CHECK, "0").getId();
        this.generalDao.executeBySQL("UPDATE pe_printing SET flag_check_status = ?, check_time = ?, " +
                "fk_check_user_id = ? where id = ? ", checked, new Date(), UserUtils.getCurrentUser().getId(), id);
    }

    /**
     * 设置费用
     *
     * @param id
     * @param price
     */
    public void doSetPrice(String id, String price) throws ServiceException {
        TycjParameterAssert.isAllNotNull(id, price);
        if (this.generalDao.checkNotEmpty("select 1 from pe_printing pp " +
                "INNER JOIN enum_const ec ON ec.id = pp.flag_check_status " +
                "where ec.`CODE` = '0' AND pp.id = ?", id)) {
            throw new ServiceException("已验收的数据，不可修改费用");
        }
        this.generalDao.executeBySQL("UPDATE pe_printing SET price = ? where id = ?", price, id);
    }
}