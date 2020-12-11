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
 * 上传列
 *
 * @author weipengsen
 */
@Entity(name = "UploadCheckItemConfig")
@Table(name = "upload_check_item_config")
public class UploadCheckItemConfig extends AbstractBean {

    private static final long serialVersionUID = -1058377368059215331L;

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
     * 是否必填
     */
    @Column(name = "not_null")
    private String notNull;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UploadConfig getUploadConfig() {
        return uploadConfig;
    }

    public void setUploadConfig(UploadConfig uploadConfig) {
        this.uploadConfig = uploadConfig;
    }

    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadCheckItemConfig that = (UploadCheckItemConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(notNull, that.notNull) &&
                Objects.equals(uploadConfig, that.uploadConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, notNull, uploadConfig);
    }

    @Override
    public String toString() {
        return "UploadCheckItemConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", notNull='" + notNull + '\'' +
                ", uploadConfig=" + uploadConfig +
                '}';
    }
}
