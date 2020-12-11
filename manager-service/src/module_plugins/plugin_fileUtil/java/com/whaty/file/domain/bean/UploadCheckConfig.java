package com.whaty.file.domain.bean;

import com.whaty.core.bean.AbstractBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 上传校验sql配置
 *
 * @author weipengsen
 */
@Entity(name = "UploadCheckConfig")
@Table(name = "upload_check_config")
public class UploadCheckConfig extends AbstractBean {

    private static final long serialVersionUID = 5452950235242734683L;

    @Id
    @GenericGenerator(name = "idGenerator",strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 校验sql
     */
    @Column(name = "check_sql")
    private String checkSql;
    /**
     * 校验顺序
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * 是否可以中断程序
     */
    @Column(name = "can_interrupt")
    private String canInterrupt;

    /**
     * 错误提示
     */
    @Column(name = "error_tip")
    private String errorTip;
    /**
     * 所属上传配置
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_upload_config_id")
    private UploadConfig uploadConfig;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckSql() {
        return checkSql;
    }

    public void setCheckSql(String checkSql) {
        this.checkSql = checkSql;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCanInterrupt() {
        return canInterrupt;
    }

    public void setCanInterrupt(String canInterrupt) {
        this.canInterrupt = canInterrupt;
    }

    public String getErrorTip() {
        return errorTip;
    }

    public void setErrorTip(String errorTip) {
        this.errorTip = errorTip;
    }

    public UploadConfig getUploadConfig() {
        return uploadConfig;
    }

    public void setUploadConfig(UploadConfig uploadConfig) {
        this.uploadConfig = uploadConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadCheckConfig that = (UploadCheckConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(checkSql, that.checkSql) &&
                Objects.equals(serialNumber, that.serialNumber) &&
                Objects.equals(canInterrupt, that.canInterrupt) &&
                Objects.equals(errorTip, that.errorTip) &&
                Objects.equals(uploadConfig, that.uploadConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, checkSql, serialNumber, canInterrupt, errorTip, uploadConfig);
    }

    @Override
    public String toString() {
        return "UploadCheckConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", checkSql='" + checkSql + '\'' +
                ", serialNumber=" + serialNumber +
                ", canInterrupt='" + canInterrupt + '\'' +
                ", errorTip='" + errorTip + '\'' +
                ", uploadConfig=" + uploadConfig +
                '}';
    }
}
