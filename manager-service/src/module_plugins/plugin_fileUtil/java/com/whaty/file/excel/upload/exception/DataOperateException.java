package com.whaty.file.excel.upload.exception;

/**
 * 持久化异常，运行时，数据持久化操作异常
 *
 * @author weipengsen
 */
public class DataOperateException extends RuntimeException {

    public DataOperateException() {
        super();
    }

    public DataOperateException(String message) {
        super(message);
    }

    public DataOperateException(Throwable t) {
        super(t);
    }

    public DataOperateException(String message, Throwable t) {
        super(message, t);
    }

}
