package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;

/**
 * 通用页面导入配置，表头实体类
 *
 * @author weipengsen
 */
@Data
public class PrintHeaderConfig extends AbstractBean {

    private static final long serialVersionUID = 5127147675257930102L;
    /**
     * id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 单元格宽度
     */
    private String width;
    /**
     * 占单元格数
     */
    private String colspan;

    public PrintHeaderConfig() {}

    public PrintHeaderConfig(String name, String width) {
        this.name = name;
        this.width = width;
        this.colspan = "1";
    }

    public PrintHeaderConfig(String name, String width, String colspan) {
        this.name = name;
        this.width = width;
        this.colspan = colspan;
    }

}
