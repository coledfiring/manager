package com.whaty.analyse.framework.type.column;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 横列数据DO
 *
 * @author weipengsen
 */
@Data
public class ColumnConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = 7702754767564163125L;

    private List<String> sql;

    private List<ColumnItemConfigDO> columns;

    private transient Map<String, Object> data;

    /**
     * 横列项配置
     *
     * @author weipengsen
     */
    @Data
    public static class ColumnItemConfigDO implements Serializable {

        private static final long serialVersionUID = 6014481111736123899L;

        private String alias;

        private String label;

        private Integer serial;

        private Boolean highlight;

    }
}
