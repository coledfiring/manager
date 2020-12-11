package com.whaty.file.template.util;

import com.whaty.file.template.constant.TemplateConstant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 工具类
 * @author weipengsen
 */
public class TemplateUtils {

    /**
     * 创建文件夹
     * @param filePath
     * @return
     */
    public static void mkdir(String filePath) {
        if (filePath.contains(TemplateConstant.FILE_TYPE_SIGN)) {
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 复制输入流，如果流已使用则不能复制
     * @param inputStream
     * @return
     */
    public static InputStream copyInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1 ) {
            bao.write(buffer, 0, len);
        }
        bao.flush();
        return new ByteArrayInputStream(bao.toByteArray());
    }

}
