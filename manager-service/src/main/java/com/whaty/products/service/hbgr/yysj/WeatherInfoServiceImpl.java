package com.whaty.products.service.hbgr.yysj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeWeather;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import com.whaty.products.service.hbgr.yysj.constant.YysjConstants;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author weipengsen  Date 2020/6/18
 */
@Lazy
@Service("weatherInfoService")
public class WeatherInfoServiceImpl extends TycjGridServiceAdapter<PeWeather> {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    public void saveWeatherInfo() throws IOException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        HttpClientHelper helper = new HttpClientHelper();
        PeWeather peWeather = JSON.parseObject(helper.doGet(YysjConstants.WEATHER_API_DAY_PATH).getContent(), PeWeather.class);
        peWeather.setCreateTime(new Date());
        this.openGeneralDao.save(peWeather);
    }

    public List<PeWeather> getWeatherredictionInfo() throws IOException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        HttpClientHelper helper = new HttpClientHelper();
        JSONArray jsonArray = JSON.parseObject(helper.doGet(YysjConstants.WEATHER_API_7_DAY_PATH).getContent()).getJSONArray("data");
        List<PeWeather> peWeathers = jsonArray.stream().map(e-> {
            PeWeather peWeather1 = JSON.parseObject(JSON.toJSONString(e), PeWeather.class);
            peWeather1.setWin((String)JSON.parseArray(peWeather1.getWin()).get(0));
            peWeather1.setTem(peWeather1.getTem().replace("℃", ""));
            peWeather1.setTem1(peWeather1.getTem1().replace("℃", ""));
            peWeather1.setTem2(peWeather1.getTem2().replace("℃", ""));
            List<String> gass = this.openGeneralDao
                    .getBySQL("SELECT gas FROM pe_plan WHERE out_tem = ?", peWeather1.getTem());
            if(CollectionUtils.isNotEmpty(gass)) {
                peWeather1.setYgas(gass.get(0));
            }
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(peWeather1.getDate());
                String now = new SimpleDateFormat("MM月dd日").format(date);
                peWeather1.setDate(now);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return peWeather1;
        }).collect(Collectors.toList());
        return peWeathers;
    }
}
