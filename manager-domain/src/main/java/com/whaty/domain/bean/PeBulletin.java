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
 * 公告
 * @author weipengsen
 */
@Data
@Entity(name = "PeBulletin")
@Table(name = "pe_bulletin")
public class PeBulletin extends AbstractSiteBean {

	private static final long serialVersionUID = -4669469436438879280L;
	@Id
	@GenericGenerator(
			name = "idGenerator",
			strategy = "uuid"
	)
	@GeneratedValue(
			generator = "idGenerator"
	)
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "FLAG_ISVALID"
	)
	private EnumConst enumConstByFlagIsvalid;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "FLAG_ISTOP"
	)
	private EnumConst enumConstByFlagIstop;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "FK_MANAGER_ID"
	)
	private PeManager peManager;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "FK_SSO_USER_ID"
	)
	private SsoUser ssoUser;
	@Column(
			name = "TITLE"
	)
	private String title;
	@Column(
			name = "PUBLISH_DATE"
	)
	private Date publishDate;
	@Column(
			name = "UPDATE_DATE"
	)
	private Date updateDate;
	@Column(
			name = "NOTE"
	)
	private String note;
	@Column(
			name = "SCOPE_STRING"
	)
	private String scopeString;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "flag_bulletin_type"
	)
	private EnumConst enumConstByFlagBulletinType;
	@Column(
			name = "site_code"
	)
	private String siteCode;

}