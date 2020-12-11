package com.whaty.framework.grid.adapter.service.impl;

import com.whaty.HasAttachFile;
import com.whaty.annotation.UnionUnique;
import com.whaty.annotation.Unique;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.cache.service.CacheService;
import com.whaty.core.commons.cache.util.CacheUtil;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.exception.ErrorCodeEnum;
import com.whaty.core.commons.util.CommonUtils;
import com.whaty.core.commons.util.OrderItem;
import com.whaty.core.commons.util.Page;
import com.whaty.core.commons.util.excel.ExcelReadUtil;
import com.whaty.core.commons.util.excel.ExcelStyleUtils;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.async.Task;
import com.whaty.core.framework.async.TaskManager;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.grid.bean.ColumnConfig;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.bean.ParameterCondition;
import com.whaty.core.framework.grid.service.impl.GridServiceImpl;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.PeUnit;
import com.whaty.framework.base.TycjBaseService;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.doamin.ExcelCheckResult;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.function.Tuple;
import com.whaty.util.SQLHandleUtils;
import com.whaty.utils.HibernatePluginsUtil;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.engine.query.ParamLocationRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 通用成教grid的service适配器
 * 用于加入所有成教内部业务的底层方法
 *
 * @author weipengsen
 */
@Lazy
@Service("gridServiceAdapter")
public class TycjGridServiceAdapter<T extends AbstractBean> extends GridServiceImpl<T> {

    @Resource(name = "tycjBaseService")
    private TycjBaseService tycjBaseService;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = "core_cacheService")
    private CacheService cacheService;

    @Autowired
    private TaskManager taskManager;

    /**
     * 单索引唯一性校验
     */
    private final static String SINGLE_UNIQUE_SQL = "select 1 from %s where %s = ?";

    @Override
    protected String getDefaultOrder(Page pageParam, GridConfig gridConfig, Map<String, Object> mapParam) {
        if (gridConfig.getColumByDateIndex("createDate") != null) {
            return "createDate desc";
        } else if (gridConfig.getColumByDateIndex("createTime") != null) {
            return "createTime desc";
        }
        return super.getDefaultOrder(pageParam, gridConfig, mapParam);
    }

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map<String, Object> mapParam) {
        if (MapUtils.isNotEmpty(pageParam.getSearchItem())) {
            pageParam.setSearchItem(pageParam.getSearchItem().entrySet().stream().filter(e -> Objects.nonNull(e.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        this.handleSearchSign(gridConfig, mapParam);
        if (ArrayUtils.isEmpty(pageParam.getOrderItem())
                && this.getOrderItem() != null && this.getOrderItem().length > 0) {
            OrderItem[] itemArr = this.getOrderItem();
            if (ArrayUtils.isNotEmpty(pageParam.getOrderItem())) {
                List<OrderItem> items = Arrays.asList(itemArr);
                items.addAll(Arrays.asList(pageParam.getOrderItem()));
                itemArr = items.toArray(new OrderItem[items.size()]);
            }
            pageParam.setOrderItem(itemArr);
        }
        if (gridConfig.gridConfigSource().getSql() != null) {
            gridConfig.gridConfigSource().setSql(this.handleSqlBeforeList(SQLHandleUtils
                    .replaceSignUseParams(gridConfig.gridConfigSource().getSql(), mapParam), mapParam));
        }
        return this.afterList(super.list(pageParam, gridConfig, mapParam));
    }

    /**
     * 查询前处理sql
     * @param sql
     * @param mapParam
     * @return
     */
    protected String handleSqlBeforeList(String sql, Map<String, Object> mapParam) {
        return sql;
    }

    /**
     * 处理查询中的占位符
     *
     * @param gridConfig
     * @param mapParam
     */
    private void handleSearchSign(GridConfig gridConfig, Map<String, Object> mapParam) {
        Consumer<Tuple<String, String>> replaceSearch;
        if ("1".equals(gridConfig.getListType())
                || StringUtils.isNotBlank(gridConfig.gridConfigSource().getSql())) {
            if (StringUtils.isBlank(gridConfig.gridConfigSource().getSql())) {
                return;
            }
            gridConfig.gridConfigSource().setSql(gridConfig.gridConfigSource().getSql()
                    .replace("${currentUserId}", UserUtils.getCurrentUserId()));
            gridConfig.gridConfigSource().setSql(gridConfig.gridConfigSource().getSql()
                    .replace("${siteCode}", this.getSiteCode()));
            replaceSearch = e -> gridConfig.gridConfigSource().setSql(gridConfig.gridConfigSource().getSql()
                    .replace(String.format("${%s}", e.t0), e.t1));
        } else {
            if (StringUtils.isBlank(gridConfig.gridConfigSource().getDc())) {
                return;
            }
            gridConfig.gridConfigSource().setDc(gridConfig.gridConfigSource().getDc()
                    .replace("${currentUserId}", UserUtils.getCurrentUserId()));
            gridConfig.gridConfigSource().setDc(gridConfig.gridConfigSource().getDc()
                    .replace("${siteCode}", this.getSiteCode()));
            replaceSearch = e -> gridConfig.gridConfigSource().setDc(gridConfig.gridConfigSource().getDc()
                    .replace(String.format("${%s}", e.t0), e.t1));
        }
        mapParam.entrySet().stream().filter(e -> Objects.nonNull(e.getValue()))
                .map(e -> new Tuple<>(e.getKey(), String.valueOf(e.getValue())))
                .forEach(replaceSearch);
    }

    /**
     * 查询后的勾子方法
     *
     * @param page
     * @return
     */
    protected Page afterList(Page page) {
        return page;
    }

    @Override
    public Map add(T bean, Map<String, Object> params, GridConfig gridConfig) {
        try {
            this.setParameterProperty(bean, params);
            this.mergeBeanByGridConfig(bean, bean, gridConfig, false);
            this.checkUnique(bean, gridConfig.getListColumnConfig());
            this.checkBeforeAdd(bean);
            this.checkBeforeAdd(bean, params);
            this.getGeneralDao().save(bean);
            this.afterAdd(bean);
            return CommonUtils.createSuccessInfoMap("添加成功");
        } catch (EntityException var5) {
            throw new ServiceException(var5.getMessage());
        }
    }

    @Override
    public Map update(T bean, GridConfig gridConfig) {
        T dbInstance = this.getGeneralDao().getById(bean.getClass(), bean.getId());
        if (dbInstance == null) {
            return CommonUtils.createFailInfoMap("更新失败，数据不存在");
        }
        this.getGeneralDao().getMyHibernateTemplate().evict(dbInstance);
        try {
            this.mergeBeanByGridConfig(dbInstance, bean, gridConfig, true);
            this.checkUnique(bean, gridConfig.getListColumnConfig());
            this.checkBeforeUpdate(dbInstance);
            this.getGeneralDao().save(dbInstance);
            this.afterUpdate(dbInstance);
            return CommonUtils.createSuccessInfoMap("更新成功");
        } catch (EntityException var5) {
            throw new ServiceException(var5.getMessage());
        }
    }

    /**
     * 校验唯一性
     *
     * @param bean
     * @param listColumnConfig
     */
    private void checkUnique(T bean, List<ColumnConfig> listColumnConfig) {
        UnionUnique unionUnique = bean.getClass().getAnnotation(UnionUnique.class);
        if (Objects.nonNull(unionUnique)) {
            this.checkUnionUnique(unionUnique, bean);
        }
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(e -> Objects.nonNull(e.getAnnotation(Unique.class)))
                .forEach(e -> this.checkSingleUnique(e, bean, listColumnConfig));
    }

    /**
     * 检查联合唯一索引
     * @param unionUnique
     * @param bean
     */
    private void checkUnionUnique(UnionUnique unionUnique, T bean) {
        String[] fieldNames = unionUnique.fieldNames();
        Arrays.sort(fieldNames);
        Field[] fields = Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(e -> Arrays.binarySearch(fieldNames, e.getName()) >= 0)
                .toArray(Field[]::new);
        String table = bean.getClass().getAnnotation(Table.class).name();
        StringBuilder sql = new StringBuilder("select 1 from " + table + " where 1 = 1");
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                sql.append(" AND ").append(this.getColumnName(field)).append(" = '")
                        .append(field.get(bean)).append("'");
            } catch (IllegalAccessException e) {
                throw new UncheckException(e);
            }
        }
        sql.append(StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "'");
        if (this.myGeneralDao.checkNotEmpty(sql.toString())) {
            throw new ServiceException(unionUnique.info());
        }
    }

    /**
     * 获取字段的数据库名
     *
     * @param field
     * @return
     */
    private String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            return column.name();
        } else {
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (Objects.nonNull(joinColumn)) {
                return joinColumn.name();
            }
        }
        return null;
    }

    /**
     * 校验单索引唯一
     *
     * @param field
     * @param bean
     * @param listColumnConfig
     */
    private void checkSingleUnique(Field field, T bean, List<ColumnConfig> listColumnConfig) {
        field.setAccessible(true);
        String additionalSql = StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "'";
        try {
            if (this.myGeneralDao.checkNotEmpty(String.format(SINGLE_UNIQUE_SQL,
                    bean.getClass().getAnnotation(Table.class).name(),
                    this.getColumnName(field)) + additionalSql, field.get(bean))) {
                ColumnConfig columnConfig = listColumnConfig.stream()
                        .filter(e1 -> this.extractFieldName(e1).equals(field.getName()))
                        .findFirst().orElse(null);
                throw new ServiceException(String.format("此%s已存在", Optional
                        .ofNullable(columnConfig).map(ColumnConfig::getName).orElse(field.getName())));
            }
        } catch (IllegalAccessException e1) {
            throw new UncheckException(e1);
        }
    }

    /**
     * 提取出字段的成员名
     *
     * @param columnConfig
     * @return
     */
    private String extractFieldName(ColumnConfig columnConfig) {
        if (columnConfig.getDataIndex().contains(".")) {
            return columnConfig.getDataIndex().substring(0, columnConfig.getDataIndex().indexOf("."));
        }
        return columnConfig.getDataIndex();
    }

    private void setParameterProperty(T bean, Map<String, Object> mapParams) {
        List<ParameterCondition> parameterConditionList = this.getParameterConditions();
        if (!CollectionUtils.isEmpty(parameterConditionList)) {
            Iterator var4 = parameterConditionList.iterator();

            while (var4.hasNext()) {
                ParameterCondition pc = (ParameterCondition) var4.next();
                if (pc.getPropertyKey() != null && pc.getParameterKey() != null && mapParams.containsKey(pc.getParameterKey())) {
                    try {
                        CommonUtils.setPropertyToBean(bean, pc.getPropertyKey(), mapParams.get(pc.getParameterKey()));
                    } catch (Exception var7) {
                        var7.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 更换HibernatePluginsUtil类，增加实体类分模块检查引用能力
     *
     * @param gridConfig
     * @param ids
     * @return
     */
    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        List idList = CommonUtils.convertIdsToList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            return CommonUtils.createFailInfoMap(ErrorCodeEnum.SYS_PARAM_ERROR.getMessage());
        } else {
            try {
                this.checkBeforeDelete(idList);
                Class clazz = this.getEntityClass(gridConfig);
                if (clazz == null) {
                    clazz = this.getEntityClass();
                }
                if (HibernatePluginsUtil.validateReferencingCurrentSession(clazz, ids)) {
                    return CommonUtils.createFailInfoMap("数据已被其他信息引用，不能删除");
                } else {
                    if (HasAttachFile.class.isAssignableFrom(clazz)) {
                        List<Object> list = this.getGeneralDao()
                                .getBySQL("select 1 from attach_file where namespace = '"
                                        + HasAttachFile.AttachFileBean.getNamespaceByClass(clazz) + "' and "
                                        + com.whaty.util.CommonUtils.madeSqlIn(ids, "link_id"));
                        if (CollectionUtils.isNotEmpty(list)) {
                            throw new EntityException("存在关联的附件，无法删除");
                        }
                    }
                    int n = this.getGeneralDao().deleteByIds(clazz, idList);
                    this.afterDelete(idList);
                    return CommonUtils.createSuccessInfoMap(String.format("删除成功，共删除%d条数据", n));
                }
            } catch (EntityException var6) {
                throw new ServiceException(var6.getMessage());
            }
        }
    }

    private Class getEntityClass() {
        Type genType = this.getClass().getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (params != null && params.length > 0 && params[0] instanceof Class) {
                return (Class) params[0];
            }
        }
        return null;
    }

    /**
     * 模板方法，获取排序字段
     *
     * @return
     */
    protected String getOrderColumnIndex() {
        return null;
    }

    /**
     * 模板方法，获取排序方式，默认asc
     *
     * @return
     */
    protected String getOrderWay() {
        return "asc";
    }

    /**
     * 模板方法，获取排序数组
     *
     * @return
     */
    protected OrderItem[] getOrderItem() {
        if (StringUtils.isNotBlank(this.getOrderColumnIndex())) {
            return new OrderItem[]{new OrderItem(this.getOrderColumnIndex(), this.getOrderWay())};
        }
        return null;
    }

    /**
     * 扩展内置afterAdd
     *
     * @param beanList
     * @throws EntityException
     */
    @Override
    public void afterExcelImport(List<T> beanList) throws EntityException {
        this.myGeneralDao.flush();
        for (T bean : beanList) {
            this.afterAdd(bean);
        }
    }

    /**
     * service层的选择全部方法
     *
     * @param paramsDataModel
     * @return
     */
    protected String getIds(ParamsDataModel paramsDataModel) {
        GridConfig gridConfig = this.initGrid(SiteUtil.getSiteCode(),
                paramsDataModel.getStringParameter("actionId"), paramsDataModel.getParams());
        this.initGrid(gridConfig, paramsDataModel.getParams());
        String ids = paramsDataModel.getStringParameter("ids");
        Page pageParam = null;
        if (paramsDataModel.getPage() != null) {
            pageParam = paramsDataModel.getPage();
        }
        int selectNum = ids == null ? 0 : ids.split(",").length;
        if (pageParam != null && pageParam.getTotalCount() > selectNum) {
            StringBuilder newIds = new StringBuilder();
            pageParam.setPageSize(pageParam.getTotalCount());
            Page page = this.list(pageParam, gridConfig, paramsDataModel.getParams());
            List<Map> items = page.getItems();
            if (CollectionUtils.isNotEmpty(items)) {
                Iterator var9 = items.iterator();
                while (var9.hasNext()) {
                    Map m = (Map) var9.next();
                    newIds.append(m.get("id").toString()).append(",");
                }
            }

            ids = newIds.toString();
        }
        return ids;
    }

    /**
     * 扩展siteCode
     *
     * @param dbInstance
     * @param bean
     * @param gridConfig
     * @param isUpdate
     */
    @Override
    protected void mergeBeanByGridConfig(Object dbInstance, Object bean, GridConfig gridConfig, boolean isUpdate) {
        super.mergeBeanByGridConfig(dbInstance, bean, gridConfig, isUpdate);
        if (bean instanceof AbstractSiteBean) {
            ((AbstractSiteBean) bean).setSiteCode(this.getSiteCode());
        }
    }

    /**
     * 扩展siteCode
     *
     * @param task
     * @param excelFile
     * @param workbook
     * @param gridConfig
     * @param params
     * @param site
     * @return
     * @throws EntityException
     */
    @Override
    protected List<T> readExcelToBean(Task task, File excelFile, Workbook workbook, GridConfig gridConfig,
                                      Map<String, Object> params, Site site) throws EntityException {
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        ArrayList<T> list = new ArrayList();
        HashSet beans = new HashSet();
        boolean isError = false;
        byte startRowIndex = 2;
        int errorInfoCellIndex = sheet.getRow(startRowIndex - 1).getPhysicalNumberOfCells();
        StringBuilder info = new StringBuilder();
        HashMap fkBeanMap = new HashMap();
        int errorRows = 0;
        for (int e = startRowIndex; e < rows; ++e) {
            this.taskManager.update(task, (e - startRowIndex) * 100 / (rows - startRowIndex));
            info.delete(0, info.length());
            int j = 0;

            try {
                Class redStyle = this.getEntityClass();
                Object infoCell = redStyle.newInstance();
                Iterator e1 = gridConfig.getListColumnConfig().iterator();

                while (e1.hasNext()) {
                    ColumnConfig columnConfig = (ColumnConfig) e1.next();
                    if (columnConfig.isAdd()) {
                        Cell cell = sheet.getRow(e).getCell(j);
                        String cellContent = (Boolean) params.get("replaceBlankSpace") ?
                                String.valueOf(ExcelReadUtil.getCellFormatValue(cell)).replace(" ", "") :
                                String.valueOf(ExcelReadUtil.getCellFormatValue(cell));
                        ++j;
                        if (this.verifyCell(e, columnConfig, cellContent, info)) {
                            this.setFkBean(e, columnConfig, cellContent, info, fkBeanMap, redStyle, infoCell, site);
                        }
                    }
                }

                if (!beans.add(infoCell)) {
                    info.append("第").append(e + 1).append("行与文件中前面的数据重复！，");
                    continue;
                }

                list.add((T) infoCell);
                try {
                    this.checkBeforeAdd((T) infoCell);
                    this.checkBeforeAdd((T) infoCell, params);
                } catch (Exception var26) {
                    var26.printStackTrace();
                    info.append("第").append(e + 1).append("行").append(var26.getMessage());
                }
            } catch (Exception var27) {
                var27.printStackTrace();
                info.append("第" + (e + 1) + "行解析异常：" + var27.getMessage() + "，");
            }

            if (info.length() > 0) {
                ++errorRows;
                if ("，".equals(info.substring(info.length() - 1))) {
                    info.delete(info.length() - 1, info.length());
                }

                isError = true;
                CellStyle var28 = ExcelStyleUtils.getRedStyle(workbook);
                Cell var29 = sheet.getRow(e).createCell(errorInfoCellIndex);
                var29.setCellStyle(var28);
                var29.setCellValue(info.toString());
            }
        }
        if (isError) {
            try {
                workbook.write(new FileOutputStream(excelFile));
            } catch (IOException var25) {
                var25.printStackTrace();
                throw new EntityException("写入校验信息失败！", var25);
            }

            throw new EntityException(errorRows + "条数据有误，导入终止。请修改后将全部数据重新导入！");
        } else {
            ExcelCheckResult excelCheckResult = this.checkExcelImportError(list, workbook,
                    new ExcelCheckResult(errorInfoCellIndex, errorRows, false, startRowIndex));
            if (excelCheckResult.isError()) {
                try {
                    workbook.write(new FileOutputStream(excelFile));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new EntityException("写入校验信息失败！", e);
                }
                throw new EntityException(excelCheckResult.getErrorRows() +
                        "条数据有误，导入终止。请修改后将全部数据重新导入！");
            }
            return list.stream().peek(e -> {
                if (e instanceof AbstractSiteBean) {
                    ((AbstractSiteBean) e).setSiteCode(this.getSiteCode());
                }
            }).collect(Collectors.toList());
        }
    }

    private boolean verifyCell(int i, ColumnConfig columnConfig, String cellContent, StringBuilder info) {
        String columnName = columnConfig.getName();
        if (!columnConfig.isAllowBlank()) {
            if (StringUtils.isBlank(cellContent) || "请选择".equals(cellContent)) {
                info.append("第").append(i + 1).append("行").append(columnName).append("不能为空！，");
                return false;
            }
        } else if (StringUtils.isBlank(cellContent) || "请选择".equals(cellContent)) {
            return false;
        }

        String checkRegular = columnConfig.getCheckRegular();
        if (StringUtils.isNotBlank(checkRegular)) {
            String testLength = columnConfig.getCheckMessage();
            testLength = StringUtils.isBlank(testLength) ? "正则校验失败。" : testLength;
            Pattern pattern = CommonUtils.patternMap.computeIfAbsent(checkRegular, (k) -> Pattern.compile(checkRegular));
            if (!pattern.matcher(cellContent).matches()) {
                info.append("第").append(i + 1).append("行").append(columnName).append("格式错误！").append(testLength)
                        .append("，");
                return false;
            }
        }

        int testLength1 = columnConfig.getMaxLength();
        if (cellContent.length() > testLength1) {
            info.append("第").append(i + 1).append("行").append(columnName).append("长度超过限制！最大")
                    .append(testLength1).append("！，");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 集成导入自定义检查表格错误
     *
     * @param beanList
     * @param workbook
     * @param excelCheckResult
     * @return
     */
    protected ExcelCheckResult checkExcelImportError(List<T> beanList, Workbook workbook,
                                                     ExcelCheckResult excelCheckResult) {
        return new ExcelCheckResult(false);
    }

    /**
     * 检查属性值是否重复
     *
     * @param beanList
     * @param fieldList
     * @return
     */
    protected Map<String, List<Integer>> checkSameField(List<T> beanList, List<String> fieldList) {
        Map<String, List<Integer>> sameDataList = new HashMap<>();
        try {
            for (int i = 0; i < beanList.size(); i++) {
                List<Integer> sameNoList = new ArrayList<>();
                String name = "";
                for (String fieldName : fieldList) {
                    Field field = beanList.get(i).getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    name += field.get(beanList.get(i)) == null ? "" : String.valueOf(field.get(beanList.get(i)));
                }
                if (sameDataList.containsKey(name)) {
                    sameDataList.get(name).add(i);
                } else {
                    sameNoList.add(i);
                    sameDataList.put(name, sameNoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sameDataList;
    }


    /**
     * 检查表格内重复数据
     *
     * @param sameDataList
     * @param workbook
     * @param excelCheckResult
     * @return
     */
    protected ExcelCheckResult checkSameData(Map<String, Map<String, List<Integer>>> sameDataList, Workbook workbook,
                                             ExcelCheckResult excelCheckResult) {
        Sheet sheet = workbook.getSheetAt(0);
        CellStyle style = ExcelStyleUtils.getRedStyle(workbook);
        Set errorNo = new HashSet<>();
        sameDataList.forEach((warnInfo, sameData) ->
                sameData.forEach((name, nameList) -> {
                    if (nameList.size() > 1 && StringUtils.isNotBlank(name)) {
                        errorNo.addAll(nameList);
                        excelCheckResult.setError(true);
                        nameList.forEach(no -> {
                            int rowNo = no + excelCheckResult.getStartRowIndex();
                            Cell cell = sheet.getRow(rowNo).getCell(excelCheckResult.getErrorInfoCellIndex()) == null ?
                                    sheet.getRow(rowNo).createCell(excelCheckResult.getErrorInfoCellIndex()) :
                                    sheet.getRow(rowNo).getCell(excelCheckResult.getErrorInfoCellIndex());
                            cell.setCellStyle(style);
                            cell.setCellValue(com.whaty.common.string.StringUtils
                                    .trimBlankAndNull(cell.getStringCellValue()).concat(warnInfo));
                        });
                    }
                })
        );
        excelCheckResult.setErrorRows(errorNo.size());
        return excelCheckResult;
    }

    @Override
    protected void beforeExcelImport(Workbook workbook, GridConfig gridConfig) throws EntityException {
        Sheet sheet = workbook.getSheetAt(0);
        byte headerLineIndex = 1;
        int k = 0;
        Iterator var6 = gridConfig.getListColumnConfig().iterator();
        while (var6.hasNext()) {
            ColumnConfig columnConfig = (ColumnConfig) var6.next();
            if (columnConfig.isAdd()) {
                Cell cell = sheet.getRow(headerLineIndex).getCell(k++);
                if (cell == null) {
                    throw new EntityException("表头错误，请按照上传模板检查一下！");
                }
                String columnName = columnConfig.isAllowBlank() ? columnConfig.getName() : columnConfig.getName() + "*";
                String cellValue = cell.getStringCellValue();
                if (!StringUtils.equals(columnName, cellValue)) {
                    throw new EntityException("表头错误，请按照上传模板检查一下！");
                }
            }
        }

    }

    /**
     * 扩展siteCode与参数动态化
     *
     * @param columnConfig
     * @param gridConfig
     * @param paramsData
     * @return
     */
    @Override
    public List<String[]> querySelectDataByColumnConfig(ColumnConfig columnConfig, GridConfig gridConfig, ParamsDataModel<T> paramsData) {
        if (CollectionUtils.isNotEmpty(columnConfig.getComboList())) {
            return columnConfig.getComboList();
        } else if (StringUtils.isNotBlank(columnConfig.getComboSQL())) {
            columnConfig.setComboSQL(columnConfig.getComboSQL().replace("${siteCode}",
                    this.getSiteCode()));
            columnConfig.setComboSQL(columnConfig.getComboSQL().replace("${currentUserId}",
                    UserUtils.getCurrentUserId()));
            this.processSelectComboSQL(columnConfig, gridConfig, paramsData);
            String comboSql = columnConfig.getComboSQL();
            if (paramsData != null) {
                comboSql = SQLHandleUtils.replaceSignUseParams(comboSql, paramsData.getParams());
            }
            comboSql = SQLHandleUtils.replaceAllBlankSign(comboSql);
            return columnConfig.getDependColumns() != null && columnConfig.getDependColumns().size() > 0
                    ? this.queryCascadeSelectData(columnConfig, gridConfig, paramsData)
                    : this.getGeneralDao().getBySQL(comboSql);
        } else if (columnConfig.getDataIndex().contains(".")) {
            String dataIndex = columnConfig.getDataIndex();
            String where = "";
            String column = dataIndex.substring(dataIndex.lastIndexOf(".") + 1);
            String bean = dataIndex.substring(0, dataIndex.lastIndexOf("."));
            bean = bean.substring(bean.lastIndexOf(".") + 1);
            if (bean.contains("_")) {
                String[] str = bean.split("_");
                bean = str[1];
                if (str.length > 2) {
                    column = str[2];
                } else {
                    column = "name";
                }
            }

            String hql;
            if (StringUtils.startsWithIgnoreCase(bean, "enumConstBy")
                    || StringUtils.startsWithIgnoreCase(bean, "coreEnumConstBy")) {
                hql = bean.replace("enumConstBy", "");
                hql = hql.replace("coreEnumConstBy", "");
                where = "and namespace='" + hql + "' and (team is null or team like '%," +
                        this.getSiteCode() + ",%' or team = '')";
                bean = "EnumConst";
            }

            bean = bean.substring(0, 1).toUpperCase() + bean.substring(1);
            hql = "select id, %s from %s where 1=1 %s";
            try {
                String className = HibernatePluginsUtil.getPersistentClass(bean).getClassName();
                if (AbstractSiteBean.class.isAssignableFrom(Class.forName(className))) {
                    hql += " and site_code = '" + this.getSiteCode() + "'";
                }
                where += this.findScopeCondition(className);
            } catch (ClassNotFoundException e) {
            }
            return this.getGeneralDao().getByHQL(ScopeHandleUtils.handleScopeSignOfSql(String
                    .format(hql, column, bean, where), UserUtils.getCurrentUserId()));
        } else {
            return null;
        }
    }

    /**
     * 寻找类中的横向权限条件
     *
     * @param className
     */
    private String findScopeCondition(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        if (PeUnit.class.isAssignableFrom(clazz)) {
            return " and [peUnit|id]";
        }
        Field unitField = Arrays.stream(clazz.getDeclaredFields())
                .filter(e -> PeUnit.class.isAssignableFrom(e.getType()))
                .findFirst().orElse(null);
        if (unitField == null) {
            return "";
        }
        return " and [peUnit|" + unitField.getName() + ".id]";
    }

    private List<String[]> queryCascadeSelectData(ColumnConfig columnConfig, GridConfig gridConfig,
                                                  ParamsDataModel<T> paramsData) {
        if (paramsData != null && paramsData.getParameter("dependColumnValues") != null) {
            Map<String, Object> dependColumnValueMap = (Map) paramsData.getParameter("dependColumnValues");
            ParamLocationRecognizer paramLocationRecognizer = ParamLocationRecognizer
                    .parseLocations(columnConfig.getComboSQL());
            if (paramLocationRecognizer.getNamedParameterDescriptionMap() != null) {
                Iterator var6 = paramLocationRecognizer.getNamedParameterDescriptionMap().keySet().iterator();

                while (var6.hasNext()) {
                    Object key = var6.next();
                    String strKey = String.valueOf(key);
                    Object value = dependColumnValueMap.get(strKey);
                    if (value == null || StringUtils.isBlank(String.valueOf(value))) {
                        return null;
                    }
                }
            }

            Map<String, Object> params = (Map) paramsData.getParameter("dependColumnValues");
            return this.getGeneralDao().getBySQL(columnConfig.getComboSQL(), params);
        } else {
            return null;
        }
    }

    @Override
    protected void setFkBean(int i, ColumnConfig columnConfig, String cellContent, StringBuilder info, Map<String, Object> fkBeanMap, Class clazz, Object instance, Site site) {
        String columnName = columnConfig.getName();

        try {
            String columnDataIndex = StringUtils.isNotBlank(columnConfig.getDataColumn()) ? columnConfig.getDataColumn() : columnConfig.getDataIndex();
            Method method;
            String type;
            if (columnDataIndex.indexOf(".") > 0) {
                if (columnDataIndex.indexOf(".") != columnDataIndex.lastIndexOf(".") || !columnDataIndex.endsWith(".name")) {
                    return;
                }

                String[] columnDataIndexs = columnDataIndex.split("\\.");
                columnDataIndex = columnDataIndexs[0];
                type = columnDataIndexs[1];
                columnDataIndex = columnDataIndex.substring(0, 1).toUpperCase() + columnDataIndex.substring(1);
                if (columnDataIndex.startsWith("EnumConstBy")) {
                    String key = "enumConst:" + columnDataIndex.substring(11) + ":" + cellContent;
                    EnumConst enumConst;
                    if (fkBeanMap.get(key) == null) {
                        // 多站点
                        enumConst = (EnumConst) this.myGeneralDao.getOneByHQL("from EnumConst where namespace = '" +
                                columnDataIndex.substring(11) + "' and name = '" + cellContent + "' " +
                                "and (team is null or team like '%," + this.getSiteCode() +
                                ",%' or team = '')");
                        fkBeanMap.put(key, enumConst);
                    } else {
                        enumConst = (EnumConst) fkBeanMap.get(key);
                    }

                    if (enumConst == null) {
                        info.append("第" + (i + 1) + "行" + columnName + "不存在！，");
                        return;
                    }

                    method = clazz.getMethod("set" + columnDataIndex, EnumConst.class);
                    method.invoke(instance, enumConst);
                } else {
                    Method me = clazz.getMethod("get" + columnDataIndex, (Class[]) null);
                    Type fkBeanType = me.getGenericReturnType();
                    String key = fkBeanType.getTypeName() + ":" + type + ":" + cellContent;
                    T fkBean = null;
                    if (fkBeanMap.get(key) == null) {
                        Class beanClazz = (Class) fkBeanType;
                        String hql = "from " + beanClazz.getName() + " where " + type + "=:" + type;
                        if (AbstractSiteBean.class.isAssignableFrom(clazz)) {
                            hql += " and siteCode = '" + this.getSiteCode() + "'";
                        }
                        List<T> fkBeanList = this.getGeneralDao().getByHQL(hql, Collections.singletonMap(type, cellContent));
                        if (!CollectionUtils.isNotEmpty(fkBeanList)) {
                            info.append("第" + (i + 1) + "行" + columnName + "不存在！，");
                            return;
                        }

                        if (fkBeanList.size() != 1) {
                            info.append("第" + (i + 1) + "行" + columnName + "外键数据查询到多条同名数据！，");
                            return;
                        }

                        fkBean = (T) fkBeanList.get(0);
                        fkBeanMap.put(key, fkBean);
                    } else {
                        fkBean = (T) fkBeanMap.get(key);
                    }

                    String id = fkBean.getId();
                    Object bean = ((Class) fkBeanType).newInstance();
                    method = bean.getClass().getMethod("setName", String.class);
                    method.invoke(bean, cellContent);
                    method = bean.getClass().getMethod("setId", String.class);
                    method.invoke(bean, id);
                    method = clazz.getMethod("set" + columnDataIndex, bean.getClass());
                    method.invoke(instance, bean);
                }
            } else {
                columnDataIndex = columnDataIndex.substring(0, 1).toUpperCase() + columnDataIndex.substring(1);
                Field field = clazz.getDeclaredField(columnConfig.getDataIndex());
                type = field.getType().getName();
                if (String.class.getName().equals(type)) {
                    method = clazz.getMethod("set" + columnDataIndex, String.class);
                    method.invoke(instance, cellContent);
                } else if (Double.class.getName().equals(type)) {
                    method = clazz.getMethod("set" + columnDataIndex, Double.class);
                    method.invoke(instance, Double.parseDouble(cellContent));
                } else if (Integer.class.getName().equals(type)) {
                    method = clazz.getMethod("set" + columnDataIndex, Integer.class);
                    method.invoke(instance, Integer.parseInt(cellContent));
                } else if (Long.class.getName().equals(type)) {
                    method = clazz.getMethod("set" + columnDataIndex, Long.class);
                    method.invoke(instance, Long.parseLong(cellContent));
                } else if (Short.class.getName().equals(type)) {
                    method = clazz.getMethod("set" + columnDataIndex, Short.class);
                    method.invoke(instance, Short.parseShort(cellContent));
                } else if (Date.class.getName().equals(type)) {
                    if (StringUtils.isNotBlank(cellContent)) {
                        method = clazz.getMethod("set" + columnDataIndex, Date.class);
                        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        if (cellContent.substring(0, cellContent.indexOf("-")).length() != 4) {
                            throw new EntityException("格式不正确");
                        }

                        method.invoke(instance, bartDateFormat.parse(cellContent));
                    }
                } else {
                    info.append("不支持的类型：").append(type).append("，");
                }
            }
        } catch (Exception var20) {
            var20.printStackTrace();
            info.append("第").append(i + 1).append("行").append(columnName).append("格式错误，添加失败！，");
        }

    }


    @Override
    public GridConfig initGrid(String siteCode, String actionId, Map<String, Object> mapParam) {
        if (StringUtils.isNotBlank(actionId) && !SiteConstant.SITE_CODE_CONTROL.equals(SiteUtil.getSiteCode())) {
            String gridCacheKey = CacheUtil.getCacheKeyWithParams("GRID_CACHE_%s_%s", new String[]{siteCode, actionId});
            String roleCode = StaticBeanUtils.getUserService().getCurrentUser().getRole().getCode();
            this.cacheService.removeDefaultFromMap(gridCacheKey, roleCode);
        }
        return super.initGrid(siteCode, actionId, mapParam);
    }

    /**
     * 模板方法，子类可以复用更改
     *
     * @return
     */
    protected String getSiteCode() {
        return MasterSlaveRoutingDataSource.getDbType();
    }
}
