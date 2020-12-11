package com.whaty.file.domain.bean;

import com.whaty.domain.bean.AbstractSiteBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Objects;

/**
 * 上传配置
 *
 * @author weipengsen
 */
@Entity(name = "UploadConfig")
@Table(name = "upload_config")
public class UploadConfig extends AbstractSiteBean {

    private static final long serialVersionUID = -3697561907720948848L;

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
     * 编号
     */
    @Column(name = "code")
    private String code;
    /**
     * 执行sql
     */
    @Column(name = "execute_sql")
    private String executeSql;
    /**
     * 上传类型
     */
    @Column(name = "type")
    private String type;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    /**
     * 所有的表头项
     */
    @Transient
    private List<UploadCheckItemConfig> uploadCheckItemConfigs;
    /**
     * 所有的校验项
     */
    @Transient
    private List<UploadCheckConfig> uploadCheckConfigs;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExecuteSql() {
        return executeSql;
    }

    public void setExecuteSql(String executeSql) {
        this.executeSql = executeSql;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<UploadCheckItemConfig> getUploadCheckItemConfigs() {
        return uploadCheckItemConfigs;
    }

    public void setUploadCheckItemConfigs(List<UploadCheckItemConfig> uploadCheckItemConfigs) {
        this.uploadCheckItemConfigs = uploadCheckItemConfigs;
    }

    public List<UploadCheckConfig> getUploadCheckConfigs() {
        return uploadCheckConfigs;
    }

    public void setUploadCheckConfigs(List<UploadCheckConfig> uploadCheckConfigs) {
        this.uploadCheckConfigs = uploadCheckConfigs;
    }

    @Override
    public String getSiteCode() {
        return siteCode;
    }

    @Override
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadConfig that = (UploadConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(executeSql, that.executeSql) &&
                Objects.equals(type, that.type) &&
                Objects.equals(siteCode, that.siteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, executeSql, type, siteCode);
    }

    @Override
    public String toString() {
        return "UploadConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", executeSql='" + executeSql + '\'' +
                ", type='" + type + '\'' +
                ", uploadCheckItemConfigs=" + uploadCheckItemConfigs +
                ", uploadCheckConfigs=" + uploadCheckConfigs +
                ", siteCode='" + siteCode + '\'' +
                '}';
    }
}
