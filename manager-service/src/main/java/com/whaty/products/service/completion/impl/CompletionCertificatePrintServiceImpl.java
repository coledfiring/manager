package com.whaty.products.service.completion.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.oauth.remote.SsoConfig;
import com.whaty.core.framework.user.util.UserSession;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 结业证书打印
 *
 *
 * @author weipengsen
 */
@Lazy
@Service("completionCertificatePrintService")
public class CompletionCertificatePrintServiceImpl extends TycjGridServiceAdapter<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 更改证书编号
     * @param ids
     * @param certificateNumber
     */
    public void updateCertificateNumber(String ids, String certificateNumber) {
        TycjParameterAssert.isAllNotBlank(ids, certificateNumber);
        if (!this.myGeneralDao.checkNotEmpty("select 1 from pe_student where id = ?", ids)) {
            throw new ParameterIllegalException();
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_student where certificate_number = ? AND id <> ?",
                certificateNumber, ids)) {
            throw new ServiceException("此证书编号已存在");
        }
        this.myGeneralDao.executeBySQL("update pe_student set certificate_number = ? where id = ?",
                certificateNumber, ids);
    }

    /**
     * 修改证书打印时间和打印人
     *
     * @param ids
     */
    public void updateCertificateDownLoadTime(String ids, String login_Id){
        TycjParameterAssert.isAllNotBlank(ids);
        this.myGeneralDao.executeBySQL("update pe_student set print_time  = now(), print_by = ? where" +
                CommonUtils.madeSqlIn(ids, "id"), login_Id);
    }

}
