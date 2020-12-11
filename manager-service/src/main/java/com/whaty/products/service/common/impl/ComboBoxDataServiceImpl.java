package com.whaty.products.service.common.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SQLConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.common.ComboBoxDataService;
import com.whaty.util.SQLHandleUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

/**
 * comboBox数据查询服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("comboBoxDataService")
public class ComboBoxDataServiceImpl implements ComboBoxDataService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public List<Object[]> getIdNameListBySqlId(String sqlId, String params) {
        String sql = this.myGeneralDao.getOneBySQL("select sqlstr from system_mylist_sql where id = ?", sqlId);
        TycjParameterAssert.isAllNotNull(sql);
        //将参数字符串转换成集合
        String[] paramItems = params.split("&");
        for (String paramStr : paramItems) {
            String[] paramEntry = paramStr.split("=");
            String key = paramEntry[0].trim();
            String value = paramEntry[1].trim();
            //用params中的参数替换sql中的占位符
            String sign = "#" + key + "#";
            sql = sql.replace(sign, value);
        }
        //执行并返回数据
        return this.myGeneralDao.getBySQL(ScopeHandleUtils.handleScopeSignOfSql(sql.replace("${siteCode}",
                MasterSlaveRoutingDataSource.getDbType()), UserUtils.getCurrentUserId()));
    }

    @Override
    public List<Object[]> getIdNameListByBeanName(String beanName) {
        List<Object[]> list;
        if (beanName.startsWith(CommonConstant.BEAN_NAME_ENUM_CONST_PREFIX)) {
            String namespace = beanName.replace("EnumConstBy", "");
            //增加对于院校的判断
            String hql = "select id, name from EnumConst where namespace='" + namespace +
                    "' and (team is null or team like '%," + MasterSlaveRoutingDataSource.getDbType() +
                    ",%' or team = '') order by code";
            list = this.myGeneralDao.getByHQL(hql);
        } else if (beanName.contains("By")) {
            String hql = "select id, name from " + beanName.substring(0, beanName.indexOf("By")) + " order by " +
                    String.format(SQLConstant.HQL_GBK_CONVERT_STR, "name");
            list = this.myGeneralDao.getByHQL(hql);
        } else {
            return this.getBasicBeanDataList(beanName, CommonConstant.BEAN_NOT_NEED_ACTIVE);
        }
        return list;
    }

    @Override
    public List<Object[]> getBasicBeanDataList(String beanName, boolean beanIsActive) {
        return this.myGeneralDao.getByHQL(ScopeHandleUtils
                .handleScopeSignOfSql(BeanSearchHQL.getHQL(beanName, beanIsActive), UserUtils.getCurrentUserId()));
    }

    private enum BeanSearchHQL {

        PE_CLASS("PeClass", (siteCode, isActive) ->
                String.format("select id, name from PeClass where [peUnit|peUnit.id] and siteCode = '%s'", siteCode)),

        PE_PLACE("PePlace", (siteCode, isActive) ->
                String.format("select id, name from PePlace where [peUnit|peUnit.id] and siteCode = '%s'", siteCode)),

        PE_HOTEL("PeHotel", (siteCode, isActive) ->
                String.format("select id, name from PeHotel where siteCode = '%s'", siteCode)),

        PE_UNIT("PeUnit", (siteCode, isActive) ->
                String.format("select id, name from PeUnit where siteCode = '%s' and [peUnit|id]", siteCode)),
        COURSE_TIME("CourseTime", (siteCode, isActive) ->
                String.format("select id, concat(startTime, ' - ', endTime, ' ', name) as name from CourseTime " +
                                "where siteCode = '%s'", siteCode)),

        MOTORCADE("Motorcade", (siteCode, isActive) ->
                String.format("select id, name from Motorcade where siteCode = '%s'", siteCode))
        ;

        private String beanName;

        private BiFunction<String, Boolean, String> hqlFunction;

        BeanSearchHQL(String beanName, BiFunction<String, Boolean, String> hqlFunction) {
            this.beanName = beanName;
            this.hqlFunction = hqlFunction;
        }

        static String getHQL(String beanName, boolean isActive) {
            BeanSearchHQL beanSearchHQL = Arrays.stream(values())
                    .filter(e -> e.getBeanName().equals(beanName)).findFirst().orElse(null);
            if (beanSearchHQL == null) {
                throw new IllegalArgumentException();
            }
            return beanSearchHQL.getHqlFunction().apply(MasterSlaveRoutingDataSource.getDbType(), isActive);
        }

        public String getBeanName() {
            return beanName;
        }

        public BiFunction<String, Boolean, String> getHqlFunction() {
            return hqlFunction;
        }
    }

    @Override
    public List<Object[]> listRoleUserCanView() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                 ");
        sql.append(" 	ro.CODE as code,                                    ");
        sql.append(" 	ro.NAME as name                                     ");
        sql.append(" FROM                                                   ");
        sql.append(" 	pe_pri_role ro                                      ");
        sql.append(" INNER JOIN enum_const ty ON ty.id = ro.FLAG_ROLE_TYPE  ");
        sql.append(" WHERE                                                  ");
        sql.append(" 	ty. CODE = '3'                                      ");
        sql.append(" AND ro. fk_web_site_id = ?                             ");
        sql.append(" AND ro.id <> ?                                         ");
        return this.myGeneralDao.getBySQL(sql.toString(), SiteUtil.getSiteId(),
                this.userService.getCurrentUser().getRole().getId());
    }

}
