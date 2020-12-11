package com.whaty.products.service.wechat.impl;

import com.whaty.domain.bean.wechat.PeWeChatBasic;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.products.service.wechat.WeChatBasicService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信基础配置读取服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("weChatBasicService")
public class WeChatBasicServiceImpl implements WeChatBasicService {

    private static final Log logger = LogFactory.getLog("weChatBasicService");

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    private Map<String, String> basicMap;

    /**
     * 初始化站点信息
     */
    public void init(String siteCode) {
        String currentDataSourceCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        if (logger.isInfoEnabled()) {
            logger.info("初始化微信站点公众号信息");
        }
        this.basicMap = new HashMap<>(16);
        DetachedCriteria dc = DetachedCriteria.forClass(PeWeChatBasic.class);
        dc.createCriteria("peWeChatSite", "peWeChatSite");
        List<PeWeChatBasic> list = this.generalDao.getList(dc);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(e -> {
                String siteId = e.getPeWeChatSite().getId();
                basicMap.put(siteCode + "_AppID_" + siteId, e.getAppId());
                basicMap.put(siteCode + "_AppSecret_" + siteId, e.getAppSecret());
                basicMap.put(siteCode + "_token_" + siteId, e.getToken());
            });
        }
        if (logger.isInfoEnabled()) {
            logger.info("初始化微信站点公众号信息成功");
        }
        MasterSlaveRoutingDataSource.setDbType(currentDataSourceCode);
    }

    @Override
    public String getValue(String siteId, String siteCode, String name) {
        if (basicMap == null) {
            this.init(siteCode);
        }
        String appID = this.basicMap.get(siteCode + "_" + name + "_" + siteId);
        if (StringUtils.isNotBlank(appID)) {
            return appID;
        }
        this.init(siteCode);
        return this.basicMap.get(siteCode + "_" + name + "_" + siteId);
    }

}
