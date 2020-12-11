package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 项目评审意见
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeReviewOpinion")
@Table(name = "pe_review_opinion")
public class PeReviewOpinion extends AbstractBean {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     *  评审人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "review_user"
    )
    private SsoUser  reviewUser;

    /**
     * 评审时间
     */
    @Column(name = "review_time")
    private Date reviewTime;

    /**
     * 评审意见
     */
    @Column(name = "review_opinion")
    private String reviewOpinion;

    /**
     * 项目
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_review_id"
    )
    private PeReview peReview;
}

