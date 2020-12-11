package com.whaty.products.service.flow.domain.tab;

import lombok.Data;

import java.io.Serializable;

/**
 * 抽象基础tab
 *
 * @author weipengsen
 */
@Data
public abstract class AbstractBaseTab<T> implements Serializable {

    private static final long serialVersionUID = 3460225344204124404L;

    private final String title;

    private final TabType type;

    public AbstractBaseTab(String title, T baseInfo) {
        this.title = title;
        this.type = this.getType();
        if (baseInfo == null) {
            return;
        }
        this.convert(baseInfo);
    }

    /**
     * 获取类型
     * @return
     */
    public abstract TabType getType();

    /**
     * 领域模型转换
     * @param origin
     * @return
     */
    public abstract AbstractBaseTab convert(T origin);

    /**
     * tab类型
     */
    protected enum TabType {

        /**
         * 基础信息
         */
        BASE_INFO,
        /**
         * 列表型信息
         */
        LIST_INFO,
        /**
         * 富文本
         */
        RICH_TEXT,
        /**
         * 文件列表
         */
        FILE_LIST,
        /**
         * 卡片信息
         */
        CARD_INFO,
        ;

    }

}
