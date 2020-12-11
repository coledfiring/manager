package com.whaty.file.excel.upload.service;

import com.whaty.file.excel.upload.exception.DataOperateException;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.framework.exception.ServiceException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * excel文件上传工具类
 *
 * @author weipengsen
 */
public interface ExcelUploadService {

    /**
     * 生成下载模板，模板按照传入路径生成文件，含有指定的表头数据
     *
     * @param realPath 生成文件的路径
     * @param headers 表头数组
     * @author weipengsen
     * @throws ServiceException
     * @throws StreamOperateException
     */
    void downBatchTemplate(String realPath, Object[] headers) throws ServiceException, StreamOperateException;

    /**
     * 生成下载模板，模板按照传入路径生成文件，含有指定表头数据，预填内容
     *
     * @param realPath 生成文件的路径
     * @param headers 表头数组
     * @param data 预填信息集合，与header对应排列
     * @author weipengsen
     * @throws ServiceException
     * @throws StreamOperateException
     */
    void downBatchTemplate(String realPath, Object[] headers, List<Object[]> data)
            throws ServiceException, StreamOperateException;

    /**
     * 生成下载模板，模板按照传入路径生成文件，含有指定表头数据，预填内容，根据header生成选择框
     *
     * @param realPath 生成文件的路径
     * @param headers 表头数组
     * @param info 标题
     * @param selectHeaders 选择框数据集合 key --> headers中的对应的字符串，value --> 选择框数据, count --> 有效数据数
     * @param data 预填信息集合，与header对应排列
     * @throws StreamOperateException
     * @throws ServiceException
     */
    void downBatchTemplate(String realPath, Object[] headers, String info, Map<String,
            List<String>> selectHeaders, List<Object[]> data) throws ServiceException, StreamOperateException;

    /**
     * 生成下载模板，含有指定表头数据，预填内容，根据header生成选择框，并生成book
     *
     * @param headers 表头数组
     * @param info 标题
     * @param selectHeaders 选择框数据集合 key --> headers中的对应的字符串，value --> 选择框数据, count --> 有效数据数
     * @param data 预填信息集合，与header对应排列
     * @throws StreamOperateException
     * @throws ServiceException
     * @return
     */
    Workbook downBatchTemplate(Object[] headers, String info, Map<String, List<String>> selectHeaders,
                               List<Object[]> data) throws ServiceException, StreamOperateException;

    /**
     * 加载、校验excel文件并将数据存入临时表
     *
     * @param upload excel文件
     * @param headers 表头，校验使用
     * @param namespace 临时表存储namespace
     * @param titleRowNum excel文件的标题数
     * @author weipengsen
     * @return 封装了excel流与插入临时表结果的map，book-->Workbook对象, headers-->文件中的表头数组，count-->插入临时表的数据数
     * @throws ServiceException
     * @throws StreamOperateException
     * @throws DataOperateException
     */
    Map<String, Object> doLoadFileDataToTable(File upload, Object[] headers, int titleRowNum, String namespace)
            throws ServiceException, StreamOperateException, DataOperateException;
    /**
     * 临时表校验工具方法,其中checkItems是校验信息与校验语句的集合，数组第一个元素为校验失败的信息，第二个是校验使用的sql，其中sql的前部已经
     * 在方法中集成，集成部分为：
     * update util_excel_table e set e.valid = '2',e.error_info = concat(ifnull(e.error_info, ''), '%s')
     * where e.namespace = '"+namespace+"' AND %s 其中第一个站位符是校验失败信息，第二个为语句，语句常用格式为EXISTS ()和
     * NOT EXISTS ()如果不对表进行联查可直接写e.columnN = '...'或者e.columnN REGEXP ''之类的必要校验，
     * 另外不需要进行非空校验，方法中集成了根据headers中是否包含“*”进行非空校验的方法
     *
     * @param namespace 临时表空间namespace
     * @param checkItems 自定义校验使用的校验信息
     * @param headers 模板头标题
     * @author weipengsen
     * @throws DataOperateException
     */
    void checkExcelTable(String namespace, List<String[]> checkItems, Object[] headers) throws DataOperateException;

    /**
     * 得到临时表空间的错误信息
     *
     * @param namespace 临时表命名空间
     * @return 返回的提示信息
     * @author weipengsen
     * @throws DataOperateException
     */
    String getExcelErrorInfo(String namespace) throws DataOperateException;

    /**
     * 获得临时表空间的错误信息数量
     *
     * @param namespace
     * @return
     * @throws DataOperateException
     */
    int getExcelErrorNum(String namespace) throws DataOperateException;

    /**
     * 将临时表中的错误信息写入文件，原本的excel文件中删除成功行，增加一列“错误信息”将临时表中的错误信息写入都对应的行
     *
     * @param realPath 生成文件的路径
     * @param excelFile 上传的原始文件
     * @param namespace 临时表命名空间
     * @param titleRowNum 文件中的非表头标题行数（如说明之类的在表头上的标题行数）
     * @throws StreamOperateException
     * @throws DataOperateException
     * @throws ServiceException
     */
    void writeTemporaryErrorInfoToFile(String realPath, File excelFile, String namespace, int titleRowNum)
            throws DataOperateException, StreamOperateException, ServiceException;

    /**
     * 关闭流
     * @param book
     * @throws StreamOperateException
     */
    void closeBook(Workbook book) throws StreamOperateException;

    /**
     * 一站式的业务类导入方法，可以满足基本的excel导入需求，包括：
     * 文件校验、临时表导入、必填和自定义校验、执行更改sql并自动生成提示信息，根据配置选择现在错误信息对照表
     *
     * @param params 参数map，
     *               upload-->(File)excel文件；
     *               headers-->(Object[])模板表头数组；
     *               titleRowNum-->(int)excel文件的标题数；
     *               checkList-->(List<String[2]>)自定义校验集合，数组中第一个元素为提示信息，第二个为校验的sql，sql中前半部分已集成
     *                  集成部分为UPDATE util_excel_Table e set e.valid = '2' where namespace= '' and；
     *               sqlList-->(List<String>)执行的sql集合；
     *               infoPrefix-->(String)提示信息的前缀，如“成绩导入成功”，在之后会集成拼接“共m条，成功m-n条，失败n条；
     * @param operateType 导入时操作的类型，类型常量在ExcelConstant，有两种：
     *                    INSERT_SUCCESS：如果有成功的返回错误信息，插入成功的
     *                    IF_FAILURE_NO_INSERT：如果有错误所有都不插入
     * @param canDownErrorInfo 是否生成错误信息对照表
     * @return 返回集合，
     *          info-->(String)返回的提示信息；
     *          error-->(String)错误信息，格式如：第2行分数格式错误；<br/>
     *          filePath-->(String)如果生成错误信息对照表则为对照表的URI，如果生成对照表时出现问题则为null，且为了不影响正常流程不会抛出异常
     *              建议在上层判断，为null拼接info与error给予用户实时提示；
     * @throws ServiceException
     * @throws StreamOperateException
     * @throws DataOperateException
     */
    Map<String, Object> doUploadFile(Map<String, Object> params, String operateType, boolean canDownErrorInfo)
            throws ServiceException, StreamOperateException, DataOperateException;

    /**
     * 将错误信息写入excel文件中
     *
     * @param realPath
     * @param errorList
     * @throws ServiceException
     * @throws StreamOperateException
     */
    void writeErrorInfoToFile(String realPath, List errorList) throws ServiceException, StreamOperateException;

    /**
     * 根据规定模板以及参数生成excel
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
    void writeDataToExcelByTemplate(InputStream inputStream, Map<String, Object[]> map, String fileName, OutputStream out) throws IOException;

}
