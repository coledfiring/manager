package com.whaty.products.service.common;

import java.io.OutputStream;

/**
 * 文件下载服务类
 * @author weipengsen
 */
public interface DownloadFileService {

    /**
     * 下载已经存在的文件
     * @param fileKey
     * @param out
     */
    void downloadExistentFile(String fileKey, OutputStream out);

}
