package com.whaty.wecharts.exception;

/**
 * weCharts的用户错误异常
 * @author weipengsen
 */
public class WeChartsServiceException extends Exception {

    public WeChartsServiceException() {
    }

    public WeChartsServiceException(String message) {
        super(message);
    }

    public WeChartsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeChartsServiceException(Throwable cause) {
        super(cause);
    }
}
