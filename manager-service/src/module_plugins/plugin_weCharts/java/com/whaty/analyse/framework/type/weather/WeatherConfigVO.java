package com.whaty.analyse.framework.type.weather;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import com.whaty.domain.bean.hbgr.yysj.PeWeather;
import lombok.Data;
import java.util.List;

/**
 * 文本块配置VO
 *
 * [
 *      {
 *          label: string,
 *          value: string,
 *          isMaster: boolean(unique)
 *      }
 * ]
 *
 * @author weipengsen
 */
@Data
public class WeatherConfigVO extends AbstractConvertConfigVO<WeatherConfigDO> {

    private static final long serialVersionUID = -7078738098160342106L;

    private List<PeWeather> items;

    public WeatherConfigVO(WeatherConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(WeatherConfigDO configDO) {
        this.setItems(configDO.getData());
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.WEATHER_ANALYSE;
    }
}
