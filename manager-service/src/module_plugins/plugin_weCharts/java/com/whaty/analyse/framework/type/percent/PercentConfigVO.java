package com.whaty.analyse.framework.type.percent;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 百分比配置VO
 *
 * @author weipengsen
 */
@Data
public class PercentConfigVO extends AbstractConvertConfigVO<PercentConfigDO> {

    private static final long serialVersionUID = 4991408518436947925L;

    private Boolean isCircle;

    private List<PercentItemConfigVO> items;

    public PercentConfigVO(PercentConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.PERCENT_ANALYSE;
    }

    @Override
    protected void convert(PercentConfigDO configDO) {
        this.setIsCircle(configDO.getIsCircle());
        this.setItems(configDO.getItems().stream()
                .map(e -> new PercentItemConfigVO(e.getLabel(), e.getSerial(), configDO.getData().get(e.getAlias())))
                .sorted(Comparator.comparingInt(PercentItemConfigVO::getSerial))
                .collect(Collectors.toList()));
    }

    /**
     * 进度条项配置VO
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static class PercentItemConfigVO implements Serializable {

        private static final long serialVersionUID = -6476220521398606291L;

        private String label;

        private Integer serial;

        private Number percent;

    }
}
