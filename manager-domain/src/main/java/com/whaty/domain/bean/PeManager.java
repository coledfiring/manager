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
import java.util.Date;

/**
 * 管理员
 */
@Data
@Entity(name = "PeManager")
@Table(name = "pe_manager")
public class PeManager extends AbstractSiteBean {

	private static final long serialVersionUID = 7763134336870803939L;
	@Id
	@GenericGenerator(
			name = "idGenerator",
			strategy = "uuid"
	)
	@GeneratedValue(
			generator = "idGenerator"
	)
	private String id;
	@Column(
			name = "login_id"
	)
	private String loginId;
	@Column(
			name = "true_name"
	)
	private String trueName;
	@Column(
			name = "name"
	)
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "fk_sso_user_id"
	)
	private SsoUser ssoUser;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "flag_active"
	)
	private EnumConst enumConstByFlagActive;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "fk_unit_id"
	)
	private PeUnit peUnit;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "flag_card_type"
	)
	private EnumConst enumConstByFlagCardType;
	@Column(
			name = "card_no"
	)
	private String cardNo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "flag_gender"
	)
	private EnumConst enumConstByFlagGender;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "flag_department_type"
	)
	private EnumConst enumConstByFlagDepartmentType;
	@Column(
			name = "birthday"
	)
	private Date birthday;
	@Column(
			name = "work_unit"
	)
	private String workUnit;
	@Column(
			name = "mobile"
	)
	private String mobile;
	@Column(
			name = "telephone"
	)
	private String telephone;
	@Column(
			name = "home_phone"
	)
	private String homePhone;
	@Column(
			name = "email"
	)
	private String email;
	@Column(
			name = "address"
	)
	private String address;
	@Column(
			name = "zip_code"
	)
	private String zipCode;
	@Column(
			name = "qq"
	)
	private String qq;
	@ManyToOne
	@JoinColumn(
			name = "FK_ROLE_ID"
	)
	private PePriRole pePriRole;
	@Column(
			name = "site_code"
	)
	private String siteCode;

}