package com.whaty.products.service.flow.strategy.list;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 已审核的审核流程策略
 *
 * @author weipengsen
 */
@Lazy
@Component("checkedCheckFlowStrategy")
public class CheckedCheckFlowStrategy extends AbstractCheckFlowListStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> listCheckFlow(String checkStatus, String needCheck, String flowType,
                                             String search, Map<String, Object> page) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                             ");
        sql.append("    count(1)                                                        ");
        sql.append(" FROM (                                                             ");
        sql.append("    SELECT                                                          ");
        sql.append("    	1                                                           ");
        sql.append("    FROM                                                            ");
        sql.append("    	check_flow cf                                               ");
        sql.append("    INNER JOIN enum_const st ON st.id = cf.flag_check_status        ");
        sql.append("    INNER JOIN enum_const ty on ty.id = cf.flag_flow_type           ");
        sql.append("    INNER JOIN pe_manager m ON m.fk_sso_user_id = cf.create_by      ");
        sql.append("    INNER JOIN enum_const nc on nc.id = cf.flag_need_check        ");
        sql.append("    LEFT JOIN pe_unit un ON un.id = m.fk_unit_id                   ");
        sql.append("    INNER JOIN check_flow_record rec on rec.fk_check_flow_id = cf.id");
        sql.append("    INNER JOIN enum_const ot on ot.id = rec.flag_check_operate      ");
        sql.append("    WHERE                                                           ");
        sql.append("        ot.code in ('1', '2')                                        ");
        sql.append("    AND rec.operate_user = '" + UserUtils.getCurrentManager().getId() + "' ");
        if (StringUtils.isNotBlank(flowType)) {
            sql.append(" AND ty.id = '" + flowType + "' ");
        }
        if (StringUtils.isNotBlank(needCheck)) {
            sql.append(" AND nc.code = '" + needCheck + "' ");
        }
        if (StringUtils.isNotBlank(checkStatus)) {
            sql.append(" AND st.code = '" + checkStatus + "' ");
        }
        if (StringUtils.isNotBlank(search)) {
            sql.append(" AND concat(ifnull(un.name, ''), ';', m.true_name, ';', cf.name) like '%" + search + "%' ");
        }
        sql.append("    GROUP BY                                                         ");
        sql.append("    	cf.id                                                        ");
        sql.append(" ) r");
        page = this.countPage(page, this.generalDao.<BigInteger>getOneBySQL(sql.toString()).intValue());
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                           ");
        sql.append(" 	cf.id as id,                                               ");
        sql.append(" 	cf.name as title,                                             ");
        sql.append(" 	un.name as applyUnit,                                         ");
        sql.append(" 	m.true_name as applyUser,                                     ");
        sql.append(" 	date_format(cf.create_date, '%Y-%m-%d %H:%i:%s') as applyTime,   ");
        sql.append(" 	date_format(cf.complete_date, '%Y-%m-%d %H:%i:%s') as completeTime,");
        sql.append(" 	st.code as checkStatus,                                       ");
        sql.append(" 	cf.fk_item_id as itemId,                                      ");
        sql.append(" 	ty.code as type,                                              ");
        sql.append(" 	ty.name as typeName                                           ");
        sql.append(" FROM                                                             ");
        sql.append(" 	check_flow cf                                                 ");
        sql.append(" INNER JOIN enum_const st ON st.id = cf.flag_check_status         ");
        sql.append(" INNER JOIN enum_const ty on ty.id = cf.flag_flow_type            ");
        sql.append(" INNER JOIN enum_const nc on nc.id = cf.flag_need_check        ");
        sql.append(" INNER JOIN pe_manager m ON m.fk_sso_user_id = cf.create_by       ");
        sql.append(" LEFT JOIN pe_unit un ON un.id = m.fk_unit_id                    ");
        sql.append(" INNER JOIN check_flow_record rec on rec.fk_check_flow_id = cf.id ");
        sql.append(" INNER JOIN enum_const ot on ot.id = rec.flag_check_operate       ");
        sql.append(" WHERE                                                            ");
        sql.append("    ot.code in ('1', '2')                                        ");
        sql.append(" AND rec.operate_user = '" + UserUtils.getCurrentManager().getId() + "' ");
        if (StringUtils.isNotBlank(flowType)) {
            sql.append(" AND ty.id = '" + flowType + "' ");
        }
        if (StringUtils.isNotBlank(needCheck)) {
            sql.append(" AND nc.code = '" + needCheck + "' ");
        }
        if (StringUtils.isNotBlank(checkStatus)) {
            sql.append(" AND st.code = '" + checkStatus + "' ");
        }
        if (StringUtils.isNotBlank(search)) {
            sql.append(" AND concat(ifnull(un.name, ''), ';', m.true_name, ';', cf.name) like '%" + search + "%' ");
        }
        sql.append(" GROUP BY                                                         ");
        sql.append(" 	cf.id                                                         ");
        sql.append(" ORDER BY cf.create_date desc ");
        sql.append(this.generatePageLimit(page));
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("page", page);
        resultMap.put("checkFlow", this.generalDao.getMapBySQL(sql.toString()));
        return resultMap;
    }
}
