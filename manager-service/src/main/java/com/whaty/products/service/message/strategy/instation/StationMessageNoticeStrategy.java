package com.whaty.products.service.message.strategy.instation;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.hibernate.dao.impl.ControlGeneralDao;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.strategy.AbstractNoticeStrategy;
import com.whaty.util.CommonUtils;
import com.whaty.util.SQLHandleUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 站内信推送
 *
 * @author weipengsen
 */
@Lazy
@Component("stationMessageNoticeStrategy")
public class StationMessageNoticeStrategy implements AbstractNoticeStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = CommonConstant.CONTROL_GENERAL_DAO_BEAN_NAME)
    private ControlGeneralDao controlGeneralDao;

    /**
     * 站内信字段数组
     */
    private final static String[] STATION_MESSAGE_COLUMN_ARRAY = new String[] {"title", "content", "foot",
            "fk_sso_user_id", "flag_is_star", "flag_readed", "send_date", "fk_send_user_id"};

    /**
     * ${}格式的占位符正则
     */
    private final static Pattern SIGN_FORMAT_PATTERN = Pattern.compile("\\$\\{.+?\\}");
    /**
     * 占位符前缀
     */
    private final static String SIGN_PREFIX = "${";
    /**
     * 占位符后缀
     */
    private final static String SIGN_SUFFIX = "}";

    @Override
    public void notice(Map<String, Object> params) throws Exception {
        if (!params.containsKey(MessageConstants.PARAM_MESSAGE_CONFIG)
                || !params.containsKey(CommonConstant.PARAM_IDS)
                || !params.containsKey(MessageConstants.PARAM_TEMPLATE_CODE)) {
            throw new ParameterIllegalException();
        }
        EnumConst noStar = this.generalDao
                .getEnumConstByNamespaceCode(MessageConstants.ENUM_CONST_NAMESPACE_IS_STAR, "0");
        EnumConst noRead = this.generalDao
                .getEnumConstByNamespaceCode(MessageConstants.ENUM_CONST_NAMESPACE_READED, "0");
        String currentCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Object> groupSqlInfo = this.controlGeneralDao
                .getBySQL("select `data_sql` from station_message_group where code = '"
                + params.get(MessageConstants.PARAM_TEMPLATE_CODE) + "'");
        MasterSlaveRoutingDataSource.setDbType(currentCode);
        if (CollectionUtils.isEmpty(groupSqlInfo)) {
            throw new ParameterIllegalException();
        }
        // 获取需要发送的用户信息
        String userId = this.userService.getCurrentUser().getId();
        Map<String, Object> signParams = new HashMap<>(2);
        signParams.put(CommonConstant.PARAM_IDS,
                ((String) params.get(CommonConstant.PARAM_IDS)).split(CommonConstant.SPLIT_ID_SIGN));
        String sql = ScopeHandleUtils.handleScopeSignOfSql((String) groupSqlInfo.get(0), userId);
        List<Map<String, Object>> userInfo = this.generalDao
                .getMapBySQL(SQLHandleUtils.replaceSignUseParams(sql, signParams));
        if (CollectionUtils.isEmpty(userInfo)) {
            throw new ServiceException("没有符合发送条件的学生");
        }
        Map<String, Object> messageConfig = (Map<String, Object>) params.get(MessageConstants.PARAM_MESSAGE_CONFIG);
        userInfo.forEach(e -> {
            e.put("title", messageConfig.get("title"));
            e.put("content", this.handleContent((String) messageConfig.get("content"), e));
            e.put("foot", messageConfig.get("foot"));
            e.put("fk_sso_user_id", e.get("userId"));
            e.put("flag_is_star", noStar.getId());
            e.put("flag_readed", noRead.getId());
            e.put("send_date", CommonUtils.changeDateToString(new Date(), "yyyy年M月d日"));
            e.put("fk_send_user_id", userId);
        });
        this.generalDao.batchExecuteInsertSql("station_message", STATION_MESSAGE_COLUMN_ARRAY, userInfo);
        // 记录次数
        if (params.containsKey(MessageConstants.PARAM_TEMPLATE_ID)) {
            MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
            this.controlGeneralDao.executeBySQL("update station_message_template set use_number = use_number + 1 " +
                    "where id = '" + params.get(MessageConstants.PARAM_TEMPLATE_ID) + "'");
            MasterSlaveRoutingDataSource.setDbType(currentCode);
        }
    }

    @Override
    public String getTableName() {
        return "station_message_group";
    }

    /**
     * 使用动态数据处理内容
     * @param content
     * @param params
     * @return
     */
    private String handleContent(String content, Map<String, Object> params) {
        Matcher m = SIGN_FORMAT_PATTERN.matcher(content);
        while (m.find()) {
            String signKey = m.group().replace(SIGN_PREFIX, "").replace(SIGN_SUFFIX, "");
            content = content.replace(m.group(), params.containsKey(signKey) && params.get(signKey) != null
                    ? String.valueOf(params.get(signKey)) : " ");
        }
        return content;
    }

}
