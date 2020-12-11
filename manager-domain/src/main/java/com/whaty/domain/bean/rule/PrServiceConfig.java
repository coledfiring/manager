package com.whaty.domain.bean.rule;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 *  报警预警规则
 */
@Entity(
		name = "PrServiceConfig"
)
@Table(
		name = "pr_service_config"
)
@Data
public class PrServiceConfig extends AbstractBean {

	private static final long serialVersionUID = -7067977864633859224L;
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
			name = "FLAG_CONFIG_TYPE"
	)
	private EnumConst enumConstByFlagServiceType;
	@Column(
			name = "NAME"
	)
	private String name;
	@Column(
			name = "CONFIG"
	)
	private String config;
	@Column(
			name = "CODE"
	)
	private String code;
	@Column(
			name = "DATETIME"
	)
	private Date datetime;
	@Column(
			name = "NOTE"
	)
	private String note;
	@Column(
			name = "TEAM"
	)
	private String team;

	@Column(
			name = "link"
	)
	private String link;
}