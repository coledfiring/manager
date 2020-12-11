package com.whaty.products.service.flow.domain.tab;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 卡片信息
 *
 * @author weipengsen
 */
@Data
public class CardInfoTab extends AbstractBaseTab<List<Map<String, Object>>> {

    private static final long serialVersionUID = -5037322155407573229L;

    private List<CardItem> items;

    public CardInfoTab(String title, List<Map<String, Object>> baseInfo) {
        super(title, baseInfo);
    }

    @Override
    public TabType getType() {
        return TabType.CARD_INFO;
    }

    @Override
    public AbstractBaseTab convert(List<Map<String, Object>> origin) {
        this.setItems(origin.stream().map(CardItem::new).collect(Collectors.toList()));
        return this;
    }

    @Data
    private class CardItem implements Serializable {

        private static final long serialVersionUID = 6595074645752396745L;

        private String title;

        private List<LabelItem> items;

        private CardItem(Map<String, Object> origin) {
            if (!origin.containsKey("title")) {
                throw new IllegalArgumentException();
            }
            this.title = (String) origin.remove("title");
            this.setItems(origin.entrySet().stream().map(LabelItem::new).collect(Collectors.toList()));
        }

    }

    @Data
    private class LabelItem implements Serializable {

        private static final long serialVersionUID = -3843055419251719034L;

        private String label;

        private String icon;

        private LabelItem(Map.Entry<String, Object> origin) {
            this.label = (String) origin.getValue();
            this.icon = origin.getKey().substring(origin.getKey().lastIndexOf("_") + 1);
        }

    }

}
