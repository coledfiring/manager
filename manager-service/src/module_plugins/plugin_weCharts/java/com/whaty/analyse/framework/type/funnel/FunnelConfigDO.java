package com.whaty.analyse.framework.type.funnel;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 漏斗图配置DO
 *
 * @author weipengsen
 */
@Data
public class FunnelConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = 4701284030179180113L;

    private String unit;

    private String sql;

    private List<FunnelItemConfigDO> items;

    private transient Map<String, ? extends Number> data;

    /**
     * 漏斗统计项do
     *
     * @author weipengsen
     */
    @Data
    public static class FunnelItemConfigDO implements Serializable {

        private static final long serialVersionUID = -6334027918567096995L;

        private String alias;

        private String name;
    }
}
