package com.whaty.products.service.flow.domain.tab;

import lombok.Data;

import java.util.Map;

/**
 * 基础信息展示
 *
 * @author weipengsen
 */
@Data
public class BaseInfoTab extends AbstractBaseTab<Map<String, Object>> {

    private static final long serialVersionUID = -4182395472576949075L;

    private String[] serialHeader;

    private Map<String, Object> items;

    public BaseInfoTab(String title, String[] serialHeader, Map<String, Object> baseInfo) {
        super(title, baseInfo);
        this.serialHeader = serialHeader;
    }

    @Override
    public TabType getType() {
        return TabType.BASE_INFO;
    }

    @Override
    public AbstractBaseTab convert(Map<String, Object> origin) {
        this.setItems(origin);
        return this;
    }
}
