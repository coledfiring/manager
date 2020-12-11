package com.whaty.framework.api.grant.service.impl;

import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.framework.api.grant.service.ApiTokenService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.httpClient.HttpClientUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.util.CommonUtils;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * api接口token服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("apiTokenService")
public class ApiTokenServiceImpl implements ApiTokenService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public Map<String, Object> getToken() throws Exception {
        String url = String.format(GrantConstant.OPEN_API_LOGIN_URL, CommonUtils.getRequest().getScheme(),
                CommonUtils.getRequest().getServerName());
        List<Object> config = this.myGeneralDao
                .getBySQL("select value from system_variables where name = 'openTokenLogin' and site_code = ?",
                        SiteUtil.getSiteCode());
        Map<String, String> params = JSONObject.fromObject(config.get(0));
        params.put(GrantConstant.PARAM_PASSWORD, CommonUtils.md5(params.get(GrantConstant.PARAM_PASSWORD)));
        params.put(GrantConstant.PARAM_GRANT_TYPE, GrantConstant.GRANT_TYPE_PASSWORD);
        params.put(GrantConstant.PARAM_CLIENT_ID, CommonUtils.getRequest().getServerName());
        String responseText = HttpClientUtil.post(url, params);
        return JSONObject.fromObject(responseText);
    }

}
