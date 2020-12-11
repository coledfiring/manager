package com.whaty.analyse.framework.type.map;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地区统计配置VO
 *
 * @author weipengsen
 */
@Data
public class MapConfigVO extends AbstractConvertConfigVO<MapConfigDO> {

    private static final long serialVersionUID = -8222126342646946125L;

    private List<MapDataConfigVO> data;

    public MapConfigVO(MapConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(MapConfigDO configDO) {
        this.setData(configDO.getData().stream()
                .map(e -> new MapDataConfigVO((String) e.get("name"), (Number) e.get("value")))
                .collect(Collectors.toList()));
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.MAP_ANALYSE;
    }

    /**
     * 地区统计数据配置VO
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static class MapDataConfigVO implements Serializable {

        private static final long serialVersionUID = -1679215049179557706L;

        private String name;

        private Number value;

    }
}
