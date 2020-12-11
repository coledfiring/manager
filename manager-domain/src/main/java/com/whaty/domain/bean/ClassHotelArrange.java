package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班级住宿安排
 *
 * @author weipengsen
 */
@Data
@Entity(name = "ClassHotelArrange")
@Table(name = "class_hotel_arrange")
public class ClassHotelArrange extends AbstractBean {

    private static final long serialVersionUID = 6072153683594284891L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_class_hotel_detail_id"
    )
    private ClassHotelDetail classHotelDetail;
    @Column(
            name = "arrange_time"
    )
    private Date arrangeTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "arrange_user"
    )
    private SsoUser arrangeUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_hotel_id"
    )
    private PeHotel peHotel;
    @Column(
            name = "male_price"
    )
    private BigDecimal malePrice;
    @Column(
            name = "female_price"
    )
    private BigDecimal femalePrice;
    @Column(
            name = "male_room_number"
    )
    private Integer maleRoomNumber;
    @Column(
            name = "female_room_number"
    )
    private Integer femaleRoomNumber;

}
