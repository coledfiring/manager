package com.whaty.framework.exception;

/**
 * 数据操作异常,使用此异常后统一异常日志管理将记录error日志，统一异常
 * 管理将向前端传递数据错误提示
 * @author weipengsen
 */
public class DataOperateException extends RuntimeException {

    private static final long serialVersionUID = 2483065085805076175L;

    public DataOperateException(String message) {
        super(message);
    }

    public DataOperateException(Throwable t) {
        super(t);
    }

}
