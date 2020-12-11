package com.whaty.framework.config.helper;

import com.whaty.framework.config.domain.IConfig;

/**
 * 有版本更新机制的配置抽象类，需要有对应的IConfig子类，没有的话用AbstractVersionConfigSuperHelper
 *
 * @author weipengsen
 */
public abstract class AbstractVersionConfigHelper<T extends IConfig>
        extends AbstractVersionConfigSuperHelper implements IConfigHelper<T> {

    @Override
    protected void updateNow() {
        this.loadConfig();
    }

}
