package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.StationMessageGroupSite;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.service.impl.GridServiceImpl;
import com.whaty.dao.GeneralDao;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超管站内信站点管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("superAdminStationMessageSiteManageService")
public class SuperAdminStationMessageSiteManageServiceImpl extends GridServiceImpl<StationMessageGroupSite> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(StationMessageGroupSite bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(StationMessageGroupSite bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或修改前检查
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(StationMessageGroupSite bean) throws EntityException {
        String sql = "select 1 from station_message_group_site where fk_message_group_id = '"
                + bean.getStationMessageGroup().getId() + "' and fk_web_site_id = '"
                + bean.getPeWebSite().getId() + "'";
        if (StringUtils.isNotBlank(bean.getId())) {
            sql += " AND id <> '" + bean.getId() + "'";
        }
        List<Object> checkList = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new EntityException("此站点已设置此站内信组");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        String sql = "select 1 from station_message_group_site si inner join enum_const ac on ac.id = si.flag_active " +
                "where ac.code = '1' AND " + CommonUtils.madeSqlIn(idList, "si.id");
        List<Object> checkList = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new EntityException("存在有效的数据，无法删除");
        }
    }
}
