package com.whaty.products.service.information.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeBulletin;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.whaty.products.service.information.constant.InformationConstant.ENUM_CONST_NAMESPACE_FLAG_BULLETIN_TYPE;

/**
 * 公告列表服务类
 * @author weipengsen
 */
@Lazy
@Service("peBulletinService")
public class PeBulletinServiceImpl extends TycjGridServiceAdapter<PeBulletin> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public DetachedCriteria initDetachedCriteria(GridConfig gridConfig, Map<String, Object> mapParam) {
        DetachedCriteria dc = DetachedCriteria.forClass(PeBulletin.class);
        dc.add(Restrictions.eq("siteCode", MasterSlaveRoutingDataSource.getDbType()));
        dc.createAlias("enumConstByFlagIsvalid", "enumConstByFlagIsvalid");
        dc.createAlias("enumConstByFlagIstop", "enumConstByFlagIstop");
        dc.createAlias("enumConstByFlagBulletinType", "enumConstByFlagBulletinType");
        //判断否是为新闻
        dc.add(Restrictions.eq("enumConstByFlagBulletinType.id",
                this.myGeneralDao.getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_BULLETIN_TYPE,
                        "peBulletinNews".equals(gridConfig.getGridId()) ? "1" : "0").getId()));
        List<String> userIdList = getSsoUserWrapper();
        dc.createAlias("ssoUser", "ssoUser");
        dc.add(Restrictions.in("ssoUser.id", userIdList));
        return dc;
    }

    @Override
    protected String getOrderColumnIndex() {
        return "publishDate";
    }

    @Override
    protected String getOrderWay() {
        return "desc";
    }


    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        for (Object id : idList) {
            String active = this.myGeneralDao.getById(PeBulletin.class, id.toString())
                    .getEnumConstByFlagIsvalid().getCode();
            if ("1".equals(active)) {
                throw new EntityException("包含有有效状态的公告信息，删除失败！");
            }
        }
    }

    /**
     * 获得横向权限允许的用户
     * @return
     */
    private List getSsoUserWrapper() {
        String sqlSite = "select id from pr_pri_manager_unit where FK_SSO_USER_ID = ?";
        int intSize = this.myGeneralDao.getBySQL(sqlSite, UserUtils.getCurrentUserId()).size();
        String sqlS = "SELECT * from pe_unit where site_code = ?";
        int intS = this.myGeneralDao.getBySQL(sqlS, MasterSlaveRoutingDataSource.getDbType()).size();

        //对于上述的四种层面  有全选或者全部选即是 总站中的总站，具有所有对于其他分站的管理权限
        List<String> userIdList = new ArrayList<>();
        if (intSize == 0 || intSize == intS) {
            userIdList = this.myGeneralDao.getBySQL("select DISTINCT FK_SSO_USER_ID from pe_bulletin");
        }
        if (CollectionUtils.isEmpty(userIdList)) {
            userIdList.add(UserUtils.getCurrentUserId());
        }
        return userIdList;
    }
}
