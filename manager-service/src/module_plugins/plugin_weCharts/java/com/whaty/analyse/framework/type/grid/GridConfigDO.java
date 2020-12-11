package com.whaty.analyse.framework.type.grid;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

/**
 * grid配置DO
 *
 * @author weipengsen
 */
@Data
public class GridConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = -3706543576256228351L;

    private String gridId;

    private String analyseCode;

    private String gridConfigUrl;

    private String detailBoardId;
}
