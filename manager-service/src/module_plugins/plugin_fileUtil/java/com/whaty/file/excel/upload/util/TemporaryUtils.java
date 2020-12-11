package com.whaty.file.excel.upload.util;

import com.whaty.core.commons.util.excel.ExcelReadUtil;
import com.whaty.file.excel.upload.constant.ErrorInfoConstant;
import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.file.excel.upload.exception.DataOperateException;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.utils.StaticBeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 临时表操作工具类
 *
 * @author weipengsen
 */
public class TemporaryUtils {

    /**
     * 一个sql语句拼接多少个数据记录
     */
    private static final int DIVIDE_SQL_NUM = 900;

    /**
     * 将sheet中的数据存入临时表，支持索引映射
     *
     * @param sheet       sheet对象
     * @param namespace   临时表namespace
     * @param titleRowNum 标题行数（不包括表头）
     * @param mappedMap   存入的数据列索引与需要交换到的列索引的对应表
     * @return 成功插入的数据量
     * @throws DataOperateException
     * @throws ServiceException
     * @author weipengsen
     */
    public static int insertTemporaryTableFromExcel(Sheet sheet, String namespace, int titleRowNum,
                                                    Map<Integer, Integer> mappedMap)
            throws DataOperateException, ServiceException {
        // 校验sheet中是否为空
        int rowNum;
        if (sheet == null || (rowNum = sheet.getLastRowNum() + 1) <= titleRowNum + 1) {
            throw new ServiceException(ErrorInfoConstant.EXCEL_FILE_IS_EMPTY);
        }
        // 验证namespace是否为空
        if (StringUtils.isBlank(namespace)) {
            throw new DataOperateException(ErrorInfoConstant.TEMPORARY_NAMESPACE_IS_EMPTY);
        }
        // 拼接sql的集合
        List<String> sqlList = new ArrayList<>();
        // 一个insert语句包含记录数
        int divideNum = DIVIDE_SQL_NUM;
        // insert语句拼接字符串
        StringBuilder sql = new StringBuilder();
        StringBuilder rowSql = new StringBuilder();
        boolean fileIsNotNull = false;
        for (int i = titleRowNum + 1, len = rowNum; i < len; i++) {
            Row row = sheet.getRow(i);
            // 第一个数据和超过分割数的数据插入insert
            if (i % divideNum == titleRowNum + 1) {
                sql.append("insert into util_excel_table(id,namespace,row,valid");
                // 根据传入的sheet对象拼接column数
                if (MapUtils.isNotEmpty(mappedMap)) {
                    for (Map.Entry<Integer, Integer> mapped : mappedMap.entrySet()) {
                        // 映射表不为空，数据存在映射到的列
                        sql.append(",column").append((mapped.getValue() + 1));
                    }
                } else {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        // 映射表不为空，数据存在映射到的列
                        sql.append(",column").append((j + 1));
                    }
                }
                sql.append(") values");
            }
            if (row != null) {
                rowSql.delete(0, rowSql.length());
                boolean rowIsNotNull = false;
                // 拼接每个记录
                rowSql.append("(replace(uuid(),'-',''),'").append(namespace).append("',")
                        .append((i + 1)).append(",'1'");
                // 根据传入的sheet对象拼接insert语句的值
                if (MapUtils.isNotEmpty(mappedMap)) {
                    for (Integer index : mappedMap.keySet()) {
                        rowIsNotNull = getContentAndJudgeNull(row, index, rowSql) || rowIsNotNull;
                    }
                } else {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        rowIsNotNull = getContentAndJudgeNull(row, j, rowSql) || rowIsNotNull;
                    }
                }
                rowSql.append("),");
                if (rowIsNotNull) {
                    sql.append(rowSql);
                    fileIsNotNull = true;
                }
            }
            // 最后一个记录和即将超过分割数的记录删除最后的“，”并将insert语句放入集合，清空拼接字符串
            if (i % divideNum == titleRowNum || i == len - 1) {
                if (sql.toString().contains("),")) {
                    sql.delete(sql.length() - 1, sql.length());
                    sqlList.add(sql.toString());
                    sql.delete(0, sql.length());
                }
            }
        }
        if (!fileIsNotNull) {
            throw new ServiceException(ErrorInfoConstant.EXCEL_FILE_IS_EMPTY);
        }
        // 批量执行
        return StaticBeanUtils.getGeneralDao().batchExecuteSql(sqlList);
    }

    /**
     * 向字符对象中插入单元格内容，并且返回单元格是否为空
     *
     * @param row
     * @param index
     * @param rowSql
     * @return
     */
    private static boolean getContentAndJudgeNull(Row row, int index, StringBuilder rowSql) {
        Cell cell = row.getCell(index);
        String cellContent = null;
        if (cell != null) {
            // poi使用cellType在获取value的时候格式化内容，所以在获取内容之前统一设置单元格格式为1 -》 字符串类型
            cell.setCellType(CellType.STRING);
            cellContent = cell.getStringCellValue();
        }
        rowSql.append(",").append(StringUtils.isBlank(cellContent) || ExcelConstant.SELECT_TIP.equals(cellContent)
                ? "NULL" : ("'" + cellContent + "'"));
        // 如果单元格内容不为空
        return StringUtils.isNotBlank(cellContent);
    }

    /**
     * 临时表校验工具方法,其中checkItems是校验信息与校验语句的集合，数组第一个元素为校验失败的信息，第二个是校验使用的sql，其中sql的前部已经
     * 在方法中集成，集成部分为：
     * update util_excel_table e set e.valid = '2',e.error_info = concat(ifnull(e.error_info, ''), '%s') where
     * e.namespace = '"+namespace+"' AND %s 其中第一个站位符是校验失败信息，第二个为语句，语句常用格式为EXISTS ()和NOT EXISTS ()
     * 如果不对表进行联查可直接写e.columnN = '...'或者e.columnN REGEXP ''之类的必要校验，
     * 另外不需要进行非空校验，方法中集成了根据headers中是否包含“*”进行非空校验的方法
     *
     * @param namespace  临时表空间namespace
     * @param checkItems 自定义校验使用的校验信息
     * @param headers    模板头标题
     * @throws DataOperateException
     * @author weipengsen
     */
    public static void checkExcelTable(String namespace, List<String[]> checkItems, Object[] headers)
            throws DataOperateException {
        String checkSql = "update util_excel_table e set e.valid = '2',e.error_info = concat(ifnull(e.error_info, ''),"
                + " '%s') where e.namespace = '" + namespace + "' and %s";
        //校验字段非空
        if (headers != null && headers.length != 0) {
            for (int i = 0; i < headers.length; i++) {
                if (((String) headers[i]).contains("*")) {
                    StaticBeanUtils.getGeneralDao().executeBySQL(String.format(checkSql,
                            String.format(ErrorInfoConstant.TEMPORARY_DATA_IS_NULL,
                                    ((String) headers[i]).replace("*", ""))
                            , "e.column" + (i + 1) + " is null or e.column" + (i + 1) + " = ''"));
                }
            }
        }
        checkCustomExcelTable(namespace, checkItems);
    }

    /**
     * 临时表校验工具方法,其中checkItems是校验信息与校验语句的集合，数组第一个元素为校验失败的信息，第二个是校验使用的sql，其中sql的前部已经
     * 在方法中集成，集成部分为：
     * update util_excel_table e set e.valid = '2',e.error_info = concat(ifnull(e.error_info, ''), '%s') where
     * e.namespace = '"+namespace+"' AND %s 其中第一个站位符是校验失败信息，第二个为语句，语句常用格式为EXISTS ()和NOT EXISTS ()
     * 如果不对表进行联查可直接写e.columnN = '...'或者e.columnN REGEXP ''之类的必要校验，
     *
     * @param namespace  临时表空间namespace
     * @param checkItems 自定义校验使用的校验信息
     * @throws DataOperateException
     * @author weipengsen
     */
    public static void checkCustomExcelTable(String namespace, List<String[]> checkItems) {
        String checkSql = "update util_excel_table e set e.valid = '2',e.error_info = concat(ifnull(e.error_info, ''),"
                + " '%s') where e.namespace = '" + namespace + "' and %s";
        //自定义校验
        if (CollectionUtils.isNotEmpty(checkItems)) {
            for (String[] item : checkItems) {
                StaticBeanUtils.getGeneralDao().executeBySQL(String.format(checkSql, item[0],
                        item[1].replace("${namespace}", "'" + namespace + "'")));
            }
        }
    }

    /**
     * 得到临时表空间的错误信息
     *
     * @param namespace 临时表命名空间
     * @return 返回的提示信息
     * @throws DataOperateException
     * @author weipengsen
     */
    public static String getExcelErrorInfo(String namespace) throws DataOperateException {
        StringBuilder info = new StringBuilder();
        //返回错误信息
        List<Object[]> errorList = getExcelErrorInfoToList(namespace);
        if (CollectionUtils.isNotEmpty(errorList)) {
            info.append(ErrorInfoConstant.TEMPORARY_ERROR_INFO_PREFIX + ExcelConstant.NEW_LINE_SIGN);
            for (Object[] errorArr : errorList) {
                info.append(String.format(ExcelConstant.UPLOAD_ERROR_INFO, errorArr[0], errorArr[1]))
                        .append(ExcelConstant.NEW_LINE_SIGN);
            }
        }
        return info.toString();
    }

    /**
     * 从数据库查询错误信息
     *
     * @param namespace
     * @return
     * @throws DataOperateException
     */
    public static List<Object[]> getExcelErrorInfoToList(String namespace) throws DataOperateException {
        List<Object[]> errorList;
        try {
            String errorSql = "select row,error_info from util_excel_table where valid = '2' and namespace = '"
                    + namespace + "'";
            errorList = StaticBeanUtils.getGeneralDao().getBySQL(errorSql);
        } catch (Exception e) {
            throw new DataOperateException(e);
        }
        return errorList;
    }

    /**
     * 获取临时表空间中的错误信息数量
     *
     * @param namespace
     * @return
     * @throws DataOperateException
     */
    public static int getExcelErrorNum(String namespace) throws DataOperateException {
        int count;
        try {
            String sql = "select count(id) from util_excel_table where valid = '2' and namespace = '" + namespace + "'";
            List<Object> numList = StaticBeanUtils.getGeneralDao().getBySQL(sql);
            count = Integer.parseInt(String.valueOf(numList.get(0)));
        } catch (Exception e) {
            throw new DataOperateException(e);
        }
        return count;
    }

    /**
     * 将临时表中的错误信息写入文件，原本的excel文件中删除成功行，
     * 增加一列“错误信息”将临时表中的错误信息写入都对应的行
     *
     * @param realPath    生成文件的路径
     * @param excelFile   上传的原始文件
     * @param namespace   临时表命名空间
     * @param titleRowNum 文件中的非表头标题行数（如说明之类的在表头上的标题行数）
     * @throws StreamOperateException
     * @throws DataOperateException
     * @throws ServiceException
     */
    public static void writeTemporaryErrorInfoToFile(String realPath, File excelFile, String namespace,
                                                     int titleRowNum)
            throws DataOperateException, StreamOperateException, ServiceException {
        Workbook originBook = null;
        Workbook errorBook = null;
        List<Object[]> errorList = getExcelErrorInfoToList(namespace);
        ExcelStreamUtils.mkDir(realPath);
        if (CollectionUtils.isNotEmpty(errorList)) {
            try (OutputStream out = new FileOutputStream(realPath)) {
                originBook = ExcelStreamUtils.loadFile(excelFile);
                errorBook = new HSSFWorkbook();
                Sheet originSheet = originBook.getSheetAt(0);
                Sheet errorSheet = errorBook.createSheet(ExcelConstant.FIRST_SHEET);
                // 增加错误信息的列索引
                int errorColumnNum = originSheet.getRow(titleRowNum).getLastCellNum();
                // 写入标题
                if (titleRowNum != 0) {
                    for (int i = 0; i < titleRowNum; i++) {
                        // 拿到源文件中的标题
                        String title = originSheet.getRow(i).getCell(0).toString();
                        // 合并单元格
                        CellRangeAddress cell = new CellRangeAddress(i, i, 0, errorColumnNum - 1);
                        errorSheet.addMergedRegion(cell);
                        // 将标题放到新的标题
                        errorSheet.createRow(i).createCell(0).setCellValue(title);
                    }
                }
                // 写入表头
                Row headerRow = originSheet.getRow(titleRowNum);
                Row newHeaderRow = errorSheet.createRow(titleRowNum);
                CellStyle redStyle = ExcelStyleUtils.getRedStyle(errorBook);
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    // 设置全部的列都为文本格式
                    HSSFDataFormat format = (HSSFDataFormat) errorBook.createDataFormat();
                    HSSFCellStyle textStyle = (HSSFCellStyle) errorBook.createCellStyle();
                    textStyle.setDataFormat(format.getFormat("@"));
                    errorSheet.setDefaultColumnStyle(i, textStyle);
                    // 将表头放入新文件中
                    Cell cell = headerRow.getCell(i);
                    String header = cell == null ? null : cell.toString();
                    Cell headerCell = newHeaderRow.createCell(i);
                    headerCell.setCellValue(header);
                    if (StringUtils.isNotBlank(header) && header.contains(ExcelConstant.MUST_SIGN)) {
                        headerCell.setCellStyle(redStyle);
                    }
                }
                // 写入错误表头
                Row header = errorSheet.getRow(titleRowNum);
                Cell headerCell = header.createCell(errorColumnNum);
                headerCell.setCellStyle(redStyle);
                headerCell.setCellValue(ExcelConstant.ERROR_INFO_HEADER);
                // 循环错误信息
                for (int i = 0; i < errorList.size(); i++) {
                    // 将错误信息放到对应的行列中
                    // 错误的行数
                    int row = (Integer) errorList.get(i)[0];
                    // 错误的信息
                    String errorInfo = (String) errorList.get(i)[1];
                    // 源文件中的错误行
                    Row errorRow = originSheet.getRow(row - 1);
                    // 新文件中对应行
                    Row newRow = errorSheet.createRow(i + titleRowNum + 1);
                    // 将源文件中的数据放到新文件
                    for (int j = 0; j < errorRow.getLastCellNum(); j++) {
                        // 源文件中的单元格数据
                        Cell cell = errorRow.getCell(j);
                        String content = String.valueOf(ExcelReadUtil.getCellFormatValue(cell));
                        if (ExcelConstant.SELECT_TIP.equals(content)) {
                            content = null;
                        }
                        // 将数据放到新文件对应格中
                        newRow.createCell(j).setCellValue(content);
                    }
                    // 将错误信息放到单元格中
                    Cell errorCell = newRow.createCell(errorColumnNum);
                    errorCell.setCellStyle(redStyle);
                    errorCell.setCellValue(errorInfo);
                }
                errorBook.write(out);
            } catch (IOException e) {
                throw new StreamOperateException(e);
            } finally {
                ExcelStreamUtils.closeBook(originBook);
                ExcelStreamUtils.closeBook(errorBook);
            }
        }
    }

}
