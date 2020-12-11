package com.whaty.analyse.framework.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 配置DO
 *
 * 用于存储配置数据生成的元数据，每种统计类型实现一个子类
 *
 * @author weipengsen
 */
@Data
public abstract class AbstractConfigDO implements Serializable {

    private static final long serialVersionUID = -6676123322111088871L;

    private String title;

    private Map<String, Object> extEChartConfig;
}
