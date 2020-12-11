package com.whaty.framework.sqlflow.exception;

/**
 * 动态sql错误异常
 * @author weipengsen
 */
public class IllegalDynamicSqlException extends RuntimeException {

    private static final long serialVersionUID = -7698256522969385637L;

    public IllegalDynamicSqlException() {
        super();
    }

    public IllegalDynamicSqlException(String s) {
        super(s);
    }

    public IllegalDynamicSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDynamicSqlException(Throwable cause) {
        super(cause);
    }
}
