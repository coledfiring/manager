package com.whaty.analyse.framework.type.compare;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 对比图统计配置DO
 *
 * @author weipengsen
 */
@Data
public class CompareConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = 574291985834791648L;

    private String label;

    private List<String> sql;

    private CompareItemConfigDO basicItem;

    private CompareItemConfigDO compareItem;

    private transient Map<String, Number> data;

    /**
     * 对比图项统计配置DO
     *
     * @author weipengsen
     */
    @Data
    public static class CompareItemConfigDO implements Serializable {

        private static final long serialVersionUID = -2705328123011529459L;

        private String label;

        private String alias;

    }

}
