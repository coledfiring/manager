package com.whaty.products.service.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeClient;
import com.whaty.domain.bean.hbgr.yysj.PeClientRecord;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * author weipengsen  Date 2020/7/15
 */
@Lazy
@Service("peClientRecordService")
public class PeClientRecordServiceImpl extends TycjGridServiceAdapter<PeClientRecord> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    public void addClientRecord(PeClientRecord peClientRecord) {
        peClientRecord.setEnumConstByFlagIsvalid(this.myGeneralDao.getEnumConstByNamespaceCode("FlagIsvalid", "0"));
        this.myGeneralDao.save(peClientRecord);
    }

    public void setProcess(String ids) {
        this.myGeneralDao.executeBySQL("UPDATE pe_client_record SET flag_isvalid = ? WHERE "
                        + CommonUtils.madeSqlIn(ids,"id"),
                this.myGeneralDao.getEnumConstByNamespaceCode("FlagIsvalid", "1").getId());

    }

    public void setNoProcess(String ids) {
        this.myGeneralDao.executeBySQL("UPDATE pe_client_record SET flag_isvalid = ? WHERE "
                        + CommonUtils.madeSqlIn(ids,"id"),
                this.myGeneralDao.getEnumConstByNamespaceCode("FlagIsvalid", "0").getId());
    }
}
