package com.whaty.products.service.seal.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeReceived;
import com.whaty.domain.bean.PrintingUnit;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 打印单位管理
 *
 * @author weipengsen
 */
@Lazy
@Service("printingUnitManageService")
public class PrintingUnitManageServiceImpl extends TycjGridServiceAdapter<PrintingUnit> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from printing_unit pu" +
                " INNER JOIN enum_const ec ON ec.id = pu.flag_is_valid " +
                "WHERE ec.code = '1' AND " + CommonUtils.madeSqlIn(idList, "pu.id"))) {
            throw new EntityException("存在有效数据不可删除");
        }
    }
}
