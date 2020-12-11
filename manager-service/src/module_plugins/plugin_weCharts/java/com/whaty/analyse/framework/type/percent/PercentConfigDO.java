package com.whaty.analyse.framework.type.percent;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 进度条配置DO
 *
 * @author weipengsen
 */
@Data
public class PercentConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = -7083685202254134781L;

    private List<String> sql;

    private Boolean isCircle;

    private List<PercentItemConfigDO> items;

    private transient Map<String, Number> data;

    /**
     * 进度条项配置DO
     *
     * @author weipengsen
     */
    @Data
    public static class PercentItemConfigDO implements Serializable {

        private static final long serialVersionUID = -5956738396515914584L;

        private String label;

        private String alias;

        private Integer serial;

    }

}
