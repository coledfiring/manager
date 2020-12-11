package com.whaty.analyse.framework.dao;

import com.whaty.analyse.framework.domain.AnalyseBoardConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBlockCondition;
import com.whaty.analyse.framework.domain.bean.AnalyseBlockConfig;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.util.CommonUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计块dao
 *
 * @author weipengsen
 */
@Component("analyseBlockDao")
public class AnalyseBlockDao {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 通过boardId获取统计块配置
     *
     * @param id
     * @return
     */
    public List<AnalyseBoardConfigVO.BoardBlockVO> listBlockByBoardId(String id) {
        List<Map<String, Object>> blockInfo = this.generalDao
                .getMapBySQL("select fk_block_id as blockId, col_num as colNum from analyse_board_block " +
                        "where fk_board_id = ? order by serial", id);
        Map<String, AnalyseBlockConfig> blockConfigs = this.generalDao
                .<AnalyseBlockConfig>getByHQL("from AnalyseBlockConfig where " +
                        CommonUtils.madeSqlIn(blockInfo.stream()
                                .map(e -> (String) e.get("blockId")).collect(Collectors.toList()), "id"))
                .stream().peek(AnalyseBlockConfig::handleAnalyseCode)
                .collect(Collectors.toMap(AnalyseBlockConfig::getId, e -> e));
        blockConfigs.values().stream().filter(AnalyseBlockConfig::isShift)
                .forEach(e -> e.setShiftTabs(this.listShiftTabs(e.getAnalyseCodes())));
        return blockInfo.stream()
                .map(e -> new AnalyseBoardConfigVO.BoardBlockVO((Number) e.get("colNum"),
                        (String) e.get("blockId"), blockConfigs.get(e.get("blockId"))))
                .collect(Collectors.toList());
    }

    /**
     * 根据快的id列举条件
     *
     * @param blockId
     * @return
     */
    public List<AnalyseBlockCondition> listConditions(String blockId) {
        TycjParameterAssert.isAllNotBlank(blockId);
        return this.generalDao.getByHQL("from AnalyseBlockCondition where analyseBlockConfig.id = ?", blockId);
    }

    /**
     * 根据id查询block配置
     *
     * @param id
     * @return
     */
    public AnalyseBlockConfig getById(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        return this.generalDao.getById(AnalyseBlockConfig.class, id);
    }

    /**
     * 列举切换卡
     *
     * @param analyseCodes
     * @return
     */
    public List<AnalyseBlockConfig.ShiftTab> listShiftTabs(List<String> analyseCodes) {
        return this.generalDao.getMapBySQL("select code, name from analyse_basic_config where " +
                    CommonUtils.madeSqlIn(analyseCodes, "code"))
                .stream().map(e -> new AnalyseBlockConfig.ShiftTab((String) e.get("name"), (String) e.get("code")))
                .collect(Collectors.toList());
    }
}
