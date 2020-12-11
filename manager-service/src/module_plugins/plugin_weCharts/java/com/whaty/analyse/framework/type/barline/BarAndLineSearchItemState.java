package com.whaty.analyse.framework.type.barline;

import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.analyse.framework.type.barline.xaxis.AbstractXAxisSearchStrategy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 查询x轴配置
 *
 * @author weipengsen
 */
public class BarAndLineSearchItemState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final BarAndLineConfigDO configDO;

    public BarAndLineSearchItemState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.configDO = (BarAndLineConfigDO) basicConfig.getIConfigDO();
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        if (CollectionUtils.isEmpty(this.configDO.getItem().getItems())) {
            this.configDO.getItem().setItems(this.searchXAxisItems());
        }
        this.basicConfig.getAnalyseParam().putSearch("items", this.configDO.getItem().getItems());
        return this.handleNextState(new BarAndLineSearchSeriesState(this.basicConfig, this.context));
    }

    /**
     * 查找横轴坐标范围
     *
     * @return
     */
    private List<String> searchXAxisItems() throws Exception {
        AbstractXAxisSearchStrategy.XItemResult result = AbstractXAxisSearchStrategy
                .newInstance(this.configDO, this.basicConfig).search();
        Optional.ofNullable(result.getExtParams()).filter(MapUtils::isNotEmpty)
                .ifPresent(e -> this.basicConfig.getAnalyseParam().getSearch().putAll(e));
        return result.getItems();
    }

}
