package com.whaty.analyse.framework.domain.bean;

import com.whaty.analyse.framework.domain.AnalyseConditionVO;
import com.whaty.common.string.StringUtils;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.AllArgsConstructor;
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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 统计块
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AnalyseBlockConfig")
@Table(name = "analyse_block_config")
public class AnalyseBlockConfig extends AbstractBean {

    private static final long serialVersionUID = -3288350202999486538L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "analyse_config_code")
    private String analyseConfigCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flag_is_shift")
    private EnumConst enumConstByFlagIsShift;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "note")
    private String note;

    @Column(name = "block_height")
    private Integer blockHeight;

    @Transient
    private List<String> analyseCodes;

    @Column(name = "fk_detail_board_id")
    private String detailBoardId;

    @Transient
    private List<AnalyseConditionVO> conditions;

    @Transient
    private List<ShiftTab> shiftTabs;

    /**
     * 将string的code处理成列表
     */
    public void handleAnalyseCode() {
        Optional.ofNullable(this.getAnalyseConfigCode()).filter(StringUtils::isNotBlank)
                .ifPresent(e -> this.setAnalyseCodes(Arrays.stream(e.split(";"))
                        .map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toList())));
    }

    /**
     * 拥有更多的统计图
     * @return
     */
    private boolean hasMoreAnalyse() {
        return Optional.ofNullable(this.getAnalyseConfigCode()).filter(StringUtils::isNotBlank)
                .map(e -> e.split(";").length).map(e -> e > 1).orElse(false);
    }

    /**
     * 是否是切换类block
     * @return
     */
    public boolean isShift() {
        return "1".equals(this.enumConstByFlagIsShift.getCode()) && this.hasMoreAnalyse();
    }

    /**
     * 切换卡
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static class ShiftTab implements Serializable {

        private static final long serialVersionUID = 1543626923341074533L;

        private String label;

        private String analyseCode;
    }
}
