package com.whaty.products.service.enroll;

import com.whaty.products.service.enroll.domain.EnrollInfoModel;

/**
 * 审核报名信息
 *
 * @author weipengsen
 */
public interface CheckEnrollInfoService {

    /**
     * 获取报名信息
     * @param studentId
     * @return
     */
    EnrollInfoModel getEnrollInfo(String studentId);

    /**
     * 审核通过
     * @param id
     */
    void doPass(String id);

    /**
     * 审核不通过
     * @param id
     * @param reason
     */
    void doNoPass(String id, String reason);

}
