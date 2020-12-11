package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.StationMessageGroupSign;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 超管站内信占位符管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("superAdminStationMessageSignManageService")
public class SuperAdminStationMessageSignManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<StationMessageGroupSign> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(StationMessageGroupSign bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(StationMessageGroupSign bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或修改前检查
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(StationMessageGroupSign bean) throws EntityException {
        String sql = "select 1 from station_message_group_sign where (name = '" + bean.getName() + "' or sign = '"
                + bean.getSign() + "') and fk_message_group_id = '" + bean.getStationMessageGroup().getId() + "'";
        if (StringUtils.isNotBlank(bean.getId())) {
            sql += " AND id <> '" + bean.getId() + "'";
        }
        List<Object> checkList = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new EntityException("此名称或占位符在组内已存在");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        String sql = "select 1 from station_message_group_sign si inner join enum_const ac on ac.id = si.flag_active" +
                " where ac.code = '1' and " + CommonUtils.madeSqlIn(idList, "si.id");
        List<Object> checkList = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new EntityException("存在是否有效为是的数据，无法删除");
        }
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "stationMessageGroup.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "stationMessageGroup.id";
    }

}
