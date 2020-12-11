package com.whaty.products.service.analyse.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


/**
 * 单位工作量统计业务类
 *
 * @author pingzhihao
 */
@Lazy
@Service("analyseUnitWorkloadService")
public class AnalyseUnitWorkloadServiceImpl extends AbstractAnalyseGridService {

    @Override
    public String getTimeConditionSql(String beginTime, String endTime) {
        return String.format(" DATEDIFF(ri.create_time, '%s') >= 0 AND DATEDIFF('%s', ri.create_time) >= 0 AND " +
                        " DATEDIFF(cl.end_time, '%s') >= 0 AND DATEDIFF('%s', cl.end_time) >= 0 ",
                beginTime, endTime, beginTime, endTime);
    }
}
