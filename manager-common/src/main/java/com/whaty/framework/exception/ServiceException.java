package com.whaty.framework.exception;

import com.whaty.core.framework.api.advice.CustomException;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 用户操作异常，使用此异常后日志管理系统将记录info日志，统一异常管理
 * 将向前端传递构造器中message的信息
 * @author weipengsen
 */
public class ServiceException extends AbstractBasicException implements CustomException {

    private static final long serialVersionUID = -2082749173203531836L;

    public ServiceException(String msg) {
        super(msg);
    }

    @Override
    protected Level getLevel() {
        return Level.INFO;
    }

    @Override
    public String getInfo() {
        return this.getMessage();
    }

    @Override
    public ResponseEntity<ResultDataModel> handle(Throwable e) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResultDataModel body = ResultDataModel.handleFailureResult(
                HttpStatus.OK.value(), this.getInfo());
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

}
