package com.whaty.products.service.basic.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeUnit;
import com.whaty.file.excel.upload.facade.ExcelUploadFacade;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.scope.constants.ScopeEnum;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.handler.tree.TreeNode;
import com.whaty.handler.tree.TreeUtils;
import com.whaty.util.CommonUtils;
import com.whaty.util.SQLHandleUtils;
import com.whaty.util.TycjCollectionUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单位管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("unitManageService")
public class UnitManageServiceImpl extends TycjGridServiceAdapter<PeUnit> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.SPRING_BEAN_NAME_EXCEL_UPLOAD_FACADE)
    private ExcelUploadFacade excelUploadFacade;

    private final static Object[] UNIT_UPLOAD_HEADERS = new Object[] {"名称*", "单位类型*", "编号*", "负责人姓名",
            "负责人手机", "负责人办公电话", "负责人邮箱", "备注", "父级"};

    @Override
    public void checkBeforeAdd(PeUnit bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(PeUnit bean) throws EntityException {
        String parentId = null;
        if (bean.getParent() != null && bean.getParent().getId() != null
                && bean.getId().equals(parentId = bean.getParent().getId())) {
            throw new EntityException("父级节点不可以是本节点");
        }
        // 获取原有bean
        PeUnit originalBean = this.myGeneralDao.getById(PeUnit.class, bean.getId());
        TycjParameterAssert.isAllNotNull(originalBean);
        // 判断 parentID是否修改过
        String originalParentId = null != originalBean.getParent() ? originalBean.getParent().getId() : null;
        if (parentId != null && originalParentId != parentId) {
            //查找是否有循环依赖  既parentId不能为当前bean的子id
            Set<TreeNode> treeNodes = this.listSiteUnitTree();
            Iterator<TreeNode> iterator = treeNodes.iterator();
            TreeNode next = null;
            //查找父节点
            while (iterator.hasNext()) {
                next = iterator.next();
                if ((next = next.getTreeNodeById(bean.getId())) != null) {
                    break;
                }
            }
            TycjParameterAssert.isAllNotNull(next);
            //查找父节点中是否存在指定子节点
            if (next.hasTreeNodeById(parentId)) {
                throw new EntityException("上级单位不可设置到该单位下级");
            }
        }
        this.checkBeforeAddOrUpdate(bean);
    }

    private void checkBeforeAddOrUpdate(PeUnit bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_unit where name = ? and site_code = ?" + additionalSql,
                bean.getName(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("此名称已被占用");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_unit where code = ? and site_code = ?" + additionalSql,
                bean.getCode(), bean.getSiteCode())) {
            throw new EntityException("此编号已被占用");
        }
    }

    @Override
    protected void afterAdd(PeUnit bean) throws EntityException {
        super.afterAdd(bean);
        // 更新redis 缓存
        this.clearCacheAfterOrAdd();
    }

    /**
     * 清除修改单位层级缓存
     */
    private void clearCacheAfterOrAdd(){
        //将受影响的用户放入缓存
        List<String> result = this.myGeneralDao.getBySQL(
                "select DISTINCT fk_sso_user_id from pe_manager where site_code = ? ", SiteUtil.getSiteCode());
        StaticBeanUtils.getRedisCacheService().putToCache(
                ScopeHandleUtils.PEUNIT_SCOPE_UPDATE_CACHE_GENERATOR.apply(SiteUtil.getSiteCode()),
                new HashSet<>(result), ScopeHandleUtils.TIME_OUT);
        // 清除当前用户缓存
        ScopeHandleUtils.removeScopeCache(UserUtils.getCurrentUserId(), ScopeEnum.peUnit);
    }

    @Override
    public Map update(PeUnit bean, GridConfig gridConfig) {
        String parentId = bean.getParent() == null ? null : bean.getParent().getId();
        // 获取原有bean
        PeUnit originalBean = this.myGeneralDao.getById(PeUnit.class, bean.getId());
        String originalParentId = originalBean.getParent() == null ? null : originalBean.getParent().getId();
        Map update = super.update(bean, gridConfig);
        // 对比单位层级是否变化
        if (!Objects.equals(parentId, originalParentId)) {
            this.clearCacheAfterOrAdd();
        }
        return update;
    }

    @Override
    protected String handleSqlBeforeList(String sql, Map<String, Object> mapParam) {
        String parentId;
        if (MapUtils.isEmpty(mapParam) || StringUtils.isBlank(parentId = (String) mapParam.get("parentId"))) {
            return SQLHandleUtils.replaceSignUseParams(sql, mapParam);
        }

        // 查找所有当前id=parentId的子节点
        Set<TreeNode> treeNodes = this.listCurrentUnitTree();
        if (CollectionUtils.isEmpty(treeNodes)) {
            mapParam.put("peUnit", parentId);
            return SQLHandleUtils.replaceSignUseParams(sql, mapParam);
        }

        Iterator<TreeNode> iterator = treeNodes.iterator();
        TreeNode next = null;
        while (iterator.hasNext()) {
            next = iterator.next();
            if (null != (next = next.getTreeNodeById(parentId))) {
                break;
            }
            next = null;
        }
        mapParam.put("peUnit", next == null ? parentId : next.getAllTreeNodeIds());
        return SQLHandleUtils.replaceSignUseParams(sql, mapParam);
    }

    /**
     * 列举站点单位树
     *
     * @return
     */
    private Set<TreeNode> listSiteUnitTree() {
        List<Map<String, Object>> unitList = this.myGeneralDao
                .getMapBySQL("select id, fk_parent_id as parentId, name as label from pe_unit where site_code = ?",
                        SiteUtil.getSiteCode());
        return Optional.ofNullable(unitList).filter(CollectionUtils::isNotEmpty)
                .map(TreeUtils::buildTree).orElse(null);
    }

    /**
     * 列举当前用户的单位树
     *
     * @return
     */
    public Set<TreeNode> listCurrentUnitTree() {
        //获取当前用户的单位
        PeUnit currentUnit = UserUtils.getCurrentUnit();
        if (null == currentUnit || StringUtils.isBlank(currentUnit.getId())) {
            return this.listSiteUnitTree();
        }
        // 获取指定权限下的权限树
        List<Map<String, Object>> result = this.myGeneralDao.getMapBySQL(
                "select pu.id as id, pu.fk_parent_id as parentId, pu.name as label from pe_unit pu " +
                        " inner join pr_pri_manager_unit pr on pr.fk_item_id = pu.id " +
                        " where pu.site_code = ? AND pr.FK_SSO_USER_ID = ? AND pr.fk_item_id IS NOT NULL ",
                SiteUtil.getSiteCode(), UserUtils.getCurrentUserId())
                .stream().peek(e -> e.put("parentId", null)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        List<Map<String, Object>> ids = new ArrayList<>(result.size() * 2);
        ids.addAll(result);
        List<Map<String, Object>> childList = result;
        //获取权限子节点
        while (true) {
            childList = this.myGeneralDao.getMapBySQL(
                    "select id, fk_parent_id as parentId, name as label from pe_unit where " + CommonUtils.madeSqlIn(
                            childList.stream().map(e -> (String) e.get("id")).collect(Collectors.toList()),
                            "fk_parent_id"));
            if (CollectionUtils.isEmpty(childList)) {
                break;
            }
            ids.addAll(childList);
        }
        return Optional.ofNullable(ids).filter(CollectionUtils::isNotEmpty).map(TreeUtils::buildTree).orElse(null);
    }

    /**
     * 下载单位导入模板
     * @param out
     */
    public void downUnitImportTemplate(OutputStream out) {
        List<Object> unitType = this.myGeneralDao.getBySQL("select name from enum_const " +
                "where namespace='FlagUnitType' and (team is null or team like '%," +
                MasterSlaveRoutingDataSource.getDbType() + ",%' or team='')");
        List<Object> unit = this.myGeneralDao.getBySQL("select id from pe_unit where site_code=?",
                MasterSlaveRoutingDataSource.getDbType());
        this.excelUploadFacade.generateExcelAndWrite(out, TycjCollectionUtils.map("单位类型*", unitType, "父级", unit),
                UNIT_UPLOAD_HEADERS, "单位管理导入", null);
    }

    /**
     * 导入单位
     * @param file
     * @return
     */
    public Map<String, Object> doUploadUnit(File file) {
        TycjParameterAssert.fileAllExists(file);
        List<String[]> checkList = new ArrayList<>();
        checkList.add(new String[] {"父级不存在", "not exists (select 1 from (select id, column1 from util_excel_table " +
                "where namespace = ${namespace}) u where e.column9 = u.column1 and u.id <> e.id) " +
                "AND not exists (select 1 from pe_unit un where un.name = e.column9 and un.site_code = '" +
                MasterSlaveRoutingDataSource.getDbType() + "')"});
        checkList.add(new String[] {"父级不可自关联", "column1 = column9"});
        checkList.add(new String[] {"名称已存在", "exists (select 1 from pe_unit un where un.name = e.column1)"});
        checkList.add(new String[] {"编号已存在", "exists (select 1 from pe_unit un where un.code = e.column3)"});
        checkList.add(new String[] {"单位类型不存在", "not exists (select 1 from enum_const ut " +
                "where ut.name = e.column2 and (ut.team is null or ut.team like '%," +
                MasterSlaveRoutingDataSource.getDbType() + ",%' or ut.team = ''))"});
        List<String> sqlList = new ArrayList<>();
        sqlList.add("UPDATE util_excel_table SET column10 = REPLACE (uuid(), '-', '') WHERE namespace = ${namespace}");
        sqlList.add("UPDATE util_excel_table u " +
                "LEFT JOIN pe_unit un ON un. NAME = u.column9 AND un.site_code = '" +
                MasterSlaveRoutingDataSource.getDbType() + "' " +
                "LEFT JOIN util_excel_table e ON e.column1 = u.column9 " +
                "AND e.namespace = ${namespace} and e.valid = '1'" +
                "SET u.column11 = ifnull(un.id, e.column10) " +
                "WHERE u.namespace = ${namespace} and u.valid = '1'");
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO pe_unit (                                                             ");
        sql.append(" 	id,                                                                            ");
        sql.append(" 	NAME,                                                                          ");
        sql.append(" 	`code`,                                                                        ");
        sql.append(" 	flag_unit_type,                                                                ");
        sql.append(" 	head_name,                                                                     ");
        sql.append(" 	head_mobile,                                                                   ");
        sql.append(" 	head_telephone,                                                                ");
        sql.append(" 	head_email,                                                                    ");
        sql.append(" 	note,                                                                          ");
        sql.append(" 	site_code,                                                                     ");
        sql.append(" 	fk_parent_id                                                                   ");
        sql.append(" ) SELECT                                                                          ");
        sql.append(" 	u.column10,                                                                    ");
        sql.append(" 	u.column1,                                                                     ");
        sql.append(" 	u.column3,                                                                     ");
        sql.append(" 	ut.id,                                                                         ");
        sql.append(" 	u.column4,                                                                     ");
        sql.append(" 	u.column5,                                                                     ");
        sql.append(" 	u.column6,                                                                     ");
        sql.append(" 	u.column7,                                                                     ");
        sql.append(" 	u.column8,                                                                     ");
        sql.append(" 	'" + MasterSlaveRoutingDataSource.getDbType() + "',                            ");
        sql.append(" 	u.column11                                                                     ");
        sql.append(" FROM                                                                              ");
        sql.append(" 	util_excel_table u                                                             ");
        sql.append(" INNER JOIN enum_const ut on ut.name = u.column2 AND ut.namespace = 'flagUnitType' ");
        sql.append(" WHERE                                                                             ");
        sql.append(" 	u.namespace = ${namespace}                                                     ");
        sql.append(" AND u.valid = '1'                                                                 ");
        sql.append(" AND (ut.team is null                                                              ");
        sql.append(" or ut.team like '%," + MasterSlaveRoutingDataSource.getDbType() + ",%'            ");
        sql.append(" or ut.team = '')                                                                  ");
        sqlList.add(sql.toString());
        return this.excelUploadFacade.doUploadFile(file, UNIT_UPLOAD_HEADERS, checkList, sqlList, 1, "导入单位");
    }
}
