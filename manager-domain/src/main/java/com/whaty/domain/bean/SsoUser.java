package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户表
 * @author weipengsen
 */
@Data
@Entity(name = "SsoUser")
@Table(name = "sso_user")
public class SsoUser extends AbstractBean {

	private static final long serialVersionUID = -3452805667314723208L;
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
			name = "FLAG_BAK"
	)
	private EnumConst enumConstByFlagBak;
	@ManyToOne
	@JoinColumn(
			name = "FK_ROLE_ID"
	)
	private PePriRole pePriRole;
	@Column(
			name = "LOGIN_ID"
	)
	private String loginId;
	@Column(
			name = "PASSWORD"
	)
	private String password;
	@Column(
			name = "LOGIN_NUM"
	)
	private Long loginNum;
	@Column(
			name = "LAST_LOGIN_DATE"
	)
	private Date lastLoginDate;
	@Column(
			name = "LAST_LOGIN_IP"
	)
	private String lastLoginIp;
	@Column(
			name = "LEARNSPACE_SITE_CODE"
	)
  	private String learnspaceSiteCode;
	/**
	 * 微信扫码登录关联id
	 */
	@Column(
			name = "wechat_union_id"
	)
	private String weChatUnionId;
	/**
	 * 绑定微信昵称
	 */
	@Column(
			name = "wechat_nick_name"
	)
	private String weChatNickName;
	/**
	 * 邮箱
	 */
	@Column(
			name = "EMAIL"
	)
	private String email;
	/**
	 * 昵称
	 */
	@Column(
			name = "NICK_NAME"
	)
	private String nickName;
	/**
	 * 手机号
	 */
	@Column(
			name = "MOBILEPHONE"
	)
	private String mobilePhone;
	/**
	 * 性别
	 */
	@Fetch(FetchMode.JOIN)
	@ManyToOne
	@JoinColumn(
			name = "FLAG_GENDER"
	)
	private EnumConst enumConstByFlagGender;
	/**
	 * 照片
	 */
	@Column(
			name = "photo"
	)
	private String photo;
	/**
	 * 姓名
	 */
	@Column(
			name = "TRUE_NAME",
			nullable = false
	)
	private String trueName;
	/**
	 * 站点编号
	 */
	@Column(
			name = "SITE_CODE"
	)
	private String siteCode;
	/**
	 * 是否需要扫码验证
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "flag_wechat_check"
	)
	private EnumConst enumConstByFlagWechatCheck;

	@Column(
			name = "profile_picture"
	)
	private String profilePicture;
	@Transient
	private Set prPriManagerSites = new HashSet(0);
	@Transient
	private Set prPriManagerMajors = new HashSet(0);
	@Transient
	private Set prPriManagerEdutypes = new HashSet(0);
	@Transient
	private Set prPriManagerGrades = new HashSet(0);

	public SsoUser() {}

	public SsoUser(String id) {
		this.id = id;
	}

}
