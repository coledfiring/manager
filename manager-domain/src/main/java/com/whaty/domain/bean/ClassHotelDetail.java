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
import java.util.Date;

/**
 * 班级住宿情况详情
 *
 * @author weipengsen
 */
@Data
@Entity(name = "ClassHotelDetail")
@Table(name = "class_hotel_detail")
public class ClassHotelDetail extends AbstractBean {

    private static final long serialVersionUID = 1472288161363267272L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "start_time"
    )
    private Date startTime;
    @Column(
            name = "end_time"
    )
    private Date endTime;
    @Column(
            name = "note"
    )
    private String note;
    @Column(
            name = "apply_time"
    )
    private Date applyTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "apply_user"
    )
    private SsoUser applyUser;

}
