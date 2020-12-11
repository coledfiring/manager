package com.whaty.products.service.flow.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.flow.CheckFlowDesignService;
import com.whaty.products.service.flow.domain.CheckFlowPage;
import com.whaty.products.service.flow.domain.config.CheckFlowConfig;
import com.whaty.products.service.flow.domain.config.CheckFlowConfigNode;
import com.whaty.util.TycjCollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Map;

/**
 * 审核流程设计
 *
 * @author weipengsen
 */
@Lazy
@Service("checkFlowDesignService")
public class CheckFlowDesignServiceImpl implements CheckFlowDesignService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public CheckFlowConfig getCheckFlowGroup(String groupId) {
        TycjParameterAssert.isAllNotBlank(groupId);
        return new CheckFlowConfig(groupId).build();
    }

    @Override
    public Map<String, Object> listRoles(CheckFlowPage page) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                ");
        sql.append(" 	count(ro.id)                                       ");
        sql.append(" FROM                                                  ");
        sql.append(" 	pe_pri_role ro                                     ");
        sql.append(" INNER JOIN enum_const rt ON rt.id = ro.FLAG_ROLE_TYPE ");
        sql.append(" WHERE                                                 ");
        sql.append(" 	rt. CODE IN ('3', '9998')                          ");
        sql.append(" AND ro.site_code = '" + SiteUtil.getSiteCode() + "'   ");
        if (StringUtils.isNotBlank(page.getSearch()) && !"null".equals(page.getSearch())) {
            sql.append(" AND ro.name like '%" + page.getSearch() + "%'     ");
        }
        Integer totalNum = this.generalDao.<BigInteger>getOneBySQL(sql.toString()).intValue();
        page.countTotalPage(totalNum);
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                ");
        sql.append(" 	ro.id AS id,                                       ");
        sql.append(" 	ro. NAME AS name                                   ");
        sql.append(" FROM                                                  ");
        sql.append(" 	pe_pri_role ro                                     ");
        sql.append(" INNER JOIN enum_const rt ON rt.id = ro.FLAG_ROLE_TYPE ");
        sql.append(" WHERE                                                 ");
        sql.append(" 	rt. CODE IN ('3', '9998')                          ");
        sql.append(" AND ro.site_code = '" + SiteUtil.getSiteCode() + "'   ");
        if (StringUtils.isNotBlank(page.getSearch()) && !"null".equals(page.getSearch())) {
            sql.append(" AND ro.name like '%" + page.getSearch() + "%'     ");
        }
        sql.append(" ORDER BY                                              ");
        sql.append(" 	ro.id                                              ");
        sql.append(" LIMIT ?, ?                                            ");
        return TycjCollectionUtils.map("page", page, "data", this.generalDao.getMapBySQL(sql.toString(),
                page.countStartLimit(), page.getPageSize()));
    }

    @Override
    public Map<String, Object> listManagers(CheckFlowPage page) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                 ");
        sql.append(" 	count(m.id)                                         ");
        sql.append(" FROM                                                   ");
        sql.append(" 	pe_manager m                                        ");
        sql.append(" INNER JOIN enum_const ac on ac.id = m.FLAG_ACTIVE      ");
        sql.append(" INNER JOIN sso_user ss on ss.id = m.FK_SSO_USER_ID     ");
        sql.append(" INNER JOIN pe_pri_role ro on ro.id = ss.FK_ROLE_ID     ");
        sql.append(" INNER JOIN enum_const rt ON rt.id = ro.FLAG_ROLE_TYPE  ");
        sql.append(" WHERE                                                  ");
        sql.append(" 	rt. CODE IN ('3', '9998')                           ");
        sql.append(" AND ac.code = '1'                                      ");
        sql.append(" AND m.site_code = '" + SiteUtil.getSiteCode() + "'     ");
        if (StringUtils.isNotBlank(page.getSearch()) && !"null".equals(page.getSearch())) {
            sql.append(" AND m.true_name like '%" + page.getSearch() + "%'  ");
        }
        Integer totalNum = this.generalDao.<BigInteger>getOneBySQL(sql.toString()).intValue();
        page.countTotalPage(totalNum);
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                 ");
        sql.append(" 	m.id AS id,                                         ");
        sql.append(" 	concat(m. TRUE_NAME, '(', m.login_id, ')') AS name  ");
        sql.append(" FROM                                                   ");
        sql.append(" 	pe_manager m                                        ");
        sql.append(" INNER JOIN enum_const ac on ac.id = m.FLAG_ACTIVE      ");
        sql.append(" INNER JOIN sso_user ss on ss.id = m.FK_SSO_USER_ID     ");
        sql.append(" INNER JOIN pe_pri_role ro on ro.id = ss.FK_ROLE_ID     ");
        sql.append(" INNER JOIN enum_const rt ON rt.id = ro.FLAG_ROLE_TYPE  ");
        sql.append(" WHERE                                                  ");
        sql.append(" 	rt. CODE IN ('3', '9998')                           ");
        sql.append(" AND ac.code = '1'                                      ");
        sql.append(" AND m.site_code = '" + SiteUtil.getSiteCode() + "'     ");
        if (StringUtils.isNotBlank(page.getSearch()) && !"null".equals(page.getSearch())) {
            sql.append(" AND m.true_name like '%" + page.getSearch() + "%'  ");
        }
        sql.append(" ORDER BY                                               ");
        sql.append(" 	m.id                                                ");
        sql.append(" LIMIT ?, ?                                             ");
        return TycjCollectionUtils.map("page", page, "data", this.generalDao.getMapBySQL(sql.toString(),
                page.countStartLimit(), page.getPageSize()));
    }

    @Override
    public void saveNode(CheckFlowConfigNode node) {
        TycjParameterAssert.validatePass(node);
        node.save();
    }

    @Override
    public void addFirstNode(String groupId) {
        TycjParameterAssert.isAllNotBlank(groupId);
        new CheckFlowConfig(groupId).addFirstNode();
    }

    @Override
    public void addNodeAfter(String groupId, String previousId) {
        TycjParameterAssert.isAllNotBlank(groupId, previousId);
        new CheckFlowConfig(groupId).addNodeAfter(previousId);
    }

    @Override
    public void deleteNode(String groupId, String nodeId) {
        TycjParameterAssert.isAllNotBlank(groupId, nodeId);
        new CheckFlowConfig(groupId).deleteNode(nodeId);
    }

    @Override
    public void saveCopyPerson(CheckFlowConfig config) {
        TycjParameterAssert.validatePass(config);
        config.saveCopyPersons();
    }

    @Override
    public Boolean isActive(String groupId) {
        TycjParameterAssert.isAllNotBlank(groupId);
        return new CheckFlowConfig(groupId).isActive();
    }

}
