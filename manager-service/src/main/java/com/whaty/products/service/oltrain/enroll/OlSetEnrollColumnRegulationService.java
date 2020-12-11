package com.whaty.products.service.oltrain.enroll;

import com.whaty.products.service.oltrain.enroll.domain.OlSetRegulation;
import com.whaty.products.service.oltrain.enroll.domain.OlSetRegulationParams;

/**
 * 设置报名规则
 *
 * @author suoqiangqiang
 */
public interface OlSetEnrollColumnRegulationService {

    /**
     * 获取规则
     * @param id
     * @return
     */
    OlSetRegulation getRegulation(String id);

    /**
     * 保存规则
     * @param param
     */
    void saveRegulation(OlSetRegulationParams param);
}
