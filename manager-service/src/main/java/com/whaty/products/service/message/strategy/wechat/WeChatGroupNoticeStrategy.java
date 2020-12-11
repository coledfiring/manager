package com.whaty.products.service.message.strategy.wechat;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.httpClient.domain.HttpClientResponseData;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.strategy.AbstractNoticeStrategy;
import com.whaty.products.service.wechat.WeChatAccessTokenService;
import com.whaty.util.CommonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 微信群发策略
 *
 * @author weipengsen
 */
@Lazy
@Component("weChatGroupNoticeStrategy")
public class WeChatGroupNoticeStrategy implements AbstractNoticeStrategy {

    private final static Logger logger = LoggerFactory.getLogger(WeChatGroupNoticeStrategy.class);

    @Resource(name = "weChatAccessTokenService")
    private WeChatAccessTokenService weChatAccessTokenService;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 发送群发信息接口的url
     */
    private static String groupNoticeURL;

    public WeChatGroupNoticeStrategy() {
        if (groupNoticeURL == null) {
            //获取
            Properties prop = null;
            try {
                prop = new Properties();
                prop.load(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(MessageConstants.PROP_FILE_PATH));
            } catch (IOException e) {
                logger.error("资源文件加载失败", e);
            }
            groupNoticeURL = prop.getProperty(MessageConstants.PROP_GROUP_SEND_TEXT_MESSAGE);
        }
    }

    @Override
    public void notice(Map<String, Object> params) throws Exception {
        // 参数校验
        if (!params.containsKey(CommonConstant.PARAM_IDS) || !params.containsKey(MessageConstants.CONTENT)) {
            throw new ParameterIllegalException();
        }
        // 校验群发是否超过次数
        this.checkGroupNotice((String) params.get(CommonConstant.PARAM_IDS));
        Map<String, Object> apiParams = this.collectApiParams(params);
        // 拼接json参数
        String jsonString = this.toTextJsonString(apiParams);
        String accessToken = this.weChatAccessTokenService.getToken();
        // 文本信息发送
        this.sendTextInfo(jsonString, accessToken);
        // 保存发送信息
        this.saveTextInfo((String) params.get(CommonConstant.PARAM_IDS), (String) params.get(MessageConstants.CONTENT),
                (String[]) params.get(MessageConstants.TOS));
    }

    @Override
    public String getTableName() {
        return null;
    }

    /**
     * 校验微信openid是否存在，用户是否超过发送上限
     *
     * @param ids
     * @return
     */
    private void checkGroupNotice(String ids) throws ServiceException {
        //判断邮件是否存在
        String[] idArr = ids.split(CommonConstant.SPLIT_ID_SIGN);
        if (idArr.length < 2) {
            throw new ServiceException("群发消息至少需要选择两个单位");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                  ");
        sql.append("     wu.openid AS openId                                                 ");
        sql.append(" FROM                                                                    ");
        sql.append("     pe_student stu                                                      ");
        sql.append(" INNER JOIN wechat_user wu ON wu.fk_sso_user_id = stu.FK_SSO_USER_ID     ");
        sql.append(" WHERE                                                                   ");
        sql.append("    (wu.openid is not null OR wu.openid <> '')                           ");
        sql.append(" AND " + CommonUtils.madeSqlIn(idArr, "stu.id"));
        List<Object> data = this.generalDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(data)) {
            throw new ServiceException("勾选的所有学生都未关注校方公众号，无法发送");
        }
        if (idArr.length != data.size()) {
            throw new ServiceException("勾选的学生中有部分学生未关注校方公众号，无法发送");
        }
        //检查学生发送信息是否到达上限
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                        ");
        sql.append("     1                                                                         ");
        sql.append(" FROM                                                                          ");
        sql.append("     pe_student stu                                                            ");
        sql.append(" INNER JOIN wechat_user wu ON wu.fk_sso_user_id = stu.FK_SSO_USER_ID           ");
        sql.append(" INNER JOIN pr_sms_send_status ps ON ps.receive_user = wu.openid               ");
        sql.append(" INNER JOIN pe_sms_info psi ON psi.ID = ps.FK_SMS_INFO_ID                      ");
        sql.append(" WHERE                                                                         ");
        sql.append("     MONTH (psi.SEND_DATE) = MONTH (now())                                     ");
        sql.append(" AND " + CommonUtils.madeSqlIn(idArr, "stu.ID"));
        sql.append(" GROUP BY                                                                      ");
        sql.append("     stu.ID                                                                    ");
        sql.append(" HAVING                                                                        ");
        sql.append("     count(ps.id) >= 4                                                         ");
        data = this.generalDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(data)) {
            throw new ServiceException("单人每月只能接受同一公众号四次群发消息推送，部分学生已超额");
        }
    }

    /**
     * 保存发送的信息
     *
     * @param ids
     * @param content
     * @param tos
     */
    private void saveTextInfo(String ids, String content, String[] tos) {
        //todo 存储消息
    }

    /**
     * 将传入参数转换为api的参数
     *
     * @param params
     * @return
     */
    private Map<String, Object> collectApiParams(Map<String, Object> params) {
        String[] openIds = this.listOpenIds((String) params.get(CommonConstant.PARAM_IDS));
        Map<String, Object> apiParams = new LinkedHashMap<>();
        apiParams.put(MessageConstants.TOS, openIds);
        apiParams.put(MessageConstants.CONTENT, params.get(MessageConstants.CONTENT));
        return apiParams;
    }

    /**
     * 发送文本信息方法
     *
     * @param jsonString
     * @param accessToken
     */
    private void sendTextInfo(String jsonString, String accessToken) throws IOException {
        HttpClientResponseData result = this.requestHttp(String.format(groupNoticeURL, accessToken), jsonString);
        //判断此次http请求是否请求成功
        if (!result.getStatus().equals(HttpStatus.OK.value())) {
            throw new ServiceException(String.format("发送失败:%s错误", result.getStatus()));
        }
        //判断此次发送任务是否成功
        Map<String, String> map = JSON.parseObject(result.getContent(), HashMap.class);
        if (!String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_STATUS_CODE))
                .equals(MessageConstants.RESPONSE_STATUS_CODE_SUCCESS)) {
            if (logger.isErrorEnabled()) {
                logger.error("微信群发信息失败，错误代码：" +
                        String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_STATUS_CODE))
                        + ",错误信息：" + String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_ERROR_INFO)));
            }
            throw new ServiceException("发送失败,微信返回错误信息为:" +
                    map.get(MessageConstants.WE_CHAT_RESPONSE_ERROR_INFO));
        }
    }

    /**
     * 将参数转换成群发接口需要的json字符串
     * {
     * "touser":[
     * "OPENID1",
     * "OPENID2"
     * ],
     * "msgtype": "text",
     * "text": { "content": "hello from boxer."}
     * }
     *
     * @param args
     * @return
     */
    private String toTextJsonString(Map<String, Object> args) {
        String[] tos = (String[]) args.get(MessageConstants.TOS);
        String content = (String) args.get(MessageConstants.CONTENT);
        JSONObject json = new JSONObject();
        json.put(MessageConstants.PARAM_MSG_TYPE, MessageConstants.MSG_TYPE_TEXT);
        JSONObject textJson = new JSONObject();
        textJson.put(MessageConstants.PARAM_CONTENT, content);
        json.put(MessageConstants.PARAM_TEXT, textJson);
        JSONArray array = JSONArray.fromObject(tos);
        json.put(MessageConstants.PARAM_TO_USER, array);
        return json.toString();
    }

    /**
     * 根据学生ids获得openId
     *
     * @param ids
     * @return
     */
    private String[] listOpenIds(String ids) throws ServiceException {
        StringBuilder sql = new StringBuilder();
        String[] idArr = ids.split(CommonConstant.SPLIT_ID_SIGN);
        sql.append(" SELECT                                                                ");
        sql.append("     wu.openid AS openId                                               ");
        sql.append(" FROM                                                                  ");
        sql.append("     wechat_user wu                                                    ");
        sql.append(" INNER JOIN pe_student stu ON stu.FK_SSO_USER_ID = wu.fk_sso_user_id   ");
        sql.append(" WHERE                                                                 ");
        sql.append("    (wu.openid is not null OR wu.openid <> '')                         ");
        sql.append(" AND " + CommonUtils.madeSqlIn(idArr, "stu.id"));
        List<Object> result = this.generalDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(result) || result.size() != idArr.length) {
            throw new ServiceException("未关注公众号，无法获取openId");
        }
        return result.toArray(new String[result.size()]);
    }
}
