package com.whaty.file.grid.service;

import com.whaty.file.grid.service.domain.TemplateVO;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 打印模板服务类
 * @author weipengsen
 */
public interface PrintTemplateService {

    /**
     * 替换打印模板中的占位符并下载文件
     * @param args
     * @param response
     * @return
     */
    void printAndDown(Map<String, String> args, HttpServletResponse response);

    /**
     * 上传模板文件
     * @param file
     * @param printId
     * @throws IOException
     */
    void doUploadTemplate(File file, String printId) throws IOException;

    /**
     * 获取所有可自定义的模板
     * @return
     */
    Map<String, TemplateVO> listTemplate();
}
