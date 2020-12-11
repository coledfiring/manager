package com.whaty.analyse.framework.domain;

/**
 * 拥有数据转换行为的VO
 * @param <T>
 * @author weipengsen
 */
public abstract class AbstractConvertConfigVO<T extends AbstractConfigDO> extends AbstractConfigVO {

    private static final long serialVersionUID = 336624515103001885L;

    public AbstractConvertConfigVO(T configDO) {
        super();
        this.title = configDO.getTitle();
        this.extEChartConfig = configDO.getExtEChartConfig();
        this.convert(configDO);
    }

    /**
     * 将do转换为vo
     * @param configDO
     * @return
     */
    protected abstract void convert(T configDO);

}
