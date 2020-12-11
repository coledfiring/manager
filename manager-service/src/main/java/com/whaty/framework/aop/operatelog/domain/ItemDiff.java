package com.whaty.framework.aop.operatelog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 操作状态差异模型
 *
 * @author weipengsen
 */
@Data
@AllArgsConstructor
public class ItemDiff {

    private String label;

    private Object before;

    private Object after;

    /**
     * 生成实例
     * @param label
     * @param before
     * @param after
     * @return
     */
    public static ItemDiff newInstance(String label, Object before, Object after) {
        if (before == null && after == null) {
            return null;
        }
        return new ItemDiff(label, before, after);
    }

    /**
     * 将空串转化为空
     *
     * @author weipengsen
     */
    public void convertBlankStringToNull() {
        if ("".equals(this.getBefore())) {
            this.setBefore(null);
        }
        if ("".equals(this.getAfter())) {
            this.setAfter(null);
        }
    }
}
