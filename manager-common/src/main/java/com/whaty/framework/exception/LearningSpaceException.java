package com.whaty.framework.exception;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 课程空间异常，日志级别为error，统一异常管理将传递课程空间同步失败信息给前台
 * @author  weipengsen
 */
public class LearningSpaceException extends AbstractBasicException {

    public LearningSpaceException() {}

    public LearningSpaceException(String msg) {
        super(msg);
    }

    public LearningSpaceException(Throwable t) {
        super(t);
    }

    @Override
    protected Level getLevel() {
        return Level.ERROR;
    }

    @Override
    public String getInfo() {
        return CommonConstant.LEARNING_SPACE_ERROR;
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
