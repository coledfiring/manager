package com.whaty.file.excel.upload.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.file.excel.upload.constant.ErrorInfoConstant;
import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.file.excel.upload.exception.DataOperateException;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.file.excel.upload.service.ExcelUploadService;
import com.whaty.file.excel.upload.util.ExcelUtils;
import com.whaty.file.excel.upload.util.ExcelStreamUtils;
import com.whaty.file.excel.upload.util.ExcelWriteUtils;
import com.whaty.file.excel.upload.util.TemporaryUtils;
import com.whaty.framework.exception.ServiceException;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * excel上传服务类
 * @author weipengsen
 */
@Service("excelUploadService")
public class ExcelUploadServiceImpl implements ExcelUploadService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void downBatchTemplate(String realPath, Object[] headers) throws ServiceException, StreamOperateException {
        ExcelWriteUtils.writeDataToExcel(realPath, headers);
    }

    @Override
    public void downBatchTemplate(String realPath, Object[] headers, List<Object[]> data)
            throws ServiceException, StreamOperateException {
        ExcelWriteUtils.writeDataToExcel(realPath, headers, data);
    }

    @Override
    public void downBatchTemplate(String realPath, Object[] headers, String info, Map<String,
            List<String>> selectHeaders, List<Object[]> data) throws ServiceException, StreamOperateException {
        ExcelWriteUtils.writeDataToExcel(realPath, headers, info, selectHeaders, data);
    }

    @Override
    public Workbook downBatchTemplate(Object[] headers, String info, Map<String,
            List<String>> selectHeaders, List<Object[]> data) throws ServiceException, StreamOperateException {
        return ExcelWriteUtils.writeDataToExcel(headers, info, selectHeaders, data);
    }

    @Override
    public Map<String, Object> doLoadFileDataToTable(File upload, Object[] headers,
                                                     int titleRowNum, String namespace)
            throws ServiceException, StreamOperateException, DataOperateException {
        //加载文件
        Workbook book = ExcelStreamUtils.loadFile(upload);
        //校验文件
        ExcelStreamUtils.checkExcelFile(book, headers, titleRowNum);
        //获得文件的内容所在的sheet
        Sheet sheet = book.getSheetAt(0);
        //获得标题行
        Row headerRow = sheet.getRow(titleRowNum);
        //获得标题对应表
        Map<Integer, Integer> mapper = ExcelStreamUtils.getHeaderMap(headers, headerRow);
        //将数据插入临时表
        int count = TemporaryUtils.insertTemporaryTableFromExcel(book.getSheetAt(0), namespace, titleRowNum, mapper);
        //获得当前文件的表头
        List<String> currentHeaders = ExcelStreamUtils.getCurrentHeaders(sheet, titleRowNum);
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put(ExcelConstant.LOAD_FILE_PARAM_BOOK, book);
        resultMap.put(ExcelConstant.LOAD_FILE_PARAM_CURRENT_HEADER, currentHeaders);
        resultMap.put(ExcelConstant.LOAD_FILE_PARAM_COUNT, count);
        return resultMap;
    }

    @Override
    public void checkExcelTable(String namespace, List<String[]> checkItems, Object[] headers)
            throws DataOperateException {
        TemporaryUtils.checkExcelTable(namespace, checkItems, headers);
    }

    @Override
    public String getExcelErrorInfo(String namespace) throws DataOperateException {
        return TemporaryUtils.getExcelErrorInfo(namespace);
    }

    @Override
    public int getExcelErrorNum(String namespace) throws DataOperateException {
        return TemporaryUtils.getExcelErrorNum(namespace);
    }

    @Override
    public void writeTemporaryErrorInfoToFile(String realPath, File excelFile, String namespace, int titleRowNum)
            throws DataOperateException, StreamOperateException, ServiceException {
        TemporaryUtils.writeTemporaryErrorInfoToFile(realPath, excelFile, namespace, titleRowNum);
    }

    @Override
    public void closeBook(Workbook book) throws StreamOperateException {
        ExcelStreamUtils.closeBook(book);
    }

    @Override
    public Map<String, Object> doUploadFile(Map<String, Object> params, String operateType, boolean canDownErrorInfo)
            throws ServiceException, StreamOperateException, DataOperateException {
        Map<String, Object> infoMap = new HashMap<>(4);
        //校验参数map
        checkParam(params);
        //拿出参数
        File upload = (File) params.get(ExcelConstant.UPLODA_FILE);
        Object[] headers = (Object[]) params.get(ExcelConstant.TEMPLATE_HEADER);
        int titleRowNum = (Integer) params.get(ExcelConstant.TITLE_ROW_NUM);
        List<String[]> checkList = (List<String[]>) params.get(ExcelConstant.CHECK_LIST);
        List<String> sqlList = (List<String>) params.get(ExcelConstant.SQL_LIST);
        String infoPrefix = (String) params.get(ExcelConstant.INFO_PREFIX);
        String namespace = UUID.randomUUID().toString().replace("-", "");
        //加载文件并把数据放入临时文件夹
        Map<String, Object> resultMap = this.doLoadFileDataToTable(upload, headers, titleRowNum, namespace);
        Workbook book = (Workbook) resultMap.get(ExcelConstant.LOAD_FILE_PARAM_BOOK);
        try {
            //自定义校验
            TemporaryUtils.checkExcelTable(namespace, checkList, headers);
            //拿到错误信息
            String errorInfo = TemporaryUtils.getExcelErrorInfo(namespace);
            infoMap.put(ExcelConstant.UPLOAD_RETURN_ERROR_INFO, errorInfo);
            //如果操作模式是有错误就所有文件不插入且错误信息不为空则抛出异常
            if (ExcelConstant.IF_FAILURE_NO_INSERT.equals(operateType)
                    && StringUtils.isBlank(errorInfo)) {
                throw new ServiceException(errorInfo);
            }
            //替换sql中的namespace
            for(int i = 0; i < sqlList.size(); i ++) {
                String sql = sqlList.get(i);
                sqlList.set(i, sql.replace("${namespace}", "'" + namespace + "'"));
            }
            try {
                //批量执行sql
                generalDao.batchExecuteSql(sqlList);
            } catch(Exception e) {
                throw new DataOperateException(e);
            }
            //计算数量
            int total = (Integer) resultMap.get(ExcelConstant.LOAD_FILE_PARAM_COUNT);
            int failureNum = errorInfo.split(ExcelConstant.NEW_LINE_SIGN).length - 1;
            int successNum = total - failureNum;
            infoMap.put(ExcelConstant.UPLOAD_RETURN_SUCCESS_NUM, successNum);
            //生成提示信息
            StringBuilder info = new StringBuilder();
            //成功导入，失败返回错误信息的模式
            if (ExcelConstant.INSERT_SUCCESS.equals(operateType)) {
                info.append(infoPrefix).append(",")
                        .append(String.format(ExcelConstant.COUNT_INFO, total, successNum, failureNum));
                infoMap.put(ExcelConstant.UPLOAD_RETURN_PARAM_INFO, info.toString());
                //如果需要下载错误信息提示表
                if(canDownErrorInfo && failureNum != 0) {
                    //生成路径
                    String filePath = ExcelUtils.getDefaultErrorInfoFilePath();
                    String realPath = ExcelUtils.getRealPath(filePath);
                    try {
                        //生成错误信息表
                        TemporaryUtils.writeTemporaryErrorInfoToFile(realPath, upload, namespace, titleRowNum);
                    } catch (Exception e) {
                        //如果生成失败，不能影响到原本逻辑
                        e.printStackTrace();
                    }
                    infoMap.put(ExcelConstant.UPLOAD_RETURN_FILE_PATH_INFO, filePath);
                }
            }
            //失败全部返回的模式
            if (ExcelConstant.IF_FAILURE_NO_INSERT.equals(operateType)) {
                info.append(infoPrefix).append(",")
                        .append(String.format(ExcelConstant.SUCCESS_COUNT_INFO, successNum));
                infoMap.put(ExcelConstant.UPLOAD_RETURN_PARAM_INFO, info.toString());
            }
        } finally {
            ExcelStreamUtils.closeBook(book);
            this.generalDao.executeBySQL("DELETE FROM util_excel_Table where namespace = '" + namespace + "'");
        }
        return infoMap;
    }

    /**
     * 校验参数
     * @param params
     */
    private void checkParam(Map<String, Object> params) {
        if(MapUtils.isEmpty(params)) {
            throw new IllegalArgumentException(ErrorInfoConstant.UPLOAD_ERROR_PARAM_ERROR);
        }
        if(params.get(ExcelConstant.UPLODA_FILE) == null
                && !(params.get(ExcelConstant.UPLODA_FILE) instanceof  File)) {
            throw new IllegalArgumentException(ErrorInfoConstant.UPLOAD_ERROR_PARAM_ERROR);
        }
        if(params.get(ExcelConstant.TEMPLATE_HEADER) == null
                && !(params.get(ExcelConstant.TEMPLATE_HEADER).getClass().isArray())) {
            throw new IllegalArgumentException(ErrorInfoConstant.UPLOAD_ERROR_PARAM_ERROR);
        }
        if(params.get(ExcelConstant.TITLE_ROW_NUM) == null
                && !(params.get(ExcelConstant.TITLE_ROW_NUM) instanceof Integer)) {
            throw new IllegalArgumentException(ErrorInfoConstant.UPLOAD_ERROR_PARAM_ERROR);
        }
        if(params.get(ExcelConstant.CHECK_LIST) == null
                && !(params.get(ExcelConstant.CHECK_LIST) instanceof List)) {
            throw new IllegalArgumentException(ErrorInfoConstant.UPLOAD_ERROR_PARAM_ERROR);
        }
        if(params.get(ExcelConstant.SQL_LIST) == null
                && !(params.get(ExcelConstant.SQL_LIST) instanceof List)) {
            throw new IllegalArgumentException(ErrorInfoConstant.UPLOAD_ERROR_PARAM_ERROR);
        }
        if(params.get(ExcelConstant.INFO_PREFIX) == null
                && !(params.get(ExcelConstant.INFO_PREFIX) instanceof String)) {
            throw new IllegalArgumentException(ErrorInfoConstant.UPLOAD_ERROR_PARAM_ERROR);
        }
    }

    @Override
    public void writeErrorInfoToFile(String realPath, List errorList) throws ServiceException, StreamOperateException {
        List<Object[]> list = new ArrayList<>();
        for(Object obj : errorList) {
            list.add(new Object[]{obj});
        }
        ExcelWriteUtils.writeDataToExcel(realPath, new Object[]{ExcelConstant.ERROR_INFO_HEADER}, list);
    }

    /**
     * 根据指定模板生成excel
     *
     * 表达式说明：
     * paramType：=1 时代表 列数固定,计算指定行数的总和      表达式规则    paramType:参数值:行起止：行结束
     * paramType：=2 时代表 行数固定,计算指定列数的总和      表达式规则    paramType:参数值:列起止：列结束
     * paramType：=3 时代表 执行sql语句获取数据             表达式规则    paramType:参数值:sql语句(含占位符)：参数名
     *
     * @param inputStream 传入的文件流
     * @param map          封装的参数数据
     * @param fileName     生成的文件名称
     * @param out     传出的文件流
     * @return
     * @throws IOException
     */
    @Override
    public void writeDataToExcelByTemplate(InputStream inputStream, Map<String, Object[]> map,
                                           String fileName, OutputStream out) throws IOException {
        String relativePath = ExcelConstant.HIGH_REPORT_PATH + File.separator + fileName;
        CommonUtils.mkDir(CommonUtils.getRealPath(relativePath));
        try (HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {
            HSSFSheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getLastRowNum();
            if (totalRows < 0) {
                throw new IOException("模板文件内容为空!");
            }
            HSSFRow headRow = sheet.getRow(0);
            int totalCells = headRow.getLastCellNum();
            //一行一行遍历
            for (int i = 0; i <= totalRows; i++) {
                HSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                //每一行的所有列单元格遍历
                for (short j = 0; j < totalCells; j++) {
                    HSSFCell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    cell.setCellType(CellType.STRING);
                    String[] cellValues = cell.getStringCellValue().split(":");
                    if (ExcelConstant.XLS_PARAM_TYPE.equals(cellValues[0])
                            && ExcelConstant.PARAM_TYPE_sql.equals(cellValues[1])) {
                        String sql = MapUtils.isEmpty(map) ? cellValues[2]
                                : String.format(cellValues[2], map.get(cellValues[3]));
                        List<Object> list = (List<Object>) generalDao.getBySQL(sql);
                        if (CollectionUtils.isEmpty(list)) {
                            cell.setCellValue(0);
                            continue;
                        }
                        //拿到数据后，填充数据到excel
                        ExcelWriteUtils.writeDataToSheetByPosition(cell.getRowIndex(),
                                cell.getColumnIndex(), sheet, list);
                    }
                }
            }
            List<Cell> cellList = new ArrayList<>();
            //将数据全部写入excel表中,再计算合计项
            ExcelWriteUtils.totalCellValue(sheet, cellList);
            //计算各个合计的总和
            if (CollectionUtils.isNotEmpty(cellList)) {
                int len = cellList.size() - 1;
                for (int i = len ; i >= 0 ; i--) {
                    ExcelWriteUtils.setCellValue(sheet, cellList.get(i), null);
                }
            }
            CommonUtils.mkDir(CommonUtils.getRealPath(relativePath));
            workbook.write(out);
            out.flush();
            out.close();
        }
    }
}
