package com.whaty.file.excel.upload.util;

import com.whaty.file.excel.upload.constant.ExcelConstant;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * excel表格样式操作工具
 *
 * @author weipengsen
 */
public class ExcelStyleUtils {

    /**
     * 获得必填表头单元格样式
     *
     * @param book
     * @return
     */
    public static CellStyle getRedStyle(Workbook book) {
        CellStyle mustStyle = null;
        if (book != null) {
            //增加红色样式
            mustStyle = book.createCellStyle();
            //设置字体
            Font redFont = book.createFont();
            redFont.setColor(Font.COLOR_RED);
            mustStyle.setFont(redFont);
        }
        return mustStyle;
    }

    /**
     * 将一列所有单元格都设置成文本格式
     *
     * @param book
     * @return HSSFCellStyle
     */
    public static CellStyle getTextFormatStyle(Workbook book) {
        //设置全部的列都为文本格式
        DataFormat format = book.createDataFormat();
        CellStyle textStyle = book.createCellStyle();
        textStyle.setDataFormat(format.getFormat("@"));
        return textStyle;
    }

    /**
     * 生成下拉选择框
     *
     * @param sheet
     * @param book
     * @param hasTitle 是否有标题
     * @param lastRow
     * @param colIndex
     * @param options
     * @return
     */
    public static DataValidation createConstraint(Sheet sheet, Workbook book, boolean hasTitle,
                                                  int lastRow, int colIndex, String[] options) {
        //判断是否有数据，没有则第一行填充请选择
        if (lastRow == 0) {
            //添加请选择提示
            Row towRow;
            if (sheet.getRow(hasTitle ? 2 : 1) == null) {
                towRow = sheet.createRow(hasTitle ? 2 : 1);
            } else {
                towRow = sheet.getRow(hasTitle ? 2 : 1);
            }
            towRow.createCell(colIndex).setCellValue(ExcelConstant.SELECT_TIP);
        }
        //判断当前数据行和默认填充行哪个大
        lastRow = lastRow > ExcelConstant.SELECT_FULL_NUM + (hasTitle ? 2 : 1) ? lastRow : ExcelConstant.SELECT_FULL_NUM;
        //拿到数据校验帮助对象
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper((HSSFSheet) sheet);
        //创建隐藏域
        Sheet hiddenSheet = book.createSheet(ExcelConstant.HIDDEN_SHEET + colIndex);
        //向隐藏域中赋值
        for (int i = 0; i < options.length; i++) {
            hiddenSheet.createRow((hasTitle ? 2 : 1) + ExcelConstant.SELECT_FULL_NUM + i)
                    .createCell(colIndex).setCellValue(options[i]);
        }
        //使用隐藏域穿件常量对象
        DataValidationConstraint dvConstraint = dvHelper
                .createFormulaListConstraint(ExcelConstant.HIDDEN_SHEET + colIndex);
        //创建函数
        Name categoryName = book.createName();
        //设置函数名
        categoryName.setNameName(ExcelConstant.HIDDEN_SHEET + colIndex);
        //设置函数表达式
        categoryName.setRefersToFormula(ExcelConstant.HIDDEN_SHEET + colIndex + "!A1:A" +
                (options.length + (hasTitle ? 2 : 1) + lastRow));
        //设置隐藏域隐藏
        book.setSheetHidden(book.getSheetIndex(ExcelConstant.HIDDEN_SHEET + colIndex), true);
        //设置选择框出现位置
        CellRangeAddressList addressList;
        addressList = new CellRangeAddressList((hasTitle ? 2 : 1), (hasTitle ? 2 : 1) + lastRow, colIndex, colIndex);
        return dvHelper.createValidation(dvConstraint, addressList);
    }

}
