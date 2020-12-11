package com.whaty.products.service.resource.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePlace;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.doamin.ExcelCheckResult;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地点资源管理
 *
 * @author weipengsen
 */
@Lazy
@Service("placeResourceManageService")
public class PlaceResourceManageServiceImpl extends TycjGridServiceAdapter<PePlace> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PePlace bean) throws EntityException {
        List<String> unitIds = ScopeHandleUtils.getScopeIdsByBeanAlias("PeUnit", UserUtils.getCurrentUserId());
        if (CollectionUtils.isNotEmpty(unitIds) && !unitIds.contains(bean.getPeUnit().getId())) {
            throw new ServiceException("所选单位不在权限范围内;");
        }
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(PePlace bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateBy(UserUtils.getCurrentUser());
        bean.setUpdateDate(new Date());
    }

    private void checkBeforeAddOrUpdate(PePlace bean) throws EntityException {
        String additionalSql = StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_place where name = ? and site_code = ?" +
                additionalSql, bean.getName(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("此名称已存在");
        }
    }

    @Override
    protected ExcelCheckResult checkExcelImportError(List<PePlace> beanList, Workbook workbook,
                                                     ExcelCheckResult excelCheckResult) {
        Map<String, Map<String, List<Integer>>> sameDataMap = new HashMap<>();
        sameDataMap.put("表格中已存在相同场地名称的数据；", this.checkSameField(beanList, Arrays.asList("name")));
        return this.checkSameData(sameDataMap, workbook, excelCheckResult);
    }
}
