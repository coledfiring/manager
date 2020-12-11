package com.whaty.analyse.framework.type.grid;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * grid配置VO
 *
 * @author weipengsen
 */
@Data
public class GridConfigVO extends AbstractConvertConfigVO<GridConfigDO> {

    private static final long serialVersionUID = -6368121743860961557L;

    @NotNull
    private String gridId;

    private String analyseCode;

    private String gridConfigUrl;

    private String detailBoardId;

    public GridConfigVO(GridConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.GRID_ANALYSE;
    }

    @Override
    protected void convert(GridConfigDO config) {
        this.setGridId(config.getGridId());
        this.setAnalyseCode(config.getAnalyseCode());
        this.setGridConfigUrl(config.getGridConfigUrl());
        this.setDetailBoardId(config.getDetailBoardId());
    }
}
