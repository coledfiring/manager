package com.whaty.analyse.framework.type.barline.xaxis;

import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.type.barline.BarAndLineConfigDO;
import com.whaty.common.string.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * x轴查询策略
 *
 * @author weipengsen
 */
public abstract class AbstractXAxisSearchStrategy {

    protected final AnalyseBasicConfig config;

    public AbstractXAxisSearchStrategy(AnalyseBasicConfig config) {
        this.config = config;
    }

    /**
     * 查找
     * @return
     * @throws Exception
     */
    public abstract XItemResult search() throws Exception;

    /**
     * 生产策略子类
     * @param configDO
     * @param basicConfig
     * @return
     */
    public static AbstractXAxisSearchStrategy newInstance(BarAndLineConfigDO configDO,
                                                          AnalyseBasicConfig basicConfig) {
        if (StringUtils.isNotBlank(configDO.getItem().getXItemSql())) {
            return new XAxisSearchSqlStrategy(basicConfig);
        } else if (StringUtils.isNotBlank(configDO.getItem().getClassName())
                && StringUtils.isNotBlank(configDO.getItem().getStaticMethodName())) {
            return new XAxisSearchReflectStrategy(basicConfig);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 返回的x轴数据，其中包括x的项和其他的额外参数集
     * 额外参数集的存在是为了应变因为传入参数的变化引发的x轴格式变化情况
     * 比如，因为时间范围变化造成x轴的年月、周、日格式的变化，进而造成查出的统计数据与x轴名称无法对应
     * 这时候，返回的额外参数中可以写入新的格式，在sql中使用动态处理语言变化查询
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class XItemResult implements Serializable {

        private static final long serialVersionUID = 2835298593881326814L;

        private List<String> items;

        private Map<String, Object> extParams;

        public XItemResult(List<String> items) {
            this.items = items;
        }
    }
}
