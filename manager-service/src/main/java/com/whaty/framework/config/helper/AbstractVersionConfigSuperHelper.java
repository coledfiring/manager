package com.whaty.framework.config.helper;

import com.whaty.designer.listener.AbstractVersionUpdateListenerWithRedis;

/**
 * 所有配置版本检测辅助类的超类
 *
 * @author weipengsen
 */
public abstract class AbstractVersionConfigSuperHelper extends AbstractVersionUpdateListenerWithRedis {

    /**
     * 加载配置
     */
    public abstract void loadConfig();

}
