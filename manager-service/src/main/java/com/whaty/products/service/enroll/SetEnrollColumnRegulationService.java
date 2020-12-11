package com.whaty.products.service.enroll;

import com.whaty.products.service.enroll.domain.SetRegulation;
import com.whaty.products.service.enroll.domain.SetRegulationParams;

/**
 * 设置报名规则
 *
 * @author weipengsen
 */
public interface SetEnrollColumnRegulationService {

    /**
     * 获取规则
     * @param id
     * @return
     */
    SetRegulation getRegulation(String id);

    /**
     * 保存规则
     * @param param
     */
    void saveRegulation(SetRegulationParams param);
}
