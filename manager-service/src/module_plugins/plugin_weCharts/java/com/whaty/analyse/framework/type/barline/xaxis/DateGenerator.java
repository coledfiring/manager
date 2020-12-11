package com.whaty.analyse.framework.type.barline.xaxis;

import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.util.CommonUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 日期生成器
 *
 * @author weipengsen
 */
public class DateGenerator {

    public static void main(String[] args) {
        System.out.println(lastSixMonth(null));
    }

    /**
     * 最近六个月
     *
     * @return
     */
    public static AbstractXAxisSearchStrategy.XItemResult lastSixMonth(AnalyseBasicConfig basicConfig) {
        return lastMonth(basicConfig, 6);
    }

    /**
     * 最近n个月
     *
     * @return
     */
    public static AbstractXAxisSearchStrategy.XItemResult lastMonth(AnalyseBasicConfig basicConfig, int monthNum) {
        Date now = new Date();
        return new AbstractXAxisSearchStrategy.XItemResult(IntStream.range(0, monthNum).mapToObj(e -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, -e);
            return calendar.getTime();
        }).sorted().map(e -> CommonUtils.changeDateToString(e, "yy-MM")).collect(Collectors.toList()),
                Collections.singletonMap("dateFormat", "%y-%m"));
    }

    /**
     * 最近一个月的天
     *
     * @param basicConfig
     * @return
     */
    public static AbstractXAxisSearchStrategy.XItemResult lastOneMonthDay(AnalyseBasicConfig basicConfig) {
        Date now = new Date();
        return new AbstractXAxisSearchStrategy.XItemResult(IntStream.range(0, 30).mapToObj(e -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, -e);
            return calendar.getTime();
        }).sorted().map(e -> CommonUtils.changeDateToString(e, "yy-MM-dd")).collect(Collectors.toList()),
                Collections.singletonMap("dateFormat", "%y-%m-%d"));
    }

    /**
     * 时间范围内的月份
     *
     * @param basicConfig
     * @return
     */
    public static AbstractXAxisSearchStrategy.XItemResult monthOfLimit(AnalyseBasicConfig basicConfig) {
        if (Objects.isNull(basicConfig.getAnalyseParam().getSearch().get("analyse_dateLimit"))) {
            return lastMonth(basicConfig, 12);
        }
        List<Date> dateLimit = ((List<String>) basicConfig.getAnalyseParam()
                .getSearch().get("analyse_dateLimit")).stream()
                .map(CommonUtils::changeStringToDate).sorted().collect(Collectors.toList());
        Function<Date, Calendar> dateToCalendar = e -> {
            Calendar c = Calendar.getInstance();
            c.setTime(e);
            return c;
        };
        Calendar end = dateToCalendar.apply(dateLimit.get(dateLimit.size() - 1));
        Calendar start = dateToCalendar.apply(dateLimit.get(0));
        int diffMonth = end.get(Calendar.MONTH) - start.get(Calendar.MONTH) +
                12 * (end.get(Calendar.YEAR) - start.get(Calendar.YEAR));
        return new AbstractXAxisSearchStrategy.XItemResult(IntStream.rangeClosed(0, diffMonth).mapToObj(e -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start.getTime());
            calendar.add(Calendar.MONTH, e);
            return calendar.getTime();
        }).sorted().map(e -> CommonUtils.changeDateToString(e, "yy-MM")).collect(Collectors.toList()),
                Collections.singletonMap("dateFormat", "%y-%m"));
    }

}
