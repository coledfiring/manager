package com.whaty.file.excel.upload.util;

import com.whaty.file.excel.upload.constant.ErrorInfoConstant;
import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.framework.exception.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 写excel文件工具类
 *
 * @author weipengsen
 */
public class ExcelWriteUtils {

    /**
     * 将数据写入excel文件，按照传入路径生成文件，含有指定的表头数据
     *
     * @param realPath 生成文件的路径
     * @param headers  表头数组
     * @throws StreamOperateException
     * @throws ServiceException
     * @author weipengsen
     */
    public static void writeDataToExcel(String realPath, Object[] headers)
            throws StreamOperateException, ServiceException {
        writeDataToExcel(realPath, headers, null, null, null);
    }

    /**
     * 将数据写入excel文件，按照传入路径生成文件，含有指定表头数据，预填内容
     *
     * @param realPath 生成文件的路径
     * @param headers  表头数组
     * @param data     预填信息集合，与header对应排列
     * @throws StreamOperateException
     * @throws ServiceException
     * @author weipengsen
     */
    public static void writeDataToExcel(String realPath, Object[] headers, List<Object[]> data)
            throws StreamOperateException, ServiceException {
        writeDataToExcel(realPath, headers, null, null, data);
    }

    /**
     * 将数据写入excel文件，按照传入路径生成文件，含有指定表头数据，预填内容，根据header生成选择框
     *
     * @param realPath      生成文件的路径
     * @param headers       表头数组
     * @param info          标题
     * @param selectHeaders 选择框数据集合 key --> headers中的对应的字符串，value --> 选择框数据
     * @param data          预填信息集合，与header对应排列
     * @throws StreamOperateException
     * @throws ServiceException
     */
    public static void writeDataToExcel(String realPath, Object[] headers, String info, Map<String, List<String>> selectHeaders,
                                        List<Object[]> data) throws StreamOperateException, ServiceException {
        Workbook book = null;
        ExcelStreamUtils.mkDir(realPath);
        try (OutputStream out = new FileOutputStream(realPath)) {
            book = writeDataToExcel(headers, info, selectHeaders, data);
            //写入io
            book.write(out);
        } catch (IOException e) {
            throw new StreamOperateException(e);
        } finally {
            ExcelStreamUtils.closeBook(book);
        }
    }

    /**
     * 将数据写入excel文件，按照传入路径生成文件，含有指定表头数据，预填内容，根据header生成选择框，并返回book
     *
     * @param headers       表头数组
     * @param info          标题
     * @param selectHeaders 选择框数据集合 key --> headers中的对应的字符串，value --> 选择框数据
     * @param data          预填信息集合，与header对应排列
     * @throws ServiceException
     */
    public static Workbook writeDataToExcel(Object[] headers, String info, Map<String, List<String>> selectHeaders,
                                            List<Object[]> data) throws ServiceException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet(ExcelConstant.FIRST_SHEET);
        //选择框最后一列序号
        int lastRow = 0;
        //增加红色样式
        CellStyle redStyle = ExcelStyleUtils.getRedStyle(book);
        int headerIndex = 0;
        //插入标题
        if (StringUtils.isNotBlank(info)) {
            headerIndex = 1;
            Row infoRow = sheet.createRow(0);
            Cell titleCell = infoRow.createCell(0);
            titleCell.setCellStyle(redStyle);
            titleCell.setCellValue(info);
            //合并单元格
            CellRangeAddress cell = new CellRangeAddress(0, 0, 0, headers.length - 1);
            sheet.addMergedRegion(cell);
        }
        //写入表头
        Row row = sheet.createRow(headerIndex);
        for (int i = 0, len = headers.length; i < len; i++) {
            Cell cell = row.createCell(i);
            HSSFDataFormat format = (HSSFDataFormat) book.createDataFormat();
            //设置全部的列都为文本格式
            HSSFCellStyle textStyle = (HSSFCellStyle) book.createCellStyle();
            textStyle.setDataFormat(format.getFormat("@"));
            sheet.setDefaultColumnStyle(i, textStyle);
            //非空的标题增加红色字体样式
            if (String.valueOf(headers[i]).contains(ExcelConstant.MUST_SIGN)) {
                redStyle.setDataFormat(format.getFormat("@"));
                cell.setCellStyle(redStyle);
            }
            cell.setCellType(CellType.STRING);
            //写入值
            cell.setCellValue(headers[i] == null ? "" : String.valueOf(headers[i]));
            if (MapUtils.isNotEmpty(selectHeaders) && selectHeaders.containsKey(String.valueOf(headers[i]))) {
                //选项列表
                List<String> options = selectHeaders.get(String.valueOf(headers[i]));
                sheet.addValidationData(ExcelStyleUtils.createConstraint(sheet, book, StringUtils.isNotBlank(info),
                        lastRow, i, options.toArray(new String[options.size()])));
            }
        }
        //写入数据
        if (CollectionUtils.isNotEmpty(data)) {
            if (data.size() >= ExcelConstant.MAX_LIMIT_ROW) {
                throw new ServiceException(String.format(ErrorInfoConstant.WRITE_EXCEL_OVER_LIMIT, data.size()));
            }
            for (int i = 0, len = data.size(); i < len; i++) {
                Row contentRow = sheet.createRow(headerIndex + i + 1);
                for (int j = 0, len2 = data.get(i).length; j < len2; j++) {
                    Cell cell = contentRow.createCell(j);
                    cell.setCellValue(data.get(i)[j] == null ? "" : String.valueOf(data.get(i)[j]));
                }
                lastRow++;
            }
        }
        return book;
    }
    /**
     * 将数据写入到excel指定单元格位置
     *
     * @param rowStart 写入数据起始行
     * @param colStart 写入数据起始列
     * @param sheet    文件流
     * @param listData 动态写入的数据集合
     */
    public static Sheet writeDataToSheetByPosition(int rowStart, int colStart, Sheet sheet, List<Object> listData) {
        return writeDataToSheetByPosition(rowStart, colStart, sheet, listData, null);
    }

    /**
     * 将数据写入到excel指定单元格位置
     *
     * @param rowStart 写入数据起始行
     * @param colStart 写入数据起始列
     * @param sheet    文件流
     * @param listData 动态写入的数据集合
     * @param style    单元格样式
     */
    public static Sheet writeDataToSheetByPosition(int rowStart, int colStart, Sheet sheet, List<Object> listData, CellStyle style) {
        //遍历行 一行一行将数据填充
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getClass().isArray()) {
                Object[] objs = (Object[]) listData.get(i);
                //遍历列，将数据写入
                for (int j = colStart; j < objs.length + colStart; j++) {
                    Row row = sheet.getRow(rowStart);
                    if (row == null) {
                        row = sheet.createRow(i);
                    }
                    Cell cell = row.createCell(j);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(objs[j - colStart].toString());
                    cell.setCellStyle(style);
                }
                rowStart++;
            } else {
                for (int j = 0; j < listData.size(); j++) {
                    Row row = sheet.getRow(rowStart);
                    Cell cell = row.getCell(colStart);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(listData.get(j).toString());
                    cell.setCellStyle(style);
                    rowStart++;
                }
            }
        }
        return sheet;
    }
    /**
     * 循环excel计算单元格合计值
     *
     * @param sheet
     */
    public static void totalCellValue(Sheet sheet, List cellList) {
        Row headRow = sheet.getRow(0);
        int totalCells = headRow.getLastCellNum();
        int totalRows = sheet.getLastRowNum();
        for (int i = 0; i <= totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            for (short j = 0; j < totalCells; j++) {
                Cell cell = row.getCell(j);
                cell.setCellType(CellType.STRING);
                String[] cellValues = cell.getStringCellValue().split(":");
                if (ExcelConstant.XLS_PARAM_TYPE.equals(cellValues[0]) && !ExcelConstant.PARAM_TYPE_sql.equals(cellValues[1])) {
                    ExcelWriteUtils.setCellValue(sheet, cell, cellList);
                }
            }
        }
    }
    /**
     *
     * 设置（计算总和类型的）单元格数据
     *
     * @param sheet         文件流 根据单元格表达式计算并赋值
     * @param cell          单元格对象
     * @param cellList      计算各个合计的总和,存储起来放到最后计算的list
     */
    public static void setCellValue(Sheet sheet, Cell cell, List<Cell> cellList) {
        int value = 0;
        String[] params = cell.getStringCellValue().split(":");
        //指定参数范围下限
        int rangeDown = Integer.parseInt(params[2]);
        //指定参数值上限
        int rangeUp = Integer.parseInt(params[3]);
        // 行数固定,计算指定列范围的总和
        if (ExcelConstant.PARAM_TYPE_COLUMN_NUM.equals(params[1])) {
            Cell cellTemp;
            Row row = sheet.getRow(cell.getRowIndex());
            for (int i = rangeDown; i <= rangeUp; i++) {
                cellTemp = row.getCell(i);
                cellTemp.setCellType(CellType.STRING);
                String cellValue = cellTemp.getStringCellValue().trim();
                if (cellList instanceof List && StringUtils.isNotEmpty(cellValue) && cellValue.contains(ExcelConstant.XLS_PARAM_TYPE)) {
                    cellList.add(cell);
                    return;
                }
                if (StringUtils.isNotEmpty(cellValue)) {
                    value += Integer.parseInt(cellValue);
                }
            }
        }
        // 列数固定,计算指定行数的总和
        if (ExcelConstant.PARAM_TYPE_ROW_NUM.equals(params[1])) {
            Row row;
            Cell cellTemp;
            for (int i = rangeDown; i <= rangeUp; i++) {
                row = sheet.getRow(i);
                cellTemp = row.getCell(cell.getColumnIndex());
                cellTemp.setCellType(CellType.STRING);
                String cellValue = cellTemp.getStringCellValue().trim();
                if (cellList instanceof List && StringUtils.isNotEmpty(cellValue) && cellValue.contains(ExcelConstant.XLS_PARAM_TYPE)) {
                    cellList.add(cell);
                    return;
                }
                if (StringUtils.isNotEmpty(cellValue)) {
                    value += Integer.parseInt(cellValue);
                }
            }
        }
        cell.setCellValue(String.valueOf(value));
    }
}
