package com.whaty.analyse.framework.type.barline.xaxis;

import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 业务生成器
 *
 * @author weipengsen
 */
public class BusinessGenerator {

    /**
     * 班级有效时间
     *
     * @param basicConfig
     * @return
     */
    public static AbstractXAxisSearchStrategy.XItemResult classActiveDayByParentId(AnalyseBasicConfig basicConfig) {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            Map<String, Object> dayInfo = StaticBeanUtils.getOpenGeneralDao()
                    .getOneMapBySQL("select date_format(start_time, '%Y-%m-%d') as startTime, " +
                                    " IF(DATEDIFF(end_time,NOW()) > 0 , " +
                                    "    IF(DATEDIFF(start_time,NOW()) > 0, 0, DATEDIFF(NOW(), start_time)), " +
                                    "    DATEDIFF(end_time, start_time)) + 1 as offset from pe_class where id = ?",
                            (String) basicConfig.getAnalyseParam().getSearch().get("parentId"));
            Date date = CommonUtils.changeStringToDate((String) dayInfo.get("startTime"));
            return new AbstractXAxisSearchStrategy.XItemResult(IntStream
                    .range(0, ((Number) dayInfo.get("offset")).intValue()).mapToObj(e -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.DAY_OF_MONTH, e);
                        return calendar.getTime();
                    }).sorted().map(e -> CommonUtils.changeDateToString(e, "yy-MM-dd"))
                    .collect(Collectors.toList()), Collections.singletonMap("dateFormat", "%y-%m-%d"));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 市场跟进次数
     * @param basicConfig
     * @return
     */
    public static AbstractXAxisSearchStrategy.XItemResult requirementFollowNum(AnalyseBasicConfig basicConfig) {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            Map<String, Object> requirementInfo = StaticBeanUtils.getOpenGeneralDao()
                    .getOneMapBySQL(AnalyseUtils.handleSql("SELECT min(followNum) AS min, max(followNum) AS max " +
                                    "FROM ( SELECT count(rfui.id) AS followNum FROM requirement_follow_up_info rfui" +
                                    " INNER JOIN requirement_info ri ON ri.id = rfui.fk_requirement_info_id" +
                                    " INNER JOIN pe_manager m ON m.id = ri.fk_create_user_id" +
                                    " INNER JOIN pe_unit un ON un.id = m.fk_unit_id " +
                                    "WHERE [analyse_dateLimit|ri.create_time] AND [peUnit|un.id] AND [unit|un.id]" +
                                    " AND un.site_code = '${siteCode}' GROUP BY ri.id ) r",
                            basicConfig.getAnalyseParam().getSearch()));
            Function<Object, Integer> numberToInteger = o -> Objects.nonNull(o) ? ((Number) o).intValue() : 0;
            return new AbstractXAxisSearchStrategy.XItemResult(IntStream
                    .rangeClosed(numberToInteger.apply(requirementInfo.get("min")),
                            numberToInteger.apply(requirementInfo.get("max"))).mapToObj(String::valueOf)
                    .sorted().collect(Collectors.toList()), null);
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
