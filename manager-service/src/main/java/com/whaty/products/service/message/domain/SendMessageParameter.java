package com.whaty.products.service.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whaty.core.framework.api.domain.ParamsDataModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 发送消息DTO领域模型
 *
 * @author weipengsen
 */
public class SendMessageParameter implements Serializable {

    private static final long serialVersionUID = -8186185639971838773L;
    /**
     * 发送消息组code
     */
    private String templateCode;
    /**
     * 发送消息类型对应的code
     */
    private String typeCode;
    /**
     * 发送时间，1为立即发送，2为定时发送
     */
    private String sendTimeType;
    /**
     * 定时发送时的发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    /**
     * 对应消息触发的id
     */
    private String triggerId;
    /**
     * 消息配置参数
     */
    private ParamsDataModel params;
    /**
     * 站点编号
     */
    private String siteCode;
    /**
     * 配置id
     */
    private String configId;

    private static final String SEND_NOW = "1";

    /**
     * 是否是立即发送
     * @return
     */
    public boolean isSendNow() {
        return SEND_NOW.equals(this.getSendTimeType());
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getSendTimeType() {
        return sendTimeType;
    }

    public void setSendTimeType(String sendTimeType) {
        this.sendTimeType = sendTimeType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ParamsDataModel getParams() {
        return params;
    }

    public void setParams(ParamsDataModel params) {
        this.params = params;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SendMessageParameter that = (SendMessageParameter) o;
        return Objects.equals(templateCode, that.templateCode) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(sendTimeType, that.sendTimeType) &&
                Objects.equals(time, that.time) &&
                Objects.equals(triggerId, that.triggerId) &&
                Objects.equals(params, that.params) &&
                Objects.equals(siteCode, that.siteCode) &&
                Objects.equals(configId, that.configId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateCode, typeCode, sendTimeType, time, triggerId, params, siteCode, configId);
    }

    @Override
    public String toString() {
        return "SendMessageParameter{" +
                "templateCode='" + templateCode + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", sendTimeType='" + sendTimeType + '\'' +
                ", time=" + time +
                ", triggerId='" + triggerId + '\'' +
                ", params=" + params +
                ", siteCode='" + siteCode + '\'' +
                ", configId='" + configId + '\'' +
                '}';
    }
}
