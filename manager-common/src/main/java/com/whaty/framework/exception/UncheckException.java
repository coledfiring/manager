package com.whaty.framework.exception;

/**
 * 传递未检查的没有被系统内已定义的所有异常包括的异常，使用此异常后日志管理系统将记录error日志，
 * 统一异常管理将向前端传递服务器内部错误的信息
 * @author weipengsen
 */
public class UncheckException extends RuntimeException {

    public UncheckException(Throwable t) {
        super(t);
    }

}
