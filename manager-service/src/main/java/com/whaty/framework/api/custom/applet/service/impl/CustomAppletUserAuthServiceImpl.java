package com.whaty.framework.api.custom.applet.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.api.custom.applet.service.CustomAppletUserAuthService;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.util.TycjCollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 第三方小程序登录验证接口实现类
 *
 * @author shanshuai
 */
@Lazy
@Service("customAppletUserAuthService")
public class CustomAppletUserAuthServiceImpl implements CustomAppletUserAuthService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> validateUserInfo(Map<String, String> paramsMap) {
        TycjParameterAssert.isAllNotEmpty(paramsMap);
        if (!paramsMap.get("siteCode").equals(SiteUtil.getSiteCode())) {
            throw new ParameterIllegalException();
        }
        Map<String, Object> studentIfoMap = generalDao.getOneMapBySQL("SELECT training_id trainingId, 'true' hasToken " +
                        "FROM pe_student WHERE site_code = ? and true_name = ? AND (mobile = ? OR reg_no = ?)",
                SiteUtil.getSiteCode(), paramsMap.get("trueName"),
                paramsMap.get("loginId"), paramsMap.get("loginId"));

        return MapUtils.isNotEmpty(studentIfoMap) ? studentIfoMap :
                TycjCollectionUtils.map("trainingId", "", "hasToken", "false");
    }

}
