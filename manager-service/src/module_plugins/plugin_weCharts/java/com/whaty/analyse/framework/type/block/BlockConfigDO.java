package com.whaty.analyse.framework.type.block;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 文本块配置DO
 *
 * @author weipengsen
 */
@Data
public class BlockConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = -4579544173469209881L;

    private List<String> sql;

    private List<BlockItemConfigDO> items;

    private transient Map<String, Object> data;

    /**
     * 文本块项配置DO
     *
     * @author weipengsen
     */
    @Data
    public static class BlockItemConfigDO implements Serializable {

        private static final long serialVersionUID = 4831258011787009876L;

        private String label;

        private String alias;

        private Integer serial;

    }

}
