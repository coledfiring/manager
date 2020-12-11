package com.whaty.file.template.strategy;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 模板策略抽象接口
 * @author weipengsen
 */
public interface AbstractTemplateStrategy extends AutoCloseable {

    /**
     * 将文件中的占位符替换
     * @param data
     * @throws Exception
     */
    void handleTemplateFileByData(Map<String, Object> data) throws Exception;

    /**
     * 将文件中的占位符替换并将多个模板文档合并为一个
     * @param data
     * @throws Exception
     */
    void handleTemplateFileByData(List<Map<String, Object>> data) throws Exception;

    /**
     * 将文档输出到指定文件
     *
     * @param outPath
     * @throws Exception
     */
    void exportDocToFile(String outPath) throws Exception;

    /**
     * 将文档输出到输出流
     *
     * @param outputStream
     * @throws Exception
     */
    void exportDocToOutputStream(OutputStream outputStream) throws Exception;

    /**
     * 搜索所有的占位符
     * @return
     */
    Set<String> collectSignInTemplate();

    /**
     * 获取文件后缀格式
     * @return
     */
    String getTypeSuffix();
}
