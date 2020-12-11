package com.whaty.analyse.framework.domain.bean;

import com.alibaba.fastjson.JSON;
import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AnalyseParam;
import com.whaty.analyse.framework.domain.AbstractConfigDO;
import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjAssert;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.common.spring.SpringUtil;
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
 * 统计配置
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AnalyseBasicConfig")
@Table(name = "analyse_basic_config")
public class AnalyseBasicConfig extends AbstractBean {

    private static final long serialVersionUID = -2359381068038205120L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flag_analyse_type")
    private EnumConst enumConstByFlagAnalyseType;

    @Column(name = "create_date")
    private Date createDate;

    private transient AnalyseParam analyseParam;

    private transient AnalyseConfigDetail analyseConfigDetail;

    private transient AbstractConfigDO iConfigDO;

    /**
     * 将详情转换成DO
     */
    public void convertDetailToDo() {
        TycjAssert.isAllNotNull(analyseConfigDetail);
        this.iConfigDO = JSON.parseObject(this.analyseConfigDetail.getConfig(),
                AnalyseType.getConfigDO(this.enumConstByFlagAnalyseType.getCode()));
    }

    public static class BasicConfigBuilder {

        private final GeneralDao generalDao;

        public BasicConfigBuilder() {
            this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.GENERAL_DAO_BEAN_NAME);
        }

        private AnalyseBasicConfig config;

        public BasicConfigBuilder withCode(String configCode) {
            this.config = this.generalDao
                    .getOneByHQL("from AnalyseBasicConfig where code = ?", configCode);
            TycjParameterAssert.isAllNotNull(this.config);
            this.config.setAnalyseConfigDetail(this.generalDao
                    .getOneByHQL("from AnalyseConfigDetail where analyseBasicConfig.id = ?", this.config.getId()));
            return this;
        }

        public BasicConfigBuilder withParam(AnalyseParam param) {
            this.config.setAnalyseParam(param);
            return this;
        }

        public AnalyseBasicConfig build() {
            this.config.convertDetailToDo();
            return config;
        }

    }

}
