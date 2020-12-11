package com.whaty.products.service.analyse.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 教师工作量统计业务类
 *
 * @author pingzhihao
 */
@Lazy
@Service("analyseTeacherWorkloadService")
public class AnalyseTeacherWorkloadServiceImpl extends AbstractAnalyseGridService {

    @Override
    public String getTimeConditionSql(String beginTime, String endTime) {
        return String.format(" DATEDIFF(cct.training_date, '%s') >= 0 AND DATEDIFF('%s', cct.training_date) >= 0  ",
                beginTime, endTime);
    }
}
