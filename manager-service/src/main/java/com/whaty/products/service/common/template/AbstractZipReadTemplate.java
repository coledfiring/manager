package com.whaty.products.service.common.template;

import com.whaty.constant.CommonConstant;
import com.whaty.framework.exception.ServiceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 抽象zip读取模板方法接口
 *
 * @author weipengsen
 */
public abstract class AbstractZipReadTemplate {

    protected File zipFile;

    protected Map<String, Object> extraParams = new HashMap<>();

    public static final String RESULT_TOTAL_NUM = "totalNum";

    public static final String RESULT_SUCCESS_NUM = "successNum";

    public static final String RESULT_ERROR_LIST = "errorList";

    public AbstractZipReadTemplate() {}

    public AbstractZipReadTemplate(File zipFile) {
        this.zipFile = zipFile;
    }

    public AbstractZipReadTemplate(File zipFile, Map<String, Object> extraParams) {
        this.zipFile = zipFile;
        this.extraParams = extraParams;
    }

    /**
     * 读取zip文件
     * @return
     * @throws IOException
     */
    public Map<String, Object> readZip() throws IOException {
        Map<String, Object> resultMap = new HashMap<>(8);
        int successNum = 0;
        int totalNum = 0;
        ZipInputStream zin = null;
        try {
            zin = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry;
            List<String> errorList = new LinkedList<>();
            while ((entry = zin.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    zin.closeEntry();
                    continue;
                }
                totalNum++;
                String fileName = entry.getName();
                if (fileName.contains("/")) {
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                }
                try {
                    long fileSize = entry.getSize();
                    if (fileSize > this.getSingleFileLimitSize()) {
                        throw new ServiceException(this.getSingleFileSizeOverMessage());
                    }
                    this.handleSingleFile(fileName, zin, extraParams);
                    successNum++;
                } catch (ServiceException e) {
                    errorList.add(fileName + e.getMessage().replace(CommonConstant.HTML_TURN_LINE_TAG, ""));
                } finally {
                    zin.closeEntry();
                }
            }
            resultMap.put(RESULT_TOTAL_NUM, totalNum);
            resultMap.put(RESULT_SUCCESS_NUM, successNum);
            resultMap.put(RESULT_ERROR_LIST, errorList);
        } finally {
            if (zin != null) {
                zin.close();
            }
        }
        return resultMap;
    }

    /**
     * 模板方法处理单个文件
     * @param fileName
     * @param input
     * @param extraParams
     */
    protected abstract void handleSingleFile(String fileName, InputStream input, Map<String, Object> extraParams);

    /**
     * 模板方法单个文件限制大小单位b
     * @return
     */
    protected abstract long getSingleFileLimitSize();

    /**
     * 模板方法当个文件超过限制后提示信息
     * @return
     */
    protected abstract String getSingleFileSizeOverMessage();

}
