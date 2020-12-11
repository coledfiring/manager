package com.whaty.framework.config.domain;

import lombok.Data;
import net.sf.json.JSONObject;

import javax.validation.constraints.NotNull;

/**
 * 平台配置
 *
 * @author weipengsen
 */
@Data
public class PlatformConfig implements IConfig {

    private static final long serialVersionUID = 7154715662904723628L;
    @NotNull
    private String mailPassword;

    private Boolean isWeChatSiteOpen;
    @NotNull
    private String mailSmtp;
    @NotNull
    private String mailUser;
    @NotNull
    private String registerOrSplit;

    private JSONObject homeIndexConfig;

    private Boolean openNoticePoll;

    private Boolean openTencentIm;

    private Boolean isOnlineTraining;

    public Boolean getOpenNoticePoll() {
        return this.openNoticePoll == null ? true : this.openNoticePoll;
    }

    public Boolean getOpenTencentIm() {
        return openTencentIm == null ? false : openTencentIm;
    }

}
