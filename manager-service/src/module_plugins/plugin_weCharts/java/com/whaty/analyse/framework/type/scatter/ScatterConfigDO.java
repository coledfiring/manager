package com.whaty.analyse.framework.type.scatter;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 * 散点图配置DO
 *
 * @author weipengsen
 */
@Data
public class ScatterConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = -3365453571451724617L;

    private List<String> sql;

    private transient List<Map<String, Object>> data;

}
