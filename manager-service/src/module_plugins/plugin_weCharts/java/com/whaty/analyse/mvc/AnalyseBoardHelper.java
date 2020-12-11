package com.whaty.analyse.mvc;

import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.dao.AnalyseBlockDao;
import com.whaty.analyse.framework.dao.AnalyseBoardDao;
import com.whaty.analyse.framework.domain.AnalyseBoardConfigVO;
import com.whaty.analyse.framework.domain.AnalyseConditionVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBoardConfig;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.utils.StaticBeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 统计看板辅助类
 *
 * @author weipengsen
 */
@Lazy
@Component("analyseBoardHelper")
public class AnalyseBoardHelper {

    @Resource(name = "analyseBoardDao")
    private AnalyseBoardDao analyseBoardDao;

    @Resource(name = "analyseBlockDao")
    private AnalyseBlockDao analyseBlockDao;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    private final static String ANALYSE_BOARD_KEY = "analyse_board_%s";

    private final static int ANALYSE_CACHE_TIMEOUT = 24 * 60 * 60;

    /**
     * 通过id获取
     * @param id
     * @return
     */
    public AnalyseBoardConfigVO getById(String id, Map<String, Object> params) {
        AnalyseBoardConfigVO config = this.redisCacheService.getFromCache(this.generateCacheKey(id));
        if (Objects.isNull(config)) {
            config = this.getConfigFromDatabase(id, params);
            TycjParameterAssert.isAllNotNull(config);
            this.redisCacheService.putToCache(this.generateCacheKey(id), config, ANALYSE_CACHE_TIMEOUT);
        }
        return config;
    }

    /**
     * 通过id移除缓存
     * @param id
     */
    public void removeFromCache(String id) {
        this.redisCacheService.remove(this.generateCacheKey(id));
    }

    /**
     * 生成缓存key
     * @param id
     * @return
     */
    private String generateCacheKey(String id) {
        return String.format(ANALYSE_BOARD_KEY, id);
    }

    /**
     * 从数据库中查询配置
     * @param id
     * @return
     */
    private AnalyseBoardConfigVO getConfigFromDatabase(String id, Map<String, Object> params) {
        AnalyseBoardConfigVO configVO = this.boardConfigConvertToVo(this.analyseBoardDao.getById(id), params);
        configVO.setConditions(AnalyseConditionVO.convert(this.analyseBoardDao.listConditions(id), params));
        configVO.setBlockConfigs(this.analyseBlockDao.listBlockByBoardId(id));
        configVO.getBlockConfigs().stream().map(AnalyseBoardConfigVO.BoardBlockVO::getBlockConfig)
                .forEach(e -> e.setConditions(AnalyseConditionVO
                        .convert(this.analyseBlockDao.listConditions(e.getId()), params)));
        return configVO;
    }

    /**
     * 将AnalyseBoardConfig对象转换为vo对象
     *
     * @param config
     * @param initParams
     * @return
     */
    private AnalyseBoardConfigVO boardConfigConvertToVo(AnalyseBoardConfig config, Map<String, Object> initParams) {
        TycjParameterAssert.isAllNotNull(config);
        AnalyseBoardConfigVO configVO = new AnalyseBoardConfigVO();
        configVO.setId(config.getId());
        configVO.setTitle(config.getTitle());
        configVO.setCode(config.getCode());
        configVO.setCanExport(config.getCanExport());
        configVO.setCreateDate(config.getCreateDate());
        String labelSql = config.getLabelSql();
        if (StringUtils.isNotBlank(labelSql)) {
            configVO.setLabel(AnalyseUtils.composeSwitchSite(() -> (String) StaticBeanUtils.getOpenGeneralDao()
                    .getOneBySQL(AnalyseUtils.handleSql(labelSql, initParams))).get());
        }
        return configVO;
    }
}
