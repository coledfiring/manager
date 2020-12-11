package com.whaty.file.domain.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
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
 * 打印占位符
 * @author weipengsen
 */
@Entity(
        name = "PePrintTemplateSign"
)
@Table(
        name = "pe_print_template_sign"
)
public class PePrintTemplateSign extends AbstractBean {

    private static final long serialVersionUID = -6256182351918763207L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    /**
     * 打印模板
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FK_PRINT_TEMPLATE_GROUP_ID"
    )
    private PePrintTemplateGroup pePrintTemplateGroup;
    /**
     * 名称
     */
    @Column(
            name = "NAME"
    )
    private String name;
    /**
     * 占位符
     */
    @Column(
            name = "SIGN"
    )
    private String sign;
    /**
     * 是否显示
     */
    @Column(
            name = "is_show"
    )
    private String isShow;
    /**
     * 排序顺序
     */
    @Column(
            name = "SERIAL_NUMBER"
    )
    private int serialNumber;
    /**
     * 是否有效
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_ACTIVE"
    )
    private EnumConst enumConstByFlagActive;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PePrintTemplateGroup getPePrintTemplateGroup() {
        return pePrintTemplateGroup;
    }

    public void setPePrintTemplateGroup(PePrintTemplateGroup pePrintTemplateGroup) {
        this.pePrintTemplateGroup = pePrintTemplateGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public EnumConst getEnumConstByFlagActive() {
        return enumConstByFlagActive;
    }

    public void setEnumConstByFlagActive(EnumConst enumConstByFlagActive) {
        this.enumConstByFlagActive = enumConstByFlagActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PePrintTemplateSign that = (PePrintTemplateSign) o;
        return serialNumber == that.serialNumber &&
                Objects.equals(id, that.id) &&
                Objects.equals(pePrintTemplateGroup, that.pePrintTemplateGroup) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sign, that.sign) &&
                Objects.equals(isShow, that.isShow) &&
                Objects.equals(enumConstByFlagActive, that.enumConstByFlagActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pePrintTemplateGroup, name, sign, isShow, serialNumber, enumConstByFlagActive);
    }

    @Override
    public String toString() {
        return "PePrintTemplateSign{" +
                "id=" + id +
                ", pePrintTemplateGroup=" + pePrintTemplateGroup +
                ", name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                ", isShow='" + isShow + '\'' +
                ", serialNumber=" + serialNumber +
                ", enumConstByFlagActive=" + enumConstByFlagActive +
                '}';
    }
}
