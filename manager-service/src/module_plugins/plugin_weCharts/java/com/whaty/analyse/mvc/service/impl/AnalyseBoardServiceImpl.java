package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.domain.AnalyseBoardConfigVO;
import com.whaty.analyse.mvc.AnalyseBoardHelper;
import com.whaty.analyse.mvc.service.AnalyseBoardService;
import com.whaty.framework.asserts.TycjParameterAssert;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 统计看板显示
 *
 * @author weipengsen
 */
@Lazy
@Service("analyseBoardService")
public class AnalyseBoardServiceImpl implements AnalyseBoardService {

    @Resource(name = "analyseBoardHelper")
    private AnalyseBoardHelper helper;

    @Override
    public AnalyseBoardConfigVO getBoard(String id, Map<String, Object> params) {
        TycjParameterAssert.isAllNotBlank(id);
        return this.helper.getById(id, params);
    }

}
