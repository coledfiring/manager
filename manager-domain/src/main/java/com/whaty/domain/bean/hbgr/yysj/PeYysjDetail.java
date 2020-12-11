package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/17
 */
@Data
@Entity(name = "PeYysjDetail")
@Table(name = "pe_yysj_detail")
public class PeYysjDetail extends AbstractSiteBean {

    private static final long serialVersionUID = -5203778830446453405L;
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
            name = "site_code"
    )
    private String siteCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @Column(
            name = "yysj_date"
    )
    private Date yysjDate;

    /**
     * 一网供压
     */
    @Column(
            name = "w1_gy"
    )
    private double w1Gy;

    /**
     * 一网回压
     */
    @Column(
            name = "w1_hy"
    )
    private double w1Hy;

    /**
     * 一网供温
     */
    @Column(
            name = "w1_gw"
    )
    private double w1Gw;

    /**
     * 一网回温
     */
    @Column(
            name = "w1_hw"
    )
    private double w1Hw;

    /**
     * 空调供压
     */
    @Column(
            name = "w2_1_gy"
    )
    private double w21Gy;

    /**
     * 空调回压
     */
    @Column(
            name = "w2_1_hy"
    )
    private double w21Hy;

    /**
     * 空调供温
     */
    @Column(
            name = "w2_1_gw"
    )
    private double w21Gw;

    /**
     * 空调回温
     */
    @Column(
            name = "w2_1_hw"
    )
    private double w21Hw;

    /**
     *  地暖供压
     */
    @Column(
            name = "w2_2_gy"
    )
    private double w22Gy;

    /**
     *  地暖回压
     */
    @Column(
            name = "w2_2_hy"
    )
    private double w22Hy;

    /**
     *  地暖供温
     */
    @Column(
            name = "w2_2_gw"
    )
    private double w22Gw;

    /**
     *  地暖回温
     */
    @Column(
            name = "w2_2_hw"
    )
    private double w22Hw;

    /**
     *  空调热量表热量
     */
    @Column(
            name = "qt_rl"
    )
    private double qtRl;

    /**
     *  地暖热量表热量
     */
    @Column(
            name = "dn_rl"
    )
    private double dnRl;

    /**
     *  空调热量表流量
     */
    @Column(
            name = "qt_ll"
    )
    private double qtLl;

    /**
     *  地暖热量表流量
     */
    @Column(
            name = "dn_ll"
    )
    private double dnLl;

    /**
     *  群楼室温
     */
    @Column(
            name = "ql_sw"
    )
    private double qlSw;

    /**
     *  塔楼室温
     */
    @Column(
            name = "tl_sw"
    )
    private double tlSw;

    /**
     *  室外温度
     */
    @Column(
            name = "sw_wd"
    )
    private double swWd;

    /**
     *  水箱液位
     */
    @Column(
            name = "bsx_yw"
    )
    private double bsxYw;

    /**
     *  膨胀水箱液位
     */
    @Column(
            name = "pzsx_yw"
    )
    private double pzsxYw;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_yysj_id"
    )
    private PeYysj peYysj;


}
