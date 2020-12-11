package com.whaty.framework.config.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 考试系统配置
 *
 * @author weipengsen
 */
@Data
public class ExamSystemConfig implements IConfig {

    private static final long serialVersionUID = 4890851110434984498L;

    private String apiKey;
    @NotNull
    private Boolean isOpen;

    private String privateKey;
    @NotNull
    private String siteCode;
    @NotNull
    private String suffixUrl;
    @NotNull
    private String domain;

}
