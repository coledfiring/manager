package com.whaty.framework.config.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 工作室配置配置
 *
 * @author weipengsen
 */
@Data
public class WorkSpaceConfig implements IConfig {

    private static final long serialVersionUID = 7154715662904723628L;

    @NotNull
    private String signatureAuthKey;
    @NotNull
    private String templatesPath;

}
