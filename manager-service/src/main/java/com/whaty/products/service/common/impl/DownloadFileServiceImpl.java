package com.whaty.products.service.common.impl;

import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.products.service.common.DownloadFileService;
import com.whaty.products.service.common.constant.ComConstant;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件下载服务实现类
 * @author weipengsen
 */
@Lazy
@Service("downloadFileService")
public class DownloadFileServiceImpl implements DownloadFileService {

    @Override
    public void downloadExistentFile(String fileKey, OutputStream out) {
        TycjParameterAssert.isAllNotBlank(fileKey);
        if(!ComConstant.FILE_PATH_LINK_MAP.containsKey(fileKey)) {
            throw new ParameterIllegalException(ComConstant.PARAM_FILE_KEY);
        }
        try (FileInputStream input = new FileInputStream(CommonUtils.getRealPath(ComConstant.DOWNLOAD_FILE_BASE_PATH +
                ComConstant.FILE_PATH_LINK_MAP.get(fileKey)));
             FileChannel channel = input.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(input.available());
            buffer.clear();
            channel.read(buffer);
            out.write(buffer.array());
            buffer.clear();
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new UncheckException(e);
        }
    }

}
