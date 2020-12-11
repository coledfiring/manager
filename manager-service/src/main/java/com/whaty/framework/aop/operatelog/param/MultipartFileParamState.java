package com.whaty.framework.aop.operatelog.param;

import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * multipartFile参数的提取状态机
 *
 * @author weipengsen
 */
@Lazy
@Component("multipartFileParamState")
public class MultipartFileParamState implements AbstractParamState<MultipartFile> {

    private final static Logger logger = LoggerFactory.getLogger(MultipartFileParamState.class);

    @Override
    public Map<String, Object> extract(MultipartFile param) {
        String paramFilePath = String.format(OperateRecordConstant.PARAM_FILE_STORE_PATH, SiteUtil.getSiteCode(),
                CommonUtils.changeDateToString(new Date()), UUID.randomUUID().toString());
        try {
            CommonUtils.convertMultipartFileToFile(param, CommonUtils.getRealPath(paramFilePath));
            return Collections.singletonMap(OperateRecordConstant.PARAM_KEY_UPLOAD_FILE_PATH, paramFilePath);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("url [%s] extract `multipartFile` param failure",
                        CommonUtils.getRequest().getRequestURI()), e);
            }
        }
        return null;
    }

}
