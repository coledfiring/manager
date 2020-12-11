package com.whaty.analyse.framework.type.compare;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 对比统计配置VO
 *
 * @author weipengsen
 */
@Data
public class CompareConfigVO extends AbstractConvertConfigVO<CompareConfigDO> {

    private static final long serialVersionUID = -6831893142191313569L;

    private String label;

    @NotNull
    private Number basicNumber;

    @NotNull
    private String basicLabel;

    @NotNull
    private Number compareNumber;

    @NotNull
    private String compareLabel;

    @NotNull
    private BigDecimal percent;

    @NotNull
    private Boolean isRise;

    public CompareConfigVO(CompareConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.COMPARE_ANALYSE;
    }

    @Override
    protected void convert(CompareConfigDO configDO) {
        this.label = configDO.getLabel();
        this.basicLabel = configDO.getBasicItem().getLabel();
        this.basicNumber = configDO.getData().get(configDO.getBasicItem().getAlias());
        this.compareLabel = configDO.getCompareItem().getLabel();
        this.compareNumber = configDO.getData().get(configDO.getCompareItem().getAlias());
        this.percent = BigDecimal.valueOf(this.compareNumber.doubleValue())
                .subtract(BigDecimal.valueOf(this.basicNumber.doubleValue()))
                .divide(BigDecimal.valueOf(this.basicNumber.doubleValue()), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100)).abs();
        this.isRise = this.compareNumber.doubleValue() - this.basicNumber.doubleValue() >= 0;
    }
}
