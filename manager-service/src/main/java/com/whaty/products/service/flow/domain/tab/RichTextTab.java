package com.whaty.products.service.flow.domain.tab;

import lombok.Data;

/**
 * 富文本tab
 *
 * @author weipengsen
 */
@Data
public class RichTextTab extends AbstractBaseTab<String> {

    private static final long serialVersionUID = 7925499439068458122L;

    private String text;

    public RichTextTab(String title, String baseInfo) {
        super(title, baseInfo);
    }

    @Override
    public TabType getType() {
        return TabType.RICH_TEXT;
    }

    @Override
    public AbstractBaseTab convert(String baseInfo) {
        this.setText(baseInfo);
        return this;
    }

}
