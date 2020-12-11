package com.whaty.products.controller.hbgr.weatherInfo;
;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeWeather;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.WeatherInfoServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * author weipengsen  Date 2020/6/18
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/weatherInfo")
public class WeatherInfoController  extends TycjGridBaseControllerAdapter<PeWeather> {


    @Resource(name = "weatherInfoService")
    private WeatherInfoServiceImpl weatherInfoService;

    @Override
    public GridService<PeWeather> getGridService() {
        return this.weatherInfoService;
    }

    @RequestMapping("/getWeatherInfo")
    public ResultDataModel getWeatherInfo() throws IOException {
           return ResultDataModel.handleSuccessResult();
    }

    @RequestMapping("/getWeatherPredictionInfo")
    public ResultDataModel getWeatherPredictionInfo() throws IOException {

        return ResultDataModel.handleSuccessResult(weatherInfoService.getWeatherredictionInfo());
    }
}
