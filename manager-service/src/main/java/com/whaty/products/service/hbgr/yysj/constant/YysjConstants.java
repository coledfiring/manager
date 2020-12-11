package com.whaty.products.service.hbgr.yysj.constant;

/**
 * author weipengsen  Date 2020/6/17
 */
public interface YysjConstants {

    String [] YYSJ_EXCEL_HEADER_NAME = new String[] {"时间", "一网供压Mpa", "一网回压Mpa", "一网供温℃", "一网回温℃",
            "空调供压Mpa", "空调回压Mpa", "空调供温℃", "空调回温℃", "地暖供压Mpa", "地暖回压Mpa" , "地暖供温℃", "地暖回温℃",
            "空调热表DJ", "地暖热表GJ", "室外温度℃", "裙楼室温℃", "塔楼室温℃", "水箱液位m", "膨胀水箱m"};

    String YYSJ_EXCEL_PATH = "/incoming/yysj/%s/%s/%s/%s";

    String WEATHER_API_DAY_PATH = "https://www.tianqiapi.com/api?version=v6&appid=59471754&appsecret=lNNIj7nc";

    String WEATHER_API_7_DAY_PATH = "https://tianqiapi.com/api?version=v1&appid=59471754&appsecret=lNNIj7nc";

    String DEVICE_INFO_QRCODE_FILE_PATH = "/incoming/device/";

    String CHECK_POINT_QRCODE_FILE_PATH = "/incoming/checkPoint/record/";

    String CLIENT_QRCODE_FILE_PATH = "/incoming/device/";

    String DEVICE_INFO_QRCODE_WEB_PATH = "http://www.shoujiaenergy.com/np/#/hbgr_home/deviceInfo?code=%s";

    String CHECK_POINT_RECORD_QRCODE_WEB_PATH = "http://www.shoujiaenergy.com/np/#/hbgr_home/checkRecord?id=%s";

    String CLIENT_QRCODE_WEB_PATH = "http://www.shoujiaenergy.com/np/#/hbgr_home/clientRecord";
}
