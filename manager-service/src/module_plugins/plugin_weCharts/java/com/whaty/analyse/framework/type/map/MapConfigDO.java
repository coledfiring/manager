package com.whaty.analyse.framework.type.map;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 地区统计配置DO
 *
 * @author weipengsen
 */
@Data
public class MapConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = 2045056164597096839L;

    private List<String> sql;

    private transient List<Map<String, Object>> data;

}
