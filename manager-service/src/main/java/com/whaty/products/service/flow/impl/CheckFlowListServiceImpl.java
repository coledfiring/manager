package com.whaty.products.service.flow.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.flow.CheckFlowListService;
import com.whaty.products.service.flow.strategy.list.AbstractCheckFlowListStrategy;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

/**
 * 审核列表
 *
 * @author weipengsen
 */
@Lazy
@Service("checkFlowListService")
public class CheckFlowListServiceImpl implements CheckFlowListService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> getCheckFlow(String checkStatus, String needCheck, String flowType, String search,
                                            String showType, Map<String, Object> page) {
        TycjParameterAssert.isAllNotBlank(showType);
        TycjParameterAssert.isAllNotNull(page);
        return CheckFlowSearch.getStrategy(showType).listCheckFlow(checkStatus, needCheck, flowType, search, page);
    }

    @Override
    public int countWaitCheck() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                        ");
        sql.append(" 	count(1)                                                   ");
        sql.append(" FROM                                                          ");
        sql.append(" 	check_flow cf                                              ");
        sql.append(" INNER JOIN enum_const st ON st.id = cf.flag_check_status      ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = cf.flag_active            ");
        sql.append(" INNER JOIN enum_const ty on ty.id = cf.flag_flow_type         ");
        sql.append(" INNER JOIN pe_manager m ON m.fk_sso_user_id = cf.create_by    ");
        sql.append(" LEFT JOIN pe_unit un ON un.id = m.fk_unit_id                  ");
        sql.append(" WHERE                                                         ");
        sql.append(" 	st.code = '1'                                              ");
        sql.append(" and (cf.scope_id is null or [peUnit|cf.scope_id])             ");
        sql.append(" AND (cf.auditors like '%" + UserUtils.getCurrentManager().getId() + "%' ");
        sql.append(" OR cf.auditors like '%" + UserUtils.getCurrentUser().getPePriRole().getId() + "%')");
        return this.generalDao.<BigInteger>getOneBySQL(ScopeHandleUtils.handleScopeSignOfSql(sql.toString(),
                UserUtils.getCurrentUserId())).intValue();
    }

    private enum CheckFlowSearch {

        /**
         * 待我审核
         */
        WAIT_CHECK_FLOW_SEARCH("wait", "waitCheckFlowStrategy"),
        /**
         * 我申请的
         */
        APPLY_CHECK_FLOW_SEARCH("apply", "applyCheckFlowStrategy"),
        /**
         * 我审核的
         */
        CHECKED_CHECK_FLOW_SEARCH("checked", "checkedCheckFlowStrategy"),
        /**
         * 抄送我的
         */
        COPY_TO_CHECK_FLOW_SEARCH("copyTo", "copyToCheckFlowStrategy"),
        ;

        private String showType;

        private String springBean;

        CheckFlowSearch(String showType, String springBean) {
            this.showType = showType;
            this.springBean = springBean;
        }

        static AbstractCheckFlowListStrategy getStrategy(String showType) {
            CheckFlowSearch search = Arrays.stream(values())
                    .filter(e -> e.getShowType().equals(showType)).findFirst().orElse(null);
            if (search == null) {
                throw new IllegalArgumentException();
            }
            return (AbstractCheckFlowListStrategy) SpringUtil.getBean(search.getSpringBean());
        }

        public String getShowType() {
            return showType;
        }

        public String getSpringBean() {
            return springBean;
        }
    }

}
