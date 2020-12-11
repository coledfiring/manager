package com.whaty.products.service.superadmin.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.cache.util.CacheUtil;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.GridBasicConfig;
import com.whaty.core.framework.bean.GridColumnConfig;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.dao.GeneralDao;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.file.excel.upload.util.ExcelStreamUtils;
import com.whaty.framework.asserts.TycjAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.common.UtilService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * grid管理成教扩展
 *
 * @author weipengsen
 */
@Lazy
@Service("tycjGridManageService")
public class TycjGridManageServiceImpl extends TycjGridServiceAdapter<GridBasicConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    private final static String[] GRID_MANAGE_HEADERS = new String[] {"id*", "title*", "entityClass*",
            "canSearch*", "canAdd*", "canDelete*", "canUpdate*", "canBatchAdd*", "canProjections*", "firstSearch*",
            "enumConstByFlagActive*", "canCheckedAll*", "listType*", "isGroupSql*", "listFunction",
            "sql", "dc", "peWebSite*"};

    private final static String[] GRID_COLUMN_MANAGE_HEADERS = new String[] {"gridConfig*", "name*", "dataIndex*",
            "dataColumn", "search*", "toAdd*", "columnCanUpdate*", "list*", "report*", "type*", "dateFormat",
            "allowBlank*", "maxLength*", "checkMessage", "checkRegular", "comboSql", "serialNumber*",
            "enumConstByFlagActive*", "sqlResult", "isHtml"};

    /**
     * 上传配置
     *
     * @param upload
     * @throws StreamOperateException
     */
    public Map<String, Object> doUploadGridConfig(File upload) throws StreamOperateException {
        // 加载文件
        Workbook book = null;
        Map<String, Object> resultMap = new HashMap<>(4);
        try {
            book = ExcelStreamUtils.loadFile(upload);
            ExcelStreamUtils.checkExcelFile(book, GRID_MANAGE_HEADERS, 0);
            Sheet sheet = book.getSheetAt(0);
            // 获得标题行
            Row headerRow = sheet.getRow(0);
            List<String> headers = IntStream.range(0, headerRow.getLastCellNum())
                    .mapToObj(headerRow::getCell)
                    .peek(cell -> cell.setCellType(CellType.STRING))
                    .map(Cell::getStringCellValue)
                    .map(e -> e.replace("*", ""))
                    .collect(Collectors.toList());
            List<GridBasicConfig> configs = IntStream.range(1, sheet.getLastRowNum() + 1)
                    .mapToObj(sheet::getRow)
                    .map(e -> this.convertRowToConfig(headers, e))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            this.myGeneralDao.saveAll(configs);
            resultMap.put("msg", "导入成功" + configs.size() + "条");
            return resultMap;
        } catch (AbstractBasicException e) {
            throw new ServiceException(e.getInfo());
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            ExcelStreamUtils.closeBook(book);
        }
    }

    /**
     * 将行转化为配置
     * @param headers
     * @param row
     * @return
     */
    private GridBasicConfig convertRowToConfig(List<String> headers, Row row) {
        if (IntStream.range(0, row.getLastCellNum()).filter(e -> row.getCell(e) != null).count() <= 0) {
            return null;
        }
        GridBasicConfig config = new GridBasicConfig();
        IntStream.range(0, row.getLastCellNum())
                .mapToObj(row::getCell)
                .peek(cell -> {
                    if (Objects.nonNull(cell)) {
                        cell.setCellType(CellType.STRING);
                    }
                }).filter(e -> Objects.nonNull(e) && !"请选择".equals(e.getStringCellValue()))
                .forEach(e -> this.setFieldToConfig(headers.get(e.getColumnIndex()), config, e.getStringCellValue()));
        return config;
    }

    /**
     * 将字段写入到配置中
     * @param fieldName
     * @param config
     * @param value
     */
    private void setFieldToConfig(String fieldName, GridBasicConfig config, String value) {
        if ("peWebSite".equals(fieldName)) {
            PeWebSite site = (PeWebSite) this.myGeneralDao.getOneByHQL("from PeWebSite where name = ?", value);
            config.setPeWebSite(site);
        } else if ("enumConstByFlagActive".equals(fieldName)) {
            config.setEnumConstByFlagActive((EnumConst) this.myGeneralDao
                    .getOneByHQL("from EnumConst where name = ? and namespace = ?", value, "flagActive"));
        } else {
            try {
                Field field = config.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (String.class.isAssignableFrom(field.getType())) {
                    field.set(config, value);
                } else {
                    Object target = BasicPackageClass.convertType(value, field.getType());
                    field.set(config, target);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new UncheckException(e);
            }
        }
    }

    /**
     * 下载grid配置
     * @param outputStream
     */
    public void downUploadGridConfig(OutputStream outputStream) {
        Map<String, List<String>> selectMap = new HashMap<>(16);
        selectMap.put("canSearch*", Arrays.asList("0", "1"));
        selectMap.put("canAdd*", Arrays.asList("0", "1"));
        selectMap.put("canDelete*", Arrays.asList("0", "1"));
        selectMap.put("canUpdate*", Arrays.asList("0", "1"));
        selectMap.put("canBatchAdd*", Arrays.asList("0", "1"));
        selectMap.put("canProjections*", Arrays.asList("0", "1"));
        selectMap.put("firstSearch*", Arrays.asList("0", "1"));
        selectMap.put("enumConstByFlagActive*", Arrays.asList("是", "否"));
        selectMap.put("canCheckedAll*", Arrays.asList("0", "1"));
        selectMap.put("listType*", Arrays.asList("0", "1"));
        selectMap.put("isGroupSql*", Arrays.asList("0", "1"));
        selectMap.put("listFunction", Arrays.asList("1", "3"));
        selectMap.put("peWebSite*", this.myGeneralDao
                .getBySQL("select name from pe_web_site"));
        this.utilService.generateExcelAndWrite(outputStream, selectMap, GRID_MANAGE_HEADERS);
    }

    /**
     * 下载column配置
     * @param outputStream
     */
    public void downUploadGridColumnConfig(OutputStream outputStream) {
        Map<String, List<String>> selectMap = new HashMap<>(16);
        selectMap.put("search*", Arrays.asList("0", "1"));
        selectMap.put("toAdd*", Arrays.asList("0", "1"));
        selectMap.put("columnCanUpdate*", Arrays.asList("0", "1"));
        selectMap.put("list*", Arrays.asList("0", "1"));
        selectMap.put("report*", Arrays.asList("0", "1"));
        selectMap.put("type*", Arrays.asList("TextField", "select", "date", "datetime", "textArea", "textEditor",
                "file", "checkbox"));
        selectMap.put("dateFormat", Arrays.asList("yyyy-MM-dd", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss"));
        selectMap.put("enumConstByFlagActive*", Arrays.asList("是", "否"));
        selectMap.put("allowBlank*", Arrays.asList("0", "1"));
        selectMap.put("isHtml", Arrays.asList("0", "1"));
        this.utilService.generateExcelAndWrite(outputStream, selectMap, GRID_COLUMN_MANAGE_HEADERS);
    }

    /**
     * 导入column配置
     * @param upload
     * @return
     */
    public Map<String, Object> doUploadGridColumnConfig(File upload) throws StreamOperateException {
        // 加载文件
        Workbook book = null;
        Map<String, Object> resultMap = new HashMap<>(4);
        try {
            book = ExcelStreamUtils.loadFile(upload);
            ExcelStreamUtils.checkExcelFile(book, GRID_COLUMN_MANAGE_HEADERS, 0);
            Sheet sheet = book.getSheetAt(0);
            // 获得标题行
            Row headerRow = sheet.getRow(0);
            List<String> headers = IntStream.range(0, headerRow.getLastCellNum())
                    .mapToObj(headerRow::getCell)
                    .peek(cell -> cell.setCellType(CellType.STRING))
                    .map(Cell::getStringCellValue)
                    .map(e -> e.replace("*", ""))
                    .collect(Collectors.toList());
            List<GridColumnConfig> configs = IntStream.range(1, sheet.getLastRowNum() + 1)
                    .mapToObj(sheet::getRow)
                    .map(e -> this.convertRowToColumnConfig(headers, e))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            this.myGeneralDao.saveAll(configs);
            List<String> gridIds = configs.stream().map(e -> e.getGridBasicConfig().getId())
                    .collect(Collectors.toList());
            resultMap.put("msg", "导入成功" + configs.size() + "条");
            SiteUtil.listSite().forEach(site ->
                gridIds.forEach(e -> {
                    String cacheKey = CacheUtil.getCacheKeyWithParams("GRID_CACHE_%s_%s", site.getCode(), e);
                    this.getCacheService().remove(cacheKey);
                }));
            return resultMap;
        } catch (AbstractBasicException e) {
            throw new ServiceException(e.getInfo());
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        } finally {
            ExcelStreamUtils.closeBook(book);
        }
    }

    /**
     * 将行转化为配置
     * @param headers
     * @param row
     * @return
     */
    private GridColumnConfig convertRowToColumnConfig(List<String> headers, Row row) {
        if (IntStream.range(0, row.getLastCellNum()).filter(e -> row.getCell(e) != null).count() <= 0) {
            return null;
        }
        GridColumnConfig config = new GridColumnConfig();
        IntStream.range(0, row.getLastCellNum())
                .mapToObj(row::getCell)
                .peek(cell -> {
                    if (Objects.nonNull(cell)) {
                        cell.setCellType(CellType.STRING);
                    }
                }).filter(e -> Objects.nonNull(e) && !"请选择".equals(e.getStringCellValue()))
                .forEach(e -> this.setFieldToColumnConfig(headers.get(e.getColumnIndex()), config, e.getStringCellValue()));
        return config;
    }

    /**
     * 将字段写入到配置中
     * @param fieldName
     * @param config
     * @param value
     */
    private void setFieldToColumnConfig(String fieldName, GridColumnConfig config, String value) {
        if ("gridConfig".equals(fieldName)) {
            GridBasicConfig grid = (GridBasicConfig) this.myGeneralDao
                    .getOneByHQL("from GridBasicConfig where id = ?", value);
            config.setGridBasicConfig(grid);
        } else if ("enumConstByFlagActive".equals(fieldName)) {
            config.setEnumConstByFlagActive((EnumConst) this.myGeneralDao
                    .getOneByHQL("from EnumConst where name = ? and namespace = ?", value, "flagActive"));
        } else {
            try {
                Field field = config.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (String.class.isAssignableFrom(field.getType())) {
                    field.set(config, value);
                } else {
                    Object target = BasicPackageClass.convertType(value, field.getType());
                    field.set(config, target);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new UncheckException(e);
            }
        }
    }

    private enum BasicPackageClass {

        INTEGER(Integer.class, Integer::parseInt),

        DOUBLE(Double.class, Double::parseDouble),

        BOOLEAN(Boolean.class, Boolean::parseBoolean),

        LONG(Long.class, Long::parseLong),
        ;

        private Class packageClass;

        private Function<String, Object> convertFunction;

        BasicPackageClass(Class packageClass, Function<String, Object> convertFunction) {
            this.packageClass = packageClass;
            this.convertFunction = convertFunction;
        }

        public Class getPackageClass() {
            return packageClass;
        }

        public Function<String, Object> getConvertFunction() {
            return convertFunction;
        }

        public static Object convertType(String value, Class<?> type) {
            BasicPackageClass packageClass = Arrays.stream(values())
                    .filter(e -> e.getPackageClass().isAssignableFrom(type)).findFirst().orElse(null);
            TycjAssert.isAllNotNull(packageClass);
            return packageClass.getConvertFunction().apply(value);
        }
    }
}
