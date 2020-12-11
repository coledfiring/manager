package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/18
 */
@Data
@Entity(name = "PeWeather")
@Table(name = "pe_weather")
public class PeWeather extends AbstractSiteBean {

    private static final long serialVersionUID = 7838197243116711316L;
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

    /**
     * 时间
     */
    @Column(
            name = "date"
    )
    private String date;

    /**
     * 星期几
     */
    @Column(
            name = "week"
    )
    private String week;


    /**
     * 城市
     */
    @Column(
            name = "city"
    )
    private String city;

    /**
     * 城市英文
     */
    @Column(
            name = "cityEn"
    )
    private String cityEn;

    /**
     * 国家
     */
    @Column(
            name = "country"
    )
    private String country;

    /**
     * 国家英文
     */
    @Column(
            name = "countryEn"
    )
    private String countryEn;

    /**
     * 天气情况
     */
    @Column(
            name = "wea"
    )
    private String wea;

    /**
     * 天气图标
     */
    @Column(
            name = "wea_img"
    )
    private String wea_img;

    /**
     * 实时温度
     */
    @Column(
            name = "tem"
    )
    private String tem;

    /**
     * 高温
     */
    @Column(
            name = "tem1"
    )
    private String tem1;

    /**
     * 低温
     */
    @Column(
            name = "tem2"
    )
    private String tem2;

    /**
     * 风向
     */
    @Column(
            name = "win"
    )
    private String win;

    /**block
     * 风力等级
     */
    @Column(
            name = "win_speed"
    )
    private String win_speed;

    /**
     * 风速
     */
    @Column(
            name = "win_meter"
    )
    private String win_meter;

    /**
     * humidity 湿度
     */
    @Column(
            name = "humidity"
    )
    private String humidity;

    /**
     * 能见度
     */
    @Column(
            name = "visibility"
    )
    private String visibility;

    /**
     * 气压
     */
    @Column(
            name = "pressure"
    )
    private String pressure;

    /**
     * 空气质量
     */
    @Column(
            name = "air"
    )
    private String air;

    /**
     * 空气质量等级
     */
    @Column(
            name = "air_level"
    )
    private String air_level;


    /**
     * 更新时间
     */
    @Column(
            name = "update_time"
    )
    private Date update_time;

    /**
     * 创建时间
     */
    @Column(
            name = "create_time"
    )
    private Date createTime;

    @Transient
    private String isEnergy;

    @Transient
    private String ygas;

    @Transient
    private String sgas;
}
