package com.whaty.analyse.framework.type.weather;

import com.whaty.analyse.framework.domain.AbstractConfigDO;
import com.whaty.domain.bean.hbgr.yysj.PeWeather;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 天气数据配置DO
 *
 * @author weipengsen
 */
@Data
public class WeatherConfigDO extends AbstractConfigDO {

    private static final long serialVersionUID = -8119178050037078479L;

    private String sql;

    private transient List<PeWeather> data;
}
