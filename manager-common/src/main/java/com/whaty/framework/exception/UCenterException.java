package com.whaty.framework.exception;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 用户中心异常，error级别异常
 *
 * @author weipengsen
 */
public class UCenterException extends AbstractBasicException {

    private static final long serialVersionUID = 6688526764299588220L;

    public UCenterException() {}

    public UCenterException(String msg) {
        super(msg);
    }

    public UCenterException(Throwable t) {
        super(t);
    }

    @Override
    protected Level getLevel() {
        return Level.ERROR;
    }

    @Override
    public String getInfo() {
        return CommonConstant.U_CENTER_ERROR;
    }

    @Override
    public String getLogInfo() {
        return this.getMessage() == null ? this.getInfo() : this.getMessage();
    }

    @Override
    public ResponseEntity<ResultDataModel> handle(Throwable throwable) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResultDataModel body = ResultDataModel.handleFailureResult(
                HttpStatus.OK.value(), this.getInfo());
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
