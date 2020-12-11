package com.whaty.file.excel.upload.util;

import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.framework.exception.ServiceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 常用工具类
 * @author weipengsen
 */
public class ExcelUtils {

    /**
     * 默认日期格式字符串
     */
    private final static String DEFAULT_DATE_STRING = "yyyyMMdd";
    /**
     * 默认日期格式化对象
     */
    private final static DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_DATE_STRING);

    /**
     * 获得绝对路径
     * @param webPath
     * @return
     */
    public static String getRealPath(String webPath) {
        return com.whaty.util.CommonUtils.getRealPath(webPath);
    }

    /**
     * 将当前日期格式化为yyyyMMdd的格式
     * @return
     */
    public static String nowDateString() {
        return LocalDateTime.now().format(DEFAULT_DATE_FORMAT);
    }

    /**
     * 查找数组中指定目标的索引位置
     * @param target
     * @param array
     * @param <T>
     * @return
     * @author weipengsen
     * @throws ServiceException
     */
    public static <T> int searchArray(T target, T[] array) throws ServiceException {
        //检验目标对象不为空
        if(target == null) {
            throw new ServiceException("the target is null");
        }
        //校验数组不为空
        if(array == null || array.length == 0) {
            throw new ServiceException("the array is null or empty");
        }
        int index = 0;
        for(T curr : array) {
            if(curr.equals(target)) {
                return index;
            }
            index ++;
        }
        return -1;
    }

    /**
     * 获得错误信息对照表的路径
     * 默认为:/incoming/tempFile/errorFile/yyyyMMdd/uuid.xls
     * @return
     */
    public static String getDefaultErrorInfoFilePath() {
        return new StringBuilder(ExcelConstant.ERROR_FILE_INCOMING_PATH).append("/")
                .append(ExcelUtils.nowDateString()).append("/")
                .append("ErrorInfoExcel").append(UUID.randomUUID().toString().replace("-", ""))
                .append(ExcelConstant.XLS_FILE_TYPE).toString();
    }

}
