package com.whaty.framework.config;

import com.whaty.designer.deal.AbstractDaemonThreadDealSupport;
import com.whaty.framework.config.helper.AbstractVersionConfigSuperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 项目初始化配置
 * @author weipengsen
 */
@Component
@DependsOn("springUtil")
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class BasicConfiguration extends AbstractDaemonThreadDealSupport implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(BasicConfiguration.class);

    private final static int LOOP_DEAL_SLEEP_TIME = 3;

    @Override
    public void afterPropertiesSet() throws Exception {
        BasicConfigHelperManagement.ALL_HELPER.forEach(AbstractVersionConfigSuperHelper::loadConfig);
        this.start();
    }

    @Override
    public String getThreadName() {
        return "basicConfigUpdatedScan";
    }

    @Override
    public void deal() {
        try {
            BasicConfigHelperManagement.ALL_HELPER.parallelStream()
                    .filter(AbstractVersionConfigSuperHelper::checkUpdated)
                    .forEach(AbstractVersionConfigSuperHelper::update);
        } catch (Exception e) {
            logger.warn("config update checker deal failure", e);
        } finally {
            try {
                TimeUnit.SECONDS.sleep(LOOP_DEAL_SLEEP_TIME);
            } catch (InterruptedException e) {
                logger.warn("config update checker sleep interrupted", e);
            }
        }
    }
}
