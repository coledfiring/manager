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
import javax.persistence.Transient;
import java.util.List;
import java.util.Objects;

/**
 * 打印模板分组
 * @author weipengsen
 */
@Entity(
		name = "PePrintTemplateGroup"
)
@Table(
		name = "pe_print_template_group"
)
public class PePrintTemplateGroup extends AbstractBean {

	private static final long serialVersionUID = -1253857084466344316L;
	/**
	 * 主键
	 */
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
	 * 标题
	 */
	@Column(
			name = "name"
	)
	private String name;
	/**
	 * 序号
	 */
	@Column(
			name = "serial_number"
	)
	private Integer serialNumber;
	/**
	 * 打印模板
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "FK_PRINT_TEMPLATE_ID"
	)
	private PePrintTemplate pePrintTemplate;
	/**
	 * 占位符
	 */
	@Transient
	private List<PePrintTemplateSign> pePrintTemplateSignList;

	@Override
	public String getId() {
		return id;
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

	public PePrintTemplate getPePrintTemplate() {
		return pePrintTemplate;
	}

	public void setPePrintTemplate(PePrintTemplate pePrintTemplate) {
		this.pePrintTemplate = pePrintTemplate;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public List<PePrintTemplateSign> getPePrintTemplateSignList() {
		return pePrintTemplateSignList;
	}

	public void setPePrintTemplateSignList(List<PePrintTemplateSign> pePrintTemplateSignList) {
		this.pePrintTemplateSignList = pePrintTemplateSignList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PePrintTemplateGroup that = (PePrintTemplateGroup) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(serialNumber, that.serialNumber) &&
				Objects.equals(pePrintTemplate, that.pePrintTemplate) &&
				Objects.equals(pePrintTemplateSignList, that.pePrintTemplateSignList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, serialNumber, pePrintTemplate, pePrintTemplateSignList);
	}

	@Override
	public String toString() {
		return "PePrintTemplateGroup{" +
				"id=" + id +
				", name='" + name + '\'' +
				", serialNumber=" + serialNumber +
				", pePrintTemplate=" + pePrintTemplate +
				", pePrintTemplateSignList=" + pePrintTemplateSignList +
				'}';
	}
}