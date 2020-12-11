package com.whaty.file.excel.upload.exception;

import java.io.IOException;

/**
 * 数据流操作异常，因为io错误导致的异常
 *
 * @author weipengsen
 */
public class StreamOperateException extends IOException {

    public StreamOperateException() {
        super();
    }

    public StreamOperateException(String message) {
        super(message);
    }

    public StreamOperateException(Throwable t) {
        super(t);
    }

    public StreamOperateException(String message, Throwable t) {
        super(message, t);
    }

}
