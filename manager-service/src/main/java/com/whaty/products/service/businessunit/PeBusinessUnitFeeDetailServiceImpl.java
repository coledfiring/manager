package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 单位学生缴费明细service
 *
 * @author shanshuai
 */
@Lazy
@Service("peBusinessUnitFeeDetailService")
public class PeBusinessUnitFeeDetailServiceImpl extends TycjGridServiceAdapter<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        List idList = com.whaty.core.commons.util.CommonUtils.convertIdsToList(ids);
        try {
            this.checkBeforeDelete(idList);
            return com.whaty.core.commons.util.CommonUtils.createSuccessInfoMap(String.format("删除成功，共删除%d条数据",
                    myGeneralDao.executeBySQL("UPDATE pe_student ps SET ps.fk_business_order_id = NULL WHERE " +
                            CommonUtils.madeSqlIn(ids, "ps.id"))));
        } catch (EntityException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception var7) {
            return com.whaty.core.commons.util.CommonUtils.createFailInfoMap("删除失败，数据被引用，不能删除");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                             ");
        sql.append("    	1                                                                              ");
        sql.append("    FROM                                                                               ");
        sql.append("    	pe_student ps                                                                  ");
        sql.append("    INNER JOIN pe_business_order ord ON ord.id = ps.fk_business_order_id               ");
        sql.append("    LEFT JOIN enum_const feeSatus ON feeSatus.id = ord.flag_business_fee_status        ");
        sql.append("    WHERE                                                                              ");
        sql.append("        " + CommonUtils.madeSqlIn(idList, "ps.id"));
        sql.append("    AND feeSatus. CODE IN ('1', '2', '4')                                              ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new EntityException("不能删除订单状态为已支付、汇款待审核、现场待确认下的学生");
        }

        sql.delete(0, sql.length());
        sql.append("    SELECT                                                                           ");
        sql.append("    	1                                                                            ");
        sql.append("    FROM                                                                             ");
        sql.append("    	pe_student ps                                                                ");
        sql.append("    LEFT JOIN enum_const feeStatus ON feeStatus.id = ps.flag_fee_status              ");
        sql.append("    WHERE                                                                            ");
        sql.append("        " + CommonUtils.madeSqlIn(idList, "ps.id"));
        sql.append("    AND (                                                                            ");
        sql.append("    	(                                                                            ");
        sql.append("    		ps.fk_business_order_id IS NOT NULL                                      ");
        sql.append("    		AND feeStatus. CODE = '1'                                                ");
        sql.append("    	)                                                                            ");
        sql.append("    	OR ps.fk_business_order_id IS NULL                                           ");
        sql.append("    )                                                                                ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new EntityException("只能删除有订单号并且缴费状态为未缴费的学生");
        }
    }

    /**
     * 添加缴费学生到订单中
     * @param ids
     * @param orderId
     * @return
     */
    @LogAndNotice("添加缴费学生到订单中")
    public int addStudentToBusinessOrder(String ids, String orderId) throws ServiceException {
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL("SELECT 1  FROM pe_student ps WHERE " +
                CommonUtils.madeSqlIn(ids, "ps.id") + " AND ps.fk_business_order_id IS NOT NULL"))) {
            throw new ServiceException("只能添加没有订单的学生");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                           ");
        sql.append("    	1                                                                            ");
        sql.append("    FROM                                                                             ");
        sql.append("    	pe_business_order ord                                                        ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.ID = ord.flag_business_fee_status   ");
        sql.append("    WHERE                                                                            ");
        sql.append("        ord.id = ?                                                                   ");
        sql.append("    AND feeStatus. CODE IN ('1', '2', '4')                                           ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), orderId))) {
            throw new ServiceException("状态为已支付、汇款待审核、现场待确认的订单不能添加学生");
        }
        return myGeneralDao.executeBySQL("UPDATE pe_student ps SET ps.fk_business_order_id = ? WHERE " +
                CommonUtils.madeSqlIn(ids, "ps.id"), orderId);
    }
}
