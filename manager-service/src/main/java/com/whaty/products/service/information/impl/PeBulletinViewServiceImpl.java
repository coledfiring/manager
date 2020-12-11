package com.whaty.products.service.information.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeBulletin;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.common.ComboBoxDataService;
import com.whaty.products.service.information.constant.InformationConstant;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公告列表服务类
 *
 * @author hanshichao
 */
@Lazy
@Service("peBulletinViewService")
public class PeBulletinViewServiceImpl extends TycjGridServiceAdapter<PeBulletin> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = "comboBoxDataService")
    private ComboBoxDataService comboBoxDataService;

    @Override
    public DetachedCriteria initDetachedCriteria(GridConfig gridConfig, Map<String, Object> mapParam) {
        DetachedCriteria dc = DetachedCriteria.forClass(PeBulletin.class);
        dc.add(Restrictions.eq("siteCode", MasterSlaveRoutingDataSource.getDbType()));
        dc.createAlias("enumConstByFlagIsvalid", "enumConstByFlagIsvalid")
                .createAlias("enumConstByFlagIstop", "enumConstByFlagIstop")
                .createAlias("enumConstByFlagBulletinType", "enumConstByFlagBulletinType")
                .createAlias("ssoUser", "ssoUser");
        dc.add(Restrictions.eq("enumConstByFlagIsvalid.code", "1"));
        // 0 - 普通公告
        dc.add(Restrictions.eq("enumConstByFlagBulletinType.code", "0"));
        dc.add(Restrictions.like("scopeString", "223", MatchMode.ANYWHERE));
        List<String> unitList = this.myGeneralDao
                .getBySQL("select fk_item_id from pr_pri_manager_unit where fk_sso_user_id = ?", UserUtils.getCurrentUserId());
        if (CollectionUtils.isNotEmpty(unitList)) {
            Disjunction dis = Restrictions.disjunction();
            unitList.forEach(unit -> dis.add(Restrictions.like("scopeString", unit, MatchMode.ANYWHERE)));
            dis.add(Restrictions.sqlRestriction(" scope_string not like '%unit%' "));
            dc.add(dis);
        }
        dc.addOrder(Order.desc("enumConstByFlagIstop.code"))
                .addOrder(Order.desc("publishDate"));
        return dc;
    }

    /**
     * 查看公告详情
     *
     * @param ids
     * @return
     */
    @LogAndNotice("查看公告详情")
    public Map<String, Object> viewDetail(String ids) {
        setIsRead(ids);
        return  Collections.singletonMap("peBulletin", this.myGeneralDao.getById(PeBulletin.class, ids));
    }

    /**
     * 设置公告为已读
     *
     * @param id
     */
    public void setIsRead(String id) {
        if (!this.myGeneralDao.checkNotEmpty("SELECT 1 FROM pr_bulletin_user  WHERE FK_BULLETIN_ID= ? " +
                " AND FK_SSO_USER_ID= ?" , id, UserUtils.getCurrentUserId())) {
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO `pr_bulletin_user` (      ");
            sql.append(" 	`ID`,                              ");
            sql.append(" 	`FK_SSO_USER_ID`,                  ");
            sql.append(" 	`FK_BULLETIN_ID`,                  ");
            sql.append(" 	`READ_TYPE`                        ");
            sql.append(" )                                     ");
            sql.append(" VALUES                                ");
            sql.append(" 	(                                  ");
            sql.append(" 	  uuid(),                          ");
            sql.append("'" + UserUtils.getCurrentUserId() + "',");
            sql.append("'" + id + "',                          ");
            sql.append("'PC'                                   ");
            sql.append("    )                                  ");
            this.myGeneralDao.executeBySQL(sql.toString());
        }
    }

    /**
     * 获取下拉选项
     *
     * @return
     */
    public Map<String, Object> getAllSelectData() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("unit", comboBoxDataService.getIdNameListByBeanName("PeUnit"));
        return resultMap;
    }

    /**
     * 保存公告
     *
     * @param formMap
     */
    @LogAndNotice("保存公告")
    public void saveBulletin(Map<String, Object> formMap) {
        String id = (String) formMap.get(InformationConstant.PARAM_BULLETIN_ID);
        PeBulletin bulletin = StringUtils.isNotBlank(id) ? this.myGeneralDao.getById(PeBulletin.class, id) : new PeBulletin();
        StringBuilder scopeString = new StringBuilder("223,");
        Map<String, Object> formData = (Map<String, Object>) formMap.get(InformationConstant.PARAM_BULLETIN_FORM);
        if ((boolean) formData.get(InformationConstant.PARAM_BULLETIN_FORM_MANAGERS)) {
            scopeString.append("225,");
        }
        if ((boolean) formData.get(InformationConstant.PARAM_BULLETIN_FORM_OPERATOR)) {
            scopeString.append("229,");
        }
        String title = (String) formData.get(InformationConstant.PARAM_BULLETIN_TITLE);
        EnumConst enumConstIsValid = this.myGeneralDao
                .getEnumConstByNamespaceAndName(InformationConstant.ENUM_CONST_NAMESPACE_IS_VALID,
                        (String) formData.get(InformationConstant.PARAM_BULLETIN_FORM_ISVALID));
        EnumConst enumConstIsTop = this.myGeneralDao
                .getEnumConstByNamespaceAndName(InformationConstant.ENUM_CONST_NAMESPACE_IS_TOP,
                        (String) formData.get(InformationConstant.PARAM_BULLETIN_FORM_ISTOP));
        EnumConst enumConstFlagBulletinType = this.myGeneralDao
                .getEnumConstByNamespaceCode(InformationConstant.ENUM_CONST_NAMESPACE_FLAG_BULLETIN_TYPE,
                        (String) formData.get(InformationConstant.ENUM_CONST_NAMESPACE_FLAG_BULLETIN_TYPE));
        bulletin.setEnumConstByFlagIsvalid(enumConstIsValid);
        bulletin.setEnumConstByFlagIstop(enumConstIsTop);
        bulletin.setEnumConstByFlagBulletinType(enumConstFlagBulletinType);
        bulletin.setScopeString(scopeString.toString());
        bulletin.setNote((String) formData.get(InformationConstant.PARAM_BULLETIN_FORM_CONTENT));
        SsoUser ssoUser = new SsoUser();
        ssoUser.setId(userService.getCurrentUser().getId());
        bulletin.setSsoUser(ssoUser);
        bulletin.setTitle(title);
        bulletin.setPublishDate(new Date());
        bulletin.setSiteCode(MasterSlaveRoutingDataSource.getDbType());
        this.myGeneralDao.save(bulletin);
    }

    /**
     * 获取公告信息
     *
     * @param id
     * @return
     */
    public Object getBulletinInfo(String id) {
        return this.myGeneralDao.getById(PeBulletin.class, id);
    }

    /**
     * 列举可选班级
     * @param search
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Map<String, Object> listClasses(String search, Integer currentPage, Integer pageSize) {
        if (PlatformConfigUtil.isOnlineTraining(SiteUtil.getSiteCode())) {
            return this.listOlClasses(search, currentPage, pageSize);
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                 ");
        sql.append(" 	count(cl.id)                                        ");
        sql.append(" FROM                                                   ");
        sql.append(" 	pe_class cl                                         ");
        sql.append(" WHERE                                                  ");
        sql.append(" 	[peUnit|cl.fk_unit_id]                              ");
        sql.append(" AND DATEDIFF(cl.end_time,now()) < 180                  ");
        sql.append(" AND cl.site_code = '" + SiteUtil.getSiteCode() + "'    ");
        if (StringUtils.isNotBlank(search) && !"null".equals(search)) {
            sql.append(" AND cl.name like '%" + search + "%'                ");
        }
        String userId = UserUtils.getCurrentUserId();
        Integer totalNum = this.myGeneralDao.<BigInteger>getOneBySQL(ScopeHandleUtils
                .handleScopeSignOfSql(sql.toString(), userId)).intValue();
        Integer totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : (totalNum / pageSize + 1);
        currentPage = currentPage > totalPage ? totalPage : currentPage;
        currentPage = currentPage <= 1 ? 1 : currentPage;
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                 ");
        sql.append(" 	cl.id as id,                                        ");
        sql.append(" 	cl.name as name                                     ");
        sql.append(" FROM                                                   ");
        sql.append(" 	pe_class cl                                         ");
        sql.append(" WHERE                                                  ");
        sql.append(" 	[peUnit|cl.fk_unit_id]                              ");
        sql.append(" AND DATEDIFF(cl.end_time,now()) < 180                  ");
        sql.append(" AND cl.site_code = '" + SiteUtil.getSiteCode() + "'    ");
        if (StringUtils.isNotBlank(search) && !"null".equals(search)) {
            sql.append(" AND cl.name like '%" + search + "%'                ");
        }
        sql.append(" ORDER BY                                               ");
        sql.append(" 	cl.create_date desc                                 ");
        sql.append(" LIMIT ?, ?                                             ");
        List<Map<String, Object>> data = this.myGeneralDao.getMapBySQL(ScopeHandleUtils
                .handleScopeSignOfSql(sql.toString(), userId), (currentPage - 1) * pageSize, pageSize);
        Map<String, Object> result = new HashMap<>(2);
        result.put("data", data);
        Map<String, Object> page = new HashMap<>(4);
        page.put("currentPage", currentPage);
        page.put("pageSize", pageSize);
        page.put("search", search);
        page.put("totalPage", totalPage);
        result.put("page", page);
        return result;
    }

    /**
     * 列举可选班级
     * @param search
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Map<String, Object> listOlClasses(String search, Integer currentPage, Integer pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                 ");
        sql.append(" 	count(cl.id)                                        ");
        sql.append(" FROM                                                   ");
        sql.append(" 	ol_pe_class cl                                      ");
        sql.append(" WHERE                                                  ");
        sql.append("    cl.site_code = '" + SiteUtil.getSiteCode() + "'     ");
        if (StringUtils.isNotBlank(search) && !"null".equals(search)) {
            sql.append(" AND cl.name like '%" + search + "%'                ");
        }
        String userId = UserUtils.getCurrentUserId();
        Integer totalNum = this.myGeneralDao.<BigInteger>getOneBySQL(ScopeHandleUtils
                .handleScopeSignOfSql(sql.toString(), userId)).intValue();
        Integer totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : (totalNum / pageSize + 1);
        currentPage = currentPage > totalPage ? totalPage : currentPage;
        currentPage = currentPage <= 1 ? 1 : currentPage;
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                 ");
        sql.append(" 	cl.id as id,                                        ");
        sql.append(" 	cl.name as name                                     ");
        sql.append(" FROM                                                   ");
        sql.append(" 	ol_pe_class cl                                      ");
        sql.append(" WHERE                                                  ");
        sql.append("   cl.site_code = '" + SiteUtil.getSiteCode() + "'      ");
        if (StringUtils.isNotBlank(search) && !"null".equals(search)) {
            sql.append(" AND cl.name like '%" + search + "%'                ");
        }
        sql.append(" ORDER BY                                               ");
        sql.append(" 	cl.create_date desc                                 ");
        sql.append(" LIMIT ?, ?                                             ");
        List<Map<String, Object>> data = this.myGeneralDao.getMapBySQL(ScopeHandleUtils
                .handleScopeSignOfSql(sql.toString(), userId), (currentPage - 1) * pageSize, pageSize);
        Map<String, Object> result = new HashMap<>(2);
        result.put("data", data);
        Map<String, Object> page = new HashMap<>(4);
        page.put("currentPage", currentPage);
        page.put("pageSize", pageSize);
        page.put("search", search);
        page.put("totalPage", totalPage);
        result.put("page", page);
        return result;
    }
}
