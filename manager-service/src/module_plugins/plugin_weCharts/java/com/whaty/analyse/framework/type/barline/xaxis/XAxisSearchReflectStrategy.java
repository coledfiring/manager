package com.whaty.analyse.framework.type.barline.xaxis;

import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.type.barline.BarAndLineConfigDO;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 反射搜索策略
 *
 * @author weipengsen
 */
public class XAxisSearchReflectStrategy extends AbstractXAxisSearchStrategy {

    private final BarAndLineConfigDO configDO;

    public XAxisSearchReflectStrategy(AnalyseBasicConfig config) {
        super(config);
        this.configDO = (BarAndLineConfigDO) config.getIConfigDO();
    }

    @Override
    public XItemResult search() throws Exception {
        Method targetMethod = Class.forName(this.configDO.getItem().getClassName())
                .getMethod(this.configDO.getItem().getStaticMethodName(), this.config.getClass());
        if (!Modifier.isStatic(targetMethod.getModifiers())) {
            throw new IllegalArgumentException();
        }
        if (!XItemResult.class.isAssignableFrom(targetMethod.getReturnType())) {
            throw new IllegalArgumentException();
        }
        return (XItemResult) targetMethod.invoke(null, this.config);
    }
}
