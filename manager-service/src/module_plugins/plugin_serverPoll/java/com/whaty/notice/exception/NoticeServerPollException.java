package com.whaty.notice.exception;

import com.whaty.framework.exception.ServiceException;

/**
 * 个人中心通知exception，用于携带通知中的异常
 * @author weipengsen
 */
public class NoticeServerPollException extends ServiceException {

    private static final long serialVersionUID = 5701531111827360122L;

    public NoticeServerPollException(String msg) {
        super(msg);
    }
}
