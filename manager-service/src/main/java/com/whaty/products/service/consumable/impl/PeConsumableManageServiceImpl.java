package com.whaty.products.service.consumable.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.grid.bean.ColumnConfig;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeConsumable;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.function.Functions;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.util.SQLHandleUtils;
import com.whaty.util.TycjCollectionUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.whaty.constant.EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_IS_CHECK;
import static com.whaty.constant.EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_USE_STATUS;
import static java.util.Objects.isNull;

/**
 * 易耗品管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("peConsumableManageService")
public class PeConsumableManageServiceImpl extends TycjGridServiceAdapter<PeConsumable> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 自定义列配置dataindex前缀
     */
    private static final String COLUMNCONFIG_DATAINDEX_PREFIX = "peConsumableItems_";

    @Override
    public void checkBeforeAdd(PeConsumable bean) throws EntityException {
        //物品数量过滤
        this.filterConsumableItems(bean);
        if (MapUtils.isEmpty(bean.getPeConsumableItems())) {
            throw new EntityException("申请易耗品的数量不能都为0");
        }
        //单位验证
        bean.setPeUnit(Optional.ofNullable(UserUtils.getCurrentUnit()).filter(e -> StringUtils.isNotBlank(e.getId()))
                .orElseThrow(() -> new EntityException("当前用户不可申请物品领用")));
        bean.setApplicantUser(UserUtils.getCurrentUser());
        bean.setApplicantTime(new Date());
        //设置为待申请，未受理，未验收
        bean.setEnumConstByFlagUseStatus(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_USE_STATUS, "0"));
        bean.setEnumConstByFlagCheckStatus(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_IS_CHECK, "1"));
    }

    @Override
    public void checkBeforeUpdate(PeConsumable bean) throws EntityException {
        //已提交审批的不可修改
        Map<String, Object> result = this.generalDao.getOneMapBySQL("select ec.`CODE` AS code " +
                " FROM pe_consumable pc " +
                " INNER JOIN enum_const ec ON ec.id = pc.flag_use_status WHERE pc.id = ? ", bean.getId());
        TycjParameterAssert.isAllNotEmpty(result);
        if (!"0".equals(result.get("code"))) {
            throw new EntityException("数据已申请，不可修改");
        }
        //物品数量过滤
        this.filterConsumableItems(bean);
        if (MapUtils.isEmpty(bean.getPeConsumableItems())) {
            throw new EntityException("申请易耗品的数量不能都为0");
        }
    }

    /**
     *  过滤掉为空或数量不大于零的 ConsumableItems值
     *
     * @param bean
     * @return
     */
    private void filterConsumableItems(PeConsumable bean) {
        bean.setPeConsumableItems(bean.getPeConsumableItems().entrySet().stream()
                .filter(e -> StringUtils.isNotBlank(e.getKey()))
                .filter(e -> Optional.ofNullable(e.getValue()).orElse(0) > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Override
    protected void afterAdd(PeConsumable bean) throws EntityException {
        //添加物品项操作
        StringBuilder sql = new StringBuilder();
        try {
            this.generalDao.flush();
            sql.append(" INSERT INTO pe_consumable_detail (id, fk_consumable_item_id, use_number, fk_consumable_id) ");
            sql.append(" VALUES ");
            String valueSql = " (REPLACE(UUID(),'-',''),'%s','%s', '%s'),";
            bean.getPeConsumableItems().entrySet().stream()
                    .map(e -> String.format(valueSql, e.getKey(), e.getValue(), bean.getId()))
                    .forEach(sql::append);
            sql.deleteCharAt(sql.lastIndexOf(","));
            this.generalDao.executeBySQL(sql.toString());
            super.afterAdd(bean);
        } catch (Exception e) {
            this.generalDao.executeBySQL(" DELETE FROM pe_consumable WHERE id = ? ", bean.getId());
            throw e;
        }
    }

    @Override
    protected void afterUpdate(PeConsumable bean) throws EntityException {
        //更新物品项操作
        this.generalDao.flush();
        Set<String> items = new HashSet<>(generalDao.getBySQL("SELECT fk_consumable_item_id AS id " +
                " FROM pe_consumable_detail WHERE fk_consumable_id = ? ", bean.getId()));
        //在items中的执行update
        String updateSql = " UPDATE pe_consumable_detail SET use_number = '%s' WHERE fk_consumable_item_id = '%s' ";
        List<String> sqlList = bean.getPeConsumableItems().entrySet().stream()
                .filter(e -> items.contains(e.getKey()))
                .map(e -> String.format(updateSql, e.getValue(), e.getKey())).collect(Collectors.toList());

        //不在items中的执行insert
        String valueSqlFormat = " (REPLACE(UUID(),'-',''),'%s','%s', '%s'),";
        String valueSql = bean.getPeConsumableItems().entrySet().stream()
                .filter(e -> !items.contains(e.getKey()))
                .map(e -> String.format(valueSqlFormat, e.getKey(), e.getValue(), bean.getId()))
                .reduce("", Functions.stringAppend);
        if (StringUtils.isNotBlank(valueSql)) {
            sqlList.add(" INSERT INTO pe_consumable_detail (id, fk_consumable_item_id, use_number, fk_consumable_id) " +
                    " VALUES " + valueSql.substring(0, valueSql.lastIndexOf(",")));
        }
        //执行sql
        generalDao.batchExecuteSql(sqlList);
        super.afterAdd(bean);
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        //已提交审批的不可修改 已验收的不可修改
        if (this.generalDao.checkNotEmpty("select 1 from pe_consumable pc " +
                "INNER JOIN enum_const ec ON ec.id = pc.flag_use_status " +
                "where ec.`CODE` <> '0' AND " + CommonUtils.madeSqlIn(idList, "pc.id"))) {
            throw new ServiceException("存在已申请的，不可删除");
        }
    }


    /**
     * 验收校验
     *
     * @param ids
     */
    public void doCheck(String ids) {
        TycjParameterAssert.isAllNotNull(ids);
        List<Map<String, Object>> result = this.generalDao.getMapBySQL(
                "select fus.code as useStatus, fcs.code as checkStatus, pc.check_price AS checkPrice " +
                " from pe_consumable pc " +
                " LEFT JOIN enum_const fus ON fus.id = pc.flag_use_status " +
                " LEFT JOIN enum_const fcs ON fcs.id = pc.flag_check_status " +
                " where " + CommonUtils.madeSqlIn(ids, "pc.id"));
        TycjParameterAssert.isAllNotEmpty(result);
        // 2 - flagUseStatus 审批通过的
        if (!result.stream().allMatch(e -> "2".equals(e.get("useStatus")))) {
            throw new ServiceException("未通过申请的，不可验收");
        }
        // 0 - flagIsCheck 已验收的
        if (result.stream().anyMatch(e -> "0".equals(e.get("checkStatus")))) {
            throw new ServiceException("存在已验收的数据，不可重复验收");
        }
        // 校验金额不为空
        if (result.stream().anyMatch(e -> isNull(e.get("checkPrice")))) {
            throw new ServiceException("存在费用未设置的数据，不可验收");
        }
        this.generalDao.executeBySQL("UPDATE pe_consumable SET flag_check_status = ?, check_time = ?, " +
                        "fk_check_user_id = ? where " + CommonUtils.madeSqlIn(ids, "id"),
                this.generalDao.getEnumConstByNamespaceCode(TrainingConstant.FLAG_IS_CHECK, "0").getId(),
                new Date(), UserUtils.getCurrentUser().getId());
    }

    /**
     * 检查领用状态
     *
     * @param ids
     * @return
     */
    private boolean checkFlagUseStatus(String ids) {
        return this.generalDao.checkNotEmpty("select 1 from pe_consumable pc " +
                "INNER JOIN enum_const ec ON ec.id = pc.flag_use_status " +
                "where ec.`CODE` <> '2' AND " + CommonUtils.madeSqlIn(ids, "pc.id"));
    }

    /**
     * 检查验收状态
     *
     * @param ids
     * @return
     */
    private boolean checkFlagCheckStatus(String ids) {
        return this.generalDao.checkNotEmpty("select 1 from pe_consumable pc " +
                "INNER JOIN enum_const ec ON ec.id = pc.flag_check_status " +
                "where ec.`CODE` = '0' AND " + CommonUtils.madeSqlIn(ids, "pc.id"));
    }

    /**
     * 设置费用
     *
     * @param ids
     * @param price
     */
    public void doSetPrice(String ids, String price) throws ServiceException {
        TycjParameterAssert.isAllNotNull(ids, price);
        if (this.checkFlagUseStatus(ids)) {
            throw new ServiceException("未通过申请的，不可设置验收费用");
        }
        if (this.checkFlagCheckStatus(ids)) {
            throw new ServiceException("已验收的数据，不可修改验收费用");
        }
        this.generalDao.executeBySQL(
                "UPDATE pe_consumable SET check_price = ? where " + CommonUtils.madeSqlIn(ids, "id"), price);
    }

    /**
     * 列举表单选项
     *
     * @return
     */
    public List<ColumnConfig> listFormItems() {
        List<Map<String, Object>> formItems = generalDao.getMapBySQL(
                "  SELECT pci.id AS id, concat(pci.name,'数量') AS name \n" +
                        "  FROM pe_consumable_item pci\n" +
                        "  INNER JOIN enum_const fct on pci.flag_consumable_type = fct.id\n" +
                        "  INNER JOIN enum_const fiv on pci.flag_is_valid = fiv.id AND fiv.code = '1'\n" +
                        "  WHERE pci.site_code = ? ", SiteUtil.getSiteCode());
        return formItems.stream().map(this::generateGridColumn).collect(Collectors.toList());
    }


    /**
     * 生成grid column配置
     *
     * @param item
     * @return
     */
    private ColumnConfig generateGridColumn(Map<String, Object> item) {
        ColumnConfig columnConfig = new ColumnConfig((String) item.get("name"), "0");
        columnConfig.setDataIndex(COLUMNCONFIG_DATAINDEX_PREFIX + item.get("id"));
        columnConfig.setDataColumn("peConsumableItems." + item.get("id"));
        columnConfig.setSearch(false);
        columnConfig.setList(false);
        columnConfig.setAdd(true);
        columnConfig.setUpdate(true);
        columnConfig.setDefaultText("0");
        columnConfig.setAllowBlank(true);
        columnConfig.setCheckMessage("数量0~9999");
        columnConfig.setCheckRegular("^[0-9]{0,4}$");
        return columnConfig;
    }

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map<String, Object> mapParam) {
        String uri = Objects.requireNonNull(CommonUtils.getRequest()).getRequestURI();
        if (!(StringUtils.contains(uri, "abstractDetail") || StringUtils.contains(uri, "abstractList"))) {
            return super.list(pageParam, gridConfig, mapParam);
        }
        //对 abstractList 、abstractDetail 操作处理
        //移除多余配置行
        gridConfig.getListColumnConfig()
                .removeIf(columnConfig -> columnConfig.getDataIndex().contains(COLUMNCONFIG_DATAINDEX_PREFIX));
        Page page = super.list(pageParam, gridConfig, mapParam);
        if (page != null && page.getItems().size() == 1) {
            //填充修改数据值
            Map<String, Object> item = (Map<String, Object>) page.getItems().get(0);
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT pci.id AS id, pcd.use_number AS num ");
            sql.append(" FROM pe_consumable_detail pcd              ");
            sql.append(" INNER JOIN pe_consumable_item pci ON pci.ID = pcd.fk_consumable_item_id   ");
            sql.append(" where pcd.fk_consumable_id = ?             ");
            item.putAll(generalDao.getMapBySQL(sql.toString(), (String) item.getOrDefault("id", "")).stream()
                    .collect(Collectors.toMap(e -> COLUMNCONFIG_DATAINDEX_PREFIX + e.get("id"), e -> e.get("num"))));
            page.getItems().set(0, item);
        }
        return page;
    }

    /***
     * 根据id获取可能的consumable的itemId字段值
     *
     * @param id
     * @return
     */
    private List<String> getItemIdByConsumableId(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        PeConsumable peConsumable = this.generalDao.getById(PeConsumable.class, id);
        if (null == peConsumable) {
            return null;
        }
        ArrayList result = new ArrayList(3);
        result.add(peConsumable.getItemId());
        result.add(peConsumable.getApplicantUser().getId());
        result.add(UserUtils.getCurrentUser().getId());
        return result;
    }

    @Override
    public List<String[]> querySelectDataByColumnConfig(ColumnConfig columnConfig, GridConfig gridConfig,
                                                        ParamsDataModel<PeConsumable> paramsData) {
        //处理指定dataIndex
        if (paramsData != null && columnConfig.getDataIndex().contains("useItemUnit")) {
            columnConfig.setComboSQL(SQLHandleUtils.replaceSignUseParams(columnConfig.getComboSQL(),
                    TycjCollectionUtils.map("itemId", this.
                            getItemIdByConsumableId(paramsData.getStringParameter("id"))), true));
        }
        return super.querySelectDataByColumnConfig(columnConfig, gridConfig, paramsData);
    }
}