package com.whaty.file.excel.upload.util;

import com.whaty.file.excel.upload.constant.ErrorInfoConstant;
import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.framework.exception.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel文件流操作工具类
 *
 * @author weipengsen
 */
public class ExcelStreamUtils {

    /**
     * 加载excel文件
     *
     * @param upload excel文件
     * @return excel文件操作对象
     * @throws StreamOperateException
     * @author weipengsen
     */
    public static Workbook loadFile(File upload) throws StreamOperateException, ServiceException {
        try {
            return loadFile(new FileInputStream(upload));
        } catch (FileNotFoundException e) {
            throw new StreamOperateException(e);
        }
    }

    /**
     * 加载excel文件
     *
     * @param input excel文件
     * @return excel文件操作对象
     * @throws StreamOperateException
     * @author weipengsen
     */
    public static Workbook loadFile(InputStream input) throws StreamOperateException, ServiceException {
        Workbook book;
        try {
            //读取文件
            book = WorkbookFactory.create(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StreamOperateException(e);
        }
        return book;
    }

    /**
     * 校验文件正确性
     *
     * @param book
     * @param headers
     * @param titleRowNum
     * @return
     */
    public static boolean checkExcelFile(Workbook book, Object[] headers, int titleRowNum)
            throws ServiceException, StreamOperateException {
        Sheet sheet = book.getSheetAt(0);
        int row = sheet.getLastRowNum() + 1;
        //校验文件是否为空
        if (row <= titleRowNum + 1) {
            throw new ServiceException(ErrorInfoConstant.EXCEL_FILE_IS_EMPTY);
        } else if (row == titleRowNum + 2) {
            Row firstRow = sheet.getRow(titleRowNum + 1);
            boolean has = false;
            for (int i = 0; i < firstRow.getLastCellNum(); i++) {
                Cell cell = firstRow.getCell(i);
                //判断数据行的第一行中内容是否都为空
                if (cell != null && StringUtils.isNotBlank(cell.toString())
                        && !ExcelConstant.SELECT_TIP.equals(cell.toString())) {
                    has = true;
                }
            }
            if (!has) {
                throw new ServiceException(ErrorInfoConstant.EXCEL_FILE_IS_EMPTY);
            }
        }
        //判断各个必填模板表头是否在文件中存在
        //拿到文件中的表头数组
        Row nowHeaders = sheet.getRow(titleRowNum);
        List<String> checkedHeader = new ArrayList<>();
        //循环模板表头
        for (Object header : headers) {
            if (StringUtils.isNotBlank((String) header)
                    && ((String) header).contains(ExcelConstant.MUST_SIGN)) {
                //是否存在标志
                boolean exists = false;
                //缓存文件表头
                for (int i = 0; i < nowHeaders.getLastCellNum(); i++) {
                    Cell headerCell = nowHeaders.getCell(i);
                    String headerContent = headerCell == null ? null : headerCell.toString();
                    //如果表头在文件中存在
                    if (header.equals(headerContent)) {
                        //设置标志位为true
                        exists = true;
                        //跳出循环
                        break;
                    }
                }
                if (!exists) {
                    //必填的表头在文件中不存在
                    throw new ServiceException(ErrorInfoConstant.EXCEL_HEADER_MUST_EXIST);
                }
            } else if (checkedHeader.contains(header)) {
                //表头存在两个相同的
                throw new ServiceException(String.format(ErrorInfoConstant.EXCEL_HEADER_IS_REPEAT, header));
            } else {
                //将表头存入已检查列表，用于查重
                checkedHeader.add((String) header);
            }
        }
        //校验当前文件的表头是否在模板中存在
        int col = nowHeaders.getLastCellNum();
        for (int i = 0; i < col; i++) {
            String content = nowHeaders.getCell(i) == null ? null : nowHeaders.getCell(i).toString();
            //如果表头为空则不计入表头数组
            if (StringUtils.isBlank(content)) {
                continue;
            }
            //表头在模板中存在
            int index = ExcelUtils.searchArray(content, headers);
            if (index == -1) {
                throw new ServiceException(ErrorInfoConstant.EXCEL_TEMPLATE_HEADER_NOT_EXIST);
            }
        }
        return true;
    }

    /**
     * 根据excel行与模板表头数组得到映射map，key-->当前文件中的索引，value-->在模板数组中的索引
     *
     * @param headers
     * @param nowHeaders
     * @return
     */
    public static Map<Integer, Integer> getHeaderMap(Object[] headers, Row nowHeaders) throws ServiceException {
        int col = nowHeaders.getLastCellNum();
        //新建一个map集合，存储文件中的表头与模板表头的对应表
        Map<Integer, Integer> mappedMap = new HashMap<>(16);
        //校验文件表头是否与模板表头对应，如果非空的标题存在则验证通过
        for (int i = 0; i < col; i++) {
            String content = nowHeaders.getCell(i) == null ? null : nowHeaders.getCell(i).toString();
            //如果表头为空则不计入表头数组
            if (StringUtils.isBlank(content)) {
                continue;
            }
            //表头在模板中存在
            int index = ExcelUtils.searchArray(content, headers);
            if (index != -1) {
                //存入映射，将文件列与模板列的数据对应
                mappedMap.put(i, index);
            }
        }
        return mappedMap;
    }

    /**
     * 关闭流
     *
     * @param book
     * @throws StreamOperateException
     */
    public static void closeBook(Workbook book) throws StreamOperateException {
        if (book != null) {
            try {
                book.close();
            } catch (IOException e) {
                throw new StreamOperateException(e);
            }
        }
    }

    /**
     * 判断文件的路径是否存在，不存在则创建目录
     *
     * @param realPath
     */
    public static void mkDir(String realPath) {
        //创建文件夹
        File file = new File(realPath.substring(0,
                realPath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取当前excel的sheet中的表头
     *
     * @param sheet
     * @param titleRowNum
     * @return
     */
    public static List<String> getCurrentHeaders(Sheet sheet, int titleRowNum) {
        Row nowHeaders = sheet.getRow(titleRowNum);
        List<String> currHeaders = new ArrayList<>();
        //校验文件表头是否与模板表头对应，如果非空的标题存在则验证通过
        for (int i = 0; i <= nowHeaders.getLastCellNum(); i++) {
            String content = nowHeaders.getCell(i) == null ? null : nowHeaders.getCell(i).toString();
            currHeaders.add(content);
        }
        return currHeaders;
    }
}
