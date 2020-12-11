package com.whaty.framework.exception;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 传参非法异常，使用此异常后日志管理系统将记录warn日志，统一异常管理
 * 将向前端传递参数错误的信息
 * @author weipengsen
 */
public class ParameterIllegalException extends AbstractBasicException {

    public ParameterIllegalException() {}

    public ParameterIllegalException(String msg) {
        super(msg);
    }

    @Override
    protected Level getLevel() {
        return Level.WARN;
    }

    @Override
    public String getInfo() {
        return CommonConstant.PARAM_ERROR;
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
