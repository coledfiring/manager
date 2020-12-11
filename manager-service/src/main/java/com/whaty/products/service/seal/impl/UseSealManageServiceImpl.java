package com.whaty.products.service.seal.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeUnit;
import com.whaty.domain.bean.UseSeal;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用印管理服务
 *
 * @author weipengsen
 */
@Lazy
@Service("useSealManageService")
public class UseSealManageServiceImpl extends TycjGridServiceAdapter<UseSeal> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(UseSeal bean) throws EntityException {
        //添加申请信
        PeUnit currentUnit = UserUtils.getCurrentUnit();
        if (Objects.isNull(currentUnit) || StringUtils.isBlank(currentUnit.getId())) {
            throw new EntityException("当前用户不可申请用印");
        }
        bean.setPeUnit(currentUnit);
        bean.setApplicantUser(UserUtils.getCurrentUser());
        bean.setApplicantTime(new Date());
        bean.setEnumConstByFlagAcceptStatus(this.generalDao
                .getEnumConstByNamespaceCode(TrainingConstant.FLAG_ACCEPT_STATUS, "1"));
    }


    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from use_seal us " +
                "INNER JOIN enum_const ec ON ec.id = us.flag_accept_status " +
                "where ec.`CODE` = '0' AND " + CommonUtils.madeSqlIn(idList, "us.id"))) {
            throw new EntityException("存在已受理的数据，不可删除");
        }
    }

    @Override
    public void checkBeforeUpdate(UseSeal bean) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from use_seal us " +
                "INNER JOIN enum_const ec ON ec.id = us.flag_accept_status " +
                "where ec.`CODE` = '0' AND us.id = ?", bean.getId())) {
            throw new EntityException("存在已受理的数据，不可更新");
        }
    }

    /**
     * 受理
     *
     * @param id
     */
    public void doAccept(String id) throws ServiceException {
        TycjParameterAssert.isAllNotNull(id);
        //未通过审批 不可受理
        if (this.generalDao.checkNotEmpty("select 1 from use_seal us " +
                " left JOIN enum_const ec ON ec.id = us.flag_check_status " +
                " where  (us.flag_check_status is null or ec.`CODE` <> '2') AND us.id = ?", id)) {
            throw new ServiceException("未通过审批，不可受理");
        }
        String accepted = this.generalDao.getEnumConstByNamespaceCode(TrainingConstant.FLAG_ACCEPT_STATUS, "0").getId();
        this.generalDao.executeBySQL("UPDATE use_seal SET flag_accept_status = ?, accept_time = ?, " +
                "fk_accept_user_id = ? where id = ? ", accepted, new Date(), UserUtils.getCurrentUser().getId(), id);
    }
}
