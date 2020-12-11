package com.whaty.framework.config.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * learnSpace配置
 *
 * @author weipengsen
 */
@Data
public class LearnSpaceConfig implements IConfig {

    private static final long serialVersionUID = 4969178318870779165L;
    @NotNull
    private String leanSpaceSiteCode;
    @NotNull
    private Boolean isOpen;
    @NotNull
    @Pattern(regexp = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$")
    private String domain;
    @NotNull
    @Pattern(regexp = "^(/[a-zA-Z0-9]+)+$")
    private String serviceUrl;
    @NotNull
    @Pattern(regexp = "^(/[a-zA-Z0-9]+)+$")
    private String httpClientUrl;

}
