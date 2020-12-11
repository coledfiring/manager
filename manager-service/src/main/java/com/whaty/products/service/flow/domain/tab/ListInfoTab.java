package com.whaty.products.service.flow.domain.tab;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 列表型信息
 *
 * @author weipengsen
 */
@Data
public class ListInfoTab extends AbstractBaseTab<List<Map<String, Object>>> {

    private static final long serialVersionUID = 6094209579271950506L;

    private String[] column;

    private List<Map<String, Object>> data;

    public ListInfoTab(String title, String[] column, List<Map<String, Object>> baseInfo) {
        super(title, baseInfo);
        this.column = column;
    }

    @Override
    public TabType getType() {
        return TabType.LIST_INFO;
    }

    @Override
    public AbstractBaseTab convert(List<Map<String, Object>> origin) {
        if (CollectionUtils.isNotEmpty(origin)) {
            this.setData(origin);
        }
        return this;
    }
}
