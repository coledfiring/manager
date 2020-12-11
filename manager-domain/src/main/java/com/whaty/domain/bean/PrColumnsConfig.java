package com.whaty.domain.bean;


import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
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
import java.util.Map;
import java.util.Objects;

/**
 * 学生信息动态项
 * @author weipengsen
 */
@Data
@Entity(name = "PrColumnsConfig")
@Table(name = "pr_columns_config")
public class PrColumnsConfig extends AbstractSiteBean {

	private static final long serialVersionUID = -904136823100382219L;
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
	 * 所属模块
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "FLAG_INFO_TYPE"
	)
	private EnumConst enumConstByFlagInfoType;
	/**
	 * 项名称
	 */
	@Column(
			name = "NAME"
	)
	private String name;
	/**
	 * 对应学生实体类属性
	 */
	@Column(
			name = "COLUMN_NAME"
	)
	private String columnName;
	/**
	 * 是否显示
	 */
	@Column(
			name = "DISPLAY"
	)
	private String display;
	/**
	 * 顺序
	 */
	@Column(
			name = "MY_ORDER"
	)
	private String myOrder;
	@Column(
			name = "EDIT"
	)
	private String edit;
	/**
	 * 项对应html表单类型
	 */
	@Column(
			name = "HTML_TYPE"
	)
	private String htmlType;
	/**
	 * 是否为空
	 */
	@Column(
			name = "NOT_NULL"
	)
	private String notNull;
	@Column(
			name = "TEXT_LENGTH"
	)
	private Long textLength;
	@Column(
			name = "DATE_FORMAT"
	)
	private String dateFormat;
	/**
	 * 正则
	 */
	@Column(
			name = "REG"
	)
	private String reg;
	/**
	 * 下拉框项
	 */
	@Column(
			name = "OPTIONS"
	)
	private String options;
	@Column(
			name = "SELECTED"
	)
	private String selected;
	@Column(
			name = "HELP_DISPLAY"
	)
	private String helpDisplay;
	/**
	 * 帮助信息
	 */
	@Column(
			name = "HELP"
	)
	private String help;
	/**
	 * 正则错误提示信息
	 */
	@Column(
			name = "ERROR"
	)
	private String error;
	@Column(
			name = "HELP_URL"
	)
	private String helpUrl;
	@Column(
			name = "HELP_URL_NAME"
	)
	private String helpUrlName;
	/**
	 * 对应实体类名
	 */
	@Column(
			name = "TABLE_NAME"
	)
	private String tableName;
	@Column(
			name = "USER_EDIT"
	)
	private String userEdit;
	@Column(
			name = "CTRL_DIV"
	)
	private String ctrlDiv;
	@Column(
			name = "CTRL_VALUE"
	)
	private String ctrlValue;
	@Column(
			name = "TEAM"
	)
	private String team;
	@Column(
			name = "LAST_NAME"
	)
	private String lastName;
	@Column(
			name = "DB_COLUMN_LENGTH"
	)
	private String dbColumnLength;
	@Column(
			name = "listSql"
	)
	private String listSql;
	@Transient
	private List<Map<String,Object>> optionMapList;
	@Column(
			name = "site_code"
	)
	private String siteCode;

}